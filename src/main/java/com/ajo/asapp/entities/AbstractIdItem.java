package com.ajo.asapp.entities;

public abstract class AbstractIdItem<IdType> {

  private IdType id;
  
  public IdType getId() {
    return this.id;
  }
  
  public void setId(IdType id) {
    this.id = id;
  }
}
