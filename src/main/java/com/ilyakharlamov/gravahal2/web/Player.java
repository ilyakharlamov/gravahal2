package com.ilyakharlamov.gravahal2.web;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

public class Player extends Observable implements Jsonizable, Observer {
	private final String name;
	private final GameSession gameSession;

	public Player(String name, GameSession gameSession) {
		if (name==null || name.length()==0) throw new IllegalArgumentException("Player name cannot be null");
		this.name=name;
		if (gameSession==null) throw new IllegalArgumentException("GameSession cannot be null");
		this.gameSession = gameSession;
		this.gameSession.deleteObserver(this);
		this.gameSession.addObserver(this);
	}

	public GameSession getGameSession() {
		return gameSession;
	}

	@Override
	public JsonElement toJson() {
		JsonObject jsonobj = new JsonObject();
		jsonobj.putString("name", name);
		Game game = gameSession.getGame();
		if (game != null) {
			jsonobj.putElement("playerboard", game.renderPlayerboard(this));			
		}
		return jsonobj;
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}


}
