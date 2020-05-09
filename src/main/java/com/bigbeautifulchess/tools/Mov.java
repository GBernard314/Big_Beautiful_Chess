package com.bigbeautifulchess.tools;

public class Mov {
	/**
	 * Departure cell
	 */
	Coord from;
	
	/**
	 * Arrival cell
	 */
	Coord to;

	public Coord getFrom() {
		return from;
	}

	public void setFrom(Coord from) {
		this.from = from;
	}

	public Coord getTo() {
		return to;
	}

	public void setTo(Coord to) {
		this.to = to;
	}

	public Mov(Coord from, Coord to) {
		super();
		this.from = from;
		this.to = to;
	}
	
}
