package gameState;

import java.awt.event.MouseEvent;

public abstract class GameState {				// super class for all game states (world, menus...)
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
}
