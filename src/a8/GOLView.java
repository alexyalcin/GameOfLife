/**
 * GOLView.java 1.0 Nov 25, 2019
 *
 * Copyright (c) 2019 Alexander Yalcin. All rights reserved.
 */
package a8;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * @author Alex
 *
 */
public class GOLView extends JPanel implements ActionListener, KeyListener, MouseListener {
	private static final int DEFAULT_HEIGHT = 700;
	private static final int  DEFAULT_WIDTH = 700;
	
	public static final int SETUP_MODE = 0;
	public static final int STEP_MODE = 1;
	public static final int SIMULATE_MODE = 2;
	
	private int pixelHeight, pixelWidth;
	
	GOLBoardView board;
	GOLController controller;
	
	private JTextField[] fieldDimensions;
	private JButton setDimensionsButton;
	
	private JTextField randomSpawnRate;
	private JButton fillRandomlyButton;
	private JButton clearButton;
	
	private JRadioButton setupModeButton;
	private JRadioButton stepModeButton;
	private JRadioButton simulateModeButton;
	private ButtonGroup modeButtons;
	
	private JTextField lowBirthThreshold;
	private JTextField highBirthThreshold;
	private JTextField lowSurviveThreshold;
	private JTextField highSurviveThreshold;
	
	private JButton startSimulationButton;
	private JButton stopSimulationButton;
	private JButton stepButton;
	
	private JButton toggleTorus;

	public GOLView() {
		pixelHeight = DEFAULT_HEIGHT;
		pixelWidth = DEFAULT_WIDTH;
		board = new GOLBoardView(pixelWidth, pixelHeight);
		this.setLayout(new BorderLayout());
		this.add(board, BorderLayout.SOUTH);
		this.addMouseListener(this);
		
		JPanel ui = new JPanel(new GridLayout(4, 3));
		ui.setPreferredSize(new Dimension(DEFAULT_WIDTH, 100));

		JPanel[][] uiGrid = new JPanel[4][3];
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 3; y++) {
				uiGrid[x][y] = new JPanel();
				uiGrid[x][y].setLayout(new BoxLayout(uiGrid[x][y], BoxLayout.X_AXIS));
				ui.add(uiGrid[x][y]);
			}
		}
		
		//Dimensions
		fieldDimensions = new JTextField[2];
		fieldDimensions[0] = new JTextField();
		fieldDimensions[1] = new JTextField();
		fieldDimensions[0].setText("40");
		fieldDimensions[1].setText("40");
		fieldDimensions[0].setColumns(3);
		fieldDimensions[1].setColumns(3);
		setDimensionsButton = new JButton("Set");
		setDimensionsButton.setPreferredSize(new Dimension(30, 3));
		setDimensionsButton.addActionListener(new GOLDimensionButtonListener());
		uiGrid[0][0].add(new JLabel("Dims"));
		uiGrid[0][0].add(fieldDimensions[0]);
		uiGrid[0][0].add(new JLabel("x"));
		uiGrid[0][0].add(fieldDimensions[1]);
		uiGrid[0][0].add(setDimensionsButton);
	
		//random spawn
		uiGrid[1][0].add(new JLabel("Spawn Rate:"));
		randomSpawnRate = new JTextField("0.05");
		fillRandomlyButton = new JButton("Set");
		fillRandomlyButton.setPreferredSize(new Dimension(30, 3));
		fillRandomlyButton.addActionListener(new GOLFillRandomlyButtonListener());
		uiGrid[1][0].add(randomSpawnRate);
		uiGrid[1][0].add(fillRandomlyButton);
		
		//mode buttons
		modeButtons = new ButtonGroup();
		setupModeButton = new JRadioButton("Setup");
		stepModeButton = new JRadioButton("Step");
		simulateModeButton = new JRadioButton("Simulate");
		simulateModeButton.setSelected(true);
		setupModeButton.addActionListener(new GOLModeRadioButtonListener() {
			public int getID() {
				return SETUP_MODE;
			}
		});
		stepModeButton.addActionListener(new GOLModeRadioButtonListener() {
			public int getID() {
				return STEP_MODE;
			}
		});
		simulateModeButton.addActionListener(new GOLModeRadioButtonListener() {
			public int getID() {
				return SIMULATE_MODE;
			}
		});
		modeButtons.add(setupModeButton);
		modeButtons.add(stepModeButton);
		modeButtons.add(simulateModeButton);
		uiGrid[0][1].add(setupModeButton);
		uiGrid[0][1].add(stepModeButton);
		uiGrid[0][1].add(simulateModeButton);

		//Step button 
		stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.switchMode(GOLView.STEP_MODE);
				stepModeButton.setSelected(true);
				controller.step();
			}
		});
		uiGrid[1][1].add(stepButton);
		this.add(ui);	
		
		//startstop
		startSimulationButton = new JButton("Start");
		stopSimulationButton = new JButton("Stop");
		startSimulationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulateModeButton.setSelected(true);
				controller.start();
			}	
		});
		
		stopSimulationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.stop();
			}	
		});
		uiGrid[1][1].add(startSimulationButton);
		uiGrid[1][1].add(stopSimulationButton);
		
		//Torus Mode
		toggleTorus = new JButton("Torus: OFF");
		toggleTorus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (toggleTorus.getText().equals("Torus: ON"))
					toggleTorus.setText("Torus: OFF");
				else toggleTorus.setText("Torus: ON");
				controller.toggleTorus();
				
			}
		});
		uiGrid[0][2].add(toggleTorus);
		
		//Birth Surive Thresholds
		
		lowBirthThreshold = new JTextField("3");
		lowBirthThreshold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int thresh = validateThreshold(lowBirthThreshold.getText(), 0, 8);
				if (thresh == -1) return;
				controller.setBirthLowThreshold(thresh);
			}
			
		});
		highBirthThreshold = new JTextField("3");
		highBirthThreshold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int thresh = validateThreshold(highBirthThreshold.getText(), 0, 8);
				if (thresh == -1) return;
				controller.setBirthHighThreshold(thresh);
			}
			
		});
		lowSurviveThreshold = new JTextField("2");
		lowSurviveThreshold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int thresh = validateThreshold(lowSurviveThreshold.getText(), 0, 8);
				if (thresh == -1) return;
				controller.setSurviveLowThreshold(thresh);
			}
			
		});
		highSurviveThreshold = new JTextField("3");
		highSurviveThreshold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int thresh = validateThreshold(highSurviveThreshold.getText(), 0, 8);
				if (thresh == -1) return;
				controller.setSurviveHighThreshold(thresh);
			}
			
		});
		uiGrid[2][0].add(new JLabel("Birth Lo:     "));
		uiGrid[2][0].add(lowBirthThreshold);
		uiGrid[2][0].add(new JLabel("Hi:"));
		uiGrid[2][0].add(highBirthThreshold);
		uiGrid[3][0].add(new JLabel("Survive Lo: "));
		uiGrid[3][0].add(lowSurviveThreshold);
		uiGrid[3][0].add(new JLabel("Hi:"));
		uiGrid[3][0].add(highSurviveThreshold);
		
		//delay 
		uiGrid[1][2].add(new JLabel("Set Delay (10-1000ms):"));
		JTextField delayText = new JTextField("100");
		delayText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int delay = validateThreshold(delayText.getText(), 10, 1000);
				if (delay == -1) return;
				controller.setDelay(delay);
			}
		});
		uiGrid[1][2].add(delayText);
		
		//clear 
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.clear();
				controller.switchMode(GOLView.SETUP_MODE);
				setupModeButton.setSelected(true);
			}
			
		});	
		uiGrid[2][1].add(clearButton);
		
		//Setting
		ButtonGroup setOption = new ButtonGroup();
		JRadioButton setAliveButton = new JRadioButton("Alive");
		JRadioButton setDeadButton = new JRadioButton("Dead");
		setAliveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setEditColor(true);
			}
			
		});
		
		setDeadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setEditColor(false);
			}
			
		});
		setAliveButton.setSelected(true);
		setOption.add(setAliveButton);
		setOption.add(setDeadButton);
		uiGrid[2][2].add(new JLabel("Set Options:"));
		uiGrid[2][2].add(setAliveButton);
		uiGrid[2][2].add(setDeadButton);
	}
	
	private static int validateThreshold(String thresh, int low, int high) {
		int t = -1;
		try {
			t = Integer.parseInt(thresh);
		} catch (NumberFormatException i) {
			return -1;
		}
		if (t >= 0 || t <= 8 ) {
			return t;
		}
		return -1;
	}


	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		board.repaint();
	}

	/**
	 * @param spotBoard
	 */
	public void setTiles(SpotBoard spotBoard) {
		this.board.setTiles(spotBoard);
	}
	
	public void setController(GOLController c) {
		this.controller = c;
	}
	
	public interface GOLTextFieldListener extends ActionListener{		
		public int getID();
	}
	
	public class GOLDimensionButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JTextField widthField = (JTextField) fieldDimensions[0];
			JTextField heightField = (JTextField) fieldDimensions[1];
			int newWidth = 0;
			int newHeight = 0;
			try {
				newWidth = Integer.parseInt(widthField.getText());
				newHeight = Integer.parseInt(heightField.getText());
			} catch (NumberFormatException i) {
				System.out.println("Invalid Input");
				return;
			}
			if (newWidth <= 500 && newWidth >= 10 && newHeight <= 500 && newHeight >= 10) {
				controller.setNewDimensions(newWidth, newHeight);
			}
		}
	}
	
	public class GOLFillRandomlyButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			double newRate = 0;
			try {
				newRate = Double.parseDouble(randomSpawnRate.getText());
			} catch (NumberFormatException i) {
				System.out.println("Invalid input");
				return;
			}
			if (newRate < 0 || newRate > 1) {
				return;
			}
			controller.fillRandomly(newRate);
		}
		
	}
	
	public abstract class GOLModeRadioButtonListener implements ActionListener {
		public abstract int getID();
		public void actionPerformed(ActionEvent e) {
			controller.switchMode(getID());
		}
	}

	/**
	 * @return
	 */
	public GOLBoardView getBoardView() {
		// TODO Auto-generated method stub
		return board;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		controller.clicked(e.getLocationOnScreen());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
