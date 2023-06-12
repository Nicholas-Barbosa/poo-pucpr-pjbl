package com.nicholas.pjbl;

import com.nicholas.pjbl.controller.IndexSceneController;
import com.nicholas.pjbl.observer.SceneSubject;
import com.nicholas.pjbl.observer.SceneSubjectImpl;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FxApplication extends Application {

	private SceneSubject subject;

	@Override
	public void start(Stage primaryStage) throws Exception {
		IndexSceneController controller = new IndexSceneController();
		subject = new SceneSubjectImpl(primaryStage);
		subject.register(controller);
		primaryStage.setTitle("PJBL");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.getIcons().add(new Image(getClass().getResource("/icon.png").toString()));
		subject.notifyObserver();
		primaryStage.show();

	}

}
