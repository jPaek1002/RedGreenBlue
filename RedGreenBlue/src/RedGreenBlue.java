import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

/**
 * Credits:
 * Eddie - Started the class, Created constants and game mechanic methods
 * Jay - Had idea and implemented the method to read keys, Created the main method
 */
public class RedGreenBlue extends JPanel implements KeyListener{
	// All different players, their corresponding ObjectCollisions and their total PlayerCollision
	private Player1 red;
	private Player2 green;
	private Player3 blue;
    private PlayerCollision playerCollision;
    
    // The storage of all keys pressed/held down
	private HashSet<Integer> keysPressed;	
	
	// The map which builds and creates all levels
	private Map map;
	
	// Assists the RedGreenBlue class to determine what action to do
	private static final int MENU = 0;
	private static final int STARTING = 1;
	private static final int PLAYING = 2;
	private int state;
	
	// Constructor
	public RedGreenBlue() {
		int[][] defaultLevel = new int[20][20];
		red = new Player1(0, 0, 'r', defaultLevel);
		green = new Player2(0, 0, 'g', defaultLevel);
		blue = new Player3(0, 0, 'b', defaultLevel);
		playerCollision = new PlayerCollision(red, green, blue);
		keysPressed = new HashSet<Integer>();
		map = new Map(red, green, blue);
		state = PLAYING;
		
		map.setLevel(1);
	}
	
	// Initializes the level
	public void init() {
		map.resetLevel();
		map.refreshMap();
		while(state == PLAYING) {
			refresh();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Refreshes all objects
	public void refresh() {
		if(keysPressed.contains(82)) { // Reset button
			map.resetLevel();
		}
 		red.refresh(keysPressed);
 		green.refresh(keysPressed);
 		blue.refresh(keysPressed);
 		
 		playerCollision.checkPlayerCollision();
 		
 		map.refresh();
		
		repaint();
	}
	
	// Draws the window
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		map.drawMap(g);
		map.drawEntities(g);
		
		red.draw(g);
		green.draw(g);
		blue.draw(g);
	}

	// Key Detection
	public void keyPressed(KeyEvent arg0) {
		keysPressed.add(arg0.getKeyCode());
	}
	
	public void keyReleased(KeyEvent arg0) {
		keysPressed.remove(arg0.getKeyCode());
	}
	
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	// Main method (sets up the window)
	public static void main(String[] args) {
		JFrame frame = new JFrame("RGB");
		frame.setSize(615, 635);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		RedGreenBlue game = new RedGreenBlue();
		frame.addKeyListener(game);
		frame.add(game);
		

		
		frame.setVisible(true);
		
		game.init();
	}
}
