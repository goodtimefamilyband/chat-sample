package com.ajo.asapp.repos;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import com.ajo.asapp.entities.Message;
import com.ajo.asapp.entities.User;

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

}
