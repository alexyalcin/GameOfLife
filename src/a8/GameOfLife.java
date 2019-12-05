/**
 * RunGame.java 1.0 Nov 25, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * @author Alex
 *
 */
public class GameOfLife {
	
	private static String map = "src/example_map.txt";
	private static GOLModel model;
	private static GOLView view;
	private static GOLController controller;
	
	static {
		model = new GOLModel();
		view = new GOLView();
		controller = new GOLController(model, view);
		controller.startThread();
	}
	
	public static void main(String[] args) {
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Game of Life");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Create panel for content. Uses BorderLayout. */
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		top_panel.add(view);
		
		main_frame.setContentPane(top_panel);
		

		/* Pack main frame and make visible. */
		
		main_frame.pack();
		main_frame.setVisible(true);		
	}

}
