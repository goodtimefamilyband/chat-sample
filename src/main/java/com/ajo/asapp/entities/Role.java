package com.ajo.asapp.entities;

public class Role extends AbstractIdItem<Integer> {
  
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
