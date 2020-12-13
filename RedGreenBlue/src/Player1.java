import java.awt.*;

public class Player1 extends Player{
	// Constructor
	public Player1(int x, int y, char c, int[][] m) {
		super(x, y, c, m);
		// Up arrow, Right arrow, Down arrow, and Left arrow correspondingly
		int[] keys = {87, 68, 83, 65};
		setCharCodes(keys);
	}
	
	// Draws the player
	public void draw(Graphics g) {
		g.setColor(Color.red); 
		super.draw(g);
	}
}
