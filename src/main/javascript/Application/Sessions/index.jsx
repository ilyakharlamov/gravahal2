/*global console*/
import React from 'react';
import Session from './Session';

export default class Sessions extends React.Component {
  constructor () {
    this.createAndJoinGameSession = this.createAndJoinGameSession.bind(this);
  }

  createAndJoinGameSession () {
    this.props.flux.getActions('gravahalapp').createAndJoinGameSession();
  }

  render () {
    console.log('render');
    const sessions = this.props.sessions && this.props.sessions.length ? this.props.sessions.map((i)=>{
      return <Session flux={this.props.flux} uuid={i.uuid} players={i.players} />;
    }) : <div>There are no active game sessions. You can create one.</div>;
    console.log("sessions", sessions);
    return <div>
      {sessions}
      <br />
      <input className="Sessions_newsessionbutton" type="button" value="New game session" onClick={this.createAndJoinGameSession}/>
    </div>;
  }
}