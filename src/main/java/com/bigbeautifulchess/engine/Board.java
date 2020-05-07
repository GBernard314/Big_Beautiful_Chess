package com.bigbeautifulchess.engine;

import java.util.ArrayList;

import com.bigbeautifulchess.tools.*;

public class Board {

	/**
	 * Emulation of a chess board <br>
	 */
	private Piece[][] cells;

	/**
	 * 0 or 1 for each player's turn
	 */
	private int turn;

	/**
	 * -1 if the game is ongoing <br>
	 * 0 if it's a draw <br>
	 * 1 if it's won by player 1 <br>
	 * 2 if it's won by player 2 <br>
	 * 666 if player 1 is being checked <br>
	 * 1666 if player 2 is being checked
	 */
	private int result;
	
	/**
	 * Empty board
	 * @param just to over charge the constructor
	 */
	public Board(boolean b) {
		super();
		Piece[][] c = new Piece[8][8];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				c[i][j] = new Piece(i, j);
			}
		}
		this.cells = c;
		this.turn = 0;
		this.result = -1;
	}

	/**
	 * Generate a ready-to-play board, initialized with every piece
	 */
	public Board() {
		super();
		Piece[][] c = new Piece[8][8];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				c[i][j] = new Piece(i, j);
			}
		}
		// Black pieces
		c[0][0] = new Piece(0, 0, 1, 'r');
		c[0][1] = new Piece(0, 1, 1, 'h');
		c[0][2] = new Piece(0, 2, 1, 'b');
		c[0][3] = new Piece(0, 3, 1, 'q');
		c[0][4] = new Piece(0, 4, 1, 'k');
		c[0][5] = new Piece(0, 5, 1, 'b');
		c[0][6] = new Piece(0, 6, 1, 'h');
		c[0][7] = new Piece(0, 7, 1, 'r');
		for (int i = 0; i <= 7; i++) {
			c[1][i] = new Piece(1, i, 1, 'p');
		}

		// White pieces
		c[7][0] = new Piece(7, 0, 0, 'r');
		c[7][1] = new Piece(7, 1, 0, 'h');
		c[7][2] = new Piece(7, 2, 0, 'b');
		c[7][3] = new Piece(7, 3, 0, 'q');
		c[7][4] = new Piece(7, 4, 0, 'k');
		c[7][5] = new Piece(7, 5, 0, 'b');
		c[7][6] = new Piece(7, 6, 0, 'h');
		c[7][7] = new Piece(7, 7, 0, 'r');
		for (int i = 0; i <= 7; i++) {
			c[6][i] = new Piece(6, i, 0, 'p');
		}
		this.cells = c;
		this.turn = 0;
		this.result = -1;
	}

	public Board(Piece[][] cells, int turn, int result) {
		super();
		this.cells = cells;
		this.turn = turn;
		this.result = result;
	}

	public Piece[][] getCells() {
		return cells;
	}

	public void setCells(Piece[][] cells) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				this.cells[i][j] = cells[i][j];
			}
		}
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Board clone() {
		Board copy = new Board();
		Piece cells[][] = new Piece[8][8];
		int result = this.getResult();
		int turn = this.getTurn();

		for (int i = 0; i < getCells().length; i++) {
			for (int j = 0; j < getCells()[i].length; j++) {
				int x = getCells()[i][j].getC().getX();
				int y = getCells()[i][j].getC().getY();
				int color = getCells()[i][j].getColor();
				char type = getCells()[i][j].getType();

				cells[i][j] = new Piece(x, y, color, type);
			}
		}
		copy.setCells(cells);
		copy.setResult(result);
		copy.setTurn(turn);

		return copy;
	}

	/*
	 * Methods
	 */

	/**
	 * Print the board in the terminal
	 */
	public void printBoard() {
		if (this.getTurn() == 0) {
			System.out.println("White turn");
		} else {
			System.out.println("Black turn");
		}
		//System.out.println("king moves = " + this.getKing().getMoves().size());
		//this.getKing().printPiece();
		if (isChecked()) {
			if (isCheckMate()) {
				System.out.println("🕱  check mate 🕱 ");
				System.out.println("result = " + this.getResult());
			} else {
				System.out.println("⚠ checked ⚠");
			}
		}

		if (isBigCastlingOk() || isSmallCastlingOk()) {
			if (isBigCastlingOk()) {
				System.out.println("big castling possible");
			}
			if (isSmallCastlingOk()) {
				System.out.println("small castling possible");
			}
		} else {
			System.out.println("castling impossible");
		}

		System.out.println("┌───┬───┬───┬───┬───┬───┬───┬───┐");

		for (int i = 0; i < cells.length; i++) {
			if (i != 0) {
				System.out.println("├───┼───┼───┼───┼───┼───┼───┼───┤");
			}
			for (int j = 0; j < cells[i].length; j++) {
				switch (cells[i][j].getType()) {
				case 'k':
					if (cells[i][j].getColor() == 0) {
						System.out.print("│ ♔  ");
					} else {
						System.out.print("│ ♚  ");
					}
					break;

				case 'q':
					if (cells[i][j].getColor() == 0) {
						System.out.print("│ ♕ ");
					} else {
						System.out.print("│ ♛ ");
					}
					break;

				case 'r':
					if (cells[i][j].getColor() == 0) {
						System.out.print("│ ♖  ");
					} else {
						System.out.print("│ ♜  ");
					}
					break;

				case 'b':
					if (cells[i][j].getColor() == 0) {
						System.out.print("│ ♗  ");
					} else {
						System.out.print("│ ♝  ");
					}
					break;

				case 'h':
					if (cells[i][j].getColor() == 0) {
						System.out.print("│ ♘  ");
					} else {
						System.out.print("│ ♞  ");
					}
					break;

				case 'p':
					if (cells[i][j].getColor() == 0) {
						System.out.print("│ □ ");
					} else {
						System.out.print("│ ■ ");
					}
					break;

				default:
					System.out.print("│   ");
					break;
				}
			}
			System.out.print("│\n");
		}
		System.out.println("└───┴───┴───┴───┴───┴───┴───┴───┘");
	}

	/**
	 * Print possible destination cells in the terminal
	 * 
	 * @param m ArrayList of cells
	 */
	public void printMovements(ArrayList<Coord> m) {
		int found = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {

				for (int j2 = 0; j2 < m.size(); j2++) {
					if (m.get(j2).getX() == i && m.get(j2).getY() == j) {
						System.out.print("| × ");
						// System.out.print("|"+m.get(j2).getX()+";"+m.get(j2).getY());
						found++;
					}
				}
				if (found == 0) {
					System.out.print("|   ");
				} else {
					found--;
				}
			}

			System.out.print("|\n");
		}
	}

	/**
	 * Gives us the Piece that is at x and y coordinates
	 * 
	 * @param x Coordinate
	 * @param y Coordinate
	 * @return a Piece at (x;y)
	 */
	public Piece getPieceOnCell(int x, int y) {
		return this.getCells()[x][y];
	}

	/**
	 * Rework the possible moves according to the board
	 * 
	 * @param king is the piece we work on
	 */
	public void cleanMovesKing(Piece king) {
		ArrayList<Coord> impossible_moves = new ArrayList<Coord>();
		ArrayList<Coord> king_final_moves = new ArrayList<Coord>();
		king_final_moves.addAll(king.getTheoretical_moves());

		// We verify if the destination cells contains same color piece
		for (int i = 0; i < king.getMoves().size(); i++) {
			int x = king.getMoves().get(i).getX();
			int y = king.getMoves().get(i).getY();
			Coord c = new Coord(x, y);
			// if it's the same color we remove it
			if (getPieceOnCell(x, y).getColor() == king.getColor()) {
				impossible_moves.add(c);
			}
		}
		king_final_moves.removeAll(impossible_moves);

		// we return ArrayList with do-able moves for king
		king.setMoves(king_final_moves);
	}

	/**
	 * Rework the possible moves according to the board
	 * 
	 * @param pawn is the piece we work on
	 */
	public void cleanMovesPawn(Piece pawn) {
		ArrayList<Coord> impossible_moves = new ArrayList<Coord>();
		ArrayList<Coord> more_moves = new ArrayList<Coord>();
		ArrayList<Coord> pawn_final_moves = pawn.getMoves();
		int removed = 0;

		// We verify if the destination cells contains a piece
		for (int i = 0; i < pawn.getMoves().size(); i++) {
			int x = pawn.getMoves().get(i).getX();
			int y = pawn.getMoves().get(i).getY();

			Coord c = new Coord(x, y);
			int color = getPieceOnCell(x, y).getColor();

			// if it contains any piece we remove it
			if (color != -1) {
				impossible_moves.add(c);
				removed++;
			}
			// if the opponent is in front, we remove every move
			if (removed == 1) {
				impossible_moves.add(c);
			}
		}

		int px = pawn.getC().getX();
		int py = pawn.getC().getY();

		// We check for diagonal eating-moves
		// if it's white
		if (pawn.getColor() == 0) {
			// we check if there is an opponent piece there and if it's not outside of the
			// board
			if (px - 1 >= 0 && py - 1 >= 0) {
				if (getPieceOnCell(px - 1, py - 1).getColor() == 1) {
					more_moves.add(new Coord(px - 1, py - 1));
				}
			}
			if (px - 1 >= 0 && py + 1 <= 7) {
				if (getPieceOnCell(px - 1, py + 1).getColor() == 1) {
					more_moves.add(new Coord(px - 1, py + 1));
				}
			}
		}
		// if it's black
		else {
			// we check if there is an opponent piece there and if it's not outside of the
			// board
			if (px + 1 <= 7 && py - 1 >= 0) {
				if (getPieceOnCell(px + 1, py - 1).getColor() == 0) {
					more_moves.add(new Coord(px + 1, py - 1));
				}
			}
			if (px + 1 <= 7 && py + 1 <= 7) {
				if (getPieceOnCell(px + 1, py + 1).getColor() == 0) {
					more_moves.add(new Coord(px + 1, py + 1));
				}
			}
		}
		pawn_final_moves.removeAll(impossible_moves);
		pawn_final_moves.addAll(more_moves);

		// we update moves with do-able moves for pawn
		pawn.setMoves(pawn_final_moves);

	}

	/**
	 * Rework the possible moves according to the board
	 * 
	 * @param rook is the piece we work on
	 */
	public void cleanMovesRook(Piece rook) {
		ArrayList<Coord> impossible_moves = new ArrayList<Coord>();
		ArrayList<Coord> rook_final_moves = rook.getMoves();
		ArrayList<Coord> moves_up = new ArrayList<Coord>();
		ArrayList<Coord> moves_right = new ArrayList<Coord>();
		ArrayList<Coord> moves_down = new ArrayList<Coord>();
		ArrayList<Coord> moves_left = new ArrayList<Coord>();
		int color = rook.getColor();
		int ymin = 0;
		int ymax = 7;
		int xmin = 0;
		int xmax = 7;
		int obstacle_up = 0;
		int obstacle_down = 0;
		int obstacle_right = 0;
		int obstacle_left = 0;

		// First we split the rook moves in 4 ArrayList for the 4 orientations
		for (int i = 0; i < rook.getMoves().size(); i++) {
			int x = rook.getMoves().get(i).getX();
			int y = rook.getMoves().get(i).getY();

			// We split in 4 orientations
			if (x == rook.getC().getX() && y > rook.getC().getY()) {
				moves_right.add(new Coord(x, y));
			} else if (x == rook.getC().getX() && y < rook.getC().getY()) {
				moves_left.add(new Coord(x, y));
			} else if (x > rook.getC().getX() && rook.getC().getY() == y) {
				moves_down.add(new Coord(x, y));
			} else if (x < rook.getC().getX() && rook.getC().getY() == y) {
				moves_up.add(new Coord(x, y));
			}
		}

		// For each orientation we need to determine if there is a piece in the middle
		// Right
		for (int i = 0; i < moves_right.size(); i++) {
			int x = moves_right.get(i).getX();
			int y = moves_right.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_right == 0) {
				ymax = y;
				obstacle_right++;
			}
			// if we find one nearer
			if (y < ymax && temp_color != -1) {
				ymax = y;
			}

		}
		// Left
		for (int i = 0; i < moves_left.size(); i++) {
			int x = moves_left.get(i).getX();
			int y = moves_left.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_left == 0) {
				ymin = y;
				obstacle_left++;
			}
			// if we find one nearer
			if (y > ymin && temp_color != -1) {
				ymin = y;
			}
		}
		// Up
		for (int i = 0; i < moves_up.size(); i++) {
			int x = moves_up.get(i).getX();
			int y = moves_up.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_up == 0) {
				xmin = x;
				obstacle_up++;
			}
			// if we find one nearer
			if (x > xmin && temp_color != -1) {
				xmin = x;
			}
		}

		// Down
		for (int i = 0; i < moves_down.size(); i++) {
			int x = moves_down.get(i).getX();
			int y = moves_down.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_down == 0) {
				xmax = x;
				obstacle_down++;
			}
			// if we find one nearer
			if (x < xmax && temp_color != -1) {
				xmax = x;
			}
		}

		// Now we keep only the ones between xmin;ymin and xmam;ymax
		for (int i = 0; i < rook_final_moves.size(); i++) {
			int x = rook_final_moves.get(i).getX();
			int y = rook_final_moves.get(i).getY();
			if (x > xmax || x < xmin || y > ymax || y < ymin) {
				impossible_moves.add(new Coord(x, y));
			}
		}

		rook_final_moves.removeAll(impossible_moves);
		impossible_moves.clear();

		// Then we remove cells where piece of same color are in
		for (int i = 0; i < rook_final_moves.size(); i++) {
			int x = rook.getMoves().get(i).getX();
			int y = rook.getMoves().get(i).getY();

			if (color == getPieceOnCell(x, y).getColor()) {
				impossible_moves.add(new Coord(x, y));
			}
		}
		rook_final_moves.removeAll(impossible_moves);

		rook.setMoves(rook_final_moves);
	}

	/**
	 * Rework the possible moves according to the board
	 * 
	 * @param bishop is the piece we work on
	 */
	public void cleanMovesBishop(Piece bishop) {
		ArrayList<Coord> impossible_moves = new ArrayList<Coord>();
		ArrayList<Coord> bishop_final_moves = bishop.getMoves();
		ArrayList<Coord> moves_NE = new ArrayList<Coord>();
		ArrayList<Coord> moves_SE = new ArrayList<Coord>();
		ArrayList<Coord> moves_SW = new ArrayList<Coord>();
		ArrayList<Coord> moves_NW = new ArrayList<Coord>();
		int color = bishop.getColor();
		Coord SW_max = new Coord(7, 7);
		Coord SE_max = new Coord(7, 0);
		Coord NW_max = new Coord(0, 7);
		Coord NE_max = new Coord(0, 0);
		int obstacle_NE = 0;
		int obstacle_NW = 0;
		int obstacle_SE = 0;
		int obstacle_SW = 0;

		// First we split the rook moves in 4 ArrayList for the 4 orientations
		for (int i = 0; i < bishop.getMoves().size(); i++) {
			int x = bishop.getMoves().get(i).getX();
			int y = bishop.getMoves().get(i).getY();

			// We split in 4 orientations
			if (x > bishop.getC().getX() && y > bishop.getC().getY()) {
				moves_SW.add(new Coord(x, y));
			} else if (x > bishop.getC().getX() && y < bishop.getC().getY()) {
				moves_SE.add(new Coord(x, y));
			} else if (x < bishop.getC().getX() && bishop.getC().getY() > y) {
				moves_NE.add(new Coord(x, y));
			} else if (x < bishop.getC().getX() && bishop.getC().getY() < y) {
				moves_NW.add(new Coord(x, y));
			}
		}

		// For each orientation we need to determine if there is a piece in the middle
		// SW
		for (int i = 0; i < moves_SW.size(); i++) {
			int x = moves_SW.get(i).getX();
			int y = moves_SW.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_SW == 0) {
				obstacle_SW++;
				SW_max = new Coord(x, y);
			}
			// if we find one nearer
			if (x < SW_max.getX() && temp_color != -1) {
				SW_max = new Coord(x, y);
			}
		}
		// SE
		for (int i = 0; i < moves_SE.size(); i++) {
			int x = moves_SE.get(i).getX();
			int y = moves_SE.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_SE == 0) {
				obstacle_SE++;
				SE_max = new Coord(x, y);
			}
			// if we find one nearer
			if (x < SE_max.getX() && temp_color != -1) {
				SE_max = new Coord(x, y);
			}
		}
		// NW
		for (int i = 0; i < moves_NW.size(); i++) {
			int x = moves_NW.get(i).getX();
			int y = moves_NW.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_NW == 0) {
				obstacle_NW++;
				NW_max = new Coord(x, y);
			}
			// if we find one nearer
			if (x > NW_max.getX() && temp_color != -1) {
				NW_max = new Coord(x, y);
			}
		}
		// NE
		for (int i = 0; i < moves_NE.size(); i++) {
			int x = moves_NE.get(i).getX();
			int y = moves_NE.get(i).getY();
			int temp_color = getPieceOnCell(x, y).getColor();
			// if there is
			if (temp_color != -1 && obstacle_NE == 0) {
				obstacle_NE++;
				NE_max = new Coord(x, y);
			}
			// if we find one nearer
			if (x > NE_max.getX() && temp_color != -1) {
				NE_max = new Coord(x, y);
			}
		}
		// Now we keep only the ones between xmin;ymin and xmam;ymax
		for (int i = 0; i < bishop_final_moves.size(); i++) {
			int x = bishop_final_moves.get(i).getX();
			int y = bishop_final_moves.get(i).getY();

			// farther than SW
			if (x > SW_max.getX() && y > SW_max.getY()) {
				impossible_moves.add(new Coord(x, y));
			}
			// farther than SE
			else if (x > SE_max.getX() && y < SE_max.getY()) {
				impossible_moves.add(new Coord(x, y));
			}
			// farther than NE
			else if (x < NE_max.getX() && y < NE_max.getY()) {
				impossible_moves.add(new Coord(x, y));
			}
			// farther than NW
			else if (x < NW_max.getX() && y > NW_max.getY()) {
				impossible_moves.add(new Coord(x, y));
			}
		}

		bishop_final_moves.removeAll(impossible_moves);
		impossible_moves.clear();

		// Then we remove cells where piece of same color are in
		for (int i = 0; i < bishop_final_moves.size(); i++) {
			int x = bishop.getMoves().get(i).getX();
			int y = bishop.getMoves().get(i).getY();

			if (color == getPieceOnCell(x, y).getColor()) {
				impossible_moves.add(new Coord(x, y));
			}
		}
		bishop_final_moves.removeAll(impossible_moves);

		bishop.setMoves(bishop_final_moves);
	}

	/**
	 * Rework the possible moves according to the board
	 * 
	 * @param queen is the piece we work on
	 */
	public void cleanMovesQueen(Piece queen) {

		// The queen is having the same moves as rook and bishop combined !
		cleanMovesBishop(queen);
		cleanMovesRook(queen);

	}

	/**
	 * Rework the possible moves according to the board
	 * 
	 * @param horse is the piece we work on
	 */
	public void cleanMovesHorse(Piece horse) {
		ArrayList<Coord> impossible_moves = new ArrayList<Coord>();
		ArrayList<Coord> horse_final_moves = horse.getMoves();
		// We verify if the destination cells contains same color piece
		for (int i = 0; i < horse.getMoves().size(); i++) {
			int x = horse.getMoves().get(i).getX();
			int y = horse.getMoves().get(i).getY();
			Coord c = new Coord(x, y);
			// if it's the same color we remove it
			if (getPieceOnCell(x, y).getColor() == horse.getColor()) {
				impossible_moves.add(c);
			}
		}
		horse_final_moves.removeAll(impossible_moves);

		// we return ArrayList with do-able moves for horse
		horse.setMoves(horse_final_moves);
	}

	/**
	 * Is the switch that call the adequate function
	 */
	public void updateMoves() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				switch (cells[i][j].getType()) {
				case 'k':
					cleanMovesKing(cells[i][j]);
					break;
				case 'p':
					cleanMovesPawn(cells[i][j]);
					break;
				case 'h':
					cleanMovesHorse(cells[i][j]);
					break;
				case 'r':
					cleanMovesRook(cells[i][j]);
					break;
				case 'q':
					cleanMovesQueen(cells[i][j]);
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * Verify is the actual player can do the big castling move
	 * 
	 * @return true or false in answer of the method's name
	 */
	public boolean isBigCastlingOk() {
		Piece king = this.getKing();
		// first we get the big rook
		// for the white
		Piece big_rook;
		if (this.getTurn() == 0) {
			big_rook = getPieceOnCell(7, 0);
		}
		// for the black
		else {
			big_rook = getPieceOnCell(0, 0);
		}

		/**
		 * we check if they moved
		 */
		if (king.getMoved() || big_rook.getMoved()) {
			return false;
		}

		/**
		 * we check if there is at least one piece between them
		 */
		// for the white
		if (this.getTurn() == 0) {
			for (int i = 1; i <= 3; i++) {
				if (this.getCells()[big_rook.getC().getX()][i].getColor() != -1) {
					return false;
				}
			}
		}
		// for the black
		else {
			for (int i = 1; i <= 3; i++) {
				if (this.getCells()[big_rook.getC().getX()][i].getColor() != -1) {
					return false;
				}
			}
		}

		// Then if the player is going to be check after castling
		// we get the soon-to-be king coordinates

		Piece soon_to_be_king = new Piece(-1, -1, this.getTurn(), 'k');
		Piece soon_to_be_rook = new Piece(-1, -1, this.getTurn(), 'r');
		soon_to_be_king.setC(new Coord(king.getC().getX(), king.getC().getY() - 2));
		soon_to_be_rook.setC(new Coord(big_rook.getC().getX(), big_rook.getC().getY() + 3));

		Board simulation = this.clone();

		// we remove the old king
		simulation.cells[king.getC().getX()][king.getC().getY()] = new Piece(king.getC().getX(), king.getC().getY());
		// and the old rook
		simulation.cells[big_rook.getC().getX()][big_rook.getC().getY()] = new Piece(big_rook.getC().getX(),
				big_rook.getC().getY());

		// we put the king in the future cell
		simulation.cells[soon_to_be_king.getC().getX()][soon_to_be_king.getC().getY()] = new Piece(
				soon_to_be_king.getC().getX(), soon_to_be_king.getC().getY(), this.getTurn(), 'k');
		// and the rook
		simulation.cells[soon_to_be_rook.getC().getX()][soon_to_be_rook.getC().getY()] = new Piece(
				soon_to_be_rook.getC().getX(), soon_to_be_rook.getC().getY(), this.getTurn(), 'r');

		if (simulation.isChecked()) {
			return false;
		}

		return true;
	}

	/**
	 * Verify is the actual player can do the small castling move
	 * 
	 * @return true or false in answer of the method's name
	 */
	public boolean isSmallCastlingOk() {
		Piece king = this.getKing();
		// first we get the big rook
		// for the white
		Piece small_rook;
		if (this.getTurn() == 0) {
			small_rook = getPieceOnCell(7, 7);
		}
		// for the black
		else {
			small_rook = getPieceOnCell(0, 7);
		}

		/**
		 * we check if they moved
		 */
		if (king.getMoved() || small_rook.getMoved()) {
			return false;
		}

		/**
		 * we check if there is at least one piece between them
		 */
		// for the white
		if (this.getTurn() == 0) {
			for (int i = 5; i <= 6; i++) {
				if (this.getCells()[small_rook.getC().getX()][i].getColor() != -1) {

					getPieceOnCell(small_rook.getC().getX(), i);
					return false;
				}
			}
		}
		// for the black
		else {
			for (int i = 5; i <= 6; i++) {
				if (this.getCells()[small_rook.getC().getX()][i].getColor() != -1) {
					return false;
				}
			}
		}

		// Then if the player is going to be check after castling
		// we get the soon-to-be king coordinates

		Piece soon_to_be_king = new Piece(-1, -1, this.getTurn(), 'k');
		Piece soon_to_be_rook = new Piece(-1, -1, this.getTurn(), 'r');

		soon_to_be_king.setC(new Coord(king.getC().getX(), king.getC().getY() + 2));
		soon_to_be_rook.setC(new Coord(small_rook.getC().getX(), small_rook.getC().getY() - 2));

		Board simulation = this.clone();

		// we remove the old king
		simulation.cells[king.getC().getX()][king.getC().getY()] = new Piece(king.getC().getX(), king.getC().getY());
		// and the old rook
		simulation.cells[small_rook.getC().getX()][small_rook.getC().getY()] = new Piece(small_rook.getC().getX(),
				small_rook.getC().getY());

		// we put the king in the future cell
		simulation.cells[soon_to_be_king.getC().getX()][soon_to_be_king.getC().getY()] = new Piece(
				soon_to_be_king.getC().getX(), soon_to_be_king.getC().getY(), this.getTurn(), 'k');
		// and the rook
		simulation.cells[soon_to_be_rook.getC().getX()][soon_to_be_rook.getC().getY()] = new Piece(
				soon_to_be_rook.getC().getX(), soon_to_be_rook.getC().getY(), this.getTurn(), 'r');

		if (simulation.isChecked()) {
			System.out.println("will be checked");
			return false;
		}

		return true;

	}

	/**
	 * Verify is the actual player can do a castling move
	 * 
	 * @return true or false in answer of the method's name
	 */
	public boolean isCastlingOk() {
		// impossible if the player is being checked
		if (isChecked()) {
			return false;
		}
		return true;
	}

	/**
	 * To find if the king is being checked
	 * 
	 * @return a boolean to answer the method name
	 */
	public boolean isChecked() {
		Piece king = getKing();
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				ArrayList<Coord> temp_moves = cells[i][j].getMoves();
				for (int j2 = 0; j2 < temp_moves.size(); j2++) {
					if (temp_moves.get(j2).equals(king.getC())) {
						this.setResult(this.getTurn() * 1000 + 666);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Simple method to have the actual turn king's
	 * 
	 * @return the piece corresponding of the king
	 */
	public Piece getKing() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (cells[i][j].getColor() == this.getTurn() && cells[i][j].getType() == 'k') {
					return cells[i][j];
				}
			}
		}
		// just in case
		return new Piece(-1, -1);
	}

	/**
	 * Method to move one piece
	 * 
	 * @param hunter the piece to move
	 * @param hunted the destination piece
	 */
	public void eat(Piece hunter, Piece hunted) {
		hunter.setMoved(true);

		int hunter_x = hunter.getC().getX();
		int hunter_y = hunter.getC().getY();
		int hunted_x = hunted.getC().getX();
		int hunted_y = hunted.getC().getY();

		// We must check that the hunted is reachable by the hunter
		for (int i = 0; i < hunter.getMoves().size(); i++) {
			if (!hunter.getMoves().contains(new Coord(hunted.getC().getX(), hunted.getC().getY()))) {
				//hunted.printPiece();
				//System.out.println("is out of reach for");
				//hunter.printPiece();
				//return;
			}
		}

		// in the hunter cell we put nothing
		this.getCells()[hunter_x][hunter_y] = new Piece(hunter_x, hunter_y);

		// in the hunted cell we put the hunter
		this.getCells()[hunted_x][hunted_y] = new Piece(hunted_x, hunted_y, hunter.getColor(), hunter.getType());

		// Todo
		// How do we manage time ?
		// little trick to alternate between 1 and 0
		this.setTurn(1 - this.getTurn());

		// if the turn is changed automatically we update moves right after eating, else
		// we wait for manual time switching
		updateMoves();
	}

	/**
	 * Verify every cells on extremity lines
	 */
	public void promotion() {
		for (int i = 0; i < 7; i++) {
			// we check if on the black end line there is a pawn
			if (this.getCells()[0][i].getType() == 'p') {
				// it become a queen
				this.getCells()[0][i].setType('q');
			}
			// we check if on the white end line there is a pawn
			if (this.getCells()[7][i].getType() == 'p') {
				this.getCells()[7][i].setType('q');
			}
		}
	}

	/**
	 * To verify is the actual player has lost
	 * @return true if lost false if not
	 */
	public boolean isCheckMate() {
		// first of all the player needs to be checked
		if (!isChecked()) {
			return false;
		}
		// we get king coordinates
		Piece king = getKing();
		int king_moves_checked = 0;
		
		Board simulation = this.clone();
		
		//we simulate every movement possible for the king and verify if he's checked
		for (int i = 0; i < king.getMoves().size(); i++) {
			Piece temp = simulation.getCells()[king.getMoves().get(i).getX()][king.getMoves().get(i).getY()];
			
			//We move king to possible cell
			simulation.getCells()[king.getMoves().get(i).getX()][king.getMoves().get(i).getY()] = king;
			simulation.getCells()[king.getC().getX()][king.getC().getY()] = new Piece(king.getC().getX(), king.getC().getY());
			
			if (simulation.isChecked()) {
				king_moves_checked++;
			}
			
			//then we move king back
			simulation.getCells()[king.getMoves().get(i).getX()][king.getMoves().get(i).getY()] = temp;
			simulation.getCells()[king.getC().getX()][king.getC().getY()] = king;
		}

		/*
		// for every moves the king can make, we verify if he's not being checked there
		// if we find at least one move where he's not in check it's fine
		for (int i = 0; i < king.getMoves().size(); i++) {
			// we run through every cells
			for (int j = 0; j < cells.length; j++) {
				for (int j2 = 0; j2 < cells[j].length; j2++) {
					ArrayList<Coord> temp_moves = getCells()[j][j2].getMoves();
					// we run through the moves of the actual piece
					for (int k = 0; k < temp_moves.size(); k++) {
						// if the move of the king is in the list of the piece move's we try the next
						// king move
						if (king.getMoves().contains(temp_moves.get(k))) {
							king_moves_checked++;
							i++;
						}
					}
				}
			}
		}*/
		// the king can't go anywhere
		if (king_moves_checked == king.getMoves().size()) {
			// the other player wins
			this.setResult(1 - this.getTurn());
			return true;
		}
		return false;
	}

}
