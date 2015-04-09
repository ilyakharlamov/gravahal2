import React from 'react';
require('./style.sass');

export default class Playerboard extends React.Component {
	handleClick (idx, e) {
		e.preventDefault();
		this.props.flux.getActions('gravahalapp').playTurn({pitid: idx});
	}

	render () {
		const playerboard = this.props.playerboard;
		const yourTurn = playerboard.isYourTurn && <h4>Now it is your turn</h4>;
		const playerGravahal = <td rowSpan="2" className="pit yours">{playerboard.yourGravahal.stones}</td>;
		var idx = playerboard.playerPits && playerboard.playerPits.length;
		var playerPits = [];
		while (idx--) {
			var pit = playerboard.playerPits[idx];
			var td;
			if (playerboard.isYourTurn && pit.stones) {
				td = <td className="pit yours"><a href='#' onClick={this.handleClick.bind(this, idx + 1)}>{pit.stones}</a></td>;
			} else {
				td = <td className="pit yours">{pit.stones}</td>;
			}
			playerPits.push(td);
		}
		const opponentPits = playerboard.opponentPits && playerboard.opponentPits.map((pit)=>{
			return <td className="pit">{pit.stones}</td>;
		});
		const opponentGravahal = <td rowSpan="2" className="pit">{playerboard.opponentGravahal.stones}</td>;
		return <div>
			{yourTurn}
			<table className="board">
				<tr>
					{opponentGravahal}
					{opponentPits}
					{playerGravahal}
				</tr>
				<tr>
					{playerPits}
				</tr>
			</table>
		</div>;
	}
}
