package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import blockMap.BlockMap;

public class Sand extends MapObject{	// sand entity to replace falling sand, fall smoothly, land and get replaced by sand
	private boolean remove;			// should this entity be removed (stopped falling)
	private BufferedImage img;
	
	public Sand(BlockMap bm, BufferedImage img, int y, int x) {			// constructor
		super(bm);
		this.img = img;
		width = 15;
		height = 15;
		this.x = x+width/2;
		this.y = y+height/2;
		this.remove = false; 
		falling = true;
		fallSpeed = 0.10;
		maxFallSpeed = 2.50;
	}
	
	public void getNextPosition(){					// smooth gravity for entity
		if(falling)
			dy += fallSpeed;
		if(dy>maxFallSpeed)
			dy = maxFallSpeed;
	}
	
	public void update(){							// updates entity
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(!falling)							// remove and replace with sand if done falling
			remove = true;
	}
	
	public void draw(Graphics2D g){				// draws sand entity
		setMapPosition();
		g.drawImage(img, (int)(x - getXmap() - width/2), (int)(y- ymap - height/2), null);
	}
	
	////////////////////////////  GETTERS AND SETTERS  //////////////////////////////////////

	public boolean isRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}
}