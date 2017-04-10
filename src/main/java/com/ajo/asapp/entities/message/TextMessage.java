package com.ajo.asapp.entities.message;

public class TextMessage extends Message {

  @Override
  public String getHtml() {
    return this.getText();
  }

}
