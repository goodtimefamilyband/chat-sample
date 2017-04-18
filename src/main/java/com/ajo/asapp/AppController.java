package com.ajo.asapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import ac.simons.oembed.OembedResponse;
import ac.simons.oembed.OembedService;

import com.ajo.asapp.entities.User;
import com.ajo.asapp.entities.message.ImageMessage;
import com.ajo.asapp.entities.message.Message;
import com.ajo.asapp.entities.message.TextMessage;
import com.ajo.asapp.entities.message.VideoMessage;
import com.ajo.asapp.repos.MessageDao;
import com.ajo.asapp.repos.MessageHashDao;
import com.ajo.asapp.repos.UserDao;
import com.ajo.asapp.repos.UserHashDao;

@Controller
public class AppController {

  public static final String MSG_TYPE_IMG = "type";
  public static final String MSG_TYPE_VID = "video";
  
  @Autowired
  private MessageDao messageDao;
  
  @Autowired
  private UserDao userDao;
  
  @Autowired
  private OembedService oembedService;
  
  private SimpMessagingTemplate template;
  
  @Autowired
  public AppController(SimpMessagingTemplate template) {
    this.template = template;
  }
  
  @GetMapping("/")
  public void home(HttpServletResponse resp) throws IOException {
    resp.sendRedirect("/app/");
  }
  
  @GetMapping("/app/")
  public String app(ModelMap model) {
    model.addAttribute("appscript", "app");
    return "app";
  }
  
  @GetMapping("/app/{sender}/")
  public String app(HttpServletResponse resp, @PathVariable int sender, ModelMap model) throws IOException {
    model.addAttribute("appscript", "app_user");
    
    User senderU = userDao.getForId(sender);
    if(senderU == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    else {
      model.addAttribute("recip", senderU);
    }
    
    return "app";
  }
  
  @GetMapping(value="/app/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal User u) {
    Map<String, Object> m = baseModel(u);
    m.put("messages", this.messageDao.getAll());
    
    return new ResponseEntity<>(m, HttpStatus.OK);
  }
  
  @GetMapping(value="/app/page/{pageNum}/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal User u,
      @PathVariable int pageNum,
      @RequestParam(value="count", defaultValue="10") String countStr) {
  
    Map<String, Object> m = baseModel(u);
    int count;
    try {
      count = Integer.parseInt(countStr);
    }
    catch(NumberFormatException ex) {
      return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
    }
    
    
    m.put("messages", this.messageDao.getAll(count, pageNum));
    return new ResponseEntity<>(m, HttpStatus.OK);
  }
  
  @GetMapping("/app/page/{pageNum}/")
  public String appPage(@PathVariable int pageNum,
      @RequestParam(value="count", defaultValue="10") String countStr,
      HttpServletResponse resp,
      ModelMap model) throws IOException {
    
    model.addAttribute("isHistory", true);
    model.addAttribute("appscript", "app_history");
    
    int count;
    try {
      count = Integer.parseInt(countStr);
      if(count == 0) {
        throw new NumberFormatException();
      }
    }
    catch(NumberFormatException ex) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return "app";
    }
    
    System.out.println("Message count: " + messageDao.getCount());
    model.addAttribute("page", pageNum);
    
    int lastPage = (messageDao.getCount() - 1) / count;
    model.addAttribute("lastPage", lastPage < 0 ? 0 : lastPage);
    
    return "app";
    
  }
  
  @GetMapping("/app/page/{sender}/{pageNum}/")
  public String appPage(@PathVariable int pageNum,
      @RequestParam(value="count", defaultValue="10") String countStr,
      @PathVariable int sender,
      @AuthenticationPrincipal User recipient,
      HttpServletResponse resp,
      ModelMap model) throws IOException {
    
    model.addAttribute("isHistory", true);
    model.addAttribute("appscript", "app_history");
    
    User s = userDao.getForId(sender);
    if(s == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return "app";
    }
    
    model.addAttribute("recip", s);
    
    int count;
    try {
      count = Integer.parseInt(countStr);
      if(count == 0) {
        throw new NumberFormatException();
      }
    }
    catch(NumberFormatException ex) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return "app";
    }
    
    model.addAttribute("page", pageNum);
    
    int lastPage = (messageDao.getCount(s, recipient) - 1) / count;
    model.addAttribute("lastPage", lastPage < 0 ? 0 : lastPage);
    
    return "app";
    
  }
  
  @GetMapping(value="/app/{sender}/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal User recipient, @PathVariable int sender) {
    Map<String, Object> m = baseModel(recipient);
    User s = userDao.getForId(sender);
    if(s == null) {
      return new ResponseEntity<>(m, HttpStatus.NOT_FOUND);
    }
    
    m.put("messages", this.messageDao.getDirectMessages(s, recipient));
    return new ResponseEntity<>(m, HttpStatus.OK);
  }
  
  @GetMapping(value="/app/page/{sender}/{pageNum}/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal User recipient, 
      @PathVariable int sender,
      @PathVariable int pageNum,
      @RequestParam(value="count", defaultValue="10") String countStr) {
    
    Map<String, Object> m = baseModel(recipient);
    
    User s = userDao.getForId(sender);
    if(s == null) {
      return new ResponseEntity<>(m, HttpStatus.NOT_FOUND);
    }
    
    int count;
    try {
      count = Integer.parseInt(countStr);
    }
    catch(NumberFormatException ex) {
      return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
    }
    
    m.put("messages", this.messageDao.getDirectMessages(s, recipient, count, pageNum));
    return new ResponseEntity<>(m, HttpStatus.OK);
  }
  
  @PostMapping(value="/app/{recipient}/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public void postMessage(HttpServletRequest req, 
      HttpServletResponse resp,
      @AuthenticationPrincipal User u, 
      @RequestParam("msg") String msg,
      @PathVariable int recipient) throws IOException {
    
    User r = userDao.getForId(recipient);
    if(u == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    
    Message m = this.getMessageForText(msg);
    
    m.setPosted(System.currentTimeMillis() / 1000);
    m.setText(msg);
    m.setAuthorId(u.getId());
    m.setAuthorName(u.getName());
    
    m.setRecipient(r.getId());
    
    messageDao.add(m);
    
    this.template.convertAndSendToUser(u.getName(), "/app/messaging", m);
    this.template.convertAndSendToUser(r.getName(), "/app/messaging", m);
    
  }
  
  
  @PostMapping("/app/messages")
  @ResponseStatus(HttpStatus.OK)
  public void postMessage(HttpServletRequest req, 
      @AuthenticationPrincipal User u, 
      @RequestParam("msg") String msg) {
    System.out.println("Got message "+ msg);
    
    Message m = this.getMessageForText(msg);
    
    m.setPosted(System.currentTimeMillis() / 1000);
    m.setText(msg);
    m.setAuthorId(u.getId());
    m.setAuthorName(u.getName());
    messageDao.add(m);
    
    this.template.convertAndSend("/app/messaging", m);
  }
  
  @PostMapping("/app/lastseen")
  //@ResponseStatus(HttpStatus.OK)
  public void setLastSeen(HttpServletResponse resp,
      @AuthenticationPrincipal User u, 
      @RequestParam("tstamp") String timestamp) throws IOException {
    
    try {
      int tstamp = Integer.parseInt(timestamp);
      this.messageDao.setLastSeen(u, null, tstamp);
      resp.sendError(HttpServletResponse.SC_OK);
    }
    catch(NumberFormatException ex) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
  
  @PostMapping("/app/{channel}/lastseen")
  public void setLastSeen(HttpServletResponse resp,
      @AuthenticationPrincipal User u, 
      @RequestParam("tstamp") String timestamp,
      @PathVariable int channel) throws IOException {
    
    User ch = this.userDao.getForId(channel);
    if(ch == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    
    try {
      int tstamp = Integer.parseInt(timestamp);
      this.messageDao.setLastSeen(u, ch, tstamp);
      resp.sendError(HttpServletResponse.SC_OK);
    }
    catch(NumberFormatException ex) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
    
  }
  
  public Message getMessageForText(String txt) {
    
    try {
      // Checking if URL is valid
      URL url = new URL(txt);
      HttpClient client = new DefaultHttpClient();
      HttpResponse resp = client.execute(new HttpHead(txt));
      Header contentType = resp.getFirstHeader("Content-Type");
      if(contentType.getValue().startsWith("image/")) {//.matches("image/.*")) {
        ImageMessage im = new ImageMessage();
        im.setWidth(300);
        im.setHeight(300);
        return im;
      }
      
      Optional<OembedResponse> optresp = this.oembedService.getOembedResponseFor(txt);      
      if(optresp.isPresent()) {
        return this.buildMessage(optresp.get());
      }
      
    }
    catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      //e.printStackTrace();
      
    } catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return new TextMessage();
  }
  
  public Message buildMessage(OembedResponse resp) {
    System.out.println(resp.getType());
    switch(resp.getType()) {
    case MSG_TYPE_IMG:
      ImageMessage im = new ImageMessage();
      im.setWidth(resp.getWidth());
      im.setHeight(resp.getHeight());
      
      return im;
      
    case MSG_TYPE_VID:
      VideoMessage vm = new VideoMessage();
      vm.setHtml(resp.getHtml());
      vm.setLength(0);
      vm.setSource(resp.getProviderName());
      
      return vm;
      
    default:
      return new TextMessage();
    }
  }
  
  public Map<String, Object> baseModel(User u) {
    Map<String, Object> m = new HashMap<>();
    m.put("unseen", this.messageDao.getNewMessages(u));
    
    return m;
  }
  
}
