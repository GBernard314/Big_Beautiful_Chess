package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bigbeautifulchess.tools.Coord;

class CoordTest {

	@Test
	void getXTest() {
		Coord test = new Coord(420, 666);
		assertEquals(420, test.getX());
	}
	
	@Test
	void getYTest() {
		Coord test = new Coord(420, 666);
		assertEquals(666, test.getY());
	}
	

}
