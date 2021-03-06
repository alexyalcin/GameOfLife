/**
 * GOLBoardView.java 1.0 Nov 26, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

/**
 * @author Alex
 *
 */
public class GOLBoardView extends JPanel{
	private int pixelHeight, pixelWidth;
	boolean highlight = false;
	
	SpotBoard spotBoard;
	
	public GOLBoardView(int w, int h) {
		pixelHeight = h;
		pixelWidth = w;
		this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
		spotBoard = new SpotBoard(0, 0);
	}

	@Override
	public void paintComponent(Graphics g) {
		int width = spotBoard.getWidth();
		int height = spotBoard.getHeight();
		final double perPixelWidth = (double) pixelWidth / width;
		final double perPixelHeight = (double) pixelHeight / height;
		for (int x = 0; x < width; x++) { 
			for (int y = 0; y < height; y++) {
				g.setColor(spotBoard.getColor(x, y));
				g.drawRect((int) (x * perPixelWidth), (int) (y * perPixelHeight), 1 + (int) perPixelWidth, 1 + (int) perPixelHeight);
				g.fillRect((int) (x * perPixelWidth), (int) (y * perPixelHeight), 1 + (int) perPixelWidth, 1 + (int) perPixelHeight);

			}
		}
		if (highlight) {
			Point mouseloc = MouseInfo.getPointerInfo().getLocation();
			Point boardStart = this.getLocationOnScreen();
			Point absolutePoint = new Point(mouseloc.x - boardStart.x, mouseloc.y - boardStart.y);
			Point topLeft = new Point((int) (perPixelWidth * (int) (absolutePoint.x / perPixelWidth)), 
					(int) (perPixelHeight * (int) (absolutePoint.y / perPixelHeight)));
			System.out.println(topLeft);

			g.setColor(Color.YELLOW);
			g.drawRect(topLeft.x, topLeft.y, (int) perPixelWidth, (int) perPixelHeight);
		}
	}

	/**
	 * @param spotBoard
	 */
	public void setTiles(SpotBoard spotBoard) {
		this.spotBoard = spotBoard;
	}
	
	public void startHighlighting() {

		highlight = true;
	}
	
	public void stopHighlighting() {
		highlight = false;
	}
	
	public void resetDimensions(int w, int h) {
		spotBoard.setBlankTiles(w, h);
	}
}