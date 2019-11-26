/**
 * GOLController.java 1.0 Nov 25, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.awt.MouseInfo;
import java.awt.Point;

/**
 * @author Alex
 *
 */
public class GOLController implements Runnable{
	GOLModel model;
	GOLView view;
	Thread activeThread;
	boolean editColor = true;
	
	int delay = 100;
	int mode;
	
 public GOLController(GOLModel model, GOLView view) {
	 this.model = model;
	 this.view = view;
	 mode = GOLView.SIMULATE_MODE;
	 view.setTiles(model.getSpotBoard());
	 view.setController(this);
 }
	public void setSurviveLowThreshold(int surviveLowThreshold) {
		model.setSurviveLowThreshold(surviveLowThreshold);
	}

	/**
	 * @param surviveHighThreshold the surviveHighThreshold to set
	 */
	public void setSurviveHighThreshold(int surviveHighThreshold) {
		model.setSurviveHighThreshold(surviveHighThreshold);
	}

	/**
	 * @param birthHighThreshold the birthHighThreshold to set
	 */
	public void setBirthHighThreshold(int birthHighThreshold) {
		model.setBirthHighThreshold(birthHighThreshold);
	}

	/**
	 * @param birthLowThreshold the birthLowThreshold to set
	 */
	public void setBirthLowThreshold(int birthLowThreshold) {
		model.setBirthLowThreshold(birthLowThreshold);
	}
 
 public void setNewDimensions(int w, int h) {
	 model.resetDimensions(w, h);
 }
 
 public void fillRandomly(double rate) {
	 model.setSpawnRate(rate);
	 model.fillRandomly();
 }
 
 public void start() {
	 model.unpause();
	 switchMode(GOLView.SIMULATE_MODE);
 }
 
 public void stop() {
	 model.pause();
 }
 
/**
 * @param id
 */
public void switchMode(int id) {
	mode = id;
	if (id == GOLView.SETUP_MODE) {
		model.pause();
		enableHighlighting();
		return;
	} else {
		disableHighlighting();
	}
	if (id == GOLView.SIMULATE_MODE) {
		model.unpause();
	} else if (id == GOLView.STEP_MODE){
		model.pause();
	}
	
}

public void step() {
	if (mode != GOLView.STEP_MODE) return;
	model.updateTiles(true);
}

public void update() {
	model.updateTiles();
	view.setTiles(model.getSpotBoard());
	view.repaint();
}

/**
 * 
 */
public void toggleTorus() {
	model.toggleWrap();
}
/* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
@Override
public void run() {
	while (true) {
		update();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
/**
 * @param i
 */
public void startThread() {
	activeThread = new Thread(this);
	activeThread.start();
}

public void setDelay(int delay) {
	this.delay = delay;
}
/**
 * 
 */
public void clear() {
	model.clear();
}

public int getSpotWidth() { 
	return model.getSpotBoard().getWidth();
}

public int getSpotHeight() {
	return model.getSpotBoard().getHeight();
}

public void enableHighlighting() {
	view.getBoardView().startHighlighting();
}

public void disableHighlighting() {
	view.getBoardView().stopHighlighting();

}
/**
 * @param b
 */
public void setEditColor(boolean b) {
	editColor = b;
}
/**
 * @param locationOnScreen
 */
public void clicked(Point locationOnScreen) {
	if (mode != GOLView.SETUP_MODE)	return;
	else { 
		Point mouseloc = locationOnScreen;
		Point boardStart = view.getBoardView().getLocationOnScreen();
		Point absolutePoint = new Point(mouseloc.x - boardStart.x, mouseloc.y - boardStart.y);
		double tileWidth = (double) 700 / model.getSpotBoard().getWidth();
		double tileHeight = (double) 700 / model.getSpotBoard().getHeight();
		
		Point coord = new Point(((int) (absolutePoint.x / tileWidth)), 
				 ((int) (absolutePoint.y / tileHeight)));
		model.setPointAt(coord.x, coord.y, editColor);
	}
}
 
}
