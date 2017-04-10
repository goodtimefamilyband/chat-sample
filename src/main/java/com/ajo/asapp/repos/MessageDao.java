package com.ajo.asapp.repos;

import java.util.Collection;

import com.ajo.asapp.entities.Message;
import com.ajo.asapp.entities.User;

public interface MessageDao extends AbstractIdDao<Message, Long> {

  public Collection<Message> getAll();
  
  public Collection<Message> getDirectMessages(User from, User recipient);
  
}
