/*global describe, it, expect*/
import React from 'react/addons';
import NewCityForm from '../index.jsx';

describe('NewCityForm__tests__', () => {
  const TestUtils = React.addons.TestUtils;
  const props = {};
  it('renders Form', () => {
    const form = TestUtils.renderIntoDocument(<NewCityForm {...props} />);
    expect(form).not.to.equal(null);
    const select = TestUtils.findRenderedDOMComponentWithTag(form, 'select');
    expect(select).not.to.equal(null);
  });
});
