package location;

import gameState.GameHandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import utilites.Button;

public abstract class Location {
	protected ArrayList<Location> links = new ArrayList<Location>();
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	protected String name;
	public abstract void init();
	public abstract void update();
	public abstract void load();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void mousePressed(MouseEvent e, GameHandler g);
	public void mouseEntered(MouseEvent e) {}
}
