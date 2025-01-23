package com.bookstore.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> appExceptionHandler(BadRequestException ex, WebRequest request) {
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());
		//return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
		return handleExceptionInternal(ex, errorResponse, null, BAD_REQUEST, request);
	}


	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> appExceptionHandler(NotFoundException ex, WebRequest request) {
		return handleExceptionInternal(ex, null, null, NOT_FOUND, request);
	}
	
//	@ExceptionHandler(NotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleBookNotFoundException(NotFoundException ex)
//	{
//		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());
//		return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
//	}
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex)
	{
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());
		return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
	}
	
}