import React from 'react';
import classnames from 'classnames';
require('./style.sass');

export default class Citytile extends React.Component {
  render() {
    const id = `citytile-item-${this.props.index}`;

    return <div className="tile-item">
      <div className="tile-item-close"><a href="#" onClick={this.props.onClose}>x</a></div>
      <div className="tile-item-inner">
        <label htmlFor={id} className="tile-item-cityname">
          {this.props.cityname}
        </label><br/>
        <img src={this.props.icon} /><br/>
        <label>
          {this.props.temp}
        </label>
      </div>
    </div>;
  }
}
