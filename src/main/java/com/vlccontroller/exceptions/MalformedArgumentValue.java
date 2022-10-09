package com.vlccontroller.exceptions;

public class MalformedArgumentValue extends RuntimeException {

  private static final long serialVersionUID = 108512365305204947L;

  public MalformedArgumentValue(String message) {
    super(message);
  }
}
