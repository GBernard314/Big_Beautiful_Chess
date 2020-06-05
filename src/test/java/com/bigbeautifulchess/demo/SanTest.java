package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.bigbeautifulchess.tools.Coord;
import com.bigbeautifulchess.tools.Mov;
import com.bigbeautifulchess.engine.Board;
import com.bigbeautifulchess.san.San;

class SanTest {

	/**
	 * Tests of printSanTestXY()
	 * X = Number of the current turn 
	 * Y = w(white) & b(black)
	 */
	
	
	
	@Test
	void printSanAll(){
		System.out.println("King moving and eating");
		printSanTest14w();
		printSanTest9w();
		
		System.out.println("Queen moving and eating");
		printSanTest13b();
		printSanTest15b();
		
		System.out.println("Rook moving and eating");
		printSanTest17w();
		printSanTest5w();
		
		System.out.println("Bishop moving and eating");
		printSanTest4b();
		printSanTest13w();
		
		System.out.println("Knight moving and eating");
		printSanTest3b();
		printSanTest10b();
		
		System.out.println("Pawn moving and eating");
		printSanTest1w();
		printSanTest12b();
		
		System.out.println("Multiples string");
		printSanTestMulti();
		
		System.out.println("Black and White promotion");
		printSanTestBPromo();
		printSanTestWPromo();
		
		System.out.println("Eat and promotion");
		printSanTestEatPromo();
	}
	
	/*
	 * Tests are inspired of 2 parties
	w   b
1.	h4	e5
2.	a4	d5
3.	b3	Nf6
4.	g3	Bc5
5.	Bg2	Bg4
6.	Nh3	Nc6
7.	Na3	Ne4
8.	e3	Bxd1
9.	Kxd1	f5
10.	f3	Nxg3
11.	c4	Nxh1
12.	Ke2	dxc4
13.	Bxh1	Qd3+	//no+
14.	Kf2	Bxa3
15.	Bxa3	Qxd2+	//no+
16.	Kf1	cxb3
17.	Re1	b2
18.	Re2	b1=Q+	//no+
19.	Kf2	Qe1+	//no+
20.	Kg2	Qxe2+	//no+
21.	Kg3	Qe1+	//no+
22.	Nf2	Qxf2+	//no+
23.	Kh3	Qg3#	//no#
	 
	and
	 
5.	Rxh6	Bxh6

	and

10. e3 bxc1=Q
	
	 */
	
	@Test
	void printSanTest1w() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='p';
		Coord from=new Coord(6, 7);
		char p2='e';
		Coord to=new Coord(4, 7);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("h4\n"),"Test");
	}
	
	@Test
	void printSanTest3b() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='h';
		Coord from=new Coord(0, 6);
		char p2='e';
		Coord to=new Coord(2, 5);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Nf6\n"),"Test");
	}
		
	@Test
	void printSanTest4b() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='b';
		Coord from=new Coord(0, 5);
		char p2='e';
		Coord to=new Coord(3, 2);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Bc5\n"),"Test");
	}
	
	@Test
	void printSanTest9w() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='k';
		Coord from=new Coord(7, 4);
		char p2='b';
		Coord to=new Coord(7, 3);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Kxd1\n"),"Test");
	}
	
	@Test
	void printSanTest10b() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='h';
		Coord from=new Coord(4, 4);
		char p2='p';
		Coord to=new Coord(5, 6);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Nxg3\n"),"Test");
	}
	
	@Test
	void printSanTest12b() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='p';
		Coord from=new Coord(3, 3);
		char p2='p';
		Coord to=new Coord(4, 2);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("dxc4\n"),"Test");
	}
	
	@Test
	void printSanTest13w() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='b';
		Coord from=new Coord(6, 6);
		char p2='p';
		Coord to=new Coord(7, 7);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Bxh1\n"),"Test");
	}
	
	@Test
	void printSanTest13b() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='q';
		Coord from=new Coord(0, 3);
		char p2='e';
		Coord to=new Coord(5, 3);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Qd3\n"),"Test");
	}
	
	@Test
	void printSanTest14w() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='k';
		Coord from=new Coord(6, 4);
		char p2='e';
		Coord to=new Coord(6, 5);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Kf2\n"),"Test");
	}

	@Test
	void printSanTest15b() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='q';
		Coord from=new Coord(0, 3);
		char p2='p';
		Coord to=new Coord(6, 3);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Qxd2\n"),"Test");
	}
	
	@Test
	void printSanTest17w() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='r';
		Coord from=new Coord(7, 0);
		char p2='e';
		Coord to=new Coord(7, 4);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Re1\n"),"Test");
	}
	
	// 2nd battery of test
	@Test
	void printSanTest5w() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='r';
		Coord from=new Coord(5, 7);
		char p2='p';
		Coord to=new Coord(2, 7);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Rxh6\n"),"Test");
	}
	
	// Test with multiples Mov
	@Test
	void printSanTestMulti() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		
		char p1='h';
		Coord from=new Coord(0, 1);
		char p2='e';
		Coord to=new Coord(2, 0);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		
		p1='h';
		from=new Coord(2, 0);
		p2='e';
		to=new Coord(0, 1);
		mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		
		b.setHistoric(historic);System.out.println(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("Na6\nNb8\n"),"Test");
	}
	
	
	// Tests promotion
	@Test
	void printSanTestWPromo() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='p';
		Coord from=new Coord(1, 1);
		char p2='e';
		Coord to=new Coord(0, 1);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("b8=Q\n"),"Test");
	}
	
	@Test
	void printSanTestBPromo() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='p';
		Coord from=new Coord(6, 1);
		char p2='e';
		Coord to=new Coord(7, 1);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("b1=Q\n"),"Test");
	}
	
	@Test
	void printSanTestEatPromo() {
		Board b = new Board();
		ArrayList<Mov> historic= new ArrayList<Mov>();
		San s= new San();
		char p1='p';
		Coord from=new Coord(6, 1);
		char p2='b';
		Coord to=new Coord(7, 2);
		Mov mov=new Mov(p1, from, p2, to);
		historic.add(mov);
		b.setHistoric(historic);System.out.print(s.printSan(b));
		assertEquals(true, s.printSan(b).equals("bxc1=Q\n"),"Test");
	}
				
}
