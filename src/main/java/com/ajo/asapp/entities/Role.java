package com.ajo.asapp.entities;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
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
    return name;
  }
}
