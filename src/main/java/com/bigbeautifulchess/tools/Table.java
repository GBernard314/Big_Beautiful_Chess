package com.bigbeautifulchess.tools;

public class Table {
	
	/**
	 * Small tool to visualize how tables work in java
	 */
	public void printTabCanvas() {
		int t[][] = new int[8][8];
		
		System.out.print("\n");
		System.out.print("x↓\ny→\n");

		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < t[i].length; j++) {
				System.out.print("|" + i + ";" + j + "");
			}
			System.out.print("| \n");
		}

	}

}
