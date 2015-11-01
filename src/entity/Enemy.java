package entity;

import gameState.GameHandler;

import java.awt.Graphics2D;
import java.util.ArrayList;

import blockMap.Block;
import blockMap.BlockMap;

public class Enemy extends MapObject{

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected double agroRange;
	protected ArrayList<Block> dropPool;				// items this enemy can drop
	protected int[] dropChances;						// chance to drop those items
	protected boolean flinching;
	protected long flinchTimer;
	
	public Enemy(BlockMap bm){										// constructor
		super(bm);
	}
	
	public int rand(int Max, int Min) {								// easier way to create a random number
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
	public void dropItems(GameHandler g){									// drops drop pool once dead
		for(int x=0; x<dropPool.size();x++){
			if(rand(100,1)<=dropChances[x]){
				ArrayList<ItemDrop> newList = g.getDrops();
				newList.add(new ItemDrop(g.getBlockMap(),this.x+rand(20,-20),this.y+rand(20,-20),
						dropPool.get(x).getIndex(),dropPool.get(x).getImg()));
				w.setDrops(newList);
			}
		}
	}
	
	public void hit(int damage, int x, int y, double knockMul){		// deals damage to this enemy and knocks them back depending on player coordinates and power (knockMul)
		if(dead || flinching) return;
		health -= damage;
		if(health <= 0)dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
		if(this.x>=x)
			dx+=10*knockMul;
		else
			dx-=10*knockMul;
		dy-=3*knockMul;
	}
	
	public boolean isDead() { return dead; } 
	
	public int getDamage() { return damage; }
	
	public void draw(Graphics2D g){}
	
	public void update(Player p){}
}