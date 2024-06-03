package com.rindus.testapi.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();		
	}
	
	public ServiceException(String message) {
		super(message);		
	}

}
