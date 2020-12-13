import java.awt.*;

/**
 * Credits:
 * Eddie - Everything
 */

public class Spike implements Entity {
	// The top left corner of the unit that contains the spikes
	private int xUnit;
	private int yUnit;
	
	// The color of the spikes
	private char color;
	
	// Orientation of the spikes. Pointing North, East, South, Right
	private int orientation;
	
	
	private Hitbox trigger;
	
	public static final int BLOCK_WIDTH = 30;

	public Spike(int x, int y, char c, int o) {
		xUnit = x;
		yUnit = y;
		color = c;
		orientation = o;
		switch (o) {
		case 0:
			trigger = new Hitbox(x * BLOCK_WIDTH, y * BLOCK_WIDTH + BLOCK_WIDTH/2, BLOCK_WIDTH,
					BLOCK_WIDTH/2);
			break;
		case 1:
			trigger = new Hitbox(x * BLOCK_WIDTH, y * BLOCK_WIDTH, 
					BLOCK_WIDTH/2, BLOCK_WIDTH);
			break;
		case 2:
			trigger = new Hitbox(x * BLOCK_WIDTH, y * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH/2);
			break;
		case 3:
			trigger = new Hitbox(x * BLOCK_WIDTH + BLOCK_WIDTH/2, y * BLOCK_WIDTH, 
					BLOCK_WIDTH/2, BLOCK_WIDTH);
		}
	}
	
	// Checks collision with the given Player
	public void checkCollision(Player p) {
		trigger.checkCollision(p);
		if(trigger.getContact() && p.getColor() != color) {
			p.setAlive(false);
		}
	}

	// Unused
	public void refresh() {}

	// Draws two small spikes next to each other within a certain unit, in particular
	public void draw(Graphics g) {
		switch (color) {
		case 'r':
			g.setColor(Color.RED);
			break;
		case 'g':
			g.setColor(Color.GREEN);
			break;
		case 'b':
			g.setColor(Color.BLUE);
			break;
			
		case 'n':
			g.setColor(Color.DARK_GRAY);
			break;
		}
		for(int i = 0; i < 2; i++) {
			drawTriangle(g, i);
		}
	}
	
	// Helper method to the draw method. Draws a triangle based on orientation and index.
	private void drawTriangle(Graphics g, int i) {
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		if(orientation == 0) {
			xPoints[0] = xUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2;
			xPoints[1] = xUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/4;
			xPoints[2] = xUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/2;
			yPoints[0] = yUnit * BLOCK_WIDTH + BLOCK_WIDTH;
			yPoints[1] = yUnit * BLOCK_WIDTH + BLOCK_WIDTH/2;
			yPoints[2] = yUnit * BLOCK_WIDTH + BLOCK_WIDTH;
		} else if(orientation == 1) {
			xPoints[0] = xUnit * BLOCK_WIDTH;
			xPoints[1] = xUnit * BLOCK_WIDTH + BLOCK_WIDTH/2;
			xPoints[2] = xUnit * BLOCK_WIDTH;
			yPoints[0] = yUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2;
			yPoints[1] = yUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/4;
			yPoints[2] = yUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/2;
		} else if(orientation == 2) {
			xPoints[0] = xUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2;
			xPoints[1] = xUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/4;
			xPoints[2] = xUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/2;
			yPoints[0] = yUnit * BLOCK_WIDTH;
			yPoints[1] = yUnit * BLOCK_WIDTH + BLOCK_WIDTH/2;
			yPoints[2] = yUnit * BLOCK_WIDTH;
		} else {
			xPoints[0] = xUnit * BLOCK_WIDTH;
			xPoints[1] = xUnit * BLOCK_WIDTH + BLOCK_WIDTH/2;
			xPoints[2] = xUnit * BLOCK_WIDTH;
			yPoints[0] = yUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2;
			yPoints[1] = yUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/4;
			yPoints[2] = yUnit * BLOCK_WIDTH + (i * BLOCK_WIDTH)/2 + BLOCK_WIDTH/2;
		}
		
		g.fillPolygon(xPoints, yPoints, 3);
	}

}
