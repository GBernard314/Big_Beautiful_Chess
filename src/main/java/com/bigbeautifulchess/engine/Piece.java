package com.bigbeautifulchess.engine;

import java.util.ArrayList;
import com.bigbeautifulchess.tools.*;

public class Piece {

	/**
	 * Coordinate on the board
	 */
	private Coord coord;

	/**
	 * 0 for white and 1 for black
	 */
	private int color;

	/**
	 * k - king<br>
	 * q - queen <br>
	 * r - rook<br>
	 * b - bishop <br>
	 * h - horse <br>
	 * p - pawn<br>
	 * e - empty
	 */
	private char type;

	/**
	 * Contains theoretical moves on the board
	 */
	private ArrayList<Coord> theoretical_moves;
	
	/**
	 * Contains all possible moves on the board
	 */
	private ArrayList<Coord> moves;

	/**
	 * Simple flag to determine if the piece moved at least once
	 */
	private boolean moved;

	/**
	 * It is used to create a piece
	 * 
	 * @param x     coordinate on the board
	 * @param y     coordinate on the board
	 * @param color of the piece
	 * @param type  of the piece
	 */
	public Piece(int x, int y, int color, char type) {
		super();
		this.coord = new Coord(x, y);
		this.color = color;
		this.type = type;
		this.theoretical_moves = moves();
		this.moves = moves();
		this.moved = false;
	}
	
	/**
	 * Used to create a piece
	 * 
	 * @param x
	 * @param y
	 * @param color
	 * @param type
	 * @param moved
	 */
	public Piece(int x, int y, int color, char type, boolean moved) {
		super();
		this.coord = new Coord(x, y);
		this.color = color;
		this.type = type;
		this.moved = moved;
		this.theoretical_moves = moves();
		this.moves = moves();
	}

	/**
	 * Used to create an empty board cell
	 * 
	 * @param x coordinate on the board
	 * @param y coordinate on the board
	 */
	public Piece(int x, int y) {
		super();
		this.coord = new Coord(x, y);
		this.color = -1;
		this.type = 0;
		this.moves = new ArrayList<Coord>();
		this.theoretical_moves = new ArrayList<Coord>();
		this.moved = false;
	}
	
	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public Coord getC() {
		return coord;
	}

	public void setC(Coord c) {
		this.coord = c;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public ArrayList<Coord> getTheoretical_moves() {
		return theoretical_moves;
	}

	public void setTheoretical_moves(ArrayList<Coord> theoretical_moves) {
		this.theoretical_moves = theoretical_moves;
	}

	public ArrayList<Coord> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<Coord> moves) {
		this.moves = moves;
	}

	public boolean getMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
		result = prime * result + (moved ? 1231 : 1237);
		result = prime * result + ((moves == null) ? 0 : moves.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (color != other.color)
			return false;
		if (coord == null) {
			if (other.coord != null)
				return false;
		} else if (!coord.equals(other.coord))
			return false;
		if (moved != other.moved)
			return false;
		if (moves == null) {
			if (other.moves != null)
				return false;
		} else if (!moves.equals(other.moves))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	

	/*
	 * Methods
	 */


	/**
	 * Print in terminal all the fields of a Piece
	 */
	public void printPiece() {
		switch (getType()) {
		case 'k':
			if (getColor() == 0) {
				System.out.print(" ♔  ");
			} else {
				System.out.print(" ♚  ");
			}
			break;

		case 'q':
			if (getColor() == 0) {
				System.out.print(" ♕  ");
			} else {
				System.out.print(" ♛  ");
			}
			break;

		case 'r':
			if (getColor() == 0) {
				System.out.print(" ♖  ");
			} else {
				System.out.print(" ♜   ");
			}
			break;

		case 'b':
			if (getColor() == 0) {
				System.out.print(" ♗  ");
			} else {
				System.out.print(" ♝ ");
			}
			break;

		case 'h':
			if (getColor() == 0) {
				System.out.print(" ♘  ");
			} else {
				System.out.print(" ♞  ");
			}
			break;

		case 'p':
			if (getColor() == 0) {
				System.out.print(" □ ");
			} else {
				System.out.print(" ■ ");
			}
			break;

		default:
			System.out.print("   ");
			break;
		}

		System.out.print("(" + getC().getX() + ";" + getC().getY() + ")\n");
	}
	

	/**
	 * Gives allowed moves for the King, on the board not checking for emptiness of
	 * destination
	 * 
	 * @return Gives an ArrayList of cell's coordinate to move
	 */
	public ArrayList<Coord> kingMoves() {
		// intermediate list
		ArrayList<Coord> king_moves = new ArrayList<Coord>();

		// First we add every moves even if they are impossible
		// N
		king_moves.add(new Coord(getC().getX(), getC().getY() + 1));
		// NE
		king_moves.add(new Coord(getC().getX() + 1, getC().getY() + 1));
		// E
		king_moves.add(new Coord(getC().getX() + 1, getC().getY()));
		// SE
		king_moves.add(new Coord(getC().getX() + 1, getC().getY() - 1));
		// S
		king_moves.add(new Coord(getC().getX(), getC().getY() - 1));
		// SW
		king_moves.add(new Coord(getC().getX() - 1, getC().getY() - 1));
		// W
		king_moves.add(new Coord(getC().getX() - 1, getC().getY()));
		// NW
		king_moves.add(new Coord(getC().getX() - 1, getC().getY() + 1));

		// We return inside moves
		return insideMoves(king_moves);
	}
	
	/**
	 * Gives allowed moves for the Pawn, on the board not checking for emptiness of
	 * destination
	 * 
	 * @return Gives an ArrayList of cell's coordinate to move
	 */
	public ArrayList<Coord> pawnMoves() {
		ArrayList<Coord> pawn_moves = new ArrayList<Coord>();
		int x = this.getC().getX();
		int y = this.getC().getY();
		// if the pawn is white he can go up
		if (getColor() == 0) {
			// if the pawn never moved it can move 2 cells
			if (!getMoved()) {
				pawn_moves.add(new Coord(y - 1, x));
				pawn_moves.add(new Coord(y - 2, x));
			}
			// else only 1 cell
			else {
				pawn_moves.add(new Coord(x, y-1));
			}
		}
		// else he only goes down
		else {
			// if the pawn never moved it can move 2 cells
			if (!this.getMoved()) {
				pawn_moves.add(new Coord(x + 1, y));
				pawn_moves.add(new Coord(x + 2, y));
			}
			// else only 1 cell
			else {
				pawn_moves.add(new Coord(x + 1, y));
			}
		}
		return insideMoves(pawn_moves);
	}
	
	/**
	 * Gives allowed moves for the Horse, on the board not checking for emptiness of
	 * destination
	 * 
	 * @return Gives an ArrayList of cell's coordinate to move
	 */

	private ArrayList<Coord> horseMoves() {
		ArrayList<Coord> horse_moves = new ArrayList<Coord>();
		int x = getC().getX();
		int y = getC().getY();
		// top right
		horse_moves.add(new Coord(x - 2, y + 1));
		horse_moves.add(new Coord(x - 1, y + 2));

		// bottom right
		horse_moves.add(new Coord(x + 1, y + 2));
		horse_moves.add(new Coord(x + 2, y + 1));

		// top left
		horse_moves.add(new Coord(x - 2, y - 1));
		horse_moves.add(new Coord(x - 1, y - 2));

		// bottom left
		horse_moves.add(new Coord(x + 1, y - 2));
		horse_moves.add(new Coord(x + 2, y - 1));

		return insideMoves(horse_moves);
	}

	/**
	 * Gives allowed moves for the Rook, on the board not checking for emptiness of
	 * destination
	 * 
	 * @return Gives an ArrayList of cell's coordinate to move
	 */
	private ArrayList<Coord> rookMoves() {
		ArrayList<Coord> rook_moves = new ArrayList<Coord>();
		int x = getC().getX();
		int y = getC().getY();
		for (int i = 1; i < 8; i++) {
			// line up
			rook_moves.add(new Coord(x - i, y));
			// line down
			rook_moves.add(new Coord(x + i, y));
			// line right
			rook_moves.add(new Coord(x, y + i));
			// line left
			rook_moves.add(new Coord(x, y - i));

		}
		return insideMoves(rook_moves);
	}

	/**
	 * Gives allowed moves for the Bishop, on the board not checking for emptiness of
	 * destination
	 * 
	 * @return Gives an ArrayList of cell's coordinate to move
	 */
	private ArrayList<Coord> bishopMoves() {
		ArrayList<Coord> bishop_moves = new ArrayList<Coord>();
		int x = getC().getX();
		int y = getC().getY();
		for (int i = 1; i < 8; i++) {
			// line up-right
			bishop_moves.add(new Coord(x - i, y + i));
			// line down-right
			bishop_moves.add(new Coord(x + i, y + i));
			// line up-left
			bishop_moves.add(new Coord(x - i, y - i));
			// line down-left
			bishop_moves.add(new Coord(x + i, y - i));
		}
		return insideMoves(bishop_moves);
	}

	/**
	 * Gives allowed moves for the Queen, on the board not checking for emptiness of
	 * destination
	 * 
	 * @return Gives an ArrayList of cell's coordinate to move
	 */
	private ArrayList<Coord> queenMoves() {
		ArrayList<Coord> queen_moves = new ArrayList<Coord>();
		int x = getC().getX();
		int y = getC().getY();
		for (int i = 1; i < 8; i++) {
			// line up-right
			queen_moves.add(new Coord(x - i, y + i));
			// line down-right
			queen_moves.add(new Coord(x + i, y + i));
			// line up-left
			queen_moves.add(new Coord(x - i, y - i));
			// line down-left
			queen_moves.add(new Coord(x + i, y - i));
			// line up
			queen_moves.add(new Coord(x - i, y));
			// line down
			queen_moves.add(new Coord(x + i, y));
			// line right
			queen_moves.add(new Coord(x, y + i));
			// line left
			queen_moves.add(new Coord(x, y - i));
		}

		return insideMoves(queen_moves);
	}


	/**
	 * Rework moves list by deleting those outside board
	 * 
	 * @param mov List with outside moves in it
	 * @return List without outside moves in it
	 */
	ArrayList<Coord> insideMoves(ArrayList<Coord> mov) {
		ArrayList<Coord> mov_out = new ArrayList<Coord>();
		for (int i = 0; i < mov.size(); i++) {
			int x = mov.get(i).getX();
			int y = mov.get(i).getY();
			if (x > 7 || x < 0 || y > 7 || y < 0) {
				mov_out.add(new Coord(x, y));
			}
		}
		mov.removeAll(mov_out);
		return mov;
	}

	/**
	 * Initialize the piece with possible destination cells
	 * @return ArrayList of coordinate
	 */
	public ArrayList<Coord> moves() {
		switch (this.getType()) {
		case 'k':
			return kingMoves();
		case 'q':
			return queenMoves();
		case 'r':
			return rookMoves();
		case 'b':
			return bishopMoves();
		case 'h':
			return horseMoves();
		case 'p':
			return pawnMoves();

		default:
			return new ArrayList<Coord>();
		}
	}
	
}
