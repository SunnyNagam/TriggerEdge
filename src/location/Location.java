package location;

import gameState.GameHandler;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import main.Game;
import utilites.Button;

public abstract class Location {
	protected ArrayList<Location> links = new ArrayList<Location>();
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	protected String name;
	public abstract void init();
	public abstract void update();
	public abstract void load();
	public void draw(java.awt.Graphics2D g){
		g.setColor(Color.BLACK);
		g.drawString("Current Room: "+name,Game.WIDTH/2-100, 50);
		for(int x=0; x<buttons.size(); x++){
			buttons.get(x).draw(g);
		}
	}
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public void mousePressed(MouseEvent e, GameHandler g){
		for(int i=0; i<buttons.size(); i++){
			if(buttons.get(i).containsPoint((int)e.getX(), (int)e.getY())){
				if(e.getButton()==1){
					g.setLocation(links.get(i));
				}
				break;
			}
		}
	}
	public void mouseEntered(MouseEvent e) {}
}
