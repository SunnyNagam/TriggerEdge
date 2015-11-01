package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import blockMap.BlockMap;

public class Projectile extends MapObject{
	private int damage;
	public boolean remove;					// should this bullet be removed
	
	public Projectile(BlockMap bm, int x, int y, double dx, double dy, int damage){		// constructor
		super(bm);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.setDamage(damage);
		width = 5;
		height = 5;
		remove = false;
	}
	
	public void getNextPosition(){
		//if(dx==0)
		if(topLeft||topRight||bottomLeft||bottomRight||middleLeft||middleRight||middleBottom||middleTop)	// removes when bullet hits a block
			remove = true;
		else{							// move based on velocity
			x+=dx;
			y+=dy;
		}
	}
	
	public void update(){			// updates bullet
		//update postion
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	
	public void draw(Graphics2D g){			// draws projectile
		setMapPosition();
		g.setColor(Color.red);
		g.fillRect((int) (x - getXmap() - width / 2), (int) (y - ymap - height / 2),
				width, height);
	}
	
	////////////////////////////  GETTERS AND SETTERS  ///////////////////////////////
	
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
}