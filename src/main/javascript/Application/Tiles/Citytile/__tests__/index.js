/*global describe, it, expect*/

import React from 'react/addons';
import Citytile from '../index.jsx';
import $ from 'jQuery';

describe('TodoListItem', () => {
  const TestUtils = React.addons.TestUtils;

  const factory = (props = {
      uid: 1,
      cityname: "Munich",
    }) => {
    return TestUtils.renderIntoDocument(
      <Citytile {...props} onClose={() => {}} />
    );
  };

  it('fills the cityname', () => {
    const item = factory();
    const domnode = React.findDOMNode(item);
    expect($(domnode).find('.tile-item-cityname').text()).to.equal('Munich');
  });
});
