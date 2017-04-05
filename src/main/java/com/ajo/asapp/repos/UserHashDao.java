package com.ajo.asapp.repos;

import com.ajo.asapp.entities.User;

public class UserHashDao extends HashDao<User, Integer> implements UserDao {

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
  
}
