import java.awt.Graphics;

/**
 * Credits:
 * Eddie - Everything
 */

public interface Entity {
	void checkCollision(Player p);
	void refresh();
	void draw(Graphics g);
}
