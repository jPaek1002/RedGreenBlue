/**
 * Credits:
 * Eddie - Everything
 */

public class PlayerCollision {
	private Player1 player1;
	private Player2 player2;
	private Player3 player3;
	
	private Player[] group;
	
	public static final int BLOCK_WIDTH = 30;
	
	public PlayerCollision(Player1 one, Player2 two, Player3 three) {
		player1 = one;
		player2 = two;
		player3 = three;
		
		group = new Player[3];
	}
	
	// To be ran after object collision in order to prevent clipping into walls
	public void checkPlayerCollision() {
		Player[] playerList = {player1, player2, player3};
		int collisionID;
		Player p1;
		Player p2;
		
		for(int i = 0; i < 3; i++) {
			p1 = playerList[i % 3];
			p2 = playerList[(i + 1) % 3];
			collisionID = checkPlayerInteraction(p1, p2);
			if(collisionID != -1) {
				if(collisionID == 0 || collisionID == 2) {
					if(collisionID == 2) { // Swaps the direction
						Player holder = p1;
						p1 = p2;
						p2 = holder;
					}
					if(p1.getDownwardsPressure()) {
						p2.resetYVelocity(false);
						p2.setDownwardsPressure(true);
						p2.setPos(p2.getXPos(), p1.getYPos() - BLOCK_WIDTH);
					} else if(p2.getUpwardsPressure()) {
						p1.resetYVelocity(true);
						p1.setUpwardsPressure(true);
						p1.setPos(p1.getXPos(), p2.getYPos() + BLOCK_WIDTH);
					} else {
						p2.resetYVelocity(false);
						p2.setDownwardsPressure(true);
						p2.setPos(p2.getXPos(), p1.getYPos() - BLOCK_WIDTH);
					}
				} else if(collisionID == 1 || collisionID == 3) {
					if(collisionID == 3) { // Swaps the direction
						Player holder = p1;
						p1 = p2;
						p2 = holder;
					}
					if(p1.getLeftwardsPressure()) {
						p2.resetXVelocity(false);
						p2.setLeftwardsPressure(true);
						p2.setPos(p1.getXPos() + BLOCK_WIDTH, p2.getYPos());
					} else if(p2.getRightwardsPressure()) {
						p1.resetXVelocity(true);
						p1.setRightwardsPressure(true);
						p1.setPos(p2.getXPos() - BLOCK_WIDTH, p1.getYPos());
					} else {
						int averageX = (p1.getXPos() + p2.getXPos())/2;
						p1.setPos(averageX - BLOCK_WIDTH/2, p1.getYPos());
						p2.setPos(averageX + BLOCK_WIDTH/2, p2.getYPos());
						
						int netForce = (int)((double)(p1.getXVelocity() + p2.getXVelocity())/2 + 0.5);
						p1.setXVelocity(netForce);
						p2.setXVelocity(netForce);
						
						p1.setRightwardsPressure(true);
						p2.setLeftwardsPressure(true);
					}
				}
			}
		}
	}
	
	// Helper to checkPlayerCollision. Returns 0 through 4 for coinciding top, right, bottom, and left sides
	// consecutively (based on one's position). Returns -1 if they don't intersect at all.
	private int checkPlayerInteraction(Player one, Player two) {
		if(!checkPointInside(one, two)) {
			return -1;
		}
		if(!(one.getAlive() && two.getAlive())) {
			return -1;
		}
		if(one.getXPos() == two.getXPos()) {
			if(one.getYPos() > two.getYPos()) {
				return 0;
			} else {
				return 2;
			}
		} else {
			double slope = (double)(one.getYPos() - two.getYPos()) / (one.getXPos() - two.getXPos()); 
			if(Math.abs(slope) <= 1) { // Sides
				if(one.getXPos() < two.getXPos()) {
					return 1;
				} else {
					return 3;
				}
			} else { // Base 'n' Floor
				if(one.getYPos() > two.getYPos()) {
					return 0;
				} else {
					return 2;
				}
			}
		}
	}
	
	// Helper method to checkPlayerInteraction to view if a specific point is within
	// a player.
	private boolean checkPointInside(Player one, Player two) {
		int xTarget;
		int yTarget;
		for(int i = 0; i < 4; i++) {
			xTarget = one.getXPos() + (i % 2) * BLOCK_WIDTH;
			yTarget = one.getYPos() + (i / 2) * BLOCK_WIDTH;
			if(xTarget >= two.getXPos() && xTarget <= two.getXPos() + BLOCK_WIDTH) {
				if(yTarget >= two.getYPos() && yTarget <= two.getYPos() + BLOCK_WIDTH) {
					return true;
				}
			}
		}
		return false;
	}
}