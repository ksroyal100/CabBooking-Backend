package com.create.exception;

import java.time.LocalDateTime;

public class ErrorDetail {

	private String error;
	private String details;
	private LocalDateTime timeStamp;
	
	public ErrorDetail(String error, String details, LocalDateTime timeStamp) {
		super();
		this.error = error;
		this.details = details;
		this.timeStamp = timeStamp;
	}
	
	
}
