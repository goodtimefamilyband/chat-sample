package com.ajo.asapp.repos;

import java.util.Collection;

import com.ajo.asapp.entities.Message;

public interface MessageDao extends AbstractIdDao<Message, Long> {

  public Collection<Message> getAll();
  
}
