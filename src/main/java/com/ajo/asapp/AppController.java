package com.ajo.asapp;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.ajo.asapp.entities.Message;
import com.ajo.asapp.repos.MessageDao;
import com.ajo.asapp.repos.MessageHashDao;
import com.ajo.asapp.repos.UserDao;
import com.ajo.asapp.repos.UserHashDao;

@Controller
public class AppController {

  private UserDao userDao = new UserHashDao();
  private MessageDao messageDao = new MessageHashDao(); 
  
  @GetMapping("/")
  public String home(HttpServletRequest req, ModelMap model) {
    return "home";
  }
  
  @GetMapping("/app/")
  public String app() {
    return "app";
  }
  
  @GetMapping(value="app/messages", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<Message>> getMessages() {
    return new ResponseEntity<>(this.messageDao.getAll(), HttpStatus.OK);
  }
  
}
