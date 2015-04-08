package com.ilyakharlamov.gravahal2.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;
import java.util.Observer;
import java.util.UUID;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SessionStorage extends Observable implements Observer, Jsonizable {
	
	final private Map<String, GameSession> sessions = new HashMap<String, GameSession>();
	private static ObjectMapper objectMapper = new ObjectMapper();

	public JsonElement toJson() {
		JsonArray jsonarr = new JsonArray();
		Iterator<Entry<String, GameSession>> iter = sessions.entrySet().iterator(); 
		while (iter.hasNext()) {
			Entry<String, GameSession> entry = iter.next();
			jsonarr.add(entry.getValue().toJson());
		}
		return jsonarr;
	}
	public GameSession getSession (String uuid) {
		return sessions.get(uuid);
	}
	@Override
	public String toString() {
		try {
			return objectMapper.writeValueAsString(this.toJson());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void addSession(GameSession gameSession) {
		gameSession.deleteObserver(this);
		gameSession.addObserver(this);
		sessions.put(gameSession.uuid.toString(), gameSession);
		setChanged();
		notifyObservers();
	}
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}

}
