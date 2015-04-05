/*global describe, it, expect*/

import React from 'react/addons';
import WeatherappActions from '../WeatherappActions';

describe('WeatherappActions', function() {
  const action = new WeatherappActions();

  it('accepts a query and returns an object', () => {
      const result = action.createCitytileAttempt('London');

      expect(result.cityquery).to.equal('London');
  });
});
