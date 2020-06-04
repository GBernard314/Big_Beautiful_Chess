package com.bigbeautifulchess.engine;

import com.bigbeautifulchess.tools.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

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
	 * -666 if player 1 is being checked <br>
	 * -1666 if player 2 is being checked
	 */
	private int result;

	/**
	 * Time allowed for black player in seconds
	 */
	private int time_black = 2400;

	/**
	 * Time allowed for white player in seconds
	 */
	private int time_white = 2400;

	/**
	 * We need to store a date in order to manage time
	 */
	private TimeStamp storage;

	/**
	 * We save every move made
	 */
	private ArrayList<Mov> historic;

	/**
	 * To have an empty board
	 *
	 * @param b
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
		this.historic = new ArrayList<Mov>();
		this.storage = new TimeStamp();
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
		c[0][0] = new Piece(0, 0, 1, 'r', false);
		c[0][1] = new Piece(0, 1, 1, 'h', false);
		c[0][2] = new Piece(0, 2, 1, 'b', false);
		c[0][3] = new Piece(0, 3, 1, 'q', false);
		c[0][4] = new Piece(0, 4, 1, 'k', false);
		c[0][5] = new Piece(0, 5, 1, 'b', false);
		c[0][6] = new Piece(0, 6, 1, 'h', false);
		c[0][7] = new Piece(0, 7, 1, 'r', false);
		for (int i = 0; i <= 7; i++) {
			c[1][i] = new Piece(1, i, 1, 'p', false);
		}

		// White pieces
		c[7][0] = new Piece(7, 0, 0, 'r', false);
		c[7][1] = new Piece(7, 1, 0, 'h', false);
		c[7][2] = new Piece(7, 2, 0, 'b', false);
		c[7][3] = new Piece(7, 3, 0, 'q', false);
		c[7][4] = new Piece(7, 4, 0, 'k', false);
		c[7][5] = new Piece(7, 5, 0, 'b', false);
		c[7][6] = new Piece(7, 6, 0, 'h', false);
		c[7][7] = new Piece(7, 7, 0, 'r', false);
		for (int i = 0; i <= 7; i++) {
			c[6][i] = new Piece(6, i, 0, 'p', false);
		}
		this.cells = c;
		this.turn = 0;
		this.result = -1;
		this.historic = new ArrayList<Mov>();
		this.storage = new TimeStamp();
	}

	/**
	 * To create instance of board with BDD data
	 *
	 * @param bdd
	 * @param turn
	 * @param result
	 * @param time_black
	 * @param time_white
	 * @param storage
	 * @param historic
	 */
	public Board(String bdd, int turn, int result, int time_black, int time_white, String storage,
			String historic) {
		super();
		Piece[][] c = new Piece[8][8];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				c[i][j] = new Piece(i, j);
			}
		}

		// we split to have the different type of pieces
		String[] types = bdd.split("\\|");
		int nb_types = types.length;

		// for each type we split to have the different colors
		for (int i = 0; i < types.length; i++) {
			String[] colors = types[i].split("\\/");
			int nb_colors = colors.length;
			// for each color we split to have the different pieces
			for (int j = 0; j < colors.length; j++) {

				String[] pieces = colors[j].split(";");
				int nb_pieces = pieces.length;

				// for each piece we split to have the x and y
				for (int k = 0; k < nb_pieces; k++) {
					String[] coords = pieces[k].split("\\,");

					int x = Integer.parseInt(coords[0]);
					int y = Integer.parseInt(coords[1]);

					// boolean
					boolean m = Integer.parseInt(coords[2]) >= 1 ? true : false;

					int color = j;
					switch (i) {
					case 0:
						c[x][y] = new Piece(x, y, color, 'p', m);
						break;
					case 1:
						c[x][y] = new Piece(x, y, color, 'r', m);
						break;
					case 2:
						c[x][y] = new Piece(x, y, color, 'h', m);
						break;
					case 3:
						c[x][y] = new Piece(x, y, color, 'b', m);
						break;
					case 4:
						c[x][y] = new Piece(x, y, color, 'q', m);
						break;
					case 5:
						c[x][y] = new Piece(x, y, color, 'k', m);
						break;

					default:
						c[x][y] = new Piece(x, y);
						break;
					}
				}
			}
		}

		ArrayList<Mov> h = new ArrayList<Mov>();

		// we split to have every move done until now
		String[] moves = historic.split(";");
		int nb_moves = moves.length;

		if (historic != "") {
			// for each move we split to have from to
			for (int i = 0; i < nb_moves; i++) {
				String[] fromTo = moves[i].split("-");

				// hunter data
				String[] dataHunter = fromTo[0].split(":");
				char hunter_type = dataHunter[0].charAt(0);

				String[] coordHunter = dataHunter[1].split(",");
				int x_hunter = Integer.parseInt(coordHunter[0]);
				int y_hunter = Integer.parseInt(coordHunter[1]);

				// hunted data
				String[] dataHunted = fromTo[1].split(":");
				char hunted_type = dataHunted[0].charAt(0);

				String[] coordHunted = dataHunted[1].split(",");
				int x_hunted = Integer.parseInt(coordHunted[0]);
				int y_hunted = Integer.parseInt(coordHunted[1]);

				h.add(new Mov(hunter_type, new Coord(x_hunter, y_hunter), hunted_type, new Coord(x_hunted, y_hunted)));
			}
		}

		this.cells = c;
		this.turn = turn;
		this.result = result;
		this.time_black = time_black;
		this.time_white = time_white;
		this.historic = h;
		this.storage = new TimeStamp(Integer.parseInt(storage.split(":")[0]), Integer.parseInt(storage.split(":")[1]), Integer.parseInt(storage.split(":")[2]));

	}


	
	/**
	 * Serialize content of Cells[][] into String
	 * @return String serialized
	 */
	public String cellsToString() {
		Piece[][] c = this.getCells();

		StringBuilder finalString = new StringBuilder();
		StringBuilder pawnW = new StringBuilder();
		StringBuilder pawnB = new StringBuilder();
		StringBuilder rookW = new StringBuilder();
		StringBuilder rookB = new StringBuilder();
		StringBuilder horseW = new StringBuilder();
		StringBuilder horseB = new StringBuilder();
		StringBuilder bishopW = new StringBuilder();
		StringBuilder bishopB = new StringBuilder();
		StringBuilder queenW = new StringBuilder();
		StringBuilder queenB = new StringBuilder();
		StringBuilder kingW = new StringBuilder();
		StringBuilder kingB = new StringBuilder();

		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				int m = c[i][j].getMoved() == true ? 1: 0;
				int color = c[i][j].getColor();
				switch (c[i][j].getType()) {
				case 'p':
					if (color == 0) {
						pawnW.append(i);
						pawnW.append(",");
						pawnW.append(j);
						pawnW.append(",");
						pawnW.append(m);
						pawnW.append(";");
					} else {
						pawnB.append(i);
						pawnB.append(",");
						pawnB.append(j);
						pawnB.append(",");
						pawnB.append(m);
						pawnB.append(";");
					}
					break;
				case 'r':
					if (color == 0) {
						rookW.append(i);
						rookW.append(",");
						rookW.append(j);
						rookW.append(",");
						rookW.append(m);
						rookW.append(";");
					} else {
						rookB.append(i);
						rookB.append(",");
						rookB.append(j);
						rookB.append(",");
						rookB.append(m);
						rookB.append(";");
					}
					break;
				case 'h':
					if (color == 0) {
						horseW.append(i);
						horseW.append(",");
						horseW.append(j);
						horseW.append(",");
						horseW.append(m);
						horseW.append(";");
					} else {
						horseB.append(i);
						horseB.append(",");
						horseB.append(j);
						horseB.append(",");
						horseB.append(m);
						horseB.append(";");
					}
					break;
				case 'b':
					if (color == 0) {
						bishopW.append(i);
						bishopW.append(",");
						bishopW.append(j);
						bishopW.append(",");
						bishopW.append(m);
						bishopW.append(";");
					} else {
						bishopB.append(i);
						bishopB.append(",");
						bishopB.append(j);
						bishopB.append(",");
						bishopB.append(m);
						bishopB.append(";");
					}
					break;
				case 'q':
					if (color == 0) {
						queenW.append(i);
						queenW.append(",");
						queenW.append(j);
						queenW.append(",");
						queenW.append(m);
						queenW.append(";");
					} else {
						queenB.append(i);
						queenB.append(",");
						queenB.append(j);
						queenB.append(",");
						queenB.append(m);
						queenB.append(";");
					}
					break;
				case 'k':
					if (color == 0) {
						kingW.append(i);
						kingW.append(",");
						kingW.append(j);
						kingW.append(",");
						kingW.append(m);
						kingW.append(";");
					} else {
						kingB.append(i);
						kingB.append(",");
						kingB.append(j);
						kingB.append(",");
						kingB.append(m);
						kingB.append(";");
					}
					break;
				}
			}
		}

		finalString.append(pawnW);
		finalString.append("/");
		finalString.append(pawnB);
		finalString.append("|");
		finalString.append(rookW);
		finalString.append("/");
		finalString.append(rookB);
		finalString.append("|");
		finalString.append(horseW);
		finalString.append("/");
		finalString.append(horseB);
		finalString.append("|");
		finalString.append(bishopW);
		finalString.append("/");
		finalString.append(bishopB);
		finalString.append("|");
		finalString.append(queenW);
		finalString.append("/");
		finalString.append(queenB);
		finalString.append("|");
		finalString.append(kingW);
		finalString.append("/");
		finalString.append(kingB);
		finalString.append("|");
		
		return finalString.toString();
	}

	/**
	 * Serialize content of historic into String
	 * @return String serialized
	 */
	public String historicToString() {
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < this.getHistoric().size(); i++) {
			Mov temp = this.getHistoric().get(i);
			char ch1 = temp.getP1();
			char ch2 = temp.getP2();
			int x1 = temp.getFrom().getX();
			int y1 = temp.getFrom().getY();
			int x2 = temp.getTo().getX();
			int y2 = temp.getTo().getY();
			strBuilder.append(ch1);
			strBuilder.append(":");
			strBuilder.append(x1);
			strBuilder.append(",");
			strBuilder.append(y1);
			strBuilder.append("-");
			strBuilder.append(ch2);
			strBuilder.append(":");
			strBuilder.append(x2);
			strBuilder.append(",");
			strBuilder.append(y2);
			strBuilder.append(";");
		}
		return strBuilder.toString();
	}
	
	public String storageToString() {
		return this.storage.toString();
	}
	
	public Board(Piece[][] cells, int turn, int result) {
		super();
		this.cells = cells;
		this.turn = turn;
		this.result = result;
		this.historic = new ArrayList<Mov>();

	}

	public ArrayList<Mov> getHistoric() {
		return historic;
	}

	public void setHistoric(ArrayList<Mov> historic) {
		this.historic = historic;
	}

	public Piece[][] getCells() {
		return cells;
	}

	public void setCells(Piece[][] cells) {
		for (int i = 0; i < this.cells.length; i++) {
			for (int j = 0; j < this.cells[i].length; j++) {
				Piece temp = new Piece(cells[i][j].getC().getX(), cells[i][j].getC().getY(), cells[i][j].getColor(),
						cells[i][j].getType(), cells[i][j].getMoved());
				// this.cells[i][j] = cells[i][j];
				this.cells[i][j] = temp;
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

	public int getTime_black() {
		return time_black;
	}

	public void setTime_black(int time_black) {
		this.time_black = time_black;
	}

	public int getTime_white() {
		return time_white;
	}

	public void setTime_white(int time_white) {
		this.time_white = time_white;
	}
	
	public void setStorage(TimeStamp storage) {
		this.storage = storage;
	}
	
	public TimeStamp getStorage() {
		return this.storage;
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
				boolean moved = getCells()[i][j].getMoved();

				cells[i][j] = new Piece(x, y, color, type, moved);
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
		// System.out.println("king moves = " + this.getKing().getMoves().size());
		// this.getKing().printPiece();
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
	 * Print the board in the terminal, only the board
	 */
	public void printBoardSimple() {
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
						System.out.print("│ ▲ ");
					} else {
						System.out.print("│ ▼ ");
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
	 * @param p ArrayList of cells
	 */
	public void printMovements(Piece p) {
		System.out.println("┌───┬───┬───┬───┬───┬───┬───┬───┐");

		int found = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {

				for (int j2 = 0; j2 < p.getMoves().size(); j2++) {
					if (p.getMoves().get(j2).getX() == i && p.getMoves().get(j2).getY() == j) {
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
		System.out.println("└───┴───┴───┴───┴───┴───┴───┴───┘");

	}

	/**
	 * Print the entire historic of moves
	 */

	public void printHistoric() {
		System.out.println("Historic : ");
		for (int i = 0; i < this.historic.size(); i++) {
			char t1 = this.historic.get(i).getP1();
			char t2 = this.historic.get(i).getP2();
			int x1 = this.historic.get(i).getFrom().getX();
			int y1 = this.historic.get(i).getFrom().getY();
			int x2 = this.historic.get(i).getTo().getX();
			int y2 = this.historic.get(i).getTo().getY();
			System.out.println(t1 + " : (" + x1 + ";" + y1 + ") -> " + t2 + " : (" + x2 + ";" + y2 + ")");
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

		impossible_moves.clear();

		// the king cannot go in check deliberately
		for (int i = 0; i < king_final_moves.size(); i++) {
			Board simulation = this.clone();
			int x = king_final_moves.get(i).getX();
			int y = king_final_moves.get(i).getY();
			simulation.getCells()[king.getC().getX()][king.getC().getY()] = new Piece(king.getC().getX(),
					king.getC().getY());
			simulation.getCells()[x][y] = new Piece(x, y, this.getTurn(), 'k', true);
			if (simulation.isChecked()) {
				impossible_moves.add(new Coord(x, y));
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
		if (pawn.getMoved()) {
			if (pawn.getColor() == 1) {
				impossible_moves.add(new Coord(pawn.getC().getX() + 2, pawn.getC().getY()));
			} else {
				impossible_moves.add(new Coord(pawn.getC().getX() - 2, pawn.getC().getY()));
			}
		}


		int px = pawn.getC().getX();
		int py = pawn.getC().getY();
		
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
			// if there is a piece directly in front we remove every move
			if (pawn.getColor() == 0) {
				if (getPieceOnCell(px-1, py).getType() != 'e') {
					pawn_final_moves.clear();
				}
			} else {
				if (getPieceOnCell(px+1, py).getType() != 'e') {
					pawn_final_moves.clear();
				}
			}
		}

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
		

		ArrayList<Coord> impossible_moves = new ArrayList<Coord>();
		ArrayList<Coord> rook_final_moves = queen.getMoves();
		ArrayList<Coord> moves_up = new ArrayList<Coord>();
		ArrayList<Coord> moves_right = new ArrayList<Coord>();
		ArrayList<Coord> moves_down = new ArrayList<Coord>();
		ArrayList<Coord> moves_left = new ArrayList<Coord>();
		int color = queen.getColor();
		int ymin = 0;
		int ymax = 7;
		int xmin = 0;
		int xmax = 7;
		int obstacle_up = 0;
		int obstacle_down = 0;
		int obstacle_right = 0;
		int obstacle_left = 0;

		// First we split the rook moves in 4 ArrayList for the 4 orientations
		for (int i = 0; i < queen.getMoves().size(); i++) {
			int x = queen.getMoves().get(i).getX();
			int y = queen.getMoves().get(i).getY();

			// We split in 4 orientations
			if (x == queen.getC().getX() && y > queen.getC().getY()) {
				moves_right.add(new Coord(x, y));
			} else if (x == queen.getC().getX() && y < queen.getC().getY()) {
				moves_left.add(new Coord(x, y));
			} else if (x > queen.getC().getX() && queen.getC().getY() == y) {
				moves_down.add(new Coord(x, y));
			} else if (x < queen.getC().getX() && queen.getC().getY() == y) {
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
			int x = queen.getMoves().get(i).getX();
			int y = queen.getMoves().get(i).getY();

			if (color == getPieceOnCell(x, y).getColor()) {
				impossible_moves.add(new Coord(x, y));
			}
		}
		rook_final_moves.removeAll(impossible_moves);
		queen.setMoves(rook_final_moves);
		
		
		ArrayList<Coord> bishop_final_moves = queen.getMoves();
		ArrayList<Coord> moves_NE = new ArrayList<Coord>();
		ArrayList<Coord> moves_SE = new ArrayList<Coord>();
		ArrayList<Coord> moves_SW = new ArrayList<Coord>();
		ArrayList<Coord> moves_NW = new ArrayList<Coord>();
		Coord SW_max = new Coord(7, 7);
		Coord SE_max = new Coord(7, 0);
		Coord NW_max = new Coord(0, 7);
		Coord NE_max = new Coord(0, 0);
		int obstacle_NE = 0;
		int obstacle_NW = 0;
		int obstacle_SE = 0;
		int obstacle_SW = 0;

		// First we split the rook moves in 4 ArrayList for the 4 orientations
		for (int i = 0; i < queen.getMoves().size(); i++) {
			int x = queen.getMoves().get(i).getX();
			int y = queen.getMoves().get(i).getY();

			// We split in 4 orientations
			if (x > queen.getC().getX() && y > queen.getC().getY()) {
				moves_SW.add(new Coord(x, y));
			} else if (x > queen.getC().getX() && y < queen.getC().getY()) {
				moves_SE.add(new Coord(x, y));
			} else if (x < queen.getC().getX() && queen.getC().getY() > y) {
				moves_NE.add(new Coord(x, y));
			} else if (x < queen.getC().getX() && queen.getC().getY() < y) {
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
			int x = queen.getMoves().get(i).getX();
			int y = queen.getMoves().get(i).getY();

			if (color == getPieceOnCell(x, y).getColor()) {
				impossible_moves.add(new Coord(x, y));
			}
		}
		bishop_final_moves.removeAll(impossible_moves);

		queen.setMoves(bishop_final_moves);

		// The queen is having the same moves as rook and bishop combined !
		//cleanMovesRook(queen);
		//cleanMovesBishop(queen);

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
				case 'b':
					cleanMovesBishop(cells[i][j]);
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

		// the king must not be in check
		if (this.isChecked()) {
			return false;
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

		Piece soon_to_be_king = new Piece(-1, -1, this.getTurn(), 'k', true);
		Piece soon_to_be_rook = new Piece(-1, -1, this.getTurn(), 'r', true);
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
				soon_to_be_king.getC().getX(), soon_to_be_king.getC().getY(), this.getTurn(), 'k', true);
		// and the rook
		simulation.cells[soon_to_be_rook.getC().getX()][soon_to_be_rook.getC().getY()] = new Piece(
				soon_to_be_rook.getC().getX(), soon_to_be_rook.getC().getY(), this.getTurn(), 'r', true);

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
		// first we get the small rook
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

		Piece soon_to_be_king = new Piece(-1, -1, this.getTurn(), 'k', true);
		Piece soon_to_be_rook = new Piece(-1, -1, this.getTurn(), 'r', true);

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
				soon_to_be_king.getC().getX(), soon_to_be_king.getC().getY(), this.getTurn(), 'k', true);
		// and the rook
		simulation.cells[soon_to_be_rook.getC().getX()][soon_to_be_rook.getC().getY()] = new Piece(
				soon_to_be_rook.getC().getX(), soon_to_be_rook.getC().getY(), this.getTurn(), 'r', true);

		if (simulation.isChecked()) {
			// System.out.println("will be checked");
			return false;
		}

		// The king must no be in check already
		if (this.isChecked()) {
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
						if (cells[i][j].getColor() != king.getColor()) {
							this.setResult((this.getTurn() * 1000 + 666)*-1);
							return true;
						}

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
	 * Method to move one piece, and also manage the time between each eat()
	 *
	 * @param hunter the piece to move
	 * @param hunted the destination piece
	 */
	public void eat(Piece hunter, Piece hunted) {
		int hunter_x = hunter.getC().getX();
		int hunter_y = hunter.getC().getY();
		int hunted_x = hunted.getC().getX();
		int hunted_y = hunted.getC().getY();

		// We must check that the hunted is reachable by the hunter
		for (int i = 0; i < hunter.getMoves().size(); i++) {
			if (!hunter.getMoves().contains(new Coord(hunted.getC().getX(), hunted.getC().getY()))) {
				// if it's not we nullify the action
				return;
			}
		}
		if (!isLegal(hunter, hunted)) {
			return;
		}

		// in the hunter cell we put nothing
		this.getCells()[hunter_x][hunter_y] = new Piece(hunter_x, hunter_y, -1, 'e', false);

		// in the hunted cell we put the hunter
		this.getCells()[hunted_x][hunted_y] = new Piece(hunted_x, hunted_y, hunter.getColor(), hunter.getType(), true);
		
		
		// Todo
		// How do we manage time ?
		// little trick to alternate between 1 and 0
		this.setTurn(1 - this.getTurn());

		// we check if it's first turn, so time will start after this move
		if (this.getTime_black() == this.getTime_white() && this.getTime_black() == 2400) {
			this.setStorage(new TimeStamp());
		} else {
			// if it's the black turn
			if (this.getTurn() == 1) {
				TimeStamp newTime = new TimeStamp();
				int delta_minutes = this.getStorage().getMinute() - newTime.getSeconds();
				int delta_seconds = this.getStorage().getSeconds() - newTime.getSeconds();
				this.setTime_black(this.getTime_black() - delta_minutes * 60 - delta_seconds);
				this.setStorage(new TimeStamp());
			}
			// if it's the white turn
			else {
				TimeStamp newTime = new TimeStamp();
				int delta_minutes = this.getStorage().getMinute() - newTime.getSeconds();
				int delta_seconds = this.getStorage().getSeconds() - newTime.getSeconds();
				this.setTime_white(this.getTime_white() - delta_minutes * 60 - delta_seconds);
				this.setStorage(new TimeStamp());
			}
		}

		// if the turn is changed automatically we update moves right after eating, else
		// we wait for manual time switching
		ArrayList<Mov> temp = this.getHistoric();
		temp.add(new Mov(hunter.getType(), hunter.getC(), hunted.getType(), hunted.getC()));
		this.setHistoric(temp);
		this.setCells(this.getCells());
		hunter.setMoved(true);
		updateMoves();
	}

	/**
	 *
	 * @param hunter piece
	 * @param hunted piece
	 * @return true if you can do this move without being in check after and is
	 *         allowed by the hunter moves
	 */
	public boolean isLegal(Piece hunter, Piece hunted) {
		Piece cells[][] = new Piece[8][8];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				int x = this.getCells()[i][j].getC().getX();
				int y = this.getCells()[i][j].getC().getY();
				int color = this.getCells()[i][j].getColor();
				char type = this.getCells()[i][j].getType();
				boolean moved = this.getCells()[i][j].getMoved();
				cells[i][j] = new Piece(x, y, color, type, moved);
			}
		}
		Board simulation = new Board(cells, this.getTurn(), this.getResult());
		int hunter_x = hunter.getC().getX();
		int hunter_y = hunter.getC().getY();
		int hunted_x = hunted.getC().getX();
		int hunted_y = hunted.getC().getY();

		// in the hunter cell we put nothing
		simulation.getCells()[hunter_x][hunter_y] = new Piece(hunter_x, hunter_y);
		// in the hunted cell we put the hunter
		simulation.getCells()[hunted_x][hunted_y] = new Piece(hunted_x, hunted_y, hunter.getColor(), hunter.getType(), true);
		simulation.updateMoves();
		if (simulation.isChecked()) {
			return false;
		}

		// if the desired landing cell is not reachable by the hunter (JS attack)
		int nb_reachable = 0;
		for (int i = 0; i < hunter.getMoves().size(); i++) {
			if (hunted_x == hunter.getMoves().get(i).getX() && hunted_y == hunter.getMoves().get(i).getY()) {
				nb_reachable++;
			}
		}

		if (nb_reachable > 0) {
			return true;
		} else {
			return false;
		}
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
	 *
	 * @return true if lost false if not
	 */
	public boolean isCheckMate() {
		if (this.getKing().getMoves().size() == 0 && this.isChecked()) {
			return true;
		}
		return false;
	}

	/**
	 * Perform big castling
	 */
	public void bigCastling() {
		if (isBigCastlingOk()) {
			// white
			if (this.turn == 0) {
				this.getCells()[7][4] = new Piece(7, 4);
				this.getCells()[7][0] = new Piece(7, 0);
				this.getCells()[7][2] = new Piece(7, 2, 0, 'k', true);
				this.getCells()[7][3] = new Piece(7, 3, 0, 'r', true);
			}
			// black
			else {
				this.getCells()[0][4] = new Piece(0, 4);
				this.getCells()[0][0] = new Piece(0, 0);
				this.getCells()[0][2] = new Piece(0, 2, 1, 'k', true);
				this.getCells()[0][3] = new Piece(0, 3, 1, 'r', true);
			}
		}
	}

	/**
	 * Perform small castling
	 */
	public void smallCastling() {
		if (isSmallCastlingOk()) {
			// white
			if (this.turn == 0) {
				this.getCells()[7][4] = new Piece(7, 4);
				this.getCells()[7][7] = new Piece(7, 7);
				this.getCells()[7][6] = new Piece(7, 6, 0, 'k', true);
				this.getCells()[7][5] = new Piece(7, 5, 0, 'r', true);
			}
			// black
			else {
				this.getCells()[0][4] = new Piece(0, 4);
				this.getCells()[0][7] = new Piece(0, 7);
				this.getCells()[0][6] = new Piece(0, 6, 1, 'k', true);
				this.getCells()[0][5] = new Piece(0, 5, 1, 'r', true);
			}
		}
	}

	public boolean draw() {
		if (getResult() == -1) {
			if (impossibleCheckMate()) {
				return true;
			} else if (fiftyMoves()) {
				return true;
			} else if (staleMate()) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Verify draw option according to "no legal moves left" rule
	 *
	 * @return
	 */
	public boolean staleMate() {
		// the player can only move the king and if he does, he'll be in check
		// then it's a draw
		Piece cells[][] = new Piece[8][8];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				int x = this.getCells()[i][j].getC().getX();
				int y = this.getCells()[i][j].getC().getY();
				int color = this.getCells()[i][j].getColor();
				char type = this.getCells()[i][j].getType();
				boolean moved = this.getCells()[i][j].getMoved();
				cells[i][j] = new Piece(x, y, color, type, moved);
			}
		}

		ArrayList<Piece> pieces_left = this.getLeftovers(this.getTurn());
		for (int i = 0; i < pieces_left.size(); i++) {
			Board simulation = new Board(cells, this.getTurn(), this.getResult());
			// we check every move of every piece if it's not illegal

			int hunter_x1 = pieces_left.get(i).getC().getX();
			int hunter_y1 = pieces_left.get(i).getC().getY();
			Piece hunter = simulation.getPieceOnCell(hunter_x1, hunter_y1);
			// hunter.printPiece();
			for (int j = 0; j < pieces_left.get(i).getMoves().size(); j++) {
				int x2 = pieces_left.get(i).getMoves().get(j).getX();
				int y2 = pieces_left.get(i).getMoves().get(j).getY();
				Piece hunted = simulation.getPieceOnCell(x2, y2);

				// System.out.println(simulation.isLegal(hunter, hunted));
				if (simulation.isLegal(hunter, hunted)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Verify draw option according to the rule "fifty moves with nothing
	 * interesting"
	 *
	 * @return
	 */
	public boolean fiftyMoves() {
		ArrayList<Mov> historic = this.getHistoric();

		// If there's less than 50 moves obviously ...
		if (historic.size() < 50) {
			return false;
		}

		int nb_lunch = 0;
		int nb_pawn_moves = 0;

		// we look at the last 50 moves and see if something ate something
		// or if a pawn was moved
		for (int i = historic.size() - 50; i < historic.size(); i++) {
			int t1 = historic.get(i).getP1();
			int t2 = historic.get(i).getP2();
			if (t1 == 'p') {
				nb_pawn_moves++;
			}
			if (t2 != 'e') {
				nb_lunch++;
			}
		}

		// if none of the above were done
		if (nb_lunch == 0 && nb_pawn_moves == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gathers in one array all the pieces that are still in game
	 *
	 * @param turn more exactly color of player's leftovers
	 * @return
	 */
	public ArrayList<Piece> getLeftovers(int turn) {
		ArrayList<Piece> leftovers = new ArrayList<Piece>();
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (cells[i][j].getColor() == turn) {
					leftovers.add(this.getPieceOnCell(i, j));
				}
			}
		}
		return leftovers;
	}

	/**
	 * Verify draw option according to the rule "no mean to check mate"
	 *
	 * @return true if impossible to check mate
	 */
	public boolean impossibleCheckMate() {
		ArrayList<Piece> boardLeftovers = new ArrayList<Piece>();
		ArrayList<Piece> whiteLeftovers = getLeftovers(0);
		ArrayList<Piece> blackLeftovers = getLeftovers(1);
		boardLeftovers.addAll(whiteLeftovers);
		boardLeftovers.addAll(blackLeftovers);
		// We can call a draw
		// if only the 2 kings are left
		if (boardLeftovers.size() == 2) {
			return true;
		}
		// if king and horse vs king
		if (boardLeftovers.size() == 3) {
			char first = boardLeftovers.get(0).getType();
			char second = boardLeftovers.get(1).getType();
			char third = boardLeftovers.get(2).getType();
			if ((first == 'k' || first == 'h') && (second == 'k' || second == 'h') && (third == 'k' || third == 'h')) {
				return true;
			}
		}
		// if king and bishop vs king
		if (boardLeftovers.size() == 3) {
			char first = boardLeftovers.get(0).getType();
			char second = boardLeftovers.get(1).getType();
			char third = boardLeftovers.get(2).getType();
			if ((first == 'k' || first == 'b') && (second == 'k' || second == 'b') && (third == 'k' || third == 'b')) {
				return true;
			}
		}
		// if king and bishop vs king and bishop and bishops are same color
		if (boardLeftovers.size() == 4) {
			char first = boardLeftovers.get(0).getType();
			char second = boardLeftovers.get(1).getType();
			char third = boardLeftovers.get(2).getType();
			char fourth = boardLeftovers.get(3).getType();
			int bishop_colors[] = new int[2];
			int cpt = 0;
			if ((first == 'k' || first == 'b') && (second == 'k' || second == 'b') && (third == 'k' || third == 'b')
					&& (fourth == 'k' || fourth == 'b')) {
				for (int i = 0; i < boardLeftovers.size(); i++) {
					if (boardLeftovers.get(i).getType() == 'b') {
						int x = boardLeftovers.get(i).getC().getX() + 1;
						int y = boardLeftovers.get(i).getC().getY() + 1;
						int tot = x * y;
						// if it's even it's
						if (tot % 2 == 0) {
							bishop_colors[cpt] = 0;
						} else {
							bishop_colors[cpt] = 1;
						}
						cpt++;
					}
				}
				// if the bishops move on the same color
				if (bishop_colors[0] == bishop_colors[1]) {
					return true;
				}
			}
		}

		return false;
	}
}
