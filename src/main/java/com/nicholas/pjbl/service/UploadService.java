package com.nicholas.pjbl.service;

public interface UploadService {

	void upload(String filePath, UploadListener listener) throws FilePathNotFound;
}
