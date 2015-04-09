package com.ilyakharlamov.gravahal2;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.sockjs.EventBusBridgeHook;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.platform.Verticle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ilyakharlamov.gravahal2.web.Game;
import com.ilyakharlamov.gravahal2.web.GameSession;
import com.ilyakharlamov.gravahal2.web.Player;
import com.ilyakharlamov.gravahal2.web.SessionStorage;
import com.ilyakharlamov.gravahal2.web.User;

public class WebserverVerticle extends Verticle {
	private static final Map<String,String> contentTypeJson;
	static
    {
		contentTypeJson = new HashMap<String, String>();
		contentTypeJson.put("Content-Type", "application/json; charset=UTF-8");
    }
	
	@Override
	public void start() {
		final Pattern gameeventsPattern = Pattern.compile("/gameevents/(\\w+)");
		final EventBus eventBus = vertx.eventBus();
		final Logger logger = container.logger();
		final SessionStorage sessionStorage = new SessionStorage();
 
		// 1) HTTP Server
		RouteMatcher httpRouteMatcher = new RouteMatcher().get("/", new
				Handler<HttpServerRequest>() {
					@Override
					public void handle(final HttpServerRequest request) {
						request.response().sendFile("web/index.html");
					}
				}).get(".*\\.(css|js)$", new Handler<HttpServerRequest>() {
					@Override
					public void handle(final HttpServerRequest request) {
						request.response().sendFile("web/" + new File(request.path()));
					}
				}).get("/sessions", new Handler<HttpServerRequest>() {
					@Override
					public void handle(final HttpServerRequest request) {
						HttpServerResponse resp = request.response();
						resp.headers().add(WebserverVerticle.contentTypeJson);
						resp.end(sessionStorage.toString());
					}
				});
				 
		HttpServer httpServer = vertx.createHttpServer();
 
		httpServer.requestHandler(httpRouteMatcher);
		
		JsonObject config = new JsonObject().putString("prefix", "/eventbus");
        JsonArray inboundPermitted = new JsonArray();
        inboundPermitted.add(new JsonObject().putString("address", "msg.from.client"));
        
        JsonArray outboundPermitted = new JsonArray();
        outboundPermitted.add(new JsonObject().putString("address", "msg.server.1"));
        outboundPermitted.add(new JsonObject().putString("address", "msg.server.2"));
        
        SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);
        sockJSServer = sockJSServer.installApp(config, new Handler<SockJSSocket>() {
		     public void handle(final SockJSSocket sock) {
		        logger.info("New session detected! sock.remoteAddress()"+sock.remoteAddress()
		        		+"\n\t sock.localAddress():"+sock.localAddress()
		        		+"\n\t sock.writeHandlerID():"+sock.writeHandlerID()
		        		+"\n\t sock.headers():"+sock.headers()
		        		+"\n\t sock.uri():"+sock.uri()
		        		);
		        final User user = new User(vertx.eventBus(), sock.writeHandlerID());
		        user.setAvailableGamesessions(sessionStorage);

		        // Message handler
		        sock.dataHandler(new Handler<Buffer>() {
		           public void handle(Buffer buffer) {
		              logger.info("In dataHandler, buffer"+buffer);
		              JsonObject msg = new JsonObject(buffer.toString());
		              String action = msg.getString("action");
		              String type = msg.getString("type");
		              JsonObject data = msg.getObject("data");
		              if (!"ping".equals(type) && (action == null|| action.length()==0)) logger.error(String.format("data %s expected to contain action", buffer));
		              if ("setName".equals(action)) {
		            	  user.setName(data.getString("name"));
		              } else if ("createAndJoinGameSession".equals(action)) {
		            	  GameSession gameSession = new GameSession();
		            	  sessionStorage.addSession(gameSession);
		            	  Player player = new Player(user.getName(), gameSession);
		            	  gameSession.addPlayer(player);
		            	  user.setSession(gameSession);
		            	  user.setCurrentPlayer(player);
		              } else if ("joinGameSession".equals(action)) {
		            	  String uuid = data.getString("uuid");
		            	  logger.info("uuid:"+uuid);
		            	  GameSession gameSession = sessionStorage.getSession(uuid);
		            	  logger.info("Session"+gameSession);
		            	  Player player = new Player(user.getName(), gameSession);
		            	  user.setSession(gameSession);
		            	  user.setCurrentPlayer(player);
		            	  gameSession.addPlayer(player);
		              } else if ("playTurn".equals(action)) {
		            	  logger.info("playTurn");
		            	  int pitid = data.getNumber("pitid").intValue();
		            	  Player player = user.getCurrentPlayer();
		            	  Game game = player.getGameSession().getGame();
		            	  game.play(pitid);
		              }
		           }
		        });

		        // Session end handler
		        sock.endHandler(new Handler<Void>() {
		           @Override
		           public void handle(Void arg) {
		              logger.info("In endHandler");
		           }
		        });
		     }
		  });
        
        sockJSServer.bridge(config, inboundPermitted, outboundPermitted);
        /*vertx.eventBus().registerHandler("msg.from.client",new Handler<Message<JsonObject>>() {
            public void handle(Message<JsonObject> event) {
            	logger.info("on msg.client.1 event.replyAddress():"+event.replyAddress()+" event.body():"+event.body()+" event.address:"+event.address());
            }
        });*/
		httpServer.listen(8080);

	}
}
