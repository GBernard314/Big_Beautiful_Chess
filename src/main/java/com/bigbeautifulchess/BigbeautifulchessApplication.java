package com.bigbeautifulchess;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.san.San;

public class BigbeautifulchessApplication {

	public static void main(String[] args) {

		System.out.println("Main started\n");
		
		Board b = new Board();
		San s = new San();

		b.updateMoves();
		
		//b.printBoard();
		
		//black turn
		b.eat(b.getPieceOnCell(0, 1), new Piece(2, 0));
		//b.printBoard();
		
		//black turn
		b.eat(b.getPieceOnCell(2, 0), new Piece(0, 1));
		//b.printBoard();
		
		//b.printHistoric();
		String string = s.printSan(b);
		System.out.println(string);
		
		System.out.println("\nMain finished");
	}
}
