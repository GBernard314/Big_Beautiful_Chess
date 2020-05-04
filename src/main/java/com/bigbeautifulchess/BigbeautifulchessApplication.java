package com.bigbeautifulchess;

import java.util.ArrayList;

import engine.*;
import tools.Coord;
import tools.Table;

public class BigbeautifulchessApplication {

	public static void main(String[] args) {

		System.out.println("Main 2 started\n");

		int x=2;
		int y=2;
		
		//ArrayList of coordinates to remove
		ArrayList<Coord> removed = new ArrayList<Coord>();
		
		//Piece to get removed
		
		removed.add(new Coord(0, 2));
		removed.add(new Coord(0, 3));
		removed.add(new Coord(1, 2));
		removed.add(new Coord(6, 0));
		
		
		
		
		Board b = new Board();
		b.updateMoves();
		
		for (int i = 0; i < removed.size(); i++) {
			b.getCells()[removed.get(i).getX()][removed.get(i).getY()] = new Piece(removed.get(i).getX(), removed.get(i).getY());
		}
		//we print it to get fancy
		b.printBoard();
		
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
