# Requirements
Requires java + maven installed

# Design notes
Server source code is written in java 7, browser UI is written in JavaScript/EcmaScript 6.
Uses vert.x as a server. It is fast and async, thanks to netty inside.
React.js for UI rendering, with Flux design pettern. It is very fast for DOM rendering.
Websocket as a transport between vertx and react. Failover to SockJS / long polling / iframe for old browsers or providers not supporting websocket.

# Quick start
```
git clone https://github.com/ilyakharlamov/gravahal2.git
cd gravahal2
mvn clean compile vertx:runMod
```
navigate to http://localhost:8080/ from two different browser tabs


# About
Node.js is not needed for production, it is only used during development and release, to compile SASS to CSS, minimify javascript and transcompile from EcmaScript 6 to good old javascript etc.

