package com.ajo.asapp.repos;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ajo.asapp.entities.User;

public class UserHashDao extends HashDao<User, Integer> implements UserDao {

  private int nextId;
  
  @Override
  public User getForName(String name) {
    // Slow, but only using for testing
    for(User u : this.map.values()) {
      if(u.getName().equals(name)) {
        return u;
      }
    }
    
    return null;
  }

  @Override
  protected Integer getNextId() {
    // TODO Auto-generated method stub
    return nextId++;
  }

  @Override
  public void setPassword(User u, String password) {
    
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    return null;
  }
  
}
