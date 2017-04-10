package com.ajo.asapp.entities.message;

public class ImageMessage extends Message {

  private static final String IMG_HTML =
      "<img src=\":t\" width=\":w\" height = \":h\" />";
  
  private int width;
  private int height;
  
  @Override
  public String getHtml() {
    // TODO Auto-generated method stub
    return IMG_HTML.replace(":t", this.getText())
        .replace(":w", Integer.toString(this.width))
        .replace(":h", Integer.toString(this.height));
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
  
}
