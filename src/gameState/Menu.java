package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import utilites.Button;
import main.Game;

public class Menu extends GameState{			// main menu
	
	private int curChoice = 0;
	// buttons
	private Button[] options = { 
			new Button("Start",290,280,30,120), 
			new Button("Help", 290,330,30,120), 
			new Button("Quit", 290,370,30,120)
	};
	
	public Menu(Game g){		// constructor
	}
	
	public void init() {}
	
	public void update() {}
	
	public void draw(Graphics2D g) {					// draws main menu
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Tahoma",Font.BOLD,56));
		g.drawString("TriggerEdge", 160, 140);
		
		g.setFont(new Font("Tahoma",Font.BOLD,36));
		for(int i=0; i<options.length; i++)
			options[i].draw(g);
	}
	
	private void select(int x){							// selects option
		if(x==0)
			Game.setCurrentState(Game.getCurrentState()+1);
		else if(x==1)								// help screen todo
			;
		else
			System.exit(0);	
	}
	public void keyPressed(int k) {							// key controls for the  menu
		if(k==KeyEvent.VK_ENTER)
			select(curChoice);
		if(k==KeyEvent.VK_UP){
			curChoice--;
			if(curChoice==-1)
				curChoice = options.length -1;
		}
		
		if(k==KeyEvent.VK_DOWN){
			curChoice++;
			if(curChoice==options.length)
				curChoice = 0;
		}
	}
	
	public void keyReleased(int k) {}
	
	public void mousePressed(MouseEvent e) {											// mouse controls for buttons
		System.out.print(e.getX()+", "+e.getY()+" ");
		for(int i=0; i<options.length; i++){
			if(options[i].containsPoint((int)e.getX(), (int)e.getY())){
				select(i);
				break;
			}
		}
	}
}
