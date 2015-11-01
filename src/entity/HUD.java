package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.Game;

public class HUD {
	private Player player;
	private Color fade_black;
	private Font font;
	
	public HUD(Player p){							// constructor
		player = p;
		font = new Font("Arial",Font.PLAIN, 14);
		fade_black = new Color(0,0,0,100);
	}
	
	public void draw(Graphics2D g){										// draws hud
		// draws black rectangles
		g.setColor(fade_black);
		g.fillRect(110, Game.HEIGHT-37-195, Game.WIDTH-118, 25);
		g.fillRect(10, Game.HEIGHT-234, 90, 15);
		g.fillRect(10, Game.HEIGHT-215, 90, 15);
		
		// draws energy and  health
		g.setColor(Color.RED);
		g.fillRect(11, Game.HEIGHT-233, (int) (88*(player.getHealth()/player.getMaxHealth())), 13);
		g.setColor(Color.BLUE);
		g.fillRect(11, Game.HEIGHT-214, (int) (88*(player.getEnergy()/player.getMaxEnergy())), 13);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf((int)player.getHealth())+"/"+String.valueOf((int)player.getMaxHealth()),42, Game.HEIGHT-224);
		g.drawString(String.valueOf((int)player.getEnergy())+"/"+String.valueOf((int)player.getMaxEnergy()),42, Game.HEIGHT-205);
		
		// draws inventory bar
		for(int x=0; x<player.inv_blocks.size() &&x<8; x++){
			if(x==player.equiped){								//pink rect
				g.setColor(new Color(255,192,203,200));
				g.fillRect(115+(x*25)-3,Game.HEIGHT-228-3,23,23);
			}
			
			// draws item hotbar
			g.setColor(Color.WHITE);
			player.inv_blocks.get(x).draw(g,  115+(x*25)+5, Game.HEIGHT-22-195-15);
			g.drawString(String.valueOf(player.each_inv.get(x)), 115+(x*25)+12, Game.HEIGHT-22-200);
			
		}
	}
}
