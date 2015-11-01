package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import location.Location;
import location.Tutorial;
import main.Game;

public class GameHandler extends GameState{
	// This is the location that the game handler 'handles' and is the current room that the player is in
	private Location location;				
	// Stores the name of the file where the location's data is saved (blockmap)
	String file;							
	
	public GameHandler(String file, boolean newGame){
		System.out.println("in gamehandler constructor");
		this.file = file;
		if (newGame){	
			location = new Tutorial();							// Creating a new instance of a room from tutorial, as it is the first room that exists when starting a new game
			location.load();									// Establishing the links to the room
		}
		else{
			//TODO read from save to get current location and set it to var 'location'
			
		}
	}
	
	public void init() {
		
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		location.draw(g);
	}

	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent e) {
		location.mousePressed(e, this);
		
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		location.load();
	}

}
