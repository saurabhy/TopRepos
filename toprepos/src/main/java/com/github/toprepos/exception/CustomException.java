package com.github.toprepos.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Custom exception (unchecked)
 */
public class CustomException extends RuntimeException{
   
	private String msg;
	private String error;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public CustomException() {
		
	}
	public CustomException(String message,String code) {
		msg= message;
		error=code;
	}
}
