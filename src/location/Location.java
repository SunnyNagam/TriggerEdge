package location;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class Location {
	protected ArrayList<Location> links = new ArrayList<Location>();
	protected String name;
	public abstract void init();
	public abstract void update();
	public abstract void load();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
}
