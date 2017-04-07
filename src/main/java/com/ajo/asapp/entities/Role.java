package com.ajo.asapp.entities;

import org.springframework.security.core.GrantedAuthority;

public class Role extends AbstractIdItem<Integer> implements GrantedAuthority {
  
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getAuthority() {
    // TODO Auto-generated method stub
    return name;
  }
}
