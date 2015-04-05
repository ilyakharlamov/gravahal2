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
		        user.sendMessageAction("connectConfirm", null);

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
        
        /*sockJSServer.setHook(new EventBusBridgeHook() 
        {
            @Override
            public void handleSocketClosed(SockJSSocket sock) {
            	logger.info("socket closed");
            }

            @Override
            public boolean handleSendOrPub(SockJSSocket sock, boolean send, JsonObject msg, String address) {
            	msg.putString("address", address);
            	logger.info("handleSendOrPub");
                return true;
            }

			@Override
			public boolean handleAuthorise(JsonObject arg0, String sessionId,
					Handler<AsyncResult<Boolean>> arg2) {
				logger.info("handleAuthorise");
				return true;
			}

			@Override
			public void handlePostRegister(SockJSSocket arg0, String arg1) {
				// TODO Auto-generated method stub
				logger.info("handlePostRegister");
				
			}

			@Override
			public boolean handlePreRegister(SockJSSocket arg0, String arg1) {
				// TODO Auto-generated method stub
				logger.info("handlePreRegister");
				return true;
			}

			@Override
			public boolean handleSocketCreated(SockJSSocket sock) {
				logger.info("socket created:"+sock.remoteAddress()+" "+sock.writeHandlerID());
				return true;
			}

			@Override
			public boolean handleUnregister(SockJSSocket arg0, String arg1) {
				// TODO Auto-generated method stub
				logger.info("handleUnregister");

				return true;
			}
        });*/
        sockJSServer.bridge(config, inboundPermitted, outboundPermitted);
        //vertx.eventBus().registerHandler("msg.client.1", )
       // vertx.eventBus().
        vertx.eventBus().registerHandler("msg.from.client",new Handler<Message<JsonObject>>() {
            public void handle(Message<JsonObject> event) {
            	logger.info("on msg.client.1 event.replyAddress():"+event.replyAddress()+" event.body():"+event.body()+" event.address:"+event.address());
            }
        });
        
        
        

		
		// Create SockJS Server
		/*  SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);

		  sockJSServer = sockJSServer.installApp(new JsonObject().putString("prefix", "/eventbus"), new Handler<SockJSSocket>() {

		     public void handle(final SockJSSocket sock) {
		        System.out.println("New session detected!");
		        // Message handler
		        sock.dataHandler(new Handler<Buffer>() {
		           public void handle(Buffer buffer) {
		              System.out.println("In dataHandler");
		           }
		        });

		        // Session end handler
		        sock.endHandler(new Handler<Void>() {
		           @Override
		           public void handle(Void arg) {
		              System.out.println("In endHandler");
		           }
		        });
		     }
		  });*/
		
		httpServer.listen(8080);
 
		/*// 2) Websockets Chat Server
				vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {
					@Override
					public void handle(final ServerWebSocket ws) {
						logger.info(String.format("ws.path():%s", ws.path()));
						final Matcher m = gameeventsPattern.matcher(ws.path());
						if (!m.matches()) {
							ws.reject();
							return;
						}
				 
						final String chatRoom = m.group(1);
						final String id = ws.textHandlerID();
						logger.info("registering new connection with id: " + id + " for chat-room: " + chatRoom);
						vertx.sharedData().getSet("chat.room." + chatRoom).add(id);
				 
						ws.closeHandler(new Handler<Void>() {
							@Override
							public void handle(final Void event) {
								logger.info("un-registering connection with id: " + id + " from chat-room: " + chatRoom);
								vertx.sharedData().getSet("chat.room." + chatRoom).remove(id);
							}
						});
				 
						ws.dataHandler(new Handler<Buffer>() {
							@Override
							public void handle(final Buffer data) {
				 
								ObjectMapper m = new ObjectMapper();
								try {
									JsonNode rootNode = m.readTree(data.toString());
									((ObjectNode) rootNode).put("received", new Date().toString());
									String jsonOutput = m.writeValueAsString(rootNode);
									logger.info("json generated: " + jsonOutput);
									for (Object chatter : vertx.sharedData().getSet("chat.room." + chatRoom)) {
										eventBus.send((String) chatter, jsonOutput);
									}
								} catch (IOException e) {
									ws.reject();
								}
							}
						});
				 
					}
				}).listen(8090);*/
	}
}
