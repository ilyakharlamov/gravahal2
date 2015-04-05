package com.ilyakharlamov.gravahal2;

import java.io.File;
import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

public class Test extends Verticle {
	public void start() {
		container.logger().info("hello world");
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
            	System.out.println("Got request: " + req.uri());
                /*System.out.println("Headers are: ");
                for (Map.Entry<String, String> entry : req.headers()) {
                  System.out.println(entry.getKey() + ":" + entry.getValue());
                }
                req.response().headers().set("Content-Type", "text/html; charset=UTF-8");
                req.response().end("<html><body><h1>Hello from vert.x!</h1></body></html>");*/
            	
            	RouteMatcher httpRouteMatcher = new RouteMatcher().get("/", new
            			Handler<HttpServerRequest>() {
            				@Override
            				public void handle(final HttpServerRequest request) {
            					request.response().sendFile("static/chat.html");
            				}
            			}).get(".*\\.(css|js)$", new Handler<HttpServerRequest>() {
            				@Override
            				public void handle(final HttpServerRequest request) {
            					request.response().sendFile("static/" + new File(request.path()));
            				}
            			});
            			 
            			vertx.createHttpServer().requestHandler(httpRouteMatcher).listen(8080, "localhost");
            	
            	//req.response().setChunked(true).write("Hello World").end();;
                //String file = req.path().equals("/") ? "index.html" : req.path();
                //req.response().sendFile("webroot/" + file);
            }
        }).listen(8080);
    }
}
