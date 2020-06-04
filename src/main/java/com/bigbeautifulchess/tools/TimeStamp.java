package com.bigbeautifulchess.tools;

import java.util.Calendar;

public class TimeStamp {
	int hour, minute, seconds;

	public TimeStamp(int hour, int minute, int seconds) {
		super();
		this.hour = hour;
		this.minute = minute;
		this.seconds = seconds;
	}

	public TimeStamp() {
		super();
		this.hour = Calendar.getInstance().get(Calendar.HOUR);
		this.minute = Calendar.getInstance().get(Calendar.MINUTE);
		this.seconds = Calendar.getInstance().get(Calendar.SECOND);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		return hour + ":" + minute + ":" + seconds;
	}

}
