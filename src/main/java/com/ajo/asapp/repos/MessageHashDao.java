package com.ajo.asapp.repos;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.ajo.asapp.entities.User;
import com.ajo.asapp.entities.message.Message;

public class MessageHashDao extends HashDao<Message, Long> implements MessageDao {

  private Queue<Message> orderedMessages = new LinkedList<>();
  private long nextId = 0;
  
  @Override
  public void add(Message m) {
    super.add(m);
    orderedMessages.add(m);
  }
  
  @Override
  public Collection<Message> getAll() {
    // TODO Auto-generated method stub
    return this.orderedMessages;
  }

  @Override
  protected Long getNextId() {
    // TODO Auto-generated method stub
    return nextId++;
  }

  @Override
  public Collection<Message> getDirectMessages(User from, User recipient) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Message> getDirectMessages(User from, User recipient, int count, int page) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Collection<Message> getAll(int count, int page) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getCount(User from, User recipient) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Collection<Message> getNewMessages(User u) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLastSeen(User u, User channel, int timestamp) {
    // TODO Auto-generated method stub
    
  }

}
