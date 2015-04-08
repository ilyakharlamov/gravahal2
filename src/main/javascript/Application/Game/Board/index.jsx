import React from 'react';

export default class Board extends React.Component {
	render () {
		const board = this.props.board;
		const pits = board.pits && board.pits.map((pit)=>{
			return <div>{pit.stones}</div>
		});
		return <div>Board
			{pits}
		</div>;
	}
}