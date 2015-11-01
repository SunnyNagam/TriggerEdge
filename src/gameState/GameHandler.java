package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import location.Location;
import location.Tutorial;
import main.Game;

public class GameHandler extends GameState{
	private Location location;
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		location.load();
	}

	String file;
	
	public GameHandler(String file, boolean newGame){
		System.out.println("in gamehandler constructor");
		this.file = file;
		if(newGame){
			location = new Tutorial();
			location.load();
		}
		else{
			//read from save to get current location and set it to var 'location'
		}
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		location.draw(g);
		
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		location.mousePressed(e, this);
		
	}

}
