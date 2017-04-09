package com.ajo.asapp;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ajo.asapp.entities.Message;
import com.ajo.asapp.entities.User;
import com.ajo.asapp.repos.MessageDao;
import com.ajo.asapp.repos.MessageHashDao;
import com.ajo.asapp.repos.UserDao;
import com.ajo.asapp.repos.UserHashDao;

@Controller
public class AppController {

  @Autowired
  private MessageDao messageDao;
  
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
  
  @GetMapping(value="/app/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getMessages() {
    Map<String, Object> m = new HashMap<>();
    m.put("messages", this.messageDao.getAll());
    return new ResponseEntity<>(m, HttpStatus.OK);
  }
  
  @PostMapping("/app/messages")
  @ResponseStatus(HttpStatus.OK)
  public void postMessage(HttpServletRequest req, 
      @AuthenticationPrincipal User u, 
      @RequestParam("msg") String msg) {
    System.out.println("Got message "+ msg);
    Message m = new Message();
    m.setPosted(System.currentTimeMillis() / 1000);
    m.setText(msg);
    m.setAuthorId(u.getId());
    m.setAuthorName(u.getName());
    messageDao.add(m);
    
    this.template.convertAndSend("/app/messaging", m);
  }
  
}
