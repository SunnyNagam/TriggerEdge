package utilites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Button {							// button to use for menus
	String text;
	private int x,y,width,height;
	
	public Button(String text, int x, int y, int height, int width){		// constructor
		this.text = text;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	
	public boolean containsPoint(int x1, int y1){				// checks if button is clicked given coordinates of mouse
		if(x1>=x && x1<=x+width)
			if(y1>=y &&y1 <=y+height)
				return true;
		return false;
	}

	public void draw(Graphics2D g) {									// draws text of button
		g.setColor(Color.BLACK);
		g.drawString(text, this.x, this.y+height);
	}

	public void setText(String text) {								// sets text of button
		this.text = text;
		
	}
	
}
