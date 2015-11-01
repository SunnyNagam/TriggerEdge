package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Plants.Tree;
import blockMap.Block;
import blockMap.BlockMap;

public class Player extends MapObject {

	// player stuff
	private double fire, maxFire;
	private boolean dead, flinching;
	private double hregen;
	private long flinchTimer;
	public boolean tog_des = false, tog_add = false;
	private int range;
	private double energyPause = 0;
	private BufferedImage img;

	// player inventory
	public ArrayList<Block> inv_blocks;
	public ArrayList<Integer> each_inv;
	public int equiped;
	public int maxStackSize = 80;

	// PROJECTILES
	public ArrayList<Projectile> bullets = new ArrayList<Projectile>();
	private boolean firing;
	private int fireCost;
	private int bulletDamage;
	public int bulletCost;
	// private ArrayList<FireBall> firBalls;

	// slash
	private boolean slashing;
	private int slashDamage, slashRange;

	// gliding
	private boolean gliding;

	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 2, 8, 1, 2, 4, 2, 5 };

	// animation actions
	private static final int IDLE = 0, WALKING = 1, JUMPING = 2, FALLING = 3,
			FIREBALL = 5, SLASHING = 6;
	
	public Player(BlockMap bm) {		// constructor
		super(bm);
		try {
			img = ImageIO.read(							// gets image of blocks
					 getClass().getResourceAsStream("/Pictures/EsunCraftPlayer.png")
					 );
		} catch (IOException e) {
			e.printStackTrace();
		}
		width = 20;
		height = 40;
		tog_des = false;
		tog_add = false;
		range = 40;
		maxEnergy = energy = 100;
		jetpackEnergy = 0.6;
		jetBoost = -2;
		inv_blocks = new ArrayList<Block>();
		each_inv = new ArrayList<Integer>();
		equiped = 0;
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.00;
		jumpStart = -4.5;
		stopJumpSpeed = 0.3;
		setFacingRight(true);
		health= maxHealth = 100;
		hregen = 0.12;
		fire = maxFire = 2500;
		fireCost = 200;
		bulletDamage = 10;
		bulletCost = 10;
		slashDamage = 15;
		slashRange = 30;
	}

	public boolean clickRang(int x, int y) {									// checks if click is within player's range
		double locX = x - (this.x - getXmap()), locY = y - (this.y - ymap);								
		if ((locX < range && locX > 0) || (locX > -range && locX < 0))
			if ((locY < range && locY > 0) || (locY > -range && locY < 0))
				return true;
		return false;
	}
	
	public void checkBeingAttacked(ArrayList<Enemy> enemies) {				// checks if enemy is attacking player
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(intersects(e))
				hit(e.getDamage());
		}
	}
	
	public int getNumInInv(int index){						// get how many of a block is in the inventory (ex. there are 124 stone blocks in players inventory)
		int sum=0;
		for(int x=0; x<inv_blocks.size(); x++)
			if(inv_blocks.get(x).getIndex()==index)
				sum+=each_inv.get(x);
		return sum;
	}
	
	public void removeFromInv(int index, int amount){		// removes a certain block and amount from inventory (ex. remove 5 iron from player's inventory)
		for(int x=0; x<inv_blocks.size()&&amount>0; x++)
			if(inv_blocks.get(x).getIndex()==index)
				if(each_inv.get(x)<amount){
					amount -= each_inv.get(x);
					each_inv.set(x, 0);
				}
				else{
					each_inv.set(x, each_inv.get(x)-amount);
					amount=0;
				}
	}
	
	public void addToInv(int index, int amount){			// adds a certain block and amount from inventory (ex. add 5 iron to player's inventory)
		boolean blocks_added = false;
		for (int x = 0; x <inv_blocks.size(); x++){
			if (inv_blocks.get(x).getIndex() == index && each_inv.get(x)<maxStackSize) {
				int temp_ea = each_inv.get(x)+1;
				each_inv.remove(x);															//Lower the count of that item in the inventory.
				each_inv.add(x,temp_ea);
				amount--;
				x--;
			}
			if(amount<=0){
				blocks_added = true;
				break;
			}
		}
		if(!blocks_added){
			Block toGet = blockMap.BlockType[index];
			inv_blocks.add(blockMap.getBlockAt(toGet, 0, 0, 0, false));
			each_inv.add(amount);
		}
	}
	
	public void pickItems(ArrayList<ItemDrop> drops) {		// picks up item drops
		for(int i=0; i<drops.size(); i++){
			ItemDrop d = drops.get(i);
			if(intersects(d)){
				d.setRemove(true);
				addToInv(d.getIndex(),1);
			}
		}
	}
	
	public void checkAttack(Tree e) {					// check dealing damage to trees
		if (isSlashing()) {
			//  adjusts damage depending on weapon
			if(inv_blocks.get(equiped).getIndex()==27)
				slashDamage = 50;
			else if(inv_blocks.get(equiped).getIndex()==28)
				slashDamage = 60;
			else if(inv_blocks.get(equiped).getIndex()==29)
				slashDamage = 30;
			else if(inv_blocks.get(equiped).getIndex()==30)
				slashDamage = 100;
			else
				slashDamage = 10;
			// hit detection range
			Rectangle slashLeft = new Rectangle((int)x-width/2-slashRange, (int)y-(height/2)-slashRange, slashRange, slashRange*2+height);
			Rectangle slashRight = new Rectangle((int)x+width/2, (int)y-(height/2)-slashRange, slashRange, slashRange*2+height);
			
			//  deal damage
			if(isFacingRight() && slashRight.intersects(e.getRectangle()))
				e.hit(slashDamage);
			else if(!isFacingRight() && slashLeft.intersects(e.getRectangle()))
				e.hit(slashDamage);
		}
	}
	public void checkAttack(Enemy e){		// check attacking enemy
		// sets damage ddepending on item equipped
			if (isSlashing()) {
				if(inv_blocks.get(equiped).getIndex()==27)
					slashDamage = 50;
				else if(inv_blocks.get(equiped).getIndex()==28)
					slashDamage = 60;
				else if(inv_blocks.get(equiped).getIndex()==29)
					slashDamage = 30;
				else if(inv_blocks.get(equiped).getIndex()==30)
					slashDamage = 100;
				else
					slashDamage = 10;
				// hit range
				Rectangle slashLeft = new Rectangle((int)x-width/2-slashRange, (int)y-(height/2)-slashRange, slashRange, slashRange*2+height);
				Rectangle slashRight = new Rectangle((int)x+width/2, (int)y-(height/2)-slashRange, slashRange, slashRange*2+height);
				if(isFacingRight() && slashRight.intersects(e.getRectangle()))
					e.hit(slashDamage,(int)x,(int)y,0.7);
				else if(!isFacingRight() && slashLeft.intersects(e.getRectangle()))
					e.hit(slashDamage, (int)x, (int)y, 0.7);
			}
			
	}
	public void checkBullet(Enemy e){		// check bullet colliding with enemy
		for (int j = 0; j < bullets.size(); j++)
			if(bullets.get(j).intersects(e)){
				e.hit(bulletDamage,(int)x,(int)y,0.7);
				bullets.remove(j);
			}
	}
		
	public void hit(int Damage){			// deals damage to player
		if(flinching) return;
		health -= Damage;
		if(health <= 0) setDead(true);
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	private void getNextPosition() {		// moves player with gravity and controls
		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking except when in air
		if ((firing) && !(jumping || isFalling())) {
			dx = 0;
		}
		// jumping
		if (jumping && !isFalling()) {
			startFallY = (int) y;
			dy = inWater?jumpStart*1.2:jumpStart;
			setFalling(true);
		}
		if (jets && energy > 0) {
			energy -= jetDash?jetpackEnergy*1.2:jetpackEnergy;
			startFallY = (int) y;
			if (!jetDash)
				dy = jetBoost;
			if (left && jetDash) {
				dx += jetBoost * 3;
			} else if (right && jetDash)
				dx += -jetBoost * 3;
			else
				jetDash = false;
		}
		if (isFalling()) {
			if (dy > 0 && gliding)
				dy += fallSpeed * 0.1;
			else {
				dy += fallSpeed;
			}
			if (dy > 0) {
				jumping = false;
			}
			if (dy < 0 && (!jumping || jets)){
				dy += stopJumpSpeed;
				falling = false;
			}
			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
	}

	public void update(BlockMap bm) {			// updates player
											// check death
		if(health <= 0)
			setDead(true);
														// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		for(int x=0; x<bullets.size(); x++){			// updates bullets
			bullets.get(x).update();
			if(bullets.get(x).isRemove()){
				bullets.remove(x);
				x--;
			}
		}
		
									// Adjusts and regenerates health and energy
		if (energyPause >= 10) {
			energyPause = 0;
			energy = 0.1;
		}
		if (energy < maxEnergy) {
			if (energy <= 0)
				energyPause += 0.15;
			else
				energy += 0.15;
		}
		if (energy <= 0)
			energy = 0;
		else if (energy >= maxEnergy)
			energy = maxEnergy;

		if (health < maxHealth && !dead)
			health += hregen;
		if (health < 0)
			health = 0;
		else if (health >= maxHealth)
			health = maxHealth;
		// check done flinching
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer)/1000000;
			if(elapsed >1000){
				flinching = false;
			}
		}
		
		// set direction
		if (currentAction != SLASHING && currentAction != FIREBALL) {
			if (right)
				setFacingRight(true);
			if (left)
				setFacingRight(false);
		}
		
		// remove blank blocks from invetory and remove blocks with amount 0
		for(int x=0; x<inv_blocks.size(); x++)
			if(inv_blocks.get(x).getIndex() == 0 || each_inv.get(x)==0){
				if(equiped >0)
					equiped--;
				inv_blocks.remove(x);
				each_inv.remove(x);
			}
	}

	public void draw(Graphics2D g) {			// draws player
		setMapPosition();

		for(int x=0; x<bullets.size(); x++)		// draws bullets
			bullets.get(x).draw(g);
		
		if (flinching) {						// flinches
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}
											// sets coordinates for fire on jetpack
		int[] xp = null, yp;
		if (isFacingRight()) {
			xp = new int[] { (int) (x - getXmap() - width / 2 - 6),
					(int) (x - getXmap() - width / 2 - 1),
					(int) (x - getXmap() - width / 2 - 4) };
		} else
			xp = new int[] { (int) (x - getXmap() + width / 2 + 6),
					(int) (x - getXmap() + width / 2 + 1),
					(int) (x - getXmap() + width / 2 + 4) };

		yp = new int[] { (int) (y - ymap - height / 2 + 29),
				(int) (y - ymap - height / 2 + 29),
				(int) (y - ymap - height / 2 + 36) };
		
		// draws player
		if(isFacingRight())
			g.drawImage(img,(int) (x - getXmap() - width / 2), (int) (y - ymap - height / 2), width, height,null);
		else
			g.drawImage(img,(int) (x - getXmap() + width / 2), (int) ((y - ymap - height / 2)), -width, height, null);
		
		// draws jetpack
		g.setColor(Color.white);
		if (isFacingRight())
			g.fillRect((int) (x - getXmap() - width / 2 - 7), (int) (y - ymap
					- height / 2 + 5), 7, 24);
		else
			g.fillRect((int) (x - getXmap() + width / 2), (int) (y - ymap - height
					/ 2 + 5), 7, 24);
		
		// draws fire on jetpack
		g.setColor(Color.red);
		if (jets && energy > 0)
			g.fillPolygon(xp, yp, 3);
		
		//draw equipped item
		if (inv_blocks.size()>0) {
			BufferedImage tempItem = inv_blocks.get(equiped).getImg();
			g.drawImage(tempItem,(int) (x - getXmap()), (int) (y - ymap - 15),15*(isFacingRight()?1:-1),15,null);
		}
	}
	
	//////////////////////////////  GETTERS AND SETTERS  ///////////////////////////////////////////
	public double getFireHealth() {
		return fire;
	}

	public double getMaxFire() {
		return maxFire;
	}

	public void setFiring() {
		firing = true;
	}

	public void setGliding(boolean b) {
		gliding = b;
	}
	
	public boolean isSlashing() {
		return slashing;
	}

	public void setSlashing(boolean slashing) {
		this.slashing = slashing;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
}
