package com.ajo.asapp.entities.message;

import com.ajo.asapp.entities.AbstractIdItem;

public abstract class Message extends AbstractIdItem<Long>{

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
  
  public abstract String getHtml();
  
}
