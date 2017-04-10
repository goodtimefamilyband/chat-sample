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
  public String home(HttpServletRequest req, ModelMap model) {
    return "home";
  }
  
  @GetMapping("/app/")
  public String app() {
    return "app";
  }
  
  @GetMapping("/app/{sender}/")
  public String app(@PathVariable int sender) {
    return "app";
  }
  
  @GetMapping(value="/app/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages() {
    Map<String, Object> m = new HashMap<>();
    m.put("messages", this.messageDao.getAll());
    return new ResponseEntity<>(m, HttpStatus.OK);
  }
  
  @GetMapping(value="/app/{sender}/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal User recipient, @PathVariable int sender) {
    Map<String, Object> m = new HashMap<>();
    User s = userDao.getForId(sender);
    if(s == null) {
      return new ResponseEntity<>(m, HttpStatus.NOT_FOUND);
    }
    
    m.put("messages", this.messageDao.getDirectMessages(s, recipient));
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
  
}
