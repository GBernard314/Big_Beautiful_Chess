package com.bigbeautifulchess.tools;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Coord {
	
	private Long id;
	
	/**
	 * x coordinate 
	 */
	private int x;
	
	/**
	 * y coordinate
	 */
	
	private int y;

	/**
	 * Initialise a coord with both x and y 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Coord(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coord other = (Coord) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public boolean equals(int x, int y) {
		if(x == this.x && y == this.y) return true;
		return false;
	}
	
	
}
