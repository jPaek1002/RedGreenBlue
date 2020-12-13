import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Player2 extends Player{
	// Constructor
	public Player2(int x, int y, char c, int[][] m) {
		super(x, y, c, m);
		int[] chars = {73, 76, 75, 74};
		setCharCodes(chars);
	}
	
	// Draws the player
	public void draw(Graphics g) {
		g.setColor(Color.green); 
		super.draw(g);
	}
}
