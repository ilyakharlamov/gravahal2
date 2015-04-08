package com.ilyakharlamov.gravahal2.web;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;

public class Board extends Observable {
		protected final Logger logger = LoggerFactory.getLogger(this.getClass());
		private Pit  []  pits;
		public final int playingPits;
		public final int totalPits;
		public final int stones;
		private static final int   PLAYING_PITS_DEFAULT = 6;
		private static final int   STONES_DEFAULT = 4;
		
		public Board(int playingPits, int stones)  {
			if (playingPits < 2) throw new IllegalArgumentException("Number of playing pits should be 2 or more");
			if (stones < 1) throw new IllegalArgumentException("Number of stones should be 1 or more");
			this.playingPits=playingPits;
			this.stones=stones;
			totalPits = 2*(playingPits+1);
			pits  =  new  Pit[totalPits];
	        for  (int  pitIndex  =  0;  pitIndex  <  totalPits;  pitIndex++)
	        	pits[pitIndex]  =  new Pit();
		}
		/*
		 * Dictionary:
		 * pitIdx is the index of pit in the array, 0-based
		 * pitNum is the id of the players' pits, 0-based but 0 is the gravahal
		 * you read pitNum 0 but cannot play on it
		 *  */
		
		public Board()  {
	        this(PLAYING_PITS_DEFAULT,STONES_DEFAULT);
	    }
		
		
		public  void  emptyStonesIntoGravahal()  {
	        for  (int  player  =  0;  player  <  2;  player++)
	           for  (int  pitNum  =  0;  pitNum  <=  playingPits;  pitNum++)  {
	               int  stones  =  pits[getPitIdx(player,pitNum)].removeStones(); 
	               pits[getGravahalPitIdx(player)].addStones(stones);
	           }
	    }
		public  boolean  gameOver()  {
			for  (int  player  =  0;  player  <  2;  player++)  {
		        int  stones  =  0;
			    for  (int  pitNum  =  1;  pitNum  <=  playingPits; pitNum++)
					stones  +=  pits[getPitIdx(player, pitNum)].getStones();
				if  (stones  ==  0)
	                 return  true;
	        }
	        return  false;
	    }
	    
		public int  getGravahalPitIdx(int  playerNum)  {
	        return  playerNum  *  (playingPits+1);
	    }

	    public  int  getPitIdx(int  playerNum, int  pitNum) {
	    	if (pitNum > playingPits) throw new IllegalArgumentException(String.format("Pitnum must be less or equal of %s", playingPits));
	        return  playerNum  *  (playingPits+1)  +  pitNum;
	    }

	    public Pit getPit(int pitIdx) {
	    	return pits[pitIdx];
	    }
	    
	    public Pit[] getPits() {
			return pits;
		}

		public  boolean  isGravahal(int  pitIdx)  {
	        return  pitIdx  %  (playingPits+1)  ==  0;
	    }
		
		/* @param  currentPlayerNum - 0 or 1 to identify the player
		 * @param  chosenPitNum -  pit has been selected by the player
		 * @return boolean - true means the current player has to play another turn
		 *                 - false means the other player has to play 
		 */
		public  boolean  move(int  currentPlayerNum,  int  chosenPitNum)  {
			if (chosenPitNum==0) throw new IllegalArgumentException("You cannot play on gravahal pit");
			if (chosenPitNum > playingPits) throw new IllegalArgumentException(String.format("There is no such pit: {}",chosenPitNum));
			int  pitIndex  =  getPitIdx(currentPlayerNum, chosenPitNum);
	        int  stones  =  pits[pitIndex].removeStones();
	        while  (stones  !=  0)  {
	           pitIndex--;
	           if  (pitIndex  <  0)
	                   pitIndex  =  totalPits  -  1;
	           if  (pitIndex  !=  getGravahalPitIdx(otherPlayerIndex(currentPlayerNum)))  {
		               pits[pitIndex].addStones(1);
		               stones--;
	           }
	        }
	        logger.info(String.format("move pitIndex:%s, currentPlayerNum:%s, getGravahal(currentPlayerNum):%s",pitIndex, currentPlayerNum, getGravahalPitIdx(currentPlayerNum)));
	        if  (pitIndex  ==  getGravahalPitIdx(currentPlayerNum))
	           return  true;
	        if  (pitOwner(pitIndex)  ==  currentPlayerNum  && pits[pitIndex].getStones()  ==  1)  {
				stones  =  pits[oppositePitIndex(pitIndex)].removeStones();
				pits[getGravahalPitIdx(currentPlayerNum)].addStones(stones);
	        }
		    return false;
		}

	    public  int  oppositePitIndex(int  pitIndex)  {
			return  totalPits  -  pitIndex;
		}

	    public  int  otherPlayerIndex(int  playerNum)  {
			if  (playerNum  ==  0)
				return  1;
			else
				return  0;
		}

		public  int  pitOwner(int  pitIndex)  {
	        return  pitIndex  /  (playingPits+1);
		}

		public void setPits(Pit[] pits) {
			this.pits = pits;
		}

		public  void  setUpForPlay()  {
	        for  (int  pitIndex  =  0;  pitIndex  <  totalPits;  pitIndex++)
	               if  (!isGravahal(pitIndex))
	                   pits[pitIndex].addStones(stones);
	    }

		public  int  stonesInGravahal(int  playerNum)  {
			return  pits[getGravahalPitIdx(playerNum)].getStones();
	    }

	    public  int  stonesInPit(int  playerNum, int pitNum)  {
	        return  pits[getPitIdx(playerNum, pitNum)].getStones();
	    }

}
