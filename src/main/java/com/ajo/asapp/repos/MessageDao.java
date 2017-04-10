package com.ajo.asapp.repos;

import java.util.Collection;

import com.ajo.asapp.entities.User;
import com.ajo.asapp.entities.message.Message;

public interface MessageDao extends AbstractIdDao<Message, Long> {

  public Collection<Message> getAll();
  
  public Collection<Message> getDirectMessages(User from, User recipient);
  
}
