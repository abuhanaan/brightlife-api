package com.fronteers.exceptions;

public abstract class ApiException extends RuntimeException {

  public ApiException(String message) {
    super(message);
  }

  public ApiException(Throwable cause) {
    super(cause);
  }
}
