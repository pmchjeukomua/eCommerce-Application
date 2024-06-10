package com.example.demo.controllers;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.model.requests.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler {
	Logger log = LogManager.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		log.error("System error : ", ex);
		
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}