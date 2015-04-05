package com.ilyakharlamov.gravahal2.web;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SessionStorage {
	
	private Map<Long, GameSession> sessions = new HashMap<Long, GameSession>();
	private static ObjectMapper objectMapper = new ObjectMapper();

	public JsonNode toJson() {
		JsonNode jsonnode = new ObjectNode(null);
		return jsonnode;
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

}
