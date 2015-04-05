// IMPORTANT: This needs to be first to get around CSS order
// randomeness in webpack.
require('./reset.css');

import React from 'react';
import FluxComponent from 'flummox/component'
import Application from './Application';
import WeatherappFlux from './Flux/WeatherappFlux';

const flux = new WeatherappFlux();

React.render(
  <FluxComponent
    flux={flux}
    render={() => <Application />} />, document.getElementById('app'));
