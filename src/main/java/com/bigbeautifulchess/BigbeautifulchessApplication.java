package com.bigbeautifulchess;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.engine.Piece;
import com.bigbeautifulchess.tools.Coord;

@SpringBootApplication
public class BigbeautifulchessApplication {

	public static void main(String[] args) {

		System.out.println("Main 2 started\n");
		SpringApplication.run(BigbeautifulchessApplication.class, args);
		
		Board game = new Board(false);
		
		game.getCells()[0][4] = new Piece(0, 4, 1, 'k', false);
		game.getCells()[1][4] = new Piece(1, 4, 1, 'p', false);
		//game.getCells()[2][2] = new Piece(2, 2, 0, 'p', false);
		game.getCells()[0][5] = new Piece(0, 5, 1, 'b', false);
		game.getCells()[1][5] = new Piece(1, 5, 1, 'p', false);
		//game.getCells()[2][3] = new Piece(2, 3, 1, 'p', false);
		game.updateMoves();
		game.printBoardSimple();
		game.eat(game.getPieceOnCell(0, 4), game.getPieceOnCell(0, 3));
		//game.eat(game.getPieceOnCell(4, 4), game.getPieceOnCell(5, 4));
		game.printBoardSimple();
		game.printMovements(game.getPieceOnCell(0, 3));
		/*
		
		//ArrayList of coordinates to remove
		ArrayList<Coord> removed = new ArrayList<Coord>();
		
		String plateau = "0,0,0/|/|/|/|/|/|7,7,1/6,6,1|";
		String historic = "k:2,4-e:4,5";
		
		//Piece to get removed
		
		removed.add(new Coord(0, 2));
		removed.add(new Coord(0, 3));
		removed.add(new Coord(1, 2));
		removed.add(new Coord(6, 0));
		
		
		
		
		Board b = new Board();

		b.updateMoves();
		
		//we remove the previously listed elements
		for (int i = 0; i < removed.size(); i++) {
			b.getCells()[removed.get(i).getX()][removed.get(i).getY()] = new Piece(removed.get(i).getX(), removed.get(i).getY());
		}
		//we print it to get fancy
		b.printBoard();
		
		
		
		//1st turn
		//white turn
		b.eat(b.getPieceOnCell(7, 0), new Piece(5, 0));
		b.printBoard();
		
		//black turn
		b.eat(b.getPieceOnCell(0, 1), new Piece(2, 0));
		b.printBoard();
		
		//white turn
		b.eat(b.getPieceOnCell(5, 0), new Piece(5, 2));
		b.printBoard();
		
		//black turn
		b.eat(b.getPieceOnCell(2, 0), new Piece(0, 1));
		b.printBoard();
		
		//white turn
		b.eat(b.getPieceOnCell(5, 2), new Piece(0, 2));
		b.printBoard();

		b.getPieceOnCell(6, 4).printPiece();		
		b.printMovements(b.getPieceOnCell(6, 1));
		b.eat(b.getPieceOnCell(6, 1), b.getPieceOnCell(5, 1));
		b.printMovements(b.getPieceOnCell(5, 1));	
		b.printHistoric();
		
		
		Board bdd = new Board(plateau, 0, -1, 0, 0, "10:10:10", historic);
		bdd.printBoardSimple();
		/*
		//To test one-by-one piece from the board
		Piece p = b.getCells()[x][y];
		System.out.print("\n");
		//p.printPiece();
		ArrayList<Coord> km = p.getMoves();
		System.out.print("\n");
		*/
		
		//Display moves on the board (as a layer)
		//b.printMovements(km);
		
		/*
		b.eat(b.getCells()[1][5], b.getCells()[0][5]);
		b.printBoard();
		System.out.print("\n");
		b.promotion();
		b.printBoard();
		*/
		
		//Debug
		//new Table().printTabCanvas();
		
		
		System.out.println("\nMain finished");
	}
}
