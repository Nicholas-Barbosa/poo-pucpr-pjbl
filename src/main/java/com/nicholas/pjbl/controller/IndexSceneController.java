package com.nicholas.pjbl.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.nicholas.pjbl.Uploads;
import com.nicholas.pjbl.domain.Upload;
import com.nicholas.pjbl.observer.SceneObserver;
import com.nicholas.pjbl.service.FilePathNotFound;
import com.nicholas.pjbl.service.GBucketUploadService;
import com.nicholas.pjbl.service.PseudoUploadService;
import com.nicholas.pjbl.service.UploadListener;
import com.nicholas.pjbl.service.UploadService;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class IndexSceneController implements SceneObserver, Initializable {

	@FXML
	private TextField inputArquivo;

	@FXML
	private Button button;

	@FXML
	private ProgressBar uploadProgress;

	private final FileChooser fileChooser = new FileChooser();

	private Stage stage;

	private UploadService diskUploader = new PseudoUploadService();

	private UploadService googleBucketUploader = new GBucketUploadService();

	private List<Upload> uploads;

	private Upload currentUpload;

	@FXML
	private Label bytesRead;

	private final DateTimeFormatter datFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.uploads = Uploads.readFromFile();
		uploadProgress.setVisible(false);
		button.setDisable(true);
		inputArquivo.setOnMouseClicked(e -> {
			File file = fileChooser.showOpenDialog(stage);
			inputArquivo.setText(file.getAbsolutePath());
			uploadProgress.setVisible(false);
			bytesRead.setText(null);
			button.setDisable(false);
		});

	}

	public void iniciar(ActionEvent actionEvent) {
		try {
			bytesRead.setText(null);
			button.setDisable(true);
			uploadProgress.setVisible(true);
			button.setText("Uploading...");
			Path path = Path.of(inputArquivo.getText());

			currentUpload = new Upload(Path.of(path.toString()).getFileName().toString(),
					path.toString().substring(path.toString().lastIndexOf(".")));

			googleBucketUploader.upload(inputArquivo.getText(), new UploadListener() {

				@Override
				public void update(long bytesRead, long size) {
					Platform.runLater(() -> {
						IndexSceneController.this.bytesRead.setText("Escrevendo " + bytesRead + " de " + size + "...");
						float readProgress = (bytesRead * 100) / size;
						readProgress /= 100;
						IndexSceneController.this.uploadProgress.setProgress(readProgress);

					});
				}

				@Override
				public void onComplete(long duration, long totalBytesRead) {
					Platform.runLater(() -> {
						currentUpload.setFinishedAt(LocalDateTime.now());
						button.setText("Upload");
						IndexSceneController.this.bytesRead
								.setText("Upload de " + totalBytesRead + " bytes concluido em " + duration + " ms");
						IndexSceneController.this.uploads.add(currentUpload);
						Uploads.saveToFile(uploads);
						IndexSceneController.this.uploadProgress.setProgress(0d);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Sucesso");
						alert.setContentText("Iniciado: "
								+ IndexSceneController.this.datFormatter.format(currentUpload.getStartedAt())
								+ "\nEncerrado: "
								+ IndexSceneController.this.datFormatter.format(currentUpload.getFinishedAt())
								+ "\nArquivo: " + currentUpload.getFileName() + "\nID: " + currentUpload.getId());
						alert.show();
						currentUpload = null;
					});

				}
			});
		} catch (FilePathNotFound e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR, "Caminho não econtrado");
			alert.show();
		}
	}

	@Override
	public void onScene(Stage stage) {
		try {
			this.stage = stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/index.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			scene.setOnKeyPressed(e -> {
				KeyEvent event = (KeyEvent) e;
				switch (event.getCode().getCode()) {
				case 27:
					Upload lastUpload = this.uploads.get(this.uploads.size() - 1);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Encerrar");
					alert.setContentText("Uploads realizados: " + this.uploads.size() + "\nÚltimo upload "
							+ datFormatter.format(lastUpload.getStartedAt()) + "\nID " + lastUpload.getId()
							+ "\nArquivo " + lastUpload.getFileName());
					alert.showAndWait();
					Platform.exit();
					break;

				default:
					break;
				}
			});

		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}
