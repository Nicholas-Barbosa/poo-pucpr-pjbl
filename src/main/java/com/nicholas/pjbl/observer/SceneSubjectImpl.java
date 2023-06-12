package com.nicholas.pjbl.observer;

import javafx.stage.Stage;

public class SceneSubjectImpl implements SceneSubject {

	private SceneObserver sceneObserver;

	private Stage stage;

	public SceneSubjectImpl(Stage stage) {
		super();
		this.stage = stage;
	}

	@Override
	public void register(SceneObserver oberserver) {
		this.sceneObserver = oberserver;

	}

	@Override
	public void notifyObserver() {
		sceneObserver.onScene(stage);

	}
}
