/*global describe, it, expect*/
import React from 'react/addons';
import Tiles from '../index.jsx';
import Immutable from 'immutable';
import $ from 'jQuery';

describe('Tiles', () => {
  const TestUtils = React.addons.TestUtils;

  const factory = (props = {
      citytiles: new Immutable.OrderedMap({1: {cityname: "Lausanne"}, 2: {cityname: "Berlin"}}),
    }) => {
    return TestUtils.renderIntoDocument(
      <Tiles {...props} />
    );
  };

  it('creates the tiles', () => {
    const item = factory();
    const domitem = React.findDOMNode(item);
    expect($(domitem).find('.tile-item').length).to.equal(2);
  });
});

