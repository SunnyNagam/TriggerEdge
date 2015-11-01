package blockMap;

import gameState.World;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Block {
	// Refrence numbers (not very important)
	public final static int SOLID = 1;									// states :D
	public final static int LIQUID = 2;
	public final static int GAS = 0;
	public final static int PLASMA = 3;
	
	private int state; 							// blocks current state of matter (solid, gas)
	private int speedMul;
	private int breakSpeed;
	private int resistance;
	private int explosivePower;
	private int size;							// size of block
	private int index;							// index of block (0=air, 1=grass)
	private double lightMul;					// amount of light a block takes away to become darker than a light source
	private int light;							// current brightness of the block (0 = brightest, 255 = darkest)
	public boolean gravity;						// if block has gravity
	private boolean craftable=false;					// if block is craftable
	private boolean backGround, falling = false;		// if block is in the background and if the block is falling
	private boolean equipOnly = false;					// if the block is only equippable and not placable (ex. Sword)
	
	private int col, row;
	private BufferedImage img;
	
	public Block(int state, boolean grav, boolean back, int size, int num, int row, int col, double lightMul, int light, BufferedImage img){	//CONSTURCTOR FOR BLOCKS
		this.setLightMul(lightMul);
		this.setLight(light);
		speedMul = 1;
		this.state = state;
		gravity = grav;
		this.setImg(img);
		this.size = size;
		setIndex(num);
		this.col = col;
		this.row = row;
		if((index>16||index==14||index==13||index==7)&&(index!=19&&index!=20))		// makes appropriate blocks craftable
			setCraftable(true);
		if(index>=18)								// makes appropriate blocks equipable
			setEquipOnly(true);
		this.backGround = back;
	}
	
	public void draw(Graphics2D g, int x, int y){
		if(light >255)									// makes sure light is not out of bounds 
			light=255;
		else if(light<0)
			light=0;
		g.drawImage(img, x, y, null);										// draws block
		g.setColor(new Color(10,10,10,light));								// sets amount of light
		if(backGround&&state!=0)											// makes background blocks darker than foreground blocks
			g.setColor(new Color(10,10,10, light+60<=255?light+60:255));
		g.fillRect(x, y, size, size);										// draws darkness
		g.setColor(Color.white);											// resets color to write text
	}
	
	public void newBloc(Block b, int x, int y){			// changes current block into new block with given coordinates
		speedMul = b.speedMul;
		this.state = b.getState();
		gravity = b.isGravity();
		this.img = b.getImg();
		this.size = b.size;
		this.index = b.getIndex();
		this.col = x;
		this.row = y;
	}
	
	public void update(BlockMap bm) {
		try{
			if(!backGround){
				if(index == 2)											// Ai for dirt (turns into grass if air is above it)
					if(World.getBlockMap().map[row-1][col]==0)
						newBloc(BlockMap.BlockType[1],col,row);			

				if(index==16){											// Ai for water
					if(World.getBlockMap().blocks[row+1][col].getState()==0)
						bm.moveBloc(row, col, row+1, col);						//go down
					else if(World.getBlockMap().blocks[row][col+1].getState()==0)
						bm.moveBloc(row, col, row, col+1);						//go right
					else if(World.getBlockMap().blocks[row][col-1].getState()==0)
						bm.moveBloc(row, col, row, col-1);						//go left
				}
				if(gravity)											// Ai for blocks with gravity
					if(World.getBlockMap().map[row+1][col]==0||World.getBlockMap().map[row+1][col]==16)
						falling = true;
			}
		}
		catch(Exception e){
			//e.printStackTrace();
		}
		///////////////////////GLOBAL BLOCK AI//////////////////    notes: 0 = brightest, 255 = darkest
		if(index==17)											// torches are always completely bright			
			light=0;
		else if(backGround&&index==0)						// background blocks that are air have a brightness of 0
			light=0;
		else if(index==0&&!backGround)						// current foreground block takes light of background block below it
			light = bm.bgblocks[row][col].light;
		else{												// Lighting engine!!!!
			int startRow = row-1, startCol = col-1, originLight = 255, primeIndexState=0;
			for(int y=0; y<3; y++){		// 2 for loops look at blocks up, down, left and right, not diagonal (in a 3 by 3 square around block)
				for(int x=(y==1?0:1); x<(y==1?3:2);x++){
					if(y!=1&&x!=1)		// does not look at itself for light
						continue;
					try{	// Finds the brightest block around it and takes that light and makes it a bit darker.
						if(backGround){			// lighting for background blocks
							if(bm.blocks[startRow+y][startCol+x].getIndex()==17){			// can take light from torches
								if(bm.blocks[startRow+y][startCol+x].getLight()<originLight){
									originLight = bm.blocks[startRow+y][startCol+x].getLight();
									primeIndexState = bm.blocks[startRow+y][startCol+x].getState();
								}
							}
							else if(bm.bgblocks[startRow+y][startCol+x].getLight()<originLight&&bm.blocks[startRow+y][startCol+x].getState()==0){
								originLight = bm.bgblocks[startRow+y][startCol+x].getLight();		// takes light from other background blocks
								primeIndexState = bm.bgblocks[startRow+y][startCol+x].getState();
							}
						}
						else																		//  lighting for foreground blocks
							if(bm.blocks[startRow+y][startCol+x].getLight()<originLight){
								originLight = bm.blocks[startRow+y][startCol+x].getLight();
								primeIndexState = bm.blocks[startRow+y][startCol+x].getState();
							}
					}
					catch(ArrayIndexOutOfBoundsException e){}
				}
			}
			if(primeIndexState!=0)						// light of this block is equal to the llight of the brightet block around it but a little darker
				light = (int) (originLight + lightMul);
			else 											// don't get that much darker if taking light from a gas (gases are more transparent)
				light = (int) (originLight + 1);
		}
		if(light >255)									// makes sure light is not out of bounds 
			light=255;
		else if(light<0)
			light=0;
	}
	
	
////////////////////////// GETTERS AND SETTERS /////////////////////////////////////////////////////////////
	
	public boolean isGravity() {return gravity;}
	public void setGravity(boolean gravity) {this.gravity = gravity;}
	public int getState(){return state;}
	public int getRow() {return row;}
	public void setRow(int Row) {this.row = Row;}
	public int getCol() {return col;}
	public void setCol(int Col) {this.col = Col;}
	public int getIndex() {return index;}
	public void setIndex(int index) {this.index = index;}
	public BufferedImage getImg() {return img;}
	public void setImg(BufferedImage img) {this.img = img;}
	public int getSize() {return size;}
	public void setSize(int size) {this.size = size;}
	public double getLightMul() {return lightMul;}
	public void setLightMul(double lightMul) {this.lightMul = lightMul;}
	public int getLight() {return light;}
	public void setLight(int light) {this.light = light;}
	public boolean isCraftable() {return craftable;}
	public void setCraftable(boolean craftable) {this.craftable = craftable;}
	public boolean isEquipOnly() {return equipOnly;}
	public void setEquipOnly(boolean equipOnly) {this.equipOnly = equipOnly;}
	public boolean isFalling() {return falling;}
	public void setFalling(boolean falling) {this.falling = falling;}	
}