package com.ajo.asapp.repos;

import com.ajo.asapp.entities.User;

public interface UserDao extends AbstractIdDao<User, Integer>{
  
  public User getForName(String name);
  
}
