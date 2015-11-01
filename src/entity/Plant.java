package entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

import gameState.World;
import blockMap.Block;
import blockMap.BlockMap;

public class Plant extends MapObject{		// plant super class

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int stage;
	
	protected ArrayList<Block> dropPool;			// items that plant can drop
	protected int[] dropChances;					// chance to drop those items
	
	protected boolean flinching;
	protected long flinchTimer;
	
	public Plant(BlockMap bm){
		super(bm);
	}
	
	public void dropItems(World w){					// drop items
		for(int x=0; x<dropPool.size();x++){
			if(rand(100,1)<=dropChances[x]){
				ArrayList<ItemDrop> newList = w.getDrops();
				newList.add(new ItemDrop(w.getBlockMap(),this.x+rand(20,-20),this.y+rand(20,-20),
						dropPool.get(x).getIndex(),dropPool.get(x).getImg()));
				w.setDrops(newList);
			}
		}
	}
	public void hit(int damage){			// deals damage to tree 
		if(dead || flinching) return;
		health -= damage;
		if(health <= 0)dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public boolean isDead() { return dead; } 
	
	public void draw(Graphics2D g){}
	
	public void update(){}
	
}
