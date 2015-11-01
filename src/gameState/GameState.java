package gameState;

import java.awt.event.MouseEvent;

// Super class for all game states (world, menus...)
// Game states dictate what the game does
public abstract class GameState {				
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
}
