package com.fronteers.controllers;

import com.fronteers.brightlife.model.Error;
import com.fronteers.exception.AuthenticationException;
import com.fronteers.exception.BadRequestException;
import com.fronteers.exception.ConflictException;
import com.fronteers.exception.NotFoundException;
import com.fronteers.exception.ProcessingException;
import com.fronteers.exception.TimeoutException;
import com.fronteers.model.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Error> handleBadRequest(
      HttpServletRequest request, Exception e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.INPUT.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Error> handleUnauthorized(
      HttpServletRequest request, AuthenticationException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.PERMISSION.name(), e.getMessage()),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Error> handleConflict
      (HttpServletRequest request, ConflictException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.INPUT.name(), e.getMessage()),
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ProcessingException.class)
  public ResponseEntity<Error> handleProcessing
      (HttpServletRequest request, ProcessingException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.INPUT.name(), e.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Error> handleNotFound
      (HttpServletRequest request, NotFoundException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.NOT_FOUND.name(), e.getMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleInternalError
      (HttpServletRequest request, Exception e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.PROCESSING.name(), e.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TimeoutException.class)
  public ResponseEntity<Error> handleTimeout
      (HttpServletRequest request, TimeoutException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.TIMEOUT.name(), e.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Error> handleEmptyBodyException
      (HttpServletRequest request, HttpMessageNotReadableException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.INPUT.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Error> handleMethodNotSupported
      (HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.INPUT.name(), e.getMessage()),
        HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Error> handleMediaTypeNotSupportedException
      (HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.PROCESSING.name(), e.getMessage()),
        HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleMethodArgumentNotValidException(
      HttpServletRequest request, MethodArgumentNotValidException e) {
    return new ResponseEntity<>(new Error(false, ErrorCode.INPUT.name(), getMessage(e)),
        HttpStatus.BAD_REQUEST);
  }

  private String getMessage(MethodArgumentNotValidException ex) {
    StringBuilder sb = new StringBuilder("Validation failed ");
    BindingResult bindingResult = ex.getBindingResult();
    sb.append(": ");
    for (ObjectError error : bindingResult.getAllErrors()) {
      if (error instanceof FieldError err) {
        sb.append("[").append(err.getField()).append(": ").append(err.getDefaultMessage())
            .append("] ");
      } else {
        sb.append('[').append(error).append("] ");
      }
    }
    return sb.toString();
  }

}
