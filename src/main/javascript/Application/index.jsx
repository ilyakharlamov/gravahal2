/*global console*/
import React from 'react';
import FluxComponent from 'flummox/component'
import Loginform from './Loginform'

require('./style.sass');

export default class Application extends React.Component {
  render() {
    return <FluxComponent
      connectToStores="gravahalapp"
      render={storeState => {
        const loginArea = storeState.name ? <div>Hello, {storeState.name}</div> : <Loginform {...storeState} />;
        return <div className="Application">
          <h1 className="Application__header">Gravahal FU by ilya.kharlamov@gmail.com</h1>
          {loginArea}
        </div>;
      }} />;
  }
}
