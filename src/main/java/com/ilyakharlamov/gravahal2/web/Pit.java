package com.ilyakharlamov.gravahal2.web;

import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

public class Pit implements Jsonizable {
	private int stones;

	Pit() {
		this.stones = 0;
	}

	public void addStones(int stones) {
		this.stones += stones;
	}

	public int getStones() {
		return stones;
	}

	public boolean isEmpty() {
		return stones == 0;
	}

	public int removeStones() {
		int stones = this.stones;
		this.stones = 0;
		return stones;
	}

	public void setStones(int stones) {
		this.stones = stones;
	}
	@Override
	public JsonElement toJson() {
		JsonObject jsonobj = new JsonObject();
		jsonobj.putBoolean("isEmpty", isEmpty());
		jsonobj.putNumber("stones", stones);
		return jsonobj;
	}
	
	public String toString () {
		return "{"+String.valueOf(stones)+"}";
	}

}
