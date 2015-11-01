package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import blockMap.BlockMap;

public class ItemDrop extends MapObject{
	private int index;
	private BufferedImage img;
	private boolean remove;
	
	public  BufferedImage resizeImage(BufferedImage image, int width, int height) {		// Resizes images, note: not my function, got it online
	   int type=0;																	// http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java
       type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
       BufferedImage resizedImage = new BufferedImage(width, height,type);
       Graphics2D g = resizedImage.createGraphics();
       g.drawImage(image, 0, 0, width, height, null);
       g.dispose();
       return resizedImage;
    }
	
	public ItemDrop(BlockMap bm, double x, double y, int index, BufferedImage img){ 		// constructor
		super(bm);
		this.setIndex(index);
		this.img = resizeImage(img,10,10);
		width = 10;
		height = 10;
		this.x = x+width/2;
		this.y = y-width/2;
		this.remove = false;
		fallSpeed = 0.05;
		maxFallSpeed = 2.50;
	}
	
	public void getNextPosition(){		// gives item drop gravity
		if(falling)
			dy += fallSpeed;
		if(dy>maxFallSpeed)
			dy = maxFallSpeed;
	}
	
	public void update(){				// constantly updates block and hit detection
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	
	public void draw(Graphics2D g){			// draws itemdrop
		setMapPosition();
		g.drawImage(img, (int)(x - getXmap() - width/2), (int)(y- ymap - height/2), null);
	}
	
	////////////////////////////  GETTER AND SETTER  ////////////////////////////////////

	public boolean isRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
