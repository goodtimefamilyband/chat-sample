package com.ajo.asapp.entities;

import java.sql.Date;

public class Message extends AbstractIdItem<Long>{

  private String text;
  private int userId;
  private Date posted;
  
  //nullable
  private Integer recipient;
  
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

  public Date getPosted() {
    return posted;
  }

  public void setPosted(Date posted) {
    this.posted = posted;
  }

  public Integer getRecipient() {
    return recipient;
  }

  public void setRecipient(Integer recipient) {
    this.recipient = recipient;
  }
  
}
