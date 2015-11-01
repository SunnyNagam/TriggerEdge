package blockMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import entity.Player;
import entity.Sand;
import entity.Plants.Tree;
import main.Game;

public class BlockMap {
	// local position
	private double x;
	private double y;

	// bounds of "camera"
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	// map
	public int[][] map;
	public int[][] bgmap;
	private int blockSize;
	private int numRows;
	private int numCols;
	public String save;
	
	//sand stuff
	private ArrayList<Sand> sands;
	
	//trees
	private ArrayList<Tree> trees;
	private String treeSave="";

	// blockset 
	private BufferedImage blockset;
	private int ref_blockWid, ref_blockHeight;
	public Block[][] blocks;
	public Block[][] bgblocks;

	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	
	// Global block variables (stuff that dosent need to be instanced more than one)
	
	public final String[] descrip = {							// descriptions of the blocks
			"NA NA NANA NANA NANA CAN'T TOUCH THIS!",
			"Like dirt but with grass on it.",
			"Soil within the ground, Rich noriching coy and brown, our world it defines. #Haiku",
			"YOU LOOK ROCKED, STUCK BETWEEN A STONE AND A HARD PLACE?",
			"DON'T EAT THE YELLOW SAND!",
			"What do you get when you squish sand?",
			"What do you get when.... Idek.",
			"My job is to dice up rocks and put them back together... i'm afriad i've hit rock bottom. ;)",
			"Shine bright like a... um... wait I know this....",
			"BLOOD DIAMONDS!!! YASSS",
			"Be a pimp and a cool with snappy gold sweatshirt",
			"I run, do you? IRON MAN?!?!",
			"Flammable, eatable, bouncy, black, coal has everything! (don't eat it)",
			"Mimes will speak of your power for generations.",
			"BOYS IN THE WOOD!!!",
			"Everybody do the plank.",
			"Water you waiting for?",
			"Light up the night with these bright lights!!! #GetHype",
			"\"Historically speaking, Asians were lactose intolerant\"--Sokasuki #Alchemy",
			"Burn baby burn. #Don'tBurnBabies",
			"Refined Diamond",
			"Refined Gold",
			"Refined Iron",
			"A Stone Pick",
			"A Iron Pick",
			"A Gold Pick",
			"A Diamond Pick",
			"A Stone Sword",
			"A Iron Sword",
			"A Gold Sword",
			"A Diamod Sword",
			"What secrets does it hold? (might be cool)"
	};
	
	public int ingredients[][][] ={						// Ingredients needed to craft certain blocks
			{{0,1}},
			{{1,1}},
			{{2,1}},
			{{3,1}},
			{{4,1}},
			{{4,1},{3,1}},
			{{6,1}},
			{{3,3}},
			{{8,1}},
			{{9,1}},
			{{10,1}},
			{{11,1}},
			{{12,1}},
			{{4,3}},
			{{15,1}},
			{{15,1}},
			{{16,1}},
			{{19,1},{14,2}},
			{{21,10}},
			{{19,1}},
			{{8,2}},
			{{10,2},{19,1}},
			{{11,2},{19,1}},
			{{14,1},{3,10}},
			{{14,1},{22,2}},
			{{14,1},{21,2}},
			{{14,1},{20,2}},
			{{14,1},{3,10}},
			{{14,1},{22,2}},
			{{14,1},{21,2}},
			{{14,1},{20,2}},
			{{1,1}}
	};
	
	public int[] amountCrafted={					// Amount of blocks created through crafting (ex. 1 coal + 2 wood = -> 4 <- torches) 
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,1,1,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1
	};
	
	public String blockName[]= {				// Names of the blocks
			"Air","Grass","Dirt","Stone","Sand","SandStone","Cobble Stone","Stone Brick","Diamond Ore","Redstone Ore",
			"Gold Ore","Iron Ore","Coal Ore","Glass","Wooden Planks", "Wood", "Water", "Torch", "Cheese", "Coal nugget",
			"Diamond Nugget", "Gold Block", "Iron Block", "Stone Pick", "Iron Pick", "Gold Pick", "Diamond Pick", "Stone Sword",
			"Iron Sword", "Gold Sword", "Diamond Stone", "Mysterious Artifact"
	};
	
	///////////////// block reference table/////////////////////
	public final int AIR = 0;
	public static Block[] BlockType;			// database of each type of block in game (like a periodic table) 
	///////////////////////////////////////////////////////////
	public BlockMap(int blockSize) {				// blockMap constructor
		this.setBlockSize(blockSize);
		numRowsToDraw = Game.HEIGHT / blockSize + 2;
		numColsToDraw = Game.WIDTH / blockSize + 2;
		setSands(new ArrayList<Sand>());
		trees = new ArrayList<Tree>();
	}
	public  BufferedImage resizeImage(BufferedImage image, int width, int height) {	// Resizes images, note: not my function, got it online
       int type=0;																	// http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java	
       type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
       BufferedImage resizedImage = new BufferedImage(width, height,type);
       Graphics2D g = resizedImage.createGraphics();
       g.drawImage(image, 0, 0, width, height, null);
       g.dispose();
       return resizedImage;
    }
	public int getGroundFrom(int x){							// gets the location of the ground from given x coordinate
		int currRow = 0, currCol = x/blockSize;				// currRow = 0 to start from top of the map
		for(int q=0; q<numRows; q++)
			if(blocks[currRow+q][currCol].getIndex()==0);
			else
				return (currRow+q-2)*blockSize;
		return -1;
	}
	 public void loadBlocks(String s) {								// given address of picture of blocks loads them into game
		 try {
			 blockset = ImageIO.read(							// gets image of blocks
					 getClass().getResourceAsStream(s)
					 );
			 
			 blockset = resizeImage(blockset,60,120);		

			 ref_blockWid = blockset.getWidth() / (blockSize);			//  finds how many blocks wide the picture is
			 ref_blockHeight = blockset.getHeight() / (blockSize);		//  finds how many blocks tall the picture is
			 BufferedImage subimage;	
			 BlockType = new Block[ref_blockWid*ref_blockHeight];
			 for(int x=0; x<ref_blockWid*ref_blockHeight; x++){
				 int num = x % 4;										// num = current row of block
				 subimage = blockset.getSubimage(						// gets image of current block
						 num * blockSize,
						 x/ref_blockWid *blockSize,
						 blockSize,
						 blockSize
						 );
				 BlockType[x] = new Block(x==0||x==17?0:x==16?2:1,x==4?true:false,false,blockSize,x, 0, 0,		//creates current block in block type
						 x==0?1:(x==16||x==13)?7:40,
				 		 255,
						 subimage);
			 }
				 
			 
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
	
	public void saveGame(Player player, String save){			//  Saves the game
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(save,false));
			out.write(String.valueOf(getNumCols()));	// writes width of map
			out.newLine();			
			out.write(String.valueOf(getNumRows()));	// writes height of map
			for(int row=0; row<getNumRows(); row++){	// writes the map to the file
				out.newLine();
				out.write(Arrays.toString(map[row]).replaceAll(", ", " ").replace("[", "").replace("]", ""));
			}
			for(int row=0; row<getNumRows(); row++){	// writes the map to the file
				out.newLine();
				out.write(Arrays.toString(bgmap[row]).replaceAll(", ", " ").replace("[", "").replace("]", ""));
			}
			out.newLine();								
			if(player.inv_blocks.size()>0){				// writes player's inventory to file
				String output ="";
				for(int x=0; x<player.inv_blocks.size(); x++)
					output += String.valueOf(player.inv_blocks.get(x).getIndex()+" ");
				out.write(output);
			}
			else
				out.write("23 27 17");									// default starting inventory for new games
			out.newLine();
			if(player.inv_blocks.size()>0){				// writes player's inventory amount to file
				String output ="";
				for(int x=0; x<player.inv_blocks.size(); x++)
					output += String.valueOf(player.each_inv.get(x)+" ");
				out.write(output);
			}
			else
				out.write("1 1 30");					// default starting inventory amounts for new games
			out.newLine();
			if(trees.size()>0)							// writes tree locations to the file
				for(int x=0; x<trees.size(); x++)
					out.write(String.valueOf((int)trees.get(x).getx())+" "+String.valueOf((int)trees.get(x).gety())+" ");
			else
				out.write(treeSave);
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void loadSave(String save, Player player) {						// Loads a save into the game with given save
		try {
			//InputStream inS = new InputStream(GameScreen.class.getClassLoader().getResourceAsStream(save));
			BufferedReader br = new BufferedReader(
					new FileReader(new File(save))
					);
			this.save = save;
			setNumCols(Integer.parseInt(br.readLine()));					// reads width and height of map to find size of map array
			setNumRows(Integer.parseInt(br.readLine()));
			map = new int[getNumRows()][getNumCols()];
			bgmap = new int[numRows][numCols];
			// Camera's global bounds
			xmin = 0;
			xmax = getNumCols() * blockSize - Game.WIDTH + 1;
			ymin = 0;
			ymax = getNumRows() * blockSize - Game.HEIGHT + 1;

			for (int row = 0; row < getNumRows(); row++) {						// Saves the map from the text file into the map array
				String[] tiles = br.readLine().split(" ");
				for (int col = 0; col < getNumCols(); col++)
					map[row][col] = Integer.parseInt(tiles[col]);
			}
			
			for (int row = 0; row < getNumRows(); row++) {						// Saves the map from the text file into the map array
				String[] tiles = br.readLine().split(" ");
				for (int col = 0; col < getNumCols(); col++)
					bgmap[row][col] = Integer.parseInt(tiles[col]);
			}
			
			String[] inv_bloc = br.readLine().split(" ");
			String[] inv_num = br.readLine().split(" ");
			for(int x= 0; x<inv_bloc.length; x++){								// Reads player inventory form file
				player.inv_blocks.add(x,new Block(
						BlockType[Integer.parseInt(inv_bloc[x])].getState(),
						BlockType[Integer.parseInt(inv_bloc[x])].isGravity(),
						false,
						blockSize,
						Integer.parseInt(inv_bloc[x]),
						0,
						0,
						BlockType[Integer.parseInt(inv_bloc[x])].getLightMul(),
						0,
						BlockType[Integer.parseInt(inv_bloc[x])].getImg()
						));
				player.each_inv.add(x,Integer.parseInt(inv_num[x]));
			}
			
			String[] treeLocations;								// reads trees from file
			try{
				treeLocations = br.readLine().split(" ");
			}
			catch(Exception e){
				treeLocations = new String[0];
			}
			for(int x=0; x<treeLocations.length/2; x++)			// adds trees from file to game
				trees.add(new Tree(this,Integer.parseInt(treeLocations[x+1]),Integer.parseInt(treeLocations[x])));
			
			blocks = new Block[getNumRows()][getNumCols()];
			bgblocks = new Block[numRows][numCols];
			for(int x=0; x<map.length; x++)										// turns map array from save file into blocks
				 for(int y=0; y<map[x].length; y++){
					 int num = map[x][y], bgnum = bgmap[x][y];
					 blocks[x][y] = new Block(BlockType[num].getState(), BlockType[num].isGravity(), false,blockSize,num, x, y,
							 BlockType[num].getLightMul(),
							 BlockType[num].getLight(),
							 BlockType[num].getImg());
					 bgblocks[x][y] = new Block(BlockType[bgnum].getState(), BlockType[bgnum].isGravity(), true,blockSize,bgnum, x, y,
							 x==0?1:20,
							 BlockType[bgnum].getLight(),
							 BlockType[bgnum].getImg());
				 }
			initBlocks();			// initializes blocks to render lighting
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Block getBlockAt(Block b, int y, int x, int light, boolean back){	// simpler way of declaring a block, uses a source block to copy it's properies 
		return new Block(b.getState(), b.gravity,back, b.getSize(), b.getIndex(), y, x, b.getLightMul(), light, b.getImg());
	}
	
	public Block getBlockAt(int i){					// an even simpler way of declaring a block
		return new Block(BlockType[i].getState(), BlockType[i].gravity,false, BlockType[i].getSize(), BlockType[i].getIndex(), 
				0, 0, BlockType[i].getLightMul(), 0, BlockType[i].getImg());
	}
	
	public void moveBloc(int origRow, int origCol, int newRow, int newCol){		// moves a block from one spot to another given orignal coordinates of a block and coordinates to move block too
		map[newRow][newCol] = map[origRow][origCol];
		map[origRow][origCol] = 0;
		Block b = blocks[origRow][origCol];
		blocks[origRow][origCol] = getBlockAt(BlockType[0],origRow,origCol,b.getLight(),false);
		blocks[origRow][origCol].update(this);
		blocks[newRow][newCol] = getBlockAt(b,newRow,newCol,b.getLight(),false);
	}

	public int getType(int row, int col) {				// returns the state (solid, liquid, gas) of a block given the coordinates of the block
		try{
			return blocks[row][col].getState();
		}
		catch(ArrayIndexOutOfBoundsException e){		// if trying to get the state of a block outside the map returns solid (so bounds of map are solid)
			//e.printStackTrace();						
			return 1;
		}
	}

	public void setPosition(double x, double y) { 									// Sets camera position												
		this.x += (x-this.x)*0.10;			// coordinates of top left corner of camera
		this.y += (y-this.y)*0.10;
		fixBounds();		// Makes sure camera doesen't go out of bounds
		setColOffset((int) this.x / blockSize);		// Resets which collom to start drawing from (in camera's sight)
		setRowOffset((int) this.y / blockSize);		// Resets which row to start drawing from (in camera's sight)
	}

	public int mouseXPos(int x) {					// given x coordinate of a 'click' finds colloum clicked on
		return (int) ((x / 2 + this.x) / blockSize);
	}

	public int mouseYPos(int y) {					// given y coordinate of a 'click' finds row clicked on
		return (int) ((y / 2 + this.y) / blockSize);
	}

	private void fixBounds() {		// Function to make sure camera does not go out of bounds
		if (x < xmin)				// If camera's x or y is out of bounds, reset x or y back into bounds
			x = xmin;
		if (y < ymin)
			y = ymin;
		if (x > xmax)
			x = xmax;
		if (y > ymax)
			y = ymax;
	}

	public void draw(Graphics2D g) {			
		for (int row = getRowOffset(); row < getRowOffset()+numRowsToDraw && row < getNumRows(); row++)			// draws only the blocks that are nessesary (the ones on the screen)
			for (int col = getColOffset(); col < getColOffset()+numColsToDraw && col < getNumCols(); col++){
					bgblocks[row][col].draw(g, col * getBlockSize() - (int) x, row * getBlockSize()-(int)y);
					blocks[row][col].draw(g, col * getBlockSize() - (int) x, row * getBlockSize()-(int)y);
			}
		for(int x=0; x<getSands().size(); x++)					// draws sand entities
			getSands().get(x).draw(g);
		for(int x=0; x<trees.size(); x++)						// draws trees
			trees.get(x).draw(g);
	}
	public void updateWater() {		
		for(int y=0; y<numRows; y++)
			for(int x=0; x<numCols; x++){
				if(map[y][x]==16)					// updates water since it has to flow regardless of if it is on the screen
					blocks[y][x].update(this);
				if(blocks[y][x].getIndex() == 4 && (blocks[y+1][x].getIndex()==0||blocks[y+1][x].getIndex()==16)){		// creates sand entity if needed
					getSands().add(new Sand(this, BlockType[4].getImg(), blocks[y][x].getRow()*blockSize, blocks[y][x].getCol()*blockSize));
					map[y][x] = map[y-1][x]==16?16:0;
					blocks[y][x] = getBlockAt(BlockType[map[y][x]], y, x, 255, false);
				}
			}
		for(int x=0; x<getSands().size(); x++){		// updates sand entity
			getSands().get(x).update();
			if(!getSands().get(x).isFalling()){			//  removes sand entity if it has landed
				System.out.println("Removed");
				map[getSands().get(x).gety()/blockSize][getSands().get(x).getx()/blockSize] = 4;
				blocks[getSands().get(x).gety()/blockSize][getSands().get(x).getx()/blockSize] =
						getBlockAt(BlockType[4], getSands().get(x).gety()/blockSize, getSands().get(x).getx()/blockSize, 255, false);
				for(int e=0; e<Game.WIDTH/blockSize; e++)		// adjusts lighting
					updateBlocks();
				getSands().remove(x);
			}
		}
	}
	
	public void updateBlocks() {		// updates only the blocks that are nessesary (the ones on the screen and water)
		for (int row = getRowOffset(); row < getRowOffset()+numRowsToDraw && row < getNumRows(); row++)
			for (int col = getColOffset(); col < getColOffset()+numColsToDraw && col < getNumCols(); col++){ 
					blocks[row][col].update(this);
					bgblocks[row][col].update(this);
			}
		for (int row = 0; row < getNumRows(); row++)			// updates sand
			for (int col = 0; col < getNumCols(); col++)
				if(blocks[row][col].getIndex()==4)
					blocks[row][col].update(this);
	}
	
	public void initBlocks(){			// only when game starts, updates all the blocks in the world once, (fixes lighting bugs)
		for (int row = 0; row < getNumRows(); row++)
			for (int col = 0; col < getNumCols(); col++){
					blocks[row][col].update(this);
					bgblocks[row][col].update(this);
			}
	}
	
	public void createWorldMap(String newSave) {			// Terrain generation!!!!!!
		int WORLD_LENGTH = 480;
		int WORLD_HEIGHT = 180;
		int anchor = WORLD_HEIGHT/4;			// location of anchor block (block to start generating ground from)
		
		//How many biomes?
		int[] biomes = new int[rand(8,4)]; 		//Chooses a random amount of biomes.
		
				
		//Which biomes?
		biomes[0] = rand(5,0);			   //Setting anchor biome
		
		// Generates the biomes
		for (int i = 1, per = rand(100,1), prev = biomes[i-1]; i < biomes.length ; i++, per = rand(100,1), prev = biomes[i-1])
			biomes[i]=prev+(per<4?(1+prev>5?-1:1):per<7?(-1+prev<0?1:-1):per<19?(2+prev>5?prev+1>5?-1:1:2):per<31?(-2+prev<0?-1+prev<0?1:-1:-2):per<66?(3+prev>5?2+prev>5?prev+1>5?-1:1:2:3):-3+prev<0?-2+prev<0?-1+prev<0?1:-1:-2:-3);

		//Place anchor block
		numCols = WORLD_LENGTH;
		numRows = WORLD_HEIGHT;
		int[][] map = new int[WORLD_HEIGHT][WORLD_LENGTH];			// temp map
		int raise = 0;									// amount to raise or drop a block by next collom
		int t = 0;								// index of biome
		
		map[anchor][0] = 1;
		int[][] biomesLoc = new int[biomes.length][2];

		System.out.println("Randomly Generating Terrain...");
		for (int x = 0, biomeCount = 0; x < WORLD_LENGTH; x++, biomeCount++) {
			if (rand(biomeCount,0) >= WORLD_LENGTH/biomes.length || x == 0) {							//Choose when to move onto the next biome
				System.out.print("Biome Index is " + biomes[t] + "\nGenerating Biome: ");
				if (biomes[t] == 0) {	//MOUNTAINS GEN
					raise = 3;
					System.out.print("Mountain\n");
				}
				
				else if (biomes[t] == 1) {//HILLS GEN
					raise = 2;
					System.out.print("Hills\n");
				}
				
				else if (biomes[t] == 2) {//FOREST GEN
					raise = 1;
					System.out.print("Forest\n");
				}
				else if (biomes[t] == 3) {//PLAINS GEN
					raise = 0;
					System.out.print("Plains\n");
				}
				else if (biomes[t] == 4) {//DESERT GEN
					raise = 1;
					System.out.print("Desert\n");
				}
				else if (biomes[t] == 5) {//OCEAN GEN
					raise = 0;
					System.out.print("Ocean\n");
				}
				System.out.println("Biome size so far:" + biomeCount);
				biomesLoc[t][1] = x;
				t++;
				if(t<biomes.length)
					biomesLoc[t][0] = x;
				biomeCount = 0; 
			}
			for (int y = anchor; y < WORLD_HEIGHT; y++) {
				if (y - anchor > (biomes[t-1]==5?18:6)) 
						map[y][x] = 3;
				else if(biomes[t-1]==5&&y-anchor>7&&y-anchor<=12&&(map[y-1][x]==4||rand(1,0)==1))
						map[y][x] = 4;
				else if(biomes[t-1]==5&&y-anchor>12&&y-anchor<=18)
						map[y][x] = 5;
				
				else  {
					if (biomes[t-1] == 4 && y-anchor<4)
						map[y][x] = 4;
					else if(biomes[t-1]==4)
						map[y][x] = 5;
					else if(biomes[t-1]==5)
						map[y][x] = 16;
					else
						map[y][x] = 2;
				}	
			}
			for(int y = anchor; y<WORLD_HEIGHT; y++){
				if(map[y][x]==3&&rand(((y-anchor)*12)+WORLD_HEIGHT,0)<3&&y-anchor<(WORLD_HEIGHT-anchor)*0.70){						// coal ore gen
					map[y][x]=12;
					for(int i=0; rand(100,0)<95-(i*2)||i<4; i++){
						int yDis = rand(1,-1), xDis = rand(1,0);
						map[y-((xDis==0||x-xDis>=0)?yDis:0)][x-(x-xDis>=0?xDis:0)] = 12;
					}
				}
				if(map[y][x]==3&&rand(290-((y-anchor)/13),0)<2&&y-anchor>(WORLD_HEIGHT-anchor)*0.40){						// gold ore gen
					map[y][x]=10;
					for(int i=0; rand(100,0)<95-(i*5)||i<2; i++){
						int yDis = rand(1,-1), xDis = rand(1,0); 
						map[y-((xDis==0||x-xDis>=0)?yDis:0)][x-(x-xDis>=0?xDis:0)] = 10;
					}
				}
				if(map[y][x]==3&&rand(220-((y-anchor)/8),0)<2&&y-anchor>(WORLD_HEIGHT-anchor)*0.20){						// iron ore gen
					map[y][x]=11;
					for(int i=0; rand(100,0)<95-(i*2)||i<4; i++){
						int yDis = rand(1,-1), xDis = rand(1,0);
						map[y-((xDis==0||x-xDis>=0)?yDis:0)][x-(x-xDis>=0?xDis:0)] = 11;
					}
				}
				if(map[y][x]==3&&rand(120-((y-anchor)/10),0)<1&&y-anchor>(WORLD_HEIGHT-anchor)*0.60){						// diamond ore gen
					map[y][x]=8;
					for(int i=0; rand(100,0)<95-(i*5)||i<2; i++){
						int yDis = rand(1,-1), xDis = rand(1,0);
						map[y-((xDis==0||x-xDis>=0)?yDis:0)][x-(x-xDis>=0?xDis:0)] = 8;
					}
				}
//				if(map[y][x]==3&&rand(100-((y-anchor)/3),0)<10){						// redStone ore gen
//					map[y][x]=9;
//				}
			}
			
			// generating trees
			int tempY = 0, currCol = x;
			for(int q=0; q<numRows; q++)						// finds the ground from x
				if(map[q][currCol]!=0){
					tempY= (0+q-1)*blockSize;
					break;
				}
			if(biomes[t-1]==1&&rand(100,1)<25)													// hill
				treeSave += String.valueOf(x*blockSize)+" "+String.valueOf(tempY)+" ";
			else if(biomes[t-1]==2&&rand(100,1)<50)																// forest
				treeSave+= String.valueOf(x*blockSize)+" "+String.valueOf(tempY)+" ";
			else if(biomes[t-1]==3&&rand(100,1)<10)																// plain
				treeSave+= String.valueOf(x*blockSize)+" "+String.valueOf(tempY)+" ";
			
			//      Changes anchor (makes world rough)
			if (anchor >= 6 && anchor <= 174 && rand(100,0) > 18)
				anchor += (rand(1,0) == 1? 1 : -1)*rand(raise, 0);
		}
		
		this.map = new int[WORLD_HEIGHT][WORLD_LENGTH];									// sets blockMap's map and background map
		this.bgmap = new int[WORLD_HEIGHT][WORLD_LENGTH];
		
		for (int i = 0; i < WORLD_HEIGHT; i++) 								// copies generated map into blockmap's map
	        this.map[i] = Arrays.copyOf(map[i], map[i].length);
		
		for (int i = 0; i < WORLD_LENGTH; i++){								// changes temperary map to gnerrated background map
	        for(int c=0; c<WORLD_HEIGHT; c++){
	        	if(map[c][i]==0&&map[c+1][i]!=0){
	        		map[c+1][i]=0;
	        		map[c+2][i]=0;
	        		map[c+3][i]=0;
	        		break;
	        	}
	        	if(map[c][i]==16&&map[c+1][i]!=16){
	        		map[c+1][i]=0;
	        		map[c+2][i]=0;
	        		map[c+3][i]=0;
	        		map[c+4][i]=0;
	        		break;
	        	}
	        }
		}
		for (int i = 0; i < WORLD_HEIGHT; i++){
	        for(int c=0; c<WORLD_LENGTH; c++){
	        	if(map[i][c]>=6&&map[i][c]<=12)
	        		map[i][c]=3;
	        	else if(map[i][c]==16)
	        		map[i][c]=0;
	        }
	        
	    this.bgmap[i] = Arrays.copyOf(map[i], map[i].length);   			// copies generated bacground map into blockmap's background map
		}
		this.save = newSave;
		
		saveGame(World.getPlayer(), newSave);								// saves and loads game
		World.loadWorldMap(newSave);
		Game.setCurrentState(Game.getCurrentState()+1);
	}
	
	////////////  GETTERS AND SETTERS ///////////////
	
	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getRowOffset() {
		return rowOffset;
	}

	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}

	public int getColOffset() {
		return colOffset;
	}

	public void setColOffset(int colOffset) {
		this.colOffset = colOffset;
	}
	
	public int getTileSize() {
		return getBlockSize();
	}

	public int getx() {
		return (int) x;
	}

	public int gety() {
		return (int) y;
	}

	public int getWidth() {
		return (getNumCols() * getBlockSize());
	}

	public int getHeight() {
		return (getNumRows() * getBlockSize());
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public ArrayList<Tree> getTrees() {
		return trees;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	
	public int rand(int max, int min) {
		return (int) (Math.random()*((max-min)+1)+min);
	}
	public ArrayList<Sand> getSands() {
		return sands;
	}
	public void setSands(ArrayList<Sand> sands) {
		this.sands = sands;
	}
}