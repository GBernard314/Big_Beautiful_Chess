package com.bigbeautifulchess.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bigbeautifulchess.tools.TimeStamp;

class TimeStampTest {

	@Test
	void toStringTest() {
		TimeStamp t = new TimeStamp(12, 12, 12);
		assertEquals("12:12:12", t.toString());
	}

	@Test
	void getHourTest() {
		TimeStamp t = new TimeStamp(12, 0, 0);
		assertEquals(12, t.getHour());
	}

	@Test
	void getMinuteTest() {
		TimeStamp t = new TimeStamp(0, 12, 0);
		assertEquals(12, t.getMinute());
	}

	@Test
	void getSecondsTest() {
		TimeStamp t = new TimeStamp(0, 0, 12);
		assertEquals(12, t.getSeconds());
	}

}
