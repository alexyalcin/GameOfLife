/**
 * GOLModel.java 1.0 Nov 25, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @author Alex
 *
 */
public class GOLModel {
	
	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 100;
	private int width, height;
	
	private int surviveLowThreshold = 2;
	private int surviveHighThreshold = 3;
	private int birthHighThreshold = 3;
	private int birthLowThreshold = 3;
	
	public double spawnRate = .05;
	
	private SpotBoard spotBoard;
	
	private boolean wrap = true;
	private boolean paused = false;
	
	public GOLModel() { 
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public GOLModel(int w, int h) {
		width = w;
		height = h;
		spotBoard = new SpotBoard(width, height);
		
		for (int x = 0; x < width; x++) {
			spotBoard.setNext(x, 20, true);
			spotBoard.setNext(x, 80, true);
		}
		
		spotBoard.updateBoard();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unpause() {
		paused = false;
	}
	
	public void clear() {
		spotBoard.setBlankTiles(width, height);
	}
	
	/**
	 * @param surviveLowThreshold the surviveLowThreshold to set
	 */
	public void setSurviveLowThreshold(int surviveLowThreshold) {
		this.surviveLowThreshold = surviveLowThreshold;
	}

	/**
	 * @param surviveHighThreshold the surviveHighThreshold to set
	 */
	public void setSurviveHighThreshold(int surviveHighThreshold) {
		this.surviveHighThreshold = surviveHighThreshold;
	}

	/**
	 * @param birthHighThreshold the birthHighThreshold to set
	 */
	public void setBirthHighThreshold(int birthHighThreshold) {
		this.birthHighThreshold = birthHighThreshold;
	}

	/**
	 * @param birthLowThreshold the birthLowThreshold to set
	 */
	public void setBirthLowThreshold(int birthLowThreshold) {
		this.birthLowThreshold = birthLowThreshold;
	}

	public SpotBoard getSpotBoard() {
		return spotBoard;
	}
	
	public void resetDimensions(int w, int h) { 
		width = w;
		height = h;
		spotBoard.setBlankTiles(w, h);
		spotBoard.updateBoard();
	}
	
	public void setSpawnRate(double r) {
		spawnRate = r;
	}
	
	public void fillRandomly() {
		Random r = new Random();
		for (int  x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (r.nextDouble() < spawnRate) {
					spotBoard.setNext(x, y, true);
				} else {
					spotBoard.setNext(x, y, false);
				}
			}
		}
		spotBoard.updateBoard();
	}
	
	private int getLiveNeighborCount(int x, int y) {
		int total = 0;
		int[][] directions = new int[][]{
			{1, 1},
			{0, 1}, 
			{1, 0},
			{1, -1},
			{-1, -1},
			{0, -1}, 
			{-1, 0},
			{-1, 1}};
		for (int[] dir : directions) {
			int x1 = x + dir[0];
			int y1 = y + dir[1];
			if (wrap) {
				x1 = (x1 + width) % width;
				y1 = (y1 + height) % height;
			}
			if (x1 >= 0 && x1 < width && y1 >= 0 && y1 < height) {
				if (spotBoard.get(x1, y1)) {
					total++;
				}
			}
		}
		return total;
	}

	/**
	 * 
	 */
	public void updateTiles(boolean step) {
		if (paused && !step) return;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int neighbors = getLiveNeighborCount(x, y);
				if (spotBoard.get(x, y)) {
					if (neighbors >= surviveLowThreshold && neighbors <= surviveHighThreshold) {
						spotBoard.setNext(x, y, true);
					} else {
						spotBoard.setNext(x, y, false);
					}
				} else {
					if (neighbors >= birthLowThreshold && neighbors <= birthHighThreshold) {
						spotBoard.setNext(x, y, true);
					} else {
						spotBoard.setNext(x, y, false);
					}
				}
			}
		}
		spotBoard.updateBoard();
	}
	
	public void updateTiles() {
		updateTiles(false);
	}
	
	public void toggleWrap() { 
		wrap = !wrap;
	}

	/**
	 * @param x
	 * @param y
	 * @param editColor
	 */
	public void setPointAt(int x, int y, boolean editColor) {
		spotBoard.setNext(x, y, editColor);
		spotBoard.updateBoard();
		
	}
}
