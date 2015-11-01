package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.Game;
import utilites.Button;

public class SaveMenu extends GameState{		// save menu
	
	private int curChoice = 0;	
	// buttons
	private Button[] options = { new Button("New Save",70,100,30,60), new Button("New Save",70,130,30,60), new Button("New Save",70,160,30,60), new Button("< Back",5,215,20,70)};
	// folder containing saves
	private File folder = new File("src/SaveFiles");
	// all the files in the folder
	private File[] listFiles = folder.listFiles();
	// directorys of save files
	private String[] optionDir = new String[options.length];
	private boolean load_scr=false;			// display "loading" screen
	
	public SaveMenu(Game g){			// constructor
		for(int x=0; x<options.length; x++){						// gets saves from folder and sets directorys
			if(x<listFiles.length)
				optionDir[x] = new String("src/"+folder.getName()+"/"+listFiles[x].getName());
			else
				optionDir[x] = new String("src/"+folder.getName()+"/"+"This doesent exist fool");
		}
	}

	
	public void init() {}
	
	public void update() {}
	
	public void draw(Graphics2D g) {		// draws menu
		g.setColor(Color.WHITE);		// draws background
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setColor(Color.BLACK);		// sets text color and font
		g.setFont(new Font("Tahoma",Font.BOLD,28));
		
		if(load_scr)					// displays loading screen
			g.drawString("LOADING", 75, 70);
		else{							// displays saves 
			g.drawString("Choose your save:", 35, 70);

			g.setFont(new Font("Tahoma",Font.BOLD,18));


			for(int i=0; i < options.length-1; i++) {
				if ((new File(optionDir[i]).exists()))
					options[i].setText(optionDir[i].split("/")[2]);
				else
					options[i].setText("Create New Save");
				options[i].draw(g);
			}
			options[options.length-1].draw(g);
		}
	}
	
	private void select(int selected){			// loads selected game save or creates new save
		load_scr = true;
		for(int y = 0; y < options.length-1; y++) {
			if(selected == y && new File(optionDir[y]).exists()) {
				Game.gameStates.add(new GameHandler(optionDir[y], false));
				Game.setCurrentState(Game.getCurrentState()+1);	//Change game state to that world
			}
			else if (selected == y && selected != options.length-1) {		// create save
				System.out.println("joasidjfisdfj");
				String newSave;
				for(int x=0; ;x++)
					if(!new File("src/SaveFiles/DefaultName"+String.valueOf(x)).exists()){
						newSave = "src/SaveFiles/DefaultName"+String.valueOf(x);
						break;
					}
				optionDir[selected] = newSave;
				PrintWriter output = null;
				try {
					output = new PrintWriter(newSave);
				}
				catch (FileNotFoundException e) {
					System.out.println("Save creation failed!");
					e.printStackTrace();
				}
				Game.gameStates.add(new GameHandler(newSave, true));
				Game.setCurrentState(Game.getCurrentState()+1);	
				output.close();
				break;
			}
		}
		if (selected == options.length-1)									// back button
			Game.setCurrentState(Game.getCurrentState()-1);			// goes back to main menu
	}
	
	public void keyPressed(int k) {					// key controls for menu
		if(k==KeyEvent.VK_ENTER)
			select(curChoice);
		if(k==KeyEvent.VK_UP){
			curChoice--;
			if(curChoice == -1)
				curChoice = options.length -1;
		}
		
		if(k==KeyEvent.VK_DOWN){
			curChoice++;
			if(curChoice==options.length)
				curChoice = 0;
		}
	}
	
	public void keyReleased(int k) {}
	
	public void mousePressed(MouseEvent e) {					// mouse controls for save menu
		for(int i=0; i<options.length; i++){
			if(options[i].containsPoint((int)e.getX()/2, (int)e.getY()/2)){
				if(e.getButton()==1)
					select(i);
				else
					try {
						Path p = Paths.get(optionDir[i]);
					    Files.delete(p);
					    optionDir[i] = "New Save";
					} 
					catch (Exception a) {
					    a.printStackTrace();
					}
				break;
			}
		}
	}
}
