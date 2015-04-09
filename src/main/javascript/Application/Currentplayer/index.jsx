import React from 'react';
import Playerboard from './Playerboard';

export default class Currentplayer extends React.Component {
	render () {
		const currentPlayer = this.props.currentPlayer;
		return <div>
			<br/>
			<Playerboard flux={this.props.flux} playerboard={currentPlayer.playerboard} />
		</div>;
	}
}
