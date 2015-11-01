package main;
import javax.swing.JFrame;
public class GameScreen {
	public static void main(String[] args){
		
		JFrame window = new JFrame("TriggerEdge");				// jframe widow
		
		window.setContentPane(new Game());						// content of window
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		window.setResizable(false);								// un-expandable
		window.pack();
		window.setVisible(true);				
	}
}