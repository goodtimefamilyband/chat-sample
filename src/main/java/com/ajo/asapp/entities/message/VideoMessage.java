package com.ajo.asapp.entities.message;

public class VideoMessage extends Message {

  private String html;
  private int length;
  private String source;
  
  @Override
  public String getHtml() {
    return this.html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

}
