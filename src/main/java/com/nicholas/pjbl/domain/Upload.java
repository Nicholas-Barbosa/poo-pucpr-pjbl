package com.nicholas.pjbl.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Upload implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id, fileName, fileExtension;
	private LocalDateTime startedAt, finishedAt;

	public Upload(String fileName, String fileExtension) {
		super();
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		startedAt = LocalDateTime.now();
		id = UUID.randomUUID().toString();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public LocalDateTime getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(LocalDateTime finishedAt) {
		this.finishedAt = finishedAt;
	}

}
