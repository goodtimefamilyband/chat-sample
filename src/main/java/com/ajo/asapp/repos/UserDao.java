package com.ajo.asapp.repos;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ajo.asapp.entities.User;

public interface UserDao extends AbstractIdDao<User, Integer>, UserDetailsService {
  
  public User getForName(String name);
  
  public void setPassword(User u, String password);
  
}
