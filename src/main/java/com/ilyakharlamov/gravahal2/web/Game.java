package com.ilyakharlamov.gravahal2.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.impl.JsonCodec;

public class Game extends Observable implements Observer, Jsonizable{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private int  currentPlayerIndex  =  0;
	Map<Player, Integer> playerToIdx = new HashMap<Player, Integer>();

	private Board  board;

	private Player  []  players;
	
	/* Initialize the game
	 * @param name0 - Player 0
	 * @param name1 - Player 1
	 */
	public Game(Player player0, Player player1, int playingPits, int stones)  {
		logger.info("Player init");
        board  =  new Board(playingPits, stones);
        board.addObserver(this);
        board.setUpForPlay();
        playerToIdx.put(player0, 0);
        playerToIdx.put(player1, 1);
        players  =  new Player[2];
        players[playerToIdx.get(player0)]=player0;
        players[playerToIdx.get(player1)]=player1;
        currentPlayerIndex  =  0;
		setChanged();
		notifyObservers();
    }

	public Board getBoard() {
		return board;
	}
	
	public int getOpponentIdx (int idx) {
		return idx == 0 ? 1 : 0;	
	}
	
	public Player getOpponent (Player player) {
		int playerIdx = playerToIdx.get(player);
		int opponentIdx = playerIdx == 0 ? 1 : 0;
		return players[opponentIdx];	
	}

	public Player getCurrentPlayer() {
		return players[currentPlayerIndex];
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	public Player[] getPlayers() {
		return players;
	}

	/*
	 * 
	 */
	public void play(int pitNum)  {
		logger.info("play"+pitNum);
	    boolean  checkTurn  =  board.move(currentPlayerIndex, pitNum); 
		logger.info("checkTurn is "+checkTurn);
		if  (!checkTurn)         			
			if  (currentPlayerIndex  ==  0)
					currentPlayerIndex  =  1;
			else
				currentPlayerIndex  =  0;
		setChanged();
		notifyObservers();
	}
	
	/*
	 * @return Winner's Name or Tie 
	 *
	 */
	public Player getWinner(){
		board.emptyStonesIntoGravahal();    
		if  (board.stonesInGravahal(0)  > board.stonesInGravahal(1)){	
			return players[0];
		}
		else 
			if (board.stonesInGravahal(0)  <  board.stonesInGravahal(1)){
				return players[1];
			}
		else
			return null;
	}

    public void setBoard(Board board) {
		this.board = board;
		setChanged();
		notifyObservers();
	}

    public void setCurrentPlayerIndex(int currentPlayer) {
		this.currentPlayerIndex = currentPlayer;
		setChanged();
		notifyObservers();
	}

    public void setPlayers(Player[] players) {
		this.players = players;
		setChanged();
		notifyObservers();
	}
	@Override
	public JsonElement toJson() {
		JsonObject jsonobj = new JsonObject();
		//players
		JsonArray jsonplayers = new JsonArray();
		for (Player player : players) {
			jsonplayers.add(player.toJson());
		}
		jsonobj.putArray("players", jsonplayers);
		//board
		return jsonobj;
	}

	public JsonElement renderPlayerboard(Player player) {
		if (player==null) throw new IllegalArgumentException("Player must not be null");
		Integer playerIdx = playerToIdx.get(player);
		if (playerIdx==null) throw new IllegalArgumentException(String.format("Player %s not found in the game", player));
		Integer opponentIdx = getOpponentIdx(playerIdx);
		JsonObject playerBoardJson = new JsonObject();
		playerBoardJson.putBoolean("isYourTurn", player.equals(getCurrentPlayer()));
		playerBoardJson.putElement("yourGravahal", board.getPits()[board.getGravahalPitIdx(playerIdx)].toJson());
		playerBoardJson.putElement("opponentGravahal", board.getPits()[board.getGravahalPitIdx(opponentIdx)].toJson());
		JsonArray playerPitsJson = new JsonArray();
		JsonArray opponentPitsJson = new JsonArray();
		for (int pitNum=1; pitNum<=board.playingPits; pitNum++) {
			playerPitsJson.add(board.getPit(board.getPitIdx(playerIdx, pitNum)).toJson());
			opponentPitsJson.add(board.getPit(board.getPitIdx(opponentIdx, pitNum)).toJson());
		}
		playerBoardJson.putElement("playerPits", playerPitsJson);
		playerBoardJson.putElement("opponentPits", opponentPitsJson);
		return playerBoardJson;
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}

}
