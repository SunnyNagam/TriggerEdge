package errorWin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.Game;

public class ErrorContent extends JPanel implements Runnable{	
	private Thread thread;
	public static final int WIDTH = 500;
	public static final int HEIGHT = 200;
	public TextArea textArea;
	//Image
	private BufferedImage image;
	private Graphics2D g;
	
	private static String message = "";

	public ErrorContent(String message){							//Game constructor
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		ErrorContent.message = message;
	}

	public void addNotify(){				//Declares parent status and adds listeners, required
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void run() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setColor(Color.WHITE);							//Draws background - TEMPORARY
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		textArea = new TextArea(message, 10, 50);
		textArea.setBounds(0, 0, WIDTH, HEIGHT);
		add(textArea);						//Add it to the screen
		setLayout(new BorderLayout());		//Required
		textArea.setEditable(false);		//Preventing the box from being editable
		textArea.setFont(new Font("Serif", Font.PLAIN, 19));//Setting font
	}
}
