package com.create.response;

import com.create.domain.UserRole;

public class JwtResponse {

	private String jwt;
	private String message;
	private boolean isAuthentication;
	private boolean isError;
	private String errorDetails;
	private UserRole type;
	
	public JwtResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAuthentication() {
		return isAuthentication;
	}

	public void setAuthentication(boolean isAuthentication) {
		this.isAuthentication = isAuthentication;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public UserRole getType() {
		return type;
	}

	public void setType(UserRole type) {
		this.type = type;
	}
}
