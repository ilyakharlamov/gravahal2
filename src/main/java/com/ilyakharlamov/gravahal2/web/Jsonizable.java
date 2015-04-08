package com.ilyakharlamov.gravahal2.web;

import java.io.Serializable;

import org.vertx.java.core.json.JsonElement;

public interface Jsonizable extends Serializable {
	JsonElement toJson ();
}
