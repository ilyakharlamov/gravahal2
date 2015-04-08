package com.ilyakharlamov.gravahal2.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ilyakharlamov.gravahal2.web.Game;
import com.ilyakharlamov.gravahal2.web.GameSession;
import com.ilyakharlamov.gravahal2.web.Player;

public class GameTest {

	@Test
	public void test() {
		GameSession gs = new GameSession();
		Player p1 = new Player("p1", gs);
		Player p2 = new Player("p2", gs);
		Game game = new Game(p1,p2, 6, 4);
		assertEquals(0, game.getCurrentPlayerIndex());
		game.play(4);
		assertEquals(0, game.getCurrentPlayerIndex());
		game.play(2);
		assertEquals(1, game.getCurrentPlayerIndex());
	}

}
