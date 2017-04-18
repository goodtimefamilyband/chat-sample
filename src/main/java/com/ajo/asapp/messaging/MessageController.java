package com.ajo.asapp.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ajo.asapp.entities.message.Message;
import com.ajo.asapp.repos.MessageDao;

@Controller
public class MessageController {

  private SimpMessagingTemplate template;
  
  @Autowired
  private MessageDao messageDao;
  
  @Autowired
  public MessageController(SimpMessagingTemplate template) {
    this.template = template;
  }
  
  @MessageMapping("/messages")
  public void handle(SimpMessageHeaderAccessor headerAccessor, Message inbound) {
    
  }
  
}
