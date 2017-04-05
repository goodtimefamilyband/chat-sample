package com.ajo.asapp.entities;

public class Message extends AbstractIdItem<Long>{

  private String text;
  private int userId;
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public int getUserId() {
    return userId;
  }
  
  public void setUserId(int userId) {
    this.userId = userId;
  }
  
}
