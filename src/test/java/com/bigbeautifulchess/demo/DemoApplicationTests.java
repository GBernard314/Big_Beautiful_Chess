package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.format.datetime.joda.LocalDateTimeParser;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.tools.Coord;

class DemoApplicationTests {

	/*
	 * Piece tests
	 */
	
	@Test
	void kingMovesTest() {
		Piece king = new Piece(0, 0, 0, 'k');
		king.setMoves(king.kingMoves());

		// if the king is alone in a corner each corner
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

		// if the king is alone in the center
		king = new Piece(3, 3, 0, 'k');
		assertEquals(8, king.getMoves().size(), "king should have 8 moves");
	}

	@Test
	void pawnMovesTest() {
		// white pawn test
		Piece pawn_white = new Piece(7, 7, 0, 'p');
		pawn_white.setMoves(pawn_white.pawnMoves());
		// if the pawn never moved it can moves 2 cells
		assertEquals(2, pawn_white.getMoves().size(), "pawn unmoved should have 2 moves");

		// else it has only 1
		pawn_white.setMoved(true);
		pawn_white.setMoves(pawn_white.pawnMoves());
		assertEquals(1, pawn_white.getMoves().size(), "pawn moved should have 1 move");

		// black pawn test
		Piece pawn = new Piece(0, 0, 1, 'p');
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

		// for no king in game
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
		assertEquals(2, tester.getKing().getMoves().size());
	}

	@Test
	void timeTest() {
		Board tester = new Board();
		LocalDateTime date = LocalDateTime.now();
		int time = 0;
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LocalDateTime date_end = LocalDateTime.now();

		int diff = (int) ChronoUnit.SECONDS.between(date, date_end);
		int time_stored = tester.getTime_black();
		int time_after = time_stored - diff;
		tester.setTime_black(time_after);

		assertEquals(2400, time_stored);
		assertEquals(2400 - time, time_after);

	}

	@Test
	void isSmallCastlingOkTest() {
		// for the white
		Board testObstruction = new Board();
		Board testMoved = new Board(false);
		Board testCheck = new Board(false);
		Board testChecked = new Board(false);
		Board testOk = new Board(false);

		assertEquals(false, testObstruction.isSmallCastlingOk());

		testMoved.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testMoved.getCells()[7][4].setMoved(true);
		testMoved.getCells()[7][7] = new Piece(7, 7, 0, 'r');
		assertEquals(false, testMoved.isSmallCastlingOk());

		testChecked.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testChecked.getCells()[7][7] = new Piece(7, 7, 0, 'r');
		testChecked.getCells()[6][4] = new Piece(6, 4, 1, 'r');
		assertEquals(false, testChecked.isSmallCastlingOk());

		testCheck.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testCheck.getCells()[7][7] = new Piece(7, 7, 0, 'r');
		testCheck.getCells()[6][6] = new Piece(6, 6, 1, 'r');
		assertEquals(false, testCheck.isSmallCastlingOk());

		testOk.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testOk.getCells()[7][7] = new Piece(7, 7, 0, 'r');
		assertEquals(true, testOk.isSmallCastlingOk());

		// for the black now

		testObstruction = new Board();
		testMoved = new Board(false);
		testCheck = new Board(false);
		testChecked = new Board(false);
		testOk = new Board(false);

		testOk.setTurn(1);
		testMoved.setTurn(1);
		testCheck.setTurn(1);
		testChecked.setTurn(1);
		testObstruction.setTurn(1);

		assertEquals(false, testObstruction.isSmallCastlingOk());

		testMoved.getCells()[0][4] = new Piece(0, 4, 1, 'k', true);
		testMoved.getCells()[0][7] = new Piece(0, 7, 1, 'r');
		assertEquals(false, testMoved.isSmallCastlingOk());

		testChecked.getCells()[0][4] = new Piece(0, 4, 1, 'k');
		testChecked.getCells()[0][7] = new Piece(0, 7, 1, 'r');
		testChecked.getCells()[1][4] = new Piece(1, 4, 0, 'r');
		assertEquals(false, testChecked.isSmallCastlingOk());

		testCheck.getCells()[0][4] = new Piece(0, 4, 1, 'k');
		testCheck.getCells()[0][7] = new Piece(0, 7, 1, 'r');
		testCheck.getCells()[1][6] = new Piece(1, 6, 0, 'r');
		assertEquals(false, testCheck.isSmallCastlingOk());

		testOk.getCells()[0][4] = new Piece(0, 4, 1, 'k');
		testOk.getCells()[0][7] = new Piece(0, 7, 1, 'r');
		assertEquals(true, testOk.isSmallCastlingOk());

	}

	@Test
	void isBigCasltingOkTest() {
		// For the white
		Board testObstruction = new Board();
		Board testMoved = new Board(false);
		Board testCheck = new Board(false);
		Board testChecked = new Board(false);
		Board testOk = new Board(false);

		assertEquals(false, testObstruction.isBigCastlingOk());

		testMoved.getCells()[7][4] = new Piece(7, 4, 0, 'k', true);
		testMoved.getCells()[7][0] = new Piece(7, 0, 0, 'r');
		assertEquals(false, testMoved.isBigCastlingOk());

		testChecked.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testChecked.getCells()[7][0] = new Piece(7, 0, 0, 'r');
		testChecked.getCells()[6][4] = new Piece(6, 4, 1, 'r');
		assertEquals(false, testChecked.isBigCastlingOk());

		testCheck.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testCheck.getCells()[7][0] = new Piece(7, 0, 0, 'r');
		testCheck.getCells()[6][2] = new Piece(6, 2, 1, 'r');
		assertEquals(false, testCheck.isBigCastlingOk());

		testOk.getCells()[7][4] = new Piece(7, 4, 0, 'k');
		testOk.getCells()[7][0] = new Piece(7, 0, 0, 'r');
		assertEquals(true, testOk.isBigCastlingOk());
		
		//For the black
		testObstruction = new Board();
		testMoved = new Board(false);
		testCheck = new Board(false);
		testChecked = new Board(false);
		testOk = new Board(false);

		testOk.setTurn(1);
		testMoved.setTurn(1);
		testCheck.setTurn(1);
		testChecked.setTurn(1);
		testObstruction.setTurn(1);

		assertEquals(false, testObstruction.isBigCastlingOk());

		testMoved.getCells()[0][4] = new Piece(0, 4, 1, 'k', true);
		testMoved.getCells()[0][0] = new Piece(0, 0, 1, 'r');
		assertEquals(false, testMoved.isBigCastlingOk());

		testChecked.getCells()[0][4] = new Piece(0, 4, 1, 'k');
		testChecked.getCells()[0][0] = new Piece(0, 0, 1, 'r');
		testChecked.getCells()[1][4] = new Piece(1, 4, 0, 'r');
		assertEquals(false, testChecked.isBigCastlingOk());

		testCheck.getCells()[0][4] = new Piece(0, 4, 1, 'k');
		testCheck.getCells()[0][0] = new Piece(0, 0, 1, 'r');
		testCheck.getCells()[1][2] = new Piece(1, 2, 0, 'r');
		assertEquals(false, testCheck.isBigCastlingOk());

		testOk.getCells()[0][4] = new Piece(0, 4, 1, 'k');
		testOk.getCells()[0][0] = new Piece(0, 0, 1, 'r');
		assertEquals(true, testOk.isBigCastlingOk());

		
	}

	@Test
	void promotionTest() {
		Board tester = new Board(false);
		tester.getCells()[0][4] = new Piece(0, 4, 0, 'p');
		tester.getCells()[7][4] = new Piece(7, 4, 1, 'p');
		assertEquals('p', tester.getPieceOnCell(0, 4).getType());
		assertEquals('p', tester.getPieceOnCell(7, 4).getType());
		tester.promotion();
		assertEquals('q', tester.getPieceOnCell(0, 4).getType());
		assertEquals('q', tester.getPieceOnCell(7, 4).getType());
		
	}

	@Test
	void isCheckMateTest() {
		Board white = new Board(false);
		white.setTurn(0);
		
		//not checked
		white.getCells()[2][0] = new Piece(2, 0, 0, 'k');
		white.updateMoves();
		assertFalse(white.isCheckMate());
		
		
		
		//checked but not mate
		white.getCells()[7][0] = new Piece(7, 0, 1, 'r');		
		white.updateMoves();
		assertTrue(white.isChecked());
		assertFalse(white.isCheckMate());
		
		
		//check mate
		white.getCells()[7][1] = new Piece(7, 1, 1, 'r');
		white.updateMoves();
		assertTrue(white.isCheckMate());
		
	}
	
	@Test
	void cleanMovesRookTest() {
		Board tester = new Board(false);
		
		tester.getCells()[3][4] = new Piece(3, 4, 0, 'r');
		tester.getCells()[3][6] = new Piece(3, 6, 0, 'p');
		tester.getCells()[5][4] = new Piece(5, 4, 1, 'p');
		tester.updateMoves();
		
		assertEquals(10, tester.getPieceOnCell(3, 4).getMoves().size());
	}
	
	@Test
	void cleanMovesBishopTest() {
		Board tester = new Board(false);
		
		tester.getCells()[3][4] = new Piece(3, 4, 0, 'b');
		tester.getCells()[1][6] = new Piece(1, 6, 0, 'p');
		tester.getCells()[2][3] = new Piece(2, 3, 1, 'p');
		tester.getCells()[7][0] = new Piece(7, 0, 0, 'p');
		tester.getCells()[6][7] = new Piece(6, 7, 1, 'p');
		tester.updateMoves();
		
		assertEquals(8, tester.getPieceOnCell(3, 4).getMoves().size());
	}

	@Test
	void cleanMovesQueenTest() {
		Board tester = new Board(false);
		
		tester.getCells()[3][4] = new Piece(3, 4, 0, 'q');
		tester.getCells()[1][6] = new Piece(1, 6, 0, 'p');
		tester.getCells()[2][3] = new Piece(2, 3, 1, 'p');
		tester.getCells()[7][0] = new Piece(7, 0, 0, 'p');
		tester.getCells()[6][7] = new Piece(6, 7, 1, 'p');
		tester.getCells()[0][4] = new Piece(0, 4, 0, 'p');
		tester.getCells()[3][0] = new Piece(3, 0, 1, 'p');
		tester.updateMoves();
		
		assertEquals(21, tester.getPieceOnCell(3, 4).getMoves().size());
	}

	@Test
	void cleanMovesPawnTest() {
		Board tester = new Board(false);
		
		tester.getCells()[5][1] = new Piece(5, 1, 0, 'p', true);
		tester.getCells()[4][0] = new Piece(4, 0, 1, 'p', true);
		
		tester.getCells()[6][6] = new Piece(6, 6, 0, 'p');
		tester.getCells()[5][5] = new Piece(5, 5, 1, 'p', true);
		tester.updateMoves();

		
		assertEquals(3, tester.getPieceOnCell(6, 6).getMoves().size());
		assertEquals(2, tester.getPieceOnCell(5, 1).getMoves().size());
		
		assertEquals(2, tester.getPieceOnCell(5, 5).getMoves().size());
		assertEquals(2, tester.getPieceOnCell(4, 0).getMoves().size());
	}

	@Test
	void cleanMovesHorseTest() {
		Board tester = new Board(false);
		
		tester.getCells()[4][7] = new Piece(4, 7, 0, 'p', true);		
		tester.getCells()[6][6] = new Piece(6, 6, 0, 'h');
		tester.getCells()[5][4] = new Piece(5, 4, 1, 'p', true);
		tester.updateMoves();
		
		assertEquals(3, tester.getPieceOnCell(6, 6).getMoves().size());

	}

	@Test
	void eatTest() {
		Board tester = new Board(false);
		
		tester.getCells()[7][3] = new Piece(7, 3, 0, 'r', true);		
		tester.getCells()[1][3] = new Piece(1, 3, 1, 'r', true);
		tester.updateMoves();
		tester.eat(tester.getPieceOnCell(7, 3), tester.getPieceOnCell(1, 3));
		
		assertEquals(0, tester.getPieceOnCell(1, 3).getColor());

		assertEquals(1, tester.getHistoric().size());
	}

	
	@Test
	void bigCastlingTest() {
		Board white_tester = new Board(false);
		Board black_tester = new Board(false);
		
		white_tester.getCells()[7][4] = new Piece(7, 4, 0, 'k');		
		white_tester.getCells()[7][0] = new Piece(7, 0, 0, 'r');

		white_tester.bigCastling();
		assertEquals('k', white_tester.getPieceOnCell(7, 2).getType());
		assertEquals('r', white_tester.getPieceOnCell(7, 3).getType());
		
		
		black_tester.getCells()[0][4] = new Piece(0, 4, 1, 'k');		
		black_tester.getCells()[0][0] = new Piece(0, 0, 1, 'r');
		
		black_tester.setTurn(1);
		black_tester.bigCastling();
		assertEquals('k', black_tester.getPieceOnCell(0, 2).getType());
		assertEquals('r', black_tester.getPieceOnCell(0, 3).getType());		
	}
	
	@Test
	void smallCastlingTest() {
		Board white_tester = new Board(false);
		Board black_tester = new Board(false);
		
		white_tester.getCells()[7][4] = new Piece(7, 4, 0, 'k');		
		white_tester.getCells()[7][7] = new Piece(7, 7, 0, 'r');
		white_tester.smallCastling();
		assertEquals('k', white_tester.getPieceOnCell(7, 6).getType());
		assertEquals('r', white_tester.getPieceOnCell(7, 5).getType());
		
		
		black_tester.getCells()[0][4] = new Piece(0, 4, 1, 'k');		
		black_tester.getCells()[0][7] = new Piece(0, 7, 1, 'r');
		
		black_tester.setTurn(1);
		black_tester.smallCastling();
		assertEquals('k', black_tester.getPieceOnCell(0, 6).getType());
		assertEquals('r', black_tester.getPieceOnCell(0, 5).getType());

	}
}
