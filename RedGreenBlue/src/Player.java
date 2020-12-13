import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * Credits:
 * Eddie - Everything (applies to Player1.java, Player2.java, and Player3.java as well.)
 */

public abstract class Player {
	// The x, y coordinates of the top left of the player
	private int xPos;
	private int yPos;
	
	// The velocity of x (Can be slowed down/sped up at any moment)
	private int xVelocity;
	private int terminalXVelocity = 10;
	
	// The velocity of y (Can only be changed when jumping, otherwise remains unchangeable by the user)
	private int yVelocity;
	private int terminalYVelocity = 10;
	
	// All of the ways a Player can be pushed/moved (downwards pressure used for jumping).
	private boolean upwardsPressure;
	private boolean rightwardsPressure;
	private boolean downwardsPressure;
	private boolean leftwardsPressure;
	
	// The keys that are used to control the player
	private int[] charCodes; // [0] = up, [1] = right, [2] = down, and [3] = left
	
	// The color of the player
	private char color;
	
	// Whether the player is dead or alive
	private boolean isAlive;
	
	// The collision with blocks for the player
	private ObjectCollision collisionCheck;
	
	// The current map
	private int[][] map;
	
	// The width of each "unit" in the level (which is also the size of the player)
	public static final int BLOCK_WIDTH = 30;
	
	public Player(int x, int y, char c, int[][] m) {
		xPos = x;
		yPos = y;
		xVelocity = 0;
		yVelocity = 0;
		color = c;
		collisionCheck = new ObjectCollision(this, m);
		upwardsPressure = false;
		leftwardsPressure = false;
		downwardsPressure = false;
		rightwardsPressure = false;
		isAlive = true;
	}
	
	// Updates the current map for the ObjectCollision to use
	public void updateMap(int[][] m) {
		collisionCheck.updateLevel(m);
	}
	
	public boolean[] checkExits(boolean[] exits, int[] blockIDs) {
		return collisionCheck.checkExits(exits, blockIDs);
	}
	
	// Alters the xVelocity or yVelocity (needs to be finished)
	public void refresh(HashSet<Integer> s) {
		if(isAlive) {
			boolean pressedX = false;
			if(!s.isEmpty()) {
				if(s.contains(charCodes[0])) {
					if(downwardsPressure) {
						yVelocity = -15;
						downwardsPressure = false;
					}
				}
				if(s.contains(charCodes[1])) {
					if(xVelocity < terminalXVelocity) {
						xVelocity += 1;
					}
					pressedX = true;
				}
				if(s.contains(charCodes[2])) {
					// To be the "interact" button
				}
				if(s.contains(charCodes[3])) {
					if(xVelocity > terminalXVelocity * -1) {
						xVelocity -= 1;
					}	
					pressedX = true;
				}
			}
		
			// Friction
			if(!pressedX) {
				if(xVelocity > 0) {
					xVelocity -= 1;
				} else if(xVelocity < 0) {
					xVelocity += 1;
				}
			}
		
			// Gravity
			if(!downwardsPressure) {
				if(yVelocity < terminalYVelocity) {
					yVelocity += 1;
				}
			}
			
			xPos += xVelocity;
			yPos += yVelocity;
			collisionCheck.checkObjectCollision();
		}
	}
	
	public void draw(Graphics g) {
		if(isAlive) {
			g.fillRect(getXPos(), getYPos(), BLOCK_WIDTH, BLOCK_WIDTH);
			g.setColor(Color.black);
			g.drawRect(getXPos(), getYPos(), BLOCK_WIDTH, BLOCK_WIDTH);
		}
	}
	
	// Accessor / Mutator methods
	public char getColor() {
		return color;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setPos(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	public int getXVelocity() {
		return xVelocity;
	}
	
	public void setXVelocity(int xVel) {
		xVelocity = xVel;
	}
	
	public void resetXVelocity() {
		setXVelocity(0);
	}
	
	public void resetXVelocity(boolean direction) {
		if(direction) { // Rightwards cancel
			if(xVelocity > 0) {
				resetXVelocity();
			}
		} else { // Leftwards cancel
			if(xVelocity < 0) {
				resetXVelocity();
			}
		}
	}
	
	public int getYVelocity() {
		return yVelocity;
	}
	
	public void setYVelocity(int yVel) {
		yVelocity = yVel;
	}
	
	public void resetYVelocity() {
		setYVelocity(0);
	}
	
	public void resetYVelocity(boolean direction) {
		if(direction) { // Upwards cancel
			if(yVelocity < 0) {
				resetYVelocity();
			}
		} else { // Downwards cancel
			if(yVelocity > 0) {
				resetYVelocity();
			}
		}
	}
	
	public void setUpwardsPressure(boolean b) {
		upwardsPressure = b;
	}
	
	public boolean getUpwardsPressure() {
		return upwardsPressure;
	}
	
	public void setRightwardsPressure(boolean b) {
		rightwardsPressure = b;
	}
	
	public boolean getRightwardsPressure() {
		return rightwardsPressure;
	}
	
	public void setDownwardsPressure(boolean b) {
		downwardsPressure = b;
	}
	
	public boolean getDownwardsPressure() {
		return downwardsPressure;
	}
	
	public void setLeftwardsPressure(boolean b) {
		leftwardsPressure = b;
	}
	
	public boolean getLeftwardsPressure() {
		return leftwardsPressure;
	}
	
	public void setCharCodes(int[] c) {
		charCodes = c;
	}
	
	public boolean getAlive() {
		return isAlive;
	}
	
	public void setAlive(boolean b) {
		isAlive = b;
	}
}
