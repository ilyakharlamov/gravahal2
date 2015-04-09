package com.ilyakharlamov.gravahal2.web;

import java.util.Observable;
import java.util.Observer;

import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

import com.fasterxml.jackson.databind.JsonNode;

public class User implements Observer{
	private String name;
	private String writeHandlerId;
	private EventBus eventBus;
	private Player currentPlayer;
	private GameSession gameSession;
	private SessionStorage availableGamesessions;
	

	public User(EventBus eventBus, String writeHandlerId) {
		if (writeHandlerId == null || writeHandlerId.length()==0) throw new RuntimeException("writeHandler must exist");
		if (eventBus == null) throw new RuntimeException("eventBus must exist");
		this.writeHandlerId=writeHandlerId;
		this.eventBus = eventBus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.sendMessageAction("nameChanged", new JsonObject().putString("name", this.name));
	}
	
	public void sendMessageAction(String actionname, JsonElement payload) {
		JsonObject message = new JsonObject();
		message.putString("action", actionname);
		message.putElement("payload", payload);
		this.sendMessage(message);
	}
	
	public void sendMessage(JsonObject json) {
	    Buffer buff = new Buffer();
	    buff.appendString(json.toString());
		eventBus.send(this.writeHandlerId, buff);
	}

	public String toString () {
		return String.format("User:%s", name);
	}
	

	public void setSession(GameSession gameSession) {
		if (this.gameSession!=null) this.gameSession.deleteObserver(this);
		this.gameSession = gameSession;
		this.gameSession.addObserver(this);
		this.sendMessageAction("gamesessionChanged", gameSession.toJson());
	}

	public void setAvailableGamesessions(SessionStorage availableGamesessions) {
		if (this.availableGamesessions!=null) this.availableGamesessions.deleteObserver(this);
		this.availableGamesessions = availableGamesessions;
		this.availableGamesessions.addObserver(this);
		this.sendMessageAction("availableGamesessionsChanged", availableGamesessions.toJson());
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		if (this.currentPlayer!=null) this.currentPlayer.deleteObservers();
		if (currentPlayer==null) return;
		this.currentPlayer = currentPlayer;
		this.currentPlayer.addObserver(this);
	}

	@Override
	public void update(Observable obs, Object obj) {
		if (obs instanceof GameSession) this.sendMessageAction("gamesessionChanged", gameSession.toJson());
		if (obs instanceof SessionStorage) this.sendMessageAction("availableGamesessionsChanged", availableGamesessions.toJson());
		if (obs instanceof Player) {
			this.sendMessageAction("currentPlayerChanged", currentPlayer.toJson());
		}
	}

}
