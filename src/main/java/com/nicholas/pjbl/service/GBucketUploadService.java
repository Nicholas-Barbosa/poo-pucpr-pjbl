package com.nicholas.pjbl.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GBucketUploadService implements UploadService {

	private final Storage storage = StorageOptions.getDefaultInstance().getService();

	@Override
	public void upload(String filePath, UploadListener listener) throws FilePathNotFound {
		if (filePath != null && Files.exists(Path.of(filePath))) {
			new Thread(() -> {
				try {
					BlobInfo blobInfo = BlobInfo.newBuilder("pjbl", Path.of(filePath).getFileName().toString()).build();
					long size = Files.size(Path.of(filePath));
					try (final InputStream in = Files.newInputStream(Path.of(filePath));
							final WriteChannel writer = storage.writer(blobInfo);) {
						byte[] buffer = new byte[1024 * 2];
						int bytesRead;
						int totalBytesRead = 0;
						long currentTimeMillis = System.currentTimeMillis();
						while ((bytesRead = in.read(buffer)) != -1) {
							writer.write(ByteBuffer.wrap(buffer));
							totalBytesRead += bytesRead;
							listener.update(totalBytesRead, size);

						}
						long finished = System.currentTimeMillis();
						listener.onComplete(finished - currentTimeMillis, totalBytesRead);

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();
			;

		} else
			throw new FilePathNotFound("Arquivo não econtrado!");
	}

}
