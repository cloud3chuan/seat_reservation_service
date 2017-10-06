package com.walmart.seatdemo.controller;

import com.walmart.seatdemo.model.SeatHoldExpireException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by chuanwang on 10/4/17.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(SeatHoldExpireException.class)
  public ResponseEntity handleException(SeatHoldExpireException e) {
    // log exception
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(e.getMessage());
  }
}