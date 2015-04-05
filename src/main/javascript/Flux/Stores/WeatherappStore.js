/*global setTimeout */
import { Store } from 'flummox';
import Weatherservice from './Data/Weatherservice';
import Immutable from 'immutable';
export default class Stores extends Store {
  constructor ( flux ) {
    super();
    this.flux = flux;
    const actionids = flux.getActionIds('weatherapp');
    this.register(actionids.createCitytileAttempt, this.handleNewCitytileAttempt);
    this.register(actionids.removeTile, this.handleRemoveTile);
    this.register(actionids.requestUpdate, this.handleRequestUpdate);
    this.weatherservice = new Weatherservice();
    this.state = {
      uidcounter: 0,
      citytiles: new Immutable.OrderedMap(),
    };
  }

  handleRemoveTile (uid) {
    this.setState({
      uidcounter: this.state.uidcounter,
      citytiles: this.state.citytiles.delete(uid),
    });
  }

  handleRequestUpdate ( param ) {
    if (!this.state.citytiles.has(param.uid)) { // stop timer if deleted
      return;
    }
    this.weatherservice.getWeather( param.cityquery ).done( (data) => {
      this.setState({
        uidcounter: this.state.uidcounter,
        citytiles: this.state.citytiles.set(param.uid, this.createCitytile(param.uid, data, param.cityquery, param.intervalsec)),
      });
    }.bind(this));
  }

  createCitytile (uid, founddata, cityquery, intervalsec) {
    return {
      cityname: founddata.cityname,
      icon: founddata.icon,
      temp: (founddata.temp).toFixed(3),
      timer: setTimeout(() => {
        this.flux.getActions('weatherapp').requestUpdate(uid, cityquery, intervalsec);
      }.bind(this), intervalsec * 1000),
    };
  }

  handleNewCitytileAttempt ( param ) {
    if (!param.cityquery) {
      this.setState({
        errormessage: "Need to enter a city name",
      });
      return;
    }
    this.weatherservice.getWeather( param.cityquery ).done( (founddata)=>{
      const newuid = this.state.uidcounter+1;
      this.setState({
        uidcounter: newuid,
        citytiles: this.state.citytiles.set(newuid, this.createCitytile(newuid, founddata, param.cityquery, param.intervalsec)),
        errormessage: null,
      });
    }.bind(this)).fail( (faildata)=>{
      this.setState({
        errormessage: `City "${faildata.cityquery}" not found`,
      });
    }.bind(this));
  }
}
