/**
 * SpotBoard.java 1.0 Nov 25, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.awt.Color;

/**
 * @author Alex
 *
 */
public class SpotBoard {
	private boolean[][] board;
	private boolean[][] nextBoard;
	private int width, height;
	
	public static Color offColor = Color.RED;
	public static Color onColor = Color.GREEN;
	
	public SpotBoard(int width, int height) {
		this.width = width;
		this.height = height;
		setBlankTiles(width, height);
	}
	
	public void setBlankTiles(int width, int height) {
		this.width = width;
		this.height = height;
		board = new boolean[width][height];
		nextBoard = new boolean[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				board[x][y] = false;
			}
		}
	}
	
	public Color getColor(int x, int y) {
		if (board[x][y]) {
			return onColor;
		} else {
			return offColor;
		}
	}
	
	public boolean get(int x, int y) {
		return board[x][y];
	}
	
	public void setNext(int x, int y, boolean state) {
		nextBoard[x][y] = state;
	}
	
	public void updateBoard() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				board[x][y] = nextBoard[x][y];
			}
		}
	}
	
	public int getWidth() { 
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	
}
