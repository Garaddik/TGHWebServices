package com.mk.exception;

public class TGHDBException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3394810563148240415L;
	private int status;

	public int getStatus() {
		return status;
	}

	private String errorMessage;

	public TGHDBException(int status, String errorMessage) {
		super();
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
