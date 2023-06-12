package com.nicholas.pjbl.service;

public abstract class UploadListener {

	public abstract void update(long bytesRead, long length);

	public abstract void onComplete(long duration, long totalBytesRead);
}
