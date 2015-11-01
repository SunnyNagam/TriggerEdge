package location;

import java.awt.Color;
import java.awt.Graphics2D;

public class Room2 extends Location{
	
	public Room2(){
		name = "Room2";
	}
	@Override
	public void init() {
		
	}
	
	public void load() {
		// TODO Auto-generated method stub
		links.add(new Tutorial());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		for(int x=0; x<links.size(); x++){
			g.drawRect(10+200*x, 100, 300, 100);
			g.drawString(links.get(x).name, 10+200*x+10, 150);
		}
		
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
