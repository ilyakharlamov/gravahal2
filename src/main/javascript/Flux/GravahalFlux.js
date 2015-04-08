import {Flux} from 'flummox';
import GravahalActions from './Actions/GravahalActions';
import GravahalStore from './Stores/GravahalStore';

export default class GravahalFlux extends Flux {
	constructor () {
		super();
		this.createActions('gravahalapp', GravahalActions);
		this.createStore('gravahalapp', GravahalStore, this);
	}
}
