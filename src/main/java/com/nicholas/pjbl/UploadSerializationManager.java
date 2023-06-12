package com.nicholas.pjbl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.nicholas.pjbl.domain.Upload;

public class UploadSerializationManager {

	private String serFolder = "C:\\Users\\nicholas.oliveira\\Desktop\\Java Workspace\\pjbl\\serialized-objects\\upload-object";

	public void saveToFile(List<Upload> uploads) {
		try (ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(serFolder)))) {
			for (Upload up : uploads) {
				out.writeObject(up);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Upload> readFromFile() {
		List<Upload> uploads = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(serFolder)))) {
			while (true) {
				Object object = in.readObject();
				if (object instanceof Upload)
					uploads.add((Upload) object);
			}
		} catch (FileNotFoundException e) {
			return new ArrayList<>();
		} catch (EOFException e) {
			return uploads;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
