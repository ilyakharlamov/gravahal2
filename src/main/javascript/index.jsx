// IMPORTANT: This needs to be first to get around CSS order
// randomeness in webpack.
require('./reset.css');

import React from 'react';
import FluxComponent from 'flummox/component'
import Application from './Application';
import GravahalFlux from './Flux/GravahalFlux';

const flux = new GravahalFlux();

React.render(
  <FluxComponent
    flux={flux}
    render={() => <Application />} />, document.getElementById('app'));
