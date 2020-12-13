import java.awt.*;

/**
 * Credits:
 * Eddie - Everything
 */

public class Text implements Entity {
	// Position of Text
	private int xPos;
	private int yPos;
	
	// What the shown text is
	private String text;
	
	// Text size
	private int textSize;
	
	// Text color
	private Color color;
	
	// Checks for fading/showing up based on player position.
	private Hitbox textTrigger;
	private boolean ifTriggered;
	
	public Text(int x, int y, int xTrigger, int yTrigger, int triggerWidth, int triggerHeight,
			String t, int s, Color c) {
		xPos = x;
		yPos = y;
		text = t;
		textSize = s;
		textTrigger = new Hitbox(xTrigger, yTrigger, triggerWidth, triggerHeight);
		color = c;
	}
	
	// Checks collision through the Hitbox
	public void checkCollision(Player p) {
		textTrigger.checkCollision(p);
		ifTriggered = ifTriggered || textTrigger.getContact();
	}
	
	// Refreshes the alpha value of the text.
	public void refresh() {
		int alpha = color.getAlpha();
		
		if(ifTriggered) {
			if(alpha <= 255) {
				alpha += 5;
			}
			if(alpha > 255) {
				alpha = 255;
			}
		} else {
			if(alpha >= 0) {
				alpha -= 5;
			}
			if(alpha < 0) {
				alpha = 0;
			}
		}
		
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		ifTriggered = false;
	}
	 
	// Draws the text onto the window
	public void draw(Graphics g) {
		Font f = new Font("Arial", Font.BOLD, textSize);
		g.setFont(f);
		g.setColor(color);
		g.drawString(text, xPos, yPos);
	}

}
