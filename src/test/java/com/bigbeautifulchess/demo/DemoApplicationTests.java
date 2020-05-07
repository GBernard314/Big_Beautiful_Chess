package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.*;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.tools.Coord;

class DemoApplicationTests {

	/*
	 * Piece tests
	 */
	
	@Test 
	void kingMovesTest(){
		Piece king = new Piece(0, 0, 0, 'k');
		king.setMoves(king.kingMoves());
		
		//if the king is alone in a corner each corner
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");
		
		king = new Piece(7, 7, 0, 'k');
		king.setMoves(king.kingMoves());
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");
		
		king = new Piece(0, 7, 0, 'k');
		king.setMoves(king.kingMoves());
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");
		
		king = new Piece(7, 0, 0, 'k');
		king.setMoves(king.kingMoves());
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");

		
		//if the king is alone in the center
		king = new Piece(3, 3, 0, 'k');
		assertEquals(8, king.getMoves().size(), "king should have 8 moves");
	}
	
	@Test
	void pawnMovesTest(){
		//white pawn test
		Piece pawn_white = new Piece(7, 7, 0, 'p');
		pawn_white.setMoves(pawn_white.pawnMoves());
		//if the pawn never moved it can moves 2 cells
		assertEquals(2, pawn_white.getMoves().size(), "pawn unmoved should have 2 moves");

		//else it has only 1
		pawn_white.setMoved(true);
		pawn_white.setMoves(pawn_white.pawnMoves());
		assertEquals(1, pawn_white.getMoves().size(), "pawn moved should have 1 move");

		
		//black pawn test
		Piece pawn = new Piece(0, 0, 1, 'p');
		pawn.setMoves(pawn.pawnMoves());
		//if the pawn never moved it can moves 2 cells
		assertEquals(2, pawn.getMoves().size(), "pawn unmoved should have 2 moves");

		//else it has only 1
		pawn.setMoved(true);
		pawn.setMoves(pawn.pawnMoves());
		assertEquals(1, pawn.getMoves().size(), "pawn moved should have 1 move");

	}
	
	@Test
	void horseMovesTest() {
		Piece horse = new Piece(0, 0, 1, 'h');
		horse.setMoves(horse.moves());
		assertEquals(2, horse.getMoves().size(), "horse in corner has 2 moves");
		
		horse = new Piece(4, 4, 1, 'h');
		horse.setMoves(horse.moves());
		assertEquals(8, horse.getMoves().size(), "horse in middle has 8 moves");
		
	}

	@Test
	void rookMovesTest() {
		Piece rook = new Piece(0, 0, 1, 'r');
		rook.setMoves(rook.moves());
		assertEquals(14, rook.getMoves().size(), "rook in corner has 14 moves");
		
		rook = new Piece(4, 4, 1, 'r');
		rook.setMoves(rook.moves());
		assertEquals(14, rook.getMoves().size(), "rook in middle has 14 moves");
		
	}

	@Test
	void bishopMovesTest() {
		Piece bishop = new Piece(0, 0, 1, 'b');
		bishop.setMoves(bishop.moves());
		assertEquals(7, bishop.getMoves().size(), "bishop in corner has 7 moves");
		
		bishop = new Piece(4, 4, 1, 'b');
		bishop.setMoves(bishop.moves());
		assertEquals(13, bishop.getMoves().size(), "rook in middle has 13 moves");
		
	}

	@Test
	void queenMovesTest() {
		Piece queen = new Piece(0, 0, 1, 'q');
		queen.setMoves(queen.moves());
		assertEquals(21, queen.getMoves().size(), "queen in corner has 21 moves");
		
		queen = new Piece(4, 4, 1, 'q');
		queen.setMoves(queen.moves());
		assertEquals(27, queen.getMoves().size(), "queen in middle has 27 moves");
		
	}

	/*
	 * Board tests
	 */
	
	@Test
	void getKingTest() {
		Board tester = new Board();
		Piece king_white = tester.getKing();
		
		tester.setTurn(1);
		Piece king_black = tester.getKing();
		
		assertEquals(king_black.getC().getX(), 0);
		assertEquals(king_black.getC().getY(), 4);
		assertEquals(king_white.getC().getX(), 7);
		assertEquals(king_white.getC().getY(), 4);
		
		//for no king in game
		Board tester2 = new Board(true);
		Piece no_king = tester2.getKing();
		assertEquals(no_king.getC().getX(), -1);
		assertEquals(no_king.getC().getY(), -1);
	}

	@Test
	void getPieceOnCellTest() {
		Board tester = new Board();
		Piece king = tester.getPieceOnCell(0, 4);
		Piece queen = tester.getPieceOnCell(0, 3);
		Piece bishop = tester.getPieceOnCell(0, 2);
		Piece horse = tester.getPieceOnCell(0, 1);
		Piece rook = tester.getPieceOnCell(0, 0);
		Piece pawn = tester.getPieceOnCell(1, 0);
		
		assertEquals(king.getType(), 'k');
		assertEquals(queen.getType(), 'q');
		assertEquals(bishop.getType(), 'b');
		assertEquals(horse.getType(), 'h');
		assertEquals(rook.getType(), 'r');
		assertEquals(pawn.getType(), 'p');
		
	}
	
	@Test
	void updateMovesTest() {
		Board tester = new Board(false);
		
		Piece king = new Piece(0, 0, 0, 'k');
		Piece pawn1 = new Piece(0, 1, 0, 'p');
		Piece pawn2 = new Piece(1, 0, 0, 'p');
		Piece pawn3 = new Piece(1, 1, 0, 'p');
		Piece pawn_black = new Piece(0, 1, 1, 'p');
		

		Piece[][] cells = tester.getCells();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				cells[i][j] = tester.getCells()[i][j];
			}
		}
		cells[0][0] = king;
		tester.setCells(cells);
		tester.updateMoves();
		assertEquals(3, tester.getKing().getMoves().size());
		
		cells[0][1] = pawn1;
		tester.setCells(cells);
		tester.updateMoves();
		assertEquals(2, tester.getKing().getMoves().size());
		
		cells[0][1] = pawn_black;
		tester.setCells(cells);
		tester.updateMoves();
		assertEquals(3, tester.getKing().getMoves().size());


		
	}
}
