package errorWin;

import javax.swing.JFrame;

public class ErrorScreen{
	
	public ErrorScreen(String message){
		
		JFrame window = new JFrame("ERfROR");					//Creates the window
		
		window.setContentPane(new ErrorContent(message));		//What the Window will display (the components)
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Closes	
		window.setResizable(false);								//Prevents window from being resized
		window.pack();											//Sets the window sized according to its components
		window.setVisible(true);								//Makes window visible
	}
}
