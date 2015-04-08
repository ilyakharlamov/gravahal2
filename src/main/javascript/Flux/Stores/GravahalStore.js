/*global window,console,vertx*/
import $ from 'jQuery';
import { Store } from 'flummox';
import Immutable from 'immutable';
import SockJS from 'sockjs-client';
//import vertx from 'vertx-eventbus-client';
//import vertx from 'vertxbus.js';
import EventBus from '../../vertxbus'

export default class GravahalStore extends Store {
  constructor ( flux ) {
    super();
    this.flux = flux;
    const actionids = flux.getActionIds('gravahalapp');
    this.actionids = actionids;
    this.register(actionids.attemptLogin, this.handleAttemptLogin);
    //this.register(actionids.setNameConfirm, this.handleSetNameConfirm);
    //
    this.register(actionids.connectConfirm, this.handleConnectConfirm);
    //
    this.register(actionids.nameChanged, this.handleNameChanged);
    //
    this.register(actionids.availableGamesessionsChanged, this.handleAvailableGamesessionsChanged);
    //
    this.register(actionids.createAndJoinGameSession, this.handleCreateAndJoinGameSession);
    //
    this.register(actionids.joinGameSession, this.handleJoinGameSession);
    //
    this.register(actionids.gamesessionChanged, this.handleGamesessionChanged);
    //
    this.register(actionids.currentPlayerChanged, this.handleCurrentPlayerChanged);
    //
    this.register(actionids.playTurn, this.handlePlayTurn);

    this.sock = new SockJS(`${window.location.href}eventbus`);
    this.sock.onmessage = this.onMessage.bind(this);
    this.setState({});
  }

  notifyServer (data) {
    window.data = data;
    this.sock.send(JSON.stringify(data));
  }

  _changeState (fieldname, data) {
    var state = this.state;
    state[fieldname] = data;
    this.setState(state);
  }

  handleAttemptLogin (data) {
    console.log(`handleAttemptLogin name: ${name}`, data);
    this.notifyServer({
      action:'setName',
      type: 'attempt',
      data,
    });
    //this.sock.close();
     /*sock.onopen = function() {
         console.log('open');
     };
     sock.onmessage = function(e) {
         console.log('message', e.data);
     };
     sock.onclose = function() {
         console.log('close');
     };

     sock.send('test');
     sock.close();*/
  }

  handleConnectConfirm (data) {
    console.log("handleConnectConfirm");
  }

  handleNameChanged (payload) {
    var state = this.state;
    state.name=payload.name
    this.setState(state);
  }

  handleAvailableGamesessionsChanged (payload) {
    var state = this.state;
    state.availableGamesessions = payload;
    this.setState(state);
  }

  handleCreateAndJoinGameSession () {
    this.notifyServer({
      action: "createAndJoinGameSession",
      data: {},
    });
  }

  handleJoinGameSession (payload) {
    this.notifyServer({
      action: "joinGameSession",
      data: payload,
    });
  }

  handleGamesessionChanged (payload) {
    this._changeState("gamesession", payload);
  }

  handleCurrentPlayerChanged (payload) {
    console.log("handleCurrentPlayerChanged payload:", payload);
    this._changeState("currentPlayer", payload);
  }

  handlePlayTurn (payload) {
    this.notifyServer({
      action: "playTurn",
      data: payload,
    });
  }

  onMessage (message) {
    console.log("onmessage", message, 'data', data);
    const data = JSON.parse(message.data);

    //actionName
    const actionName = data && data.action;
    console.log("onmessage actionName:", actionName, "data", data);
    const action = this.flux.getActions('gravahalapp')[actionName];
    console.log(typeof action);
    if ( !action ) throw new Error(`Action "${actionName}" is not found`);
    //payload
    const payload = data.payload;
    action.call(action, payload);
  }
}