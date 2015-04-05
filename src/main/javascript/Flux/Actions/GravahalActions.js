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
}

