import {Flux} from 'flummox';
import WeatherappActions from './Actions/WeatherappActions';
import GravahalActions from './Actions/GravahalActions';
import WeatherappStore from './Stores/WeatherappStore';
import GravahalStore from './Stores/GravahalStore';

export default class WeatherappFlux extends Flux {
	constructor () {
		super();
		this.createActions('weatherapp', WeatherappActions);
		this.createActions('gravahalapp', GravahalActions);
		this.createStore('weatherapp', WeatherappStore, this);
		this.createStore('gravahalapp', GravahalStore, this);
	}
}
