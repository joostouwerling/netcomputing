package com.sportperformancemanagement.webservice.dao;

/**
 * Class to indicate that the resource that is being tried
 * to insert already exists in the database.
 * @author joostouwerling
 *
 */
public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = 8643484248083511242L;
	
	public AlreadyExistsException(String err) {
		super(err);
	}

}
