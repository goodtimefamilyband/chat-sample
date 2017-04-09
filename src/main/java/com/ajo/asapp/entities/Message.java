package com.ajo.asapp.entities;

import java.text.DateFormat;

public class Message extends AbstractIdItem<Long>{

  private String text;
  private Long posted;
  private int authorId;
  private String authorName;
  
  //nullable
  private Integer recipient;
  
  public String getText() {
    return text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public long getPosted() {
    return posted;
  }
  
  public void setPosted(long posted) {
    this.posted = posted;
  }

  public Integer getRecipient() {
    return recipient;
  }

  public void setRecipient(Integer recipient) {
    this.recipient = recipient;
  }

  public int getAuthorId() {
    return authorId;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }
  
}
