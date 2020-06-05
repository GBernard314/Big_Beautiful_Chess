package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bigbeautifulchess.engine.Piece;

class PieceTest {

	/*
	 * Piece tests
	 */
	
	@Test
	void kingMovesTest() {
		Piece king = new Piece(0, 0, 0, 'k', true);
		king.setMoves(king.kingMoves());

		// if the king is alone in a corner each corner
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");

		king = new Piece(7, 7, 0, 'k', true);
		king.setMoves(king.kingMoves());
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");

		king = new Piece(0, 7, 0, 'k', true);
		king.setMoves(king.kingMoves());
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");

		king = new Piece(7, 0, 0, 'k', true);
		king.setMoves(king.kingMoves());
		assertEquals(3, king.getMoves().size(), "king should have 3 moves");

		// if the king is alone in the center
		king = new Piece(3, 3, 0, 'k', true);
		assertEquals(8, king.getMoves().size(), "king should have 8 moves");
	}

	@Test
	void pawnMovesTest() {
		// white pawn test
		Piece pawn_white = new Piece(7, 7, 0, 'p', false);
		pawn_white.setMoves(pawn_white.pawnMoves());
		// if the pawn never moved it can moves 2 cells
		assertEquals(2, pawn_white.getMoves().size(), "pawn unmoved should have 2 moves");

		// else it has only 1
		pawn_white.setMoved(true);
		pawn_white.setMoves(pawn_white.pawnMoves());
		assertEquals(1, pawn_white.getMoves().size(), "pawn moved should have 1 move");

		// black pawn test
		Piece pawn = new Piece(0, 0, 1, 'p', false);
		pawn.setMoves(pawn.pawnMoves());
		// if the pawn never moved it can moves 2 cells
		assertEquals(2, pawn.getMoves().size(), "pawn unmoved should have 2 moves");

		// else it has only 1
		pawn.setMoved(true);
		pawn.setMoves(pawn.pawnMoves());
		assertEquals(1, pawn.getMoves().size(), "pawn moved should have 1 move");

	}

	@Test
	void horseMovesTest() {
		Piece horse = new Piece(0, 0, 1, 'h', true);
		horse.setMoves(horse.moves());
		assertEquals(2, horse.getMoves().size(), "horse in corner has 2 moves");

		horse = new Piece(4, 4, 1, 'h', true);
		horse.setMoves(horse.moves());
		assertEquals(8, horse.getMoves().size(), "horse in middle has 8 moves");

	}

	@Test
	void rookMovesTest() {
		Piece rook = new Piece(0, 0, 1, 'r', true);
		rook.setMoves(rook.moves());
		assertEquals(14, rook.getMoves().size(), "rook in corner has 14 moves");

		rook = new Piece(4, 4, 1, 'r', true);
		rook.setMoves(rook.moves());
		assertEquals(14, rook.getMoves().size(), "rook in middle has 14 moves");

	}

	@Test
	void bishopMovesTest() {
		Piece bishop = new Piece(0, 0, 1, 'b', true);
		bishop.setMoves(bishop.moves());
		assertEquals(7, bishop.getMoves().size(), "bishop in corner has 7 moves");

		bishop = new Piece(4, 4, 1, 'b', true);
		bishop.setMoves(bishop.moves());
		assertEquals(13, bishop.getMoves().size(), "rook in middle has 13 moves");

	}

	@Test
	void queenMovesTest() {
		Piece queen = new Piece(0, 0, 1, 'q', true);
		queen.setMoves(queen.moves());
		assertEquals(21, queen.getMoves().size(), "queen in corner has 21 moves");

		queen = new Piece(4, 4, 1, 'q', true);
		queen.setMoves(queen.moves());
		assertEquals(27, queen.getMoves().size(), "queen in middle has 27 moves");

	}


}
