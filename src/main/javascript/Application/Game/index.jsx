import React from 'react';
import Board from './Board';

export default class Game extends React.Component {
	render () {
		const game = this.props.game;

		var message = game ? `Game started` : `Game not started yet, waiting for more players`;
		const players = game && game.players && game.players.map((p)=>{
			return <div>{p.name}</div>;
		});
		const board = game && <Board board={game.board} />;
		return <div>
			<div>{message}</div>
			<div>Players: {players}</div>
			<br/>
			{board}
		</div>;
	}
}