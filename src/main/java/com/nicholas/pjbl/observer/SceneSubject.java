package com.nicholas.pjbl.observer;

public interface SceneSubject {

	void register(SceneObserver oberserver);

	void notifyObserver();
}
