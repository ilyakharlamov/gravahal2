/*global describe, it, expect*/

import React from 'react/addons';
import Application from '../index';
import WeatherappFlux from '../../Flux/WeatherappFlux';
import FluxContainer from 'flummox/component';

describe('Application', () => {
  it('displays the header', () => {
    const TestUtils = React.addons.TestUtils;

    const flux = new WeatherappFlux();

    const application = TestUtils.renderIntoDocument(
      <FluxContainer
        flux={flux}
        render={() => <Application />} />
    );

    const label = TestUtils.findRenderedDOMComponentWithTag(application, 'h1');
    const labelDOM = React.findDOMNode(label);

    expect(labelDOM.textContent).to.equal('Weatherapp by ilya.kharlamov@gmail.com');
  });
});
