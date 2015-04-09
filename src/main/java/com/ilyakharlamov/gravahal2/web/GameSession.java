package com.ilyakharlamov.gravahal2.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

public class GameSession extends Observable implements Observer, Jsonizable{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public final UUID uuid;
	private Game game;
	private List<Player> players;
	final int MIN_NUM_OF_PLAYERS=2;
	final int MAX_NUM_OF_PLAYERS=2;
	public final int PLAYING_PITS_DEFAULT=6;
	public final int STONES_DEFAULT=6;
	public GameSession() {
		this.uuid=UUID.randomUUID();
		players = new ArrayList<Player>();
	}
	
	public void addPlayer(Player player) {
		if (players.size() >= MAX_NUM_OF_PLAYERS) throw new RuntimeException("Cannot add more players");
		players.add(player);
		if (players.size()==MIN_NUM_OF_PLAYERS) {
			this.game = new Game(players.get(0), players.get(1), PLAYING_PITS_DEFAULT, STONES_DEFAULT);
			logger.info("Game started");
			this.game.addObserver(this);
		}
		setChanged();
		notifyObservers();
	}
	public void removePlayer(Player player) {
		for (Player p:players) {
			if (p.equals(player)) {
				players.remove(player);
				if (players.size() < MIN_NUM_OF_PLAYERS) game = null;
			}
		}
		setChanged();
		notifyObservers();
	}
	@Override
	public JsonElement toJson() {
		JsonObject jsonobj = new JsonObject();
		jsonobj.putString("uuid", uuid.toString());
		jsonobj.putElement("game", this.game!=null ? this.game.toJson() : null);
		JsonArray jsonplayers = new JsonArray();
		for (Player player : players) {
			jsonplayers.add(player.toJson());
		}
		jsonobj.putArray("players", jsonplayers);
		return jsonobj;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		if (this.game!=null) this.game.deleteObserver(this);
		this.game = game;
		this.game.addObserver(this);
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable obs, Object obj) {
		this.setChanged();
		notifyObservers();
	}
}
