import { Actions } from 'flummox';

export default class GravahalActions extends Actions {
  attemptLogin (name) {
    return {
      name
    };
  }
  connectConfirm (payload) {
    return payload;
  }

  nameChanged (payload) {
    return payload;
  }

  availableGamesessionsChanged (payload) {
    return payload;
  }

  createAndJoinGameSession () {
    return {};
  }

  joinGameSession (payload) {
    return payload;
  }

  gamesessionChanged (payload) {
    return payload;
  }

  currentPlayerChanged (payload) {
    return payload;
  }

  playTurn (payload) {
    return payload;
  }
}

