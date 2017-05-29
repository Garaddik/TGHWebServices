package com.mk.exception;

public class TGHException extends Exception {

	private int status;

	public int getStatus() {
		return status;
	}

	private String errorMessage;

	public TGHException(int status, String errorMessage) {
		super();
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
