import { Actions } from 'flummox';

export default class WeatherappActions extends Actions {
  createCitytileAttempt (cityquery, intervalsec) {
    return {
      cityquery,
      intervalsec,
    };
  }

  removeTile (uid) {
    return uid;
  }

  requestUpdate (uid, cityquery, intervalsec) {
    return {
      uid,
      cityquery,
      intervalsec,
    };
  }
}

