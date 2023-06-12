package com.nicholas.pjbl.service;

public class FilePathNotFound extends Exception {

	private String message;

	public FilePathNotFound(String message) {
		super(message);
	}

}
