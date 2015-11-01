package location;

import gameState.GameHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import utilites.Button;

public class Room2 extends Location{
	
	public Room2(){
		name = "Room2";
	}
	@Override
	public void init() {
		
	}
	
	public void load() {
		// TODO Auto-generated method stub
		links.add(new Room1());
		for(int x=0; x<links.size(); x++){
			buttons.add(new Button(links.get(x).name,10+250*x,200,250,200));
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
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
	public void mousePressed(MouseEvent e, GameHandler g) {
		super.mousePressed(e, g);
		
	}

}
