package com.bigbeautifulchess.tools;

public class Mov {
	/**
	 * Departure cell
	 */
	Coord from;
	
	/**
	 * Type of piece in the departure cell
	 */
	char p1;
	
	/**
	 * Arrival cell
	 */
	Coord to;
	
	/**
	 * Type of piece in the landing cell
	 */
	char p2;
	
	
	public Coord getFrom() {
		return from;
	}
	
	public void setFrom(Coord from) {
		this.from = from;
	}

	public char getP1() {
		return p1;
	}

	public void setP1(char p1) {
		this.p1 = p1;
	}

	public Coord getTo() {
		return to;
	}

	public void setTo(Coord to) {
		this.to = to;
	}
	public char getP2() {
		return p2;
	}
	
	public void setP2(char p2) {
		this.p2 = p2;
	}

	public Mov(char p1, Coord from, char p2, Coord to) {
		super();
		this.from = from;
		this.p1 = p1;
		this.to = to;
		this.p2 = p2;
	}


	public Mov(Coord from, Coord to) {
		super();
		this.from = from;
		this.to = to;
	}
	
}
