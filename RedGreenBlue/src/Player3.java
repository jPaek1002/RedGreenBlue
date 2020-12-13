import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Player3 extends Player{
	// Constructor
	public Player3(int x, int y, char c, int[][] m) {
		super(x, y, c, m);
		int[] chars = {38, 39, 40, 37};
		setCharCodes(chars);
	}
	
	// Draws the player
	public void draw(Graphics g) {
		g.setColor(Color.blue); 
		super.draw(g);
	}
}
