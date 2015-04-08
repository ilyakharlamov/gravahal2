import React from 'react';
require('./style.sass');

export default class Playerboard extends React.Component {
	handleClick (idx, e) {
		e.preventDefault();
		console.log('click', this, arguments);
		console.log('this.props.flux', this.props.flux);
		this.props.flux.getActions('gravahalapp').playTurn({pitid: idx});
	}

	render () {
		const playerboard = this.props.playerboard;
		const yourTurn = playerboard.isYourTurn && <h4>Your turn</h4>;
		const playerGravahal = <td rowSpan="2" className="gravahal">{playerboard.yourGravahal.stones}</td>;
		var idx = playerboard.playerPits && playerboard.playerPits.length;
		var playerPits = [];
		while (idx--) {
			var pit = playerboard.playerPits[idx];
			var td;
			if (playerboard.isYourTurn) {
				td = <td><a href='#' onClick={this.handleClick.bind(this, idx + 1)}>{pit.stones}</a></td>;
			} else {
				td = <td>{pit.stones}</td>;
			}
			playerPits.push(td);
		}
		const opponentPits = playerboard.opponentPits && playerboard.opponentPits.map((pit)=>{
			return <td>{pit.stones}</td>;
		});
		const opponentGravahal = <td rowSpan="2" className="gravahal">{playerboard.opponentGravahal.stones}</td>;
		return <div>
			{yourTurn}
			<table className="board">
				<tr>
					{opponentGravahal}
					{opponentPits}
				</tr>
				<tr>
					{playerPits}
					{playerGravahal}
				</tr>
			</table>
		</div>;
	}
}
