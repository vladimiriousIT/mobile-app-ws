package com.appsdeveloperblog.app.ws.exceptions;

public class UserServiceException extends RuntimeException{
  private static final long serialVersionUID = 4865903039190150223L;
  public UserServiceException(String message) {
    super(message);
  }
}
