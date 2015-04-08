package com.ilyakharlamov.gravahal2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ilyakharlamov.gravahal2.web.Board;

public class BoardTest {

	@Test
	public void testSetup() {
		Board board = new Board(6,6);
		board.setUpForPlay();
		assertEquals(14, board.getPits().length);
		assertEquals(14, board.totalPits);
	}
	
	@Test
	public void testGravahalPitIdx() {
		Board board = new Board(6,4);
		board.setUpForPlay();
		assertEquals(0, board.getGravahalPitIdx(0));
		assertEquals(7, board.getGravahalPitIdx(1));
		assertTrue(board.isGravahal(board.getGravahalPitIdx(0)));
		assertTrue(board.isGravahal(board.getGravahalPitIdx(0)));
	}
	
	@Test
	public void testNumberOfStonesInit() {
		Board board = new Board(6,4);
		board.setUpForPlay();
		assertEquals(0, board.getPits()[board.getGravahalPitIdx(0)].getStones());
		assertEquals(0, board.getPits()[board.getGravahalPitIdx(1)].getStones());
	}
	
	@Test
	public void testPitIdx() {
		boolean isExceptionThrown;
		Board board = new Board(6,4);
		board.setUpForPlay();
		isExceptionThrown = false;
		try{
			board.getPitIdx(0, 7);
		} catch (IllegalArgumentException e) {
			isExceptionThrown = true;
		}
		assertTrue(isExceptionThrown);
		isExceptionThrown = false;
		try{
			board.getPitIdx(0, 6);
		} catch (IllegalArgumentException e) {
			isExceptionThrown = true;
		}
		assertFalse(isExceptionThrown);
		assertEquals(1, board.getPitIdx(0, 1));
	}
	

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidMoveOnGravahal() {
		Board board = new Board(6,4);
		board.setUpForPlay();
		board.move(0, 0);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidMoveOutside() {
		Board board = new Board(6,4);
		board.setUpForPlay();
		board.move(0, 7);
	}
	@Test
	public void testEmptyAfterMove() {
		Board board = new Board(6,4);
		board.setUpForPlay();
		int pitnum=3;
		int playernum = 0;
		board.move(playernum, pitnum);
		assertEquals(0, board.getPits()[board.getPitIdx(playernum, pitnum)].getStones());
	}
	
	@Test
	public void testIncreaseAfterMove() {
		final int playingstones = 4;
		Board board = new Board(6,playingstones);
		board.setUpForPlay();
		final int pitnum=3;
		final int nextpitnum= pitnum-1;
		final int playernum = 0;
		assertEquals(playingstones, board.getPits()[board.getPitIdx(playernum, nextpitnum)].getStones());
		board.move(playernum, pitnum);
		assertEquals(playingstones+1, board.getPits()[board.getPitIdx(playernum, nextpitnum)].getStones());
	}
	
	@Test
	public void testNextTurn() {
		Board board = new Board(6,4);
		board.setUpForPlay();
		final int playernum = 0;
		final boolean isNextTurn = board.move(playernum, 4);
		assertEquals(1, board.getPits()[board.getPitIdx(playernum, 0)].getStones());
		assertTrue(isNextTurn);
	}
}
