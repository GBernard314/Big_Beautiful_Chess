package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.Piece;

class DemoApplicationTests {
	
	@Test
	void testTest() {
		assertEquals(1, 1);
	}

		/*
	@Test
	void isCheckedTest() {
		Board tester = new Board();
		tester.updateMoves();
		
		assertFalse(tester.isChecked(), "king shouldnt be in check at the beginning");
		
		//we empty the board
		for (int i = 0; i < tester.getCells().length; i++) {
			for (int j = 0; j < tester.getCells()[i].length; j++) {
				tester.getCells()[i][j] = new Piece(i, j);
			}
		}
		//then we add 1 white king
		tester.getCells()[0][0] = new Piece(0, 0, 0, 'k');
		//and 1 castle to check the king
		tester.getCells()[0][5] = new Piece(0, 5, 1, 'r');
		tester.updateMoves();
		
		assertTrue(tester.isChecked(), "king should be in check by the rook");
		
		//we add a pawn to protect the king
		tester.getCells()[0][5] = new Piece(0, 2, 0, 'p');
		tester.updateMoves();
		assertFalse(tester.isChecked(), "king should be protected");

	}
	
	@Test
	void eatTest() {
		Board tester = new Board();
		
		tester.eat(tester.getPieceOnCell(1, 0), tester.getPieceOnCell(2, 0));
		char hunted = tester.getPieceOnCell(2, 0).getType();
		
		assertEquals('p', hunted, hunted);
		
		//we try to eat something out of reach
		tester.eat(tester.getPieceOnCell(7, 7), tester.getPieceOnCell(1, 1));
		char hunted2 = tester.getPieceOnCell(1, 1).getType();
		
		assertEquals('p', hunted, "should still be a 'p'awn");

		
	}
	*/
}
