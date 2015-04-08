/*global console*/
import React from 'react';
import FluxComponent from 'flummox/component'
import Loginform from './Loginform'
import Sessions from './Sessions';
import Game from './Game';
import Currentplayer from './Currentplayer';

require('./style.sass');

export default class Application extends React.Component {
  render() {
    return <FluxComponent
      connectToStores="gravahalapp"
      render={storeState => {
        const loginArea = storeState.name ? <div>Hello, {storeState.name}</div> : <Loginform {...storeState} />;
        const currentPlayerArea = storeState.currentPlayer && <Currentplayer flux={storeState.flux} currentPlayer={storeState.currentPlayer} />;
        const sessionArea = storeState.name && !storeState.gamesession && <Sessions sessions={storeState.availableGamesessions} {...storeState}/>;
        return <div className="Application">
          <h1 className="Application__header">Gravahal by ilya.kharlamov@gmail.com</h1>
          {loginArea}
          {currentPlayerArea}
          {sessionArea}
        </div>;
      }} />;
  }
}
