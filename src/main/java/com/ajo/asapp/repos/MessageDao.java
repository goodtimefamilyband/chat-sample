package com.ajo.asapp.repos;

import java.util.Collection;

import com.ajo.asapp.entities.User;
import com.ajo.asapp.entities.message.Message;

public interface MessageDao extends AbstractIdDao<Message, Long> {

  public Collection<Message> getAll();
  
  public Collection<Message> getAll(int count, int page);
  
  public Collection<Message> getDirectMessages(User from, User recipient);
  
  public Collection<Message> getDirectMessages(User from, User recipient, int count, int page);
  
  public int getCount(User from, User recipient);
  
  public Collection<Message> getNewMessages(User u);
  
  public void setLastSeen(User u, User channel, int timestamp);
  
}
