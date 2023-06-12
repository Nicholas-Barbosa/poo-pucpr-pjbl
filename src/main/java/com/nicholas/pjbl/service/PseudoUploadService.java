package com.nicholas.pjbl.service;

import java.nio.file.Files;
import java.nio.file.Path;

public class PseudoUploadService implements UploadService {

	@Override
	public void upload(String filePath, UploadListener listener) throws FilePathNotFound {
		if (filePath != null && Files.exists(Path.of(filePath))) {
			new Thread(() -> {
				int size = 5000;
				int bytesRead = 0;
				while (bytesRead < size) {
					try {
						bytesRead += 100;
						Thread.sleep(1000);
						System.out.println("update!!!!");
						listener.update(bytesRead, size);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();

		} else
			throw new FilePathNotFound("Caminho dos arquivos não existem!!!");

	}
}
