import React from 'react';
import Citytile from './Citytile';

require('./style.sass');

export default class Tiles extends React.Component {
  _onCloseTile(uid) {
    this.props.flux.getActions('weatherapp').removeTile(uid);
  }

  render() {
    if (!this.props.citytiles.size) {
      return null;
    }
    return <div className="tiles-container">
      {this.props.citytiles.map((citytile, uid) => <Citytile
        key={uid}
        index={uid}
        cityname={citytile.cityname}
        icon={citytile.icon}
        temp={citytile.temp}
        onClose={this._onCloseTile.bind(this, uid)} />
        ).toArray()}
    </div>;
  }
}
