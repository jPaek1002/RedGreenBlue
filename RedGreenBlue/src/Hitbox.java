import java.awt.Graphics;

/**
 * Credits:
 * Eddie - Everything
 */

public class Hitbox implements Entity {
	private int xPos;
	private int yPos;
	private int xLength;
	private int yLength;
	private boolean ifContact;
	
	public static final int BLOCK_WIDTH = 30;

	public Hitbox(int x, int y, int w, int h) {
		xPos = x;
		yPos = y;
		xLength = w;
		yLength = h;
		ifContact = false;
	}
	
	// Checks if the given player collides within the box 
	public void checkCollision(Player p) {
		ifContact = false;
		if(p.getXPos() + BLOCK_WIDTH > xPos && p.getXPos() < xPos + xLength &&
				p.getYPos() + BLOCK_WIDTH > yPos && p.getYPos() < yPos + yLength) {
			ifContact = true;
		}
	}
	
	// Unused
	public void draw(Graphics g) {}
	public void refresh() {}
	
	// Accessor method
	public boolean getContact() {
		return ifContact;
	}
}
