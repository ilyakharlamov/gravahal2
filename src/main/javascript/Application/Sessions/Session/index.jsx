/*global console*/
import React from 'react';

export default class Session extends React.Component {
  constructor () {
    this.joinGameSession = this.joinGameSession.bind(this);
  }

  joinGameSession () {
    this.props.flux.getActions('gravahalapp').joinGameSession({uuid: this.props.uuid});
  }

  render () {
    const players = this.props.players && this.props.players.map((p)=>{
      return <div>{p.name}</div>;
    });
    const joinButton = this.props.players.length === 1 && <input className="Session_join_button" type="button" value="Join" onClick={this.joinGameSession}/>;
    return <div>{this.props.uuid} players: {players}
      {joinButton}
    </div>;
  }
}
