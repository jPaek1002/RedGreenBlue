import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Credits:
 * Jay - Starting the file, Creating the draw method and file reader
 * Eddie - Level transition, Exit detection
 */

public class Map {
	// Fields
	private int level;
	private int[][] stage;
	
	// Players
	private Player1 red;
	private Player2 green;
	private Player3 blue;
	
	// Entities
	private Entity[] entities;
	
	// Exit Testers
	private boolean[] exits;
	private int[] blockIDs;
	
	// Universal Constants
	public static final int STAGE_SIZE = 20;
	public static final int BLOCK_WIDTH = 30;
	public static final int MAX_STAGES = 16;
	
	// BlockIDs
	public static final int AIR = 0;
	public static final int STANDARD_BLOCK = 1;
	public static final int RED_BLOCK = 2;
	public static final int GREEN_BLOCK = 3;
	public static final int BLUE_BLOCK = 4;
	public static final int RED_SPAWN = 5;
	public static final int GREEN_SPAWN = 6;
	public static final int BLUE_SPAWN = 7;
	public static final int EXIT = 8;
	public static final int RED_EXIT = 9;
	public static final int GREEN_EXIT = 10;
	public static final int BLUE_EXIT = 11;
	public static final int DEATH_BLOCK = 12;

	// Constructor
	public Map(Player1 one, Player2 two, Player3 three) {
		level = 1;
		red = one; 
		green = two;
		blue = three;
		entities = new Entity[0];
	}
	
	// Updates the current stage and entities to match the current level.
	public void updateMap() throws IOException {
		File stageText = new File("stage" + level + ".txt");
		BufferedReader reader = new BufferedReader(new FileReader(stageText));
		String[][] strStage = new String[STAGE_SIZE][STAGE_SIZE];
		String line;
		StringTokenizer st;
		
		for(int i = 0; i < STAGE_SIZE; i++) {
			line = reader.readLine();
			st = new StringTokenizer(line);
			for(int j = 0; j < STAGE_SIZE; j++) {
				strStage[j][i] = st.nextToken(); 
			}
		}
		
		stage = translateStage(strStage);
		
		line = reader.readLine();
		int count = Integer.parseInt(line);
		if(count > 0) {
			entities = new Entity[count];
		} else {
			entities = new Entity[0];
		}
		
		for(int i = 0; i < count; i++) {
			line = reader.readLine();
			String[] sections = line.split("/");
			
			int x, y, xT, yT, tW, tH, r, g, b, a, o;
			char c;
			
			
			switch(sections[0]) {
			case "Text":
				st = new StringTokenizer(sections[1]);
				x = Integer.parseInt(st.nextToken());
				y = Integer.parseInt(st.nextToken());
				
				st = new StringTokenizer(sections[2]);
				xT = Integer.parseInt(st.nextToken());
				yT = Integer.parseInt(st.nextToken());
				tW = Integer.parseInt(st.nextToken());
				tH = Integer.parseInt(st.nextToken());
				
				st = new StringTokenizer(sections[3]);
				r = Integer.parseInt(st.nextToken());
				g = Integer.parseInt(st.nextToken());
				b = Integer.parseInt(st.nextToken());
				a = Integer.parseInt(st.nextToken());
				Color color = new Color(r, g, b, a);
				
				int s = Integer.parseInt(sections[4]);
				String t = sections[5];
				
				
				
				Text txt = new Text(x, y, xT, yT, tW, tH, t, s, color);
				entities[i] = txt;
				break;
			case "Spike":
				st = new StringTokenizer(sections[1]);
				x = Integer.parseInt(st.nextToken());
				y = Integer.parseInt(st.nextToken());
				
				o = Integer.parseInt(sections[2]);
				c = sections[3].charAt(0);
				
				Spike spike = new Spike(x, y, c, o);
				entities[i] = spike;
				break;
			}
		}
	}
	
	// Helper to updateMap. Parses the two-dimensional array that represents a stage from
	// String to int type.
	private int[][] translateStage(String[][] strStage) {
		int[][] newStage = new int[STAGE_SIZE][STAGE_SIZE];
		for(int i = 0; i < STAGE_SIZE; i++) {
			for(int j = 0; j < STAGE_SIZE; j++) {
				newStage[i][j] = Integer.parseInt(strStage[i][j]);
			}
		}
		return newStage;
	}
	
	// Resets the level (revives all dead players, puts everyone back to their original positions
	public void resetLevel() {
		refreshMap();
		
		red.setAlive(true);
		green.setAlive(true);
		blue.setAlive(true);
		
		exits = new boolean[3];
		blockIDs = new int[3];
		for(int i = 0; i < 3; i++) {
			exits[i] = true;
			blockIDs[i] = -1;
		}
		int index = 0;
		
		for(int i = 0; i < STAGE_SIZE; i++) {
			for(int j = 0 ; j < STAGE_SIZE; j++) {
				switch(stage[i][j]) {
				case RED_SPAWN:
					red.setPos(i * BLOCK_WIDTH, j * BLOCK_WIDTH);
					break;
				case GREEN_SPAWN:
					green.setPos(i * BLOCK_WIDTH, j * BLOCK_WIDTH);
					break;
				case BLUE_SPAWN:
					blue.setPos(i * BLOCK_WIDTH, j * BLOCK_WIDTH);
					break;
				case EXIT:
				case RED_EXIT:
				case GREEN_EXIT:
				case BLUE_EXIT:
					exits[index] = false;
					blockIDs[index] = i * 20 + j;
					index++;
					break;
				}
			}
		}
	}

	// Method looped to check for when a level is completed
	public void refresh() {
		for(int i = 0; i < 3; i++) {
			if(blockIDs[i] != -1) {
				exits[i] = false;
			}
		}
		
		exits = red.checkExits(exits, blockIDs);
		exits = green.checkExits(exits, blockIDs);
		exits = blue.checkExits(exits, blockIDs);
		
		if(exits[0] && exits[1] && exits[2]) {
			nextLevel();
		}
		
		for(int i = 0; i < entities.length; i++) {
			Entity object = entities[i];
			object.checkCollision(red);
			object.checkCollision(green);
			object.checkCollision(blue);
			
			object.refresh();
		}
	}
	
	// Goes and automatically refreshes the next level
	public void nextLevel() {
		if(level < MAX_STAGES) {
			level++;
		}
		resetLevel();
	}
	
	// Refreshes the map and all related components
	public void refreshMap() {
		try {
			updateMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		red.updateMap(stage);
		green.updateMap(stage);
		blue.updateMap(stage);
	}
	
	// Draws the Map
	public void drawMap(Graphics g) {
		int x;
		int y;
		for(int i = 0; i < STAGE_SIZE; i++) {
			for(int j = 0; j < STAGE_SIZE; j++) {
				x = i * BLOCK_WIDTH;
				y = j * BLOCK_WIDTH;
				switch(stage[i][j]) {
					case STANDARD_BLOCK:
						g.setColor(Color.DARK_GRAY);
						g.fillRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case RED_BLOCK:
						g.setColor(Color.RED);
						g.fillRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case GREEN_BLOCK:
						g.setColor(Color.GREEN);
						g.fillRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case BLUE_BLOCK:
						g.setColor(Color.BLUE);
						g.fillRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case DEATH_BLOCK:
						g.setColor(Color.BLACK);
						g.fillRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case RED_SPAWN:
						g.setColor(Color.RED);
						g.drawRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case GREEN_SPAWN:
						g.setColor(Color.GREEN);
						g.drawRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case BLUE_SPAWN:
						g.setColor(Color.BLUE);
						g.drawRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
						break;
					case EXIT:
						g.setColor(Color.GRAY);
						drawExit(g, x, y);
						break;
					case RED_EXIT:
						g.setColor(Color.RED);
						drawExit(g, x, y);
						break;
					case GREEN_EXIT:
						g.setColor(Color.GREEN);
						drawExit(g, x, y);
						break;
					case BLUE_EXIT:
						g.setColor(Color.BLUE);
						drawExit(g, x, y);
						break;
				}
			}
		}
	}

	// Helper to the drawMap method. Draws an exit with color variations dependent on what was
	// set before.
	private void drawExit(Graphics g, int x, int y) {
		g.fillRect(x, y, BLOCK_WIDTH, BLOCK_WIDTH);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x + 2, y + 2, BLOCK_WIDTH - 4, BLOCK_WIDTH - 2);
	}
	
	// Draws the entities
	public void drawEntities(Graphics g) {
		for(int i = 0; i < entities.length; i++) {
			entities[i].draw(g);
		}
		
	}
	
	// Accessor/Mutator methods
	public void setLevel(int i) {
		level = i;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int[][] getStage() {
		return stage;
	}
}