package main;

import gameState.GameState;
import gameState.Menu;
import gameState.SaveMenu;
//import gameState.World;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Game extends JPanel implements Runnable, KeyListener, MouseListener{

	// Dimensions of game screen
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 600;
	
	// Game thread
	private Thread thread;
	private boolean running; 
	private int FPS = 60;
	private long targetTime = 1000/FPS; 
	
	// Image variables
	private BufferedImage image;
	private Graphics2D g;
	
	// Creating the gamestates that the game will run in
	public static ArrayList<GameState> gameStates = new ArrayList<GameState>();    // Creates arryList to keep track of gamestates
	private static int currentState = 0;
	public static final int MENUSTATE = 0;
	public static final int SAVEMENUSTATE = 1;
	public static final int WORLDSTATE = 2;
	
	public Game(){
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	// Declares parent status and adds listeners
	public void addNotify(){				
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}
	
	private void init(){							
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gameStates.add(new Menu(this));			// Initializes game states
		gameStates.add(new SaveMenu(this));
	}
	
	public void run(){
		init();
		
		//Vars to keep track of game's run times
		long start, elapsed, wait;					
		while(running){
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed/1000000;
			if(wait <0) wait = 5;
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void update() {								// updates current game state
		gameStates.get(getCurrentState()).update();
	}
	
	private void draw() {								// draws current game state
		gameStates.get(getCurrentState()).draw(g);
	}
	
	private void drawToScreen() {						// scales and draws game with formating
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH,  HEIGHT, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	
	public void keyPressed(KeyEvent key) {
		gameStates.get(getCurrentState()).keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {
		gameStates.get(getCurrentState()).keyReleased(key.getKeyCode());
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		gameStates.get(getCurrentState()).mousePressed(e);
		System.out.println(e.getX()/2+", "+e.getY()/2);
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {
		gameStates.get(getCurrentState()).mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {}
	

//////////////////////////  GETTERS AND SETTERS  //////////////////////////////	
	public static int getCurrentState() {
		return currentState;
	}

	// Function that allows other classes and the current state to change the state and move the game to the next state
	public static void setCurrentState(int currentState) {
		Game.currentState = currentState;
	}

	public static void setGameStates(GameState e) {
		gameStates.add(e);
	}

	public static GameState getGameStates(int x) {
		return gameStates.get(x);
	}

	public static void setGameStates(ArrayList<GameState> gameStates) {
		Game.gameStates = gameStates;
	}
}
