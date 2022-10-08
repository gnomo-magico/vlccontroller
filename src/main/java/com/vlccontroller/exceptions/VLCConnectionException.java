package com.vlccontroller.exceptions;



public class VLCConnectionException extends RuntimeException {


  private static final long serialVersionUID = 734956261442841417L;

  public VLCConnectionException(String message){
    super(message);
  }
}
