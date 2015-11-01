package entity;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import main.Game;
import blockMap.Block;
import blockMap.BlockMap;

public abstract class MapObject {
	
	//players stuff
	protected double health, maxHealth, energy, maxEnergy;
	protected double jetpackEnergy;
	protected boolean jetDash;
	
	//tile stuff
	protected BlockMap blockMap;
	protected int blockSize;
	protected double xmap;
	protected double ymap;
	
	public double x;		//positions
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimen
	protected int width, height;
	
	//collistion
	private int currRow;
	private int currCol;
	protected double xdest, ydest, xtemp, ytemp;
	protected boolean topLeft,topRight,bottomLeft,bottomRight,middleLeft,middleRight,middleBottom,middleTop; 		// finds which part of entity hit a block
	protected boolean destBlock;
	
	//animaiton
	protected int currentAction;
	protected int previousAction;
	private boolean facingRight;
	
	//damage
	protected int startFallY;
	protected double fallDamage;
	
	// movement
	protected boolean left,right,up,down,jumping;
	protected boolean falling;
	protected boolean jets;
	protected boolean inWater=false;
	
	// mov attributes
	protected double moveSpeed, maxSpeed, stopSpeed, fallSpeed, maxFallSpeed, jumpStart, stopJumpSpeed, jetBoost;
	protected double waterSpeedMulti;
	
	//constructor
	public MapObject(BlockMap bm){
		blockMap = bm;
		blockSize = bm.getTileSize();
		waterSpeedMulti = 0.50;
	}
	
	public boolean intersects (MapObject o){		// checks if this object intersects another object
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public boolean intersects (Point p){			// checks if this object intersects a point
		if(p.getX()>x-width/2 && p.getX()<x+width/2)
			if(p.getY()>y-height/2 && p.getY()<y+height/2)
				return true;
		return false;
	}
	
	public int rand(int Max, int Min) {				// simplified random int formula
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
	public Rectangle getRectangle(){				// gets this rectangle for hit detection
		return new Rectangle( (int)x-width/2, (int)y-height/2, width, height);
	}
	
	public void calculateCorners(double x, double y){		// sets hit detection point variables
		int leftTile = (int)(x - width/2)/blockSize;
		int rightTile = (int)(x + width/2 -1)/blockSize;
		int topTile = (int)(y - height/2)/blockSize;
		int bottomTile = (int)(y + height/2-1)/blockSize;
		
		int tl = blockMap.getType(topTile, leftTile);					// gets state of corners and points of objects
		int tr = blockMap.getType(topTile, rightTile);
		
		int ml = blockMap.getType((int)y/blockSize, leftTile);
		int mr = blockMap.getType((int)y/blockSize, rightTile);
		
		int mb = blockMap.getType(bottomTile, (int)x/blockSize);
		int mt = blockMap.getType(topTile, (int)x/blockSize);
		
		int bl = blockMap.getType(bottomTile, leftTile);
		int br = blockMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Block.SOLID;							// checks if objects hits a solid block
		topRight = tr == Block.SOLID;
		middleLeft = ml == Block.SOLID;
		middleRight = mr == Block.SOLID;
		middleBottom = mb == Block.SOLID;
		middleTop = mt == Block.SOLID;
		bottomLeft = bl == Block.SOLID;
		bottomRight = br == Block.SOLID;

		if(tl==2||tr==2||ml==2||mr==2||mb==2||mt==2||bl==2||br==2)		// checks if object is in water
			inWater = true;
		else
			inWater = false;
	}
	
	public void checkTileMapCollision(){		// checks hit detection and moves object
		setCurrCol((int)x / blockSize);
		setCurrRow((int)y / blockSize);
		
		xdest = x+dx;
		ydest = y+dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);					// moves vertically and checks hit detection
		if(dy < 0){
			if(topLeft || topRight || middleTop){
				dy=0;
				ytemp = (getCurrRow()- (int)height/blockSize + 1) * blockSize+ height / 2;
			}
			else{
				ytemp += inWater?dy*waterSpeedMulti:dy;
			}
		}
		
		if(dy >0){
			if(bottomLeft || bottomRight || middleBottom){
				dy = 0;
				setFalling(false);
				health -= (y - startFallY > 0 ? y - startFallY : 0)/6;
				ytemp = height >= blockSize ? (getCurrRow()+ (int)height/blockSize) * blockSize - height / 2 
						: (getCurrRow()+ 1) * blockSize - height / 2 - 4 ;
			}
			else{
				ytemp += inWater?dy*waterSpeedMulti:dy;
			}
		}
		
		calculateCorners(xdest, y);				//  moves horizontally and checks hit detection
		if(dx < 0){
			if(topLeft || bottomLeft || middleLeft){
				dx=0;
				xtemp = (getCurrCol() - (int)width/blockSize +1)* blockSize + width / 2;
			}
			else
				xtemp += inWater?dx*waterSpeedMulti:dx;
		}
		if(dx >0){
			if(topRight || bottomRight || middleRight){
				dx = 0;
				xtemp = (getCurrCol() + 1) *blockSize - width / 2;
			}
			else
				xtemp += inWater?dx*waterSpeedMulti:dx;
		}
		
		if(!isFalling()){					// checks and sets falling
			calculateCorners(x,ydest+1);
			if(!bottomLeft && !bottomRight &&!middleBottom){
				setFalling(true);
				startFallY = (int)y;
			}
		}
	}
	
	public boolean onScreen(){					// checks if a object is on the screen currently
		return x+ getXmap() + width <0 ||
			x+getXmap()-width >Game.WIDTH ||
			y+ymap + height < 0 ||
			y+ ymap - height >Game.HEIGHT;
	}
	

	////////////////////////////////  GETTERS AND SETTERS /////////////////////////
	
	public int getx(){ return (int)x;}
	public int gety(){ return (int)y;}
	public int getWidth(){ return width;}
	public int getHeight(){ return height;}
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	public void setMapPosition(){
		setXmap(blockMap.getx());
		ymap = blockMap.gety();
	}
	
	public void setLeft(boolean b) { left = b;}
	public void setRight(boolean b) { right = b;}
	public void setUp(boolean b) { up = b;}
	public void setDown(boolean b) { down = b;}
	public void setJumping(boolean b) { jumping = b;}
	public void setJets(boolean b) { jets = b;}
	public void setDash(boolean b) {jetDash = b;}
	
	public boolean getJumping(){return jumping;}
	public boolean getJets() {return jets;}
	public double getHealth() {return health;}
	public double getMaxHealth() {return maxHealth;}
	public double getEnergy() {return energy;}
	public double getMaxEnergy() {return maxEnergy;}
	
	public void setHealth(double d) {health = d;}
	public void setMaxHealth(double d) {maxHealth = d;}
	public void setEnergy(double d) {energy = d;}
	public void setMaxEnergy(double d) {maxEnergy = d;}

	public int getCurrRow() {
		return currRow;
	}

	public void setCurrRow(int currRow) {
		this.currRow = currRow;
	}

	public int getCurrCol() {
		return currCol;
	}

	public void setCurrCol(int currCol) {
		this.currCol = currCol;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public double getXmap() {
		return xmap;
	}

	public void setXmap(double xmap) {
		this.xmap = xmap;
	}
	
	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}
}