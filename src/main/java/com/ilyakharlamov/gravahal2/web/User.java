package com.ilyakharlamov.gravahal2.web;

import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;

public class User {
	private String name;
	private String writeHandlerId;
	private EventBus eventBus;
	

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
	
	public void sendMessageAction(String actionname, JsonObject payload) {
		JsonObject message = new JsonObject();
		message.putString("action", actionname);
		message.putObject("payload", payload);
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

}
