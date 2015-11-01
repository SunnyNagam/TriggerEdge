package location;

import java.awt.Color;
import java.awt.Graphics2D;

public class Tutorial extends Location{
	
	public Tutorial(){
		name = "Tutorial";
	}
	
	public void load(){
		Location e = new Room1();
		links.add(e);
	}
	@Override
	public void init() {
		
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		for(int x=0; x<links.size(); x++){
			g.drawRect(10+100*x, 100, 100, 100);
			g.drawString(links.get(x).name, 10+100*x+10, 150);
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
