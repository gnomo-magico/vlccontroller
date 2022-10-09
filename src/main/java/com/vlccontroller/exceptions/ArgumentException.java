package com.vlccontroller.exceptions;

public class ArgumentException extends RuntimeException {

  private static final long serialVersionUID = 4492325233595888383L;

  public ArgumentException(String message) {
    super(message);
  }
}
