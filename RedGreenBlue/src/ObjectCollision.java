/**
 * Credits:
 * Eddie - Started file, Collision with all things
 * Jay - Boundary collision
 */

public class ObjectCollision {
	// The map of the level, stored in a two-dimensional array
	private int[][] map;
	private int mapLength = 20;
	
	// The player that this instance observes
	private Player character;
	
	// The width of each "unit" in the level (which is also the size of the player)
	public static final int BLOCK_WIDTH = 30;
	
	// Constructor
	public ObjectCollision(Player p, int[][] m) {
		character = p;
		map = m;
	}
	
	// Updates the level to match what is loaded
	public void updateLevel(int[][] m) {
		map = m;
	}
	
	// Runs through all collisions
	public void checkObjectCollision() {
		checkCollisionBlocks();
		
		checkBoundaries();
	}

	// Calculates collision of a player with solid blocks
	public void checkCollisionBlocks() {
		int xPos = character.getXPos();
		int yPos = character.getYPos();
		
		character.setUpwardsPressure(false);
		character.setRightwardsPressure(false);
		character.setDownwardsPressure(false);
		character.setLeftwardsPressure(false);
		
		int unitXPos = xPos/BLOCK_WIDTH;
		int unitYPos = yPos/BLOCK_WIDTH;
		int[] unitsToCheck = new int[4];
		// Checks the direct array around the player
		unitsToCheck[0] = checkBlock(unitXPos, unitYPos);         // TL
		unitsToCheck[1] = checkBlock(unitXPos + 1, unitYPos);     // TR
		unitsToCheck[2] = checkBlock(unitXPos + 1, unitYPos + 1); // BR
		unitsToCheck[3] = checkBlock(unitXPos, unitYPos + 1);     // BL
		
		for(int i = 0; i < 4; i++) { // First checks groups of 2
			if(unitsToCheck[i % 4] != -1 && unitsToCheck[(i + 1) % 4] != -1) { // Both blocks are collidable
				int x1 = unitsToCheck[i % 4] / 20;
				int x2 = unitsToCheck[(i + 1) % 4] / 20;
				int y1 = unitsToCheck[i % 4] % 20;
				int y2 = unitsToCheck[(i + 1) % 4] % 20;
				int collisionID1 = calculatePush(x1, y1);
				int collisionID2 = calculatePush(x2, y2);
				if(collisionID1 == collisionID2) { // Interacts with both at the same angle
					applyCollision(collisionID1, x1 * BLOCK_WIDTH, y1 * BLOCK_WIDTH);
				} else if(collisionID1 == -1 && collisionID2 != -1) { // Interacts with only one block
					applyCollision(collisionID2, x2 * BLOCK_WIDTH, y2 * BLOCK_WIDTH);
				} else if(collisionID1 != -1 && collisionID2 == -1) { // Interacts with the other block
					applyCollision(collisionID1, x1 * BLOCK_WIDTH, y1 * BLOCK_WIDTH);
				} else {
					int addon = 0; // Used to limit the copied code
					if(x1 == x2) {
						addon = 1;
					}
					
					if(collisionID1 == addon || collisionID2 == addon) {
						applyCollision(addon, x1 * BLOCK_WIDTH, y1 * BLOCK_WIDTH);
					} else if(collisionID1 == 2 + addon || collisionID2 == 2 + addon) {
						applyCollision(2 + addon, x1 * BLOCK_WIDTH, y1 * BLOCK_WIDTH);
					}
				}
			}
		}
		
		for(int i = 0; i < 4; i++) {
			int x = unitsToCheck[i] / 20;
			int y = unitsToCheck[i] % 20;
			applyCollision(calculatePush(x, y), x * BLOCK_WIDTH, y * BLOCK_WIDTH);
		}
	}
	
	// Helper method to checkCollisionBlocks. Changes the Player's position to correspond
	// with what it collides with.
	private void applyCollision(int collisionID, int xBlock, int yBlock) {
		switch (collisionID) {
		case 0:
			character.setPos(character.getXPos(), yBlock - BLOCK_WIDTH);
			character.setDownwardsPressure(true);
			character.resetYVelocity(false);
			break;
		case 1:
			character.setPos(xBlock + BLOCK_WIDTH, character.getYPos());
			character.setLeftwardsPressure(true);
			character.resetXVelocity(false);
			break;
		case 2:
			character.setPos(character.getXPos(), yBlock + BLOCK_WIDTH);
			character.setUpwardsPressure(true);
			character.resetYVelocity(true);
			break;
		case 3:
			character.setPos(xBlock - BLOCK_WIDTH, character.getYPos());
			character.setRightwardsPressure(true);
			character.resetXVelocity(true);
			break;
			
			default:
				break;
		}
	}
	
	// Helper method to checkObjectCollisionBlocks. Finds the correct direction the player
	// should be pushed when colliding with a block. Returns 0 if it is pushed up, 1 to push
	// right, 2 to push down, and 3 to push left. -1 to not push at all.
	private int calculatePush(int x, int y) {
		int xBlock = x * BLOCK_WIDTH + BLOCK_WIDTH / 2;
		int yBlock = y * BLOCK_WIDTH + BLOCK_WIDTH / 2;
		int xCenter = character.getXPos() + BLOCK_WIDTH / 2;
		int yCenter = character.getYPos() + BLOCK_WIDTH / 2;
		if(!(xCenter >= xBlock - BLOCK_WIDTH && xCenter <= xBlock + BLOCK_WIDTH &&
				yCenter >= yBlock - BLOCK_WIDTH && yCenter <= yBlock + BLOCK_WIDTH)) { // Checks if it actually collides
			return -1;
		}
		if(xCenter == xBlock) {
			if(yCenter < yBlock) {
				return 0;
			} else {
				return 2;
			}
		}
		double slope = (double)(yCenter - yBlock) / 
				(xCenter - xBlock);
		
		if(Math.abs(slope) <= 1) { 
			if(xCenter < xBlock) {
				return 3;
			} else {
				return 1;
			}
		} else { 
			if(yCenter < yBlock) {
				return 0;
			} else {
				return 2;
			}
		}
	}
	
	// Helper method to checkObjectCollisionBlocks, sieves through the output of fillChecker
	// to find what blocks will be necessary for checkObjectCollisionBlocks.
	private int checkBlock(int x, int y) {
		if(x >= 0 && x < mapLength && y >= 0 && y < mapLength) {
			int colorID;
			switch(character.getColor()) {
			case 'r':
				colorID = 2;
				break;
			case 'g':
				colorID = 3;
				break;
			case 'b':
				colorID = 4;
				break;
			default:
				colorID = 1;
			}
			if(map[x][y] >= 1 && map[x][y] <= 4 && map[x][y] != colorID) { // Touchable Block
				return fillChecker(x, y);
			}
			
			if(map[x][y] == 12) { // Death Block
				character.setAlive(false);
			}
		}
		return -1;
	}
	
	public boolean[] checkExits(boolean[] exits, int[] blockIDs) {
		int colorID = 0;
		switch(character.getColor()) {
		case 'r':
			colorID = 1;
			break;
		case 'g':
			colorID = 2;
			break;
		case 'b':
			colorID = 3;
			break;
		}
		
		int x = character.getXPos();
		int y = character.getYPos();
		int xBlock;
		int yBlock;
		
		for(int i = 0; i < exits.length; i++) {
			if(blockIDs[i] != -1) {
				xBlock = blockIDs[i] / 20;
				yBlock = blockIDs[i] % 20;
				if(x >= xBlock * BLOCK_WIDTH - BLOCK_WIDTH && x <= xBlock * BLOCK_WIDTH + BLOCK_WIDTH &&
					y >= yBlock * BLOCK_WIDTH - BLOCK_WIDTH && y <= yBlock * BLOCK_WIDTH + BLOCK_WIDTH) {
				
					if(map[xBlock][yBlock] == 8 || map[xBlock][yBlock] == 8 + colorID) {
						exits[i] = true;
					}
				}
			}
		}
		
		return exits;
	}
	
	// Helper method to checkBlocks and checkCollisionObjects, determines what units will be 
	// checked. (Outputs the format of the x value multiplied by 20 plus y. Since y never exceeds
	// 20, you can use the mod option to find y. Outputs -1 if it doesn't exist.)
	private int fillChecker(int x, int y) {
		if(x >= 0 && x < mapLength && y >= 0 && y < mapLength) {
			return x * 20 + y;
		}
		return -1;
	}
	
	public void checkBoundaries() {
		if(character.getXPos() < 0) {
			character.setPos(0, character.getYPos());
			character.resetXVelocity(false);
			character.setLeftwardsPressure(true);
		} else if(character.getXPos() > 575) {
			character.setPos(575, character.getYPos());
			character.resetXVelocity(true);
			character.setRightwardsPressure(true);
		}
		
		if(character.getYPos() < 0) {
			character.setPos(character.getXPos(), 0);
			character.resetYVelocity(true);
			character.setUpwardsPressure(true);
		} else if(character.getYPos() > 605) { // Death
			character.resetXVelocity();
			character.resetYVelocity();
			character.setAlive(false);
		}
	}
}