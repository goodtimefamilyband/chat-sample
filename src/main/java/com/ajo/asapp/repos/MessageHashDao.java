package com.ajo.asapp.repos;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import com.ajo.asapp.entities.Message;

public class MessageHashDao extends HashDao<Message, Long> implements MessageDao {

  private Queue<Message> orderedMessages = new LinkedList<>();
  
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

}
