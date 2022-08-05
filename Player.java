import java.awt.Graphics;
import java.awt.Color;

public class Player implements Runnable {
	
	//Coordinate
	private Coord position;

	//Color
	private Color black;
	private Color brown;
	private Color white;

	//Strings
	private String direction = "";
	private String lastDirection = "up";

	//Constructor
	public Player(Coord start) {
		this.position = start;

		black = new Color(0,0,0);
		white = new Color(255,255,255);
		brown = new Color(150,75,0);
	}

	//Getting Coordinate
	public Coord getPos() {
		return position;
	}

	//Getting Direction
	public String getDirection() {
		return lastDirection;
	}

	//Move
	public void move(String s) {
		direction = s;
	}
	
	//Required Run Method
	@Override
	public void run() {

		Thread thisThread = Thread.currentThread();

		while(true) {

			if(direction.equals("up")) {
				position = new Coord(position.getX(), position.getY() - 1);
				lastDirection = "up";
				direction = "";
			}
			if(direction.equals("down")) {
				position = new Coord(position.getX(), position.getY() + 1);
				lastDirection = "down";
				direction = "";
			}
			if(direction.equals("left")) {
				position = new Coord(position.getX() - 1, position.getY());
				lastDirection = "left";
				direction = "";
			}
			if(direction.equals("right")) {
				position = new Coord(position.getX() + 1, position.getY());
				lastDirection = "right";
				direction = "";
			}
			
			//Resetting Player if off map
			if(position.getX()>49) {
				sendTo("left");
			}
			
			if(position.getX()<0) {
				sendTo("right");
			}
			
			if(position.getY()<0) {
				sendTo("bot");
			}
			
			if(position.getY()>49) {
				sendTo("top");
			}
			try {
				Thread.sleep(150);
			} catch(InterruptedException ex) {
				thisThread.interrupt();
			}
			if(thisThread.isInterrupted()) {
				break;
			}
		}
	}
	
	//Reset
	public void reset() {
		position = new Coord(25,25);
	}
	
	//Sending to sides
	public void sendTo(String s) {
		if(s.equals("top")) {
			position = new Coord(position.getX(),0);
		}
		if(s.equals("bot")) {
			position = new Coord(position.getX(),49);
		}
		if(s.equals("left")) {
			position = new Coord(0,position.getY());
		}
		if(s.equals("right")) {
			position = new Coord(49,position.getY());
		}
	}

	//Drawing Player
	public void drawMe(Graphics g, String s) {
		if(s.equals("water")) {
			g.setColor(brown);
			g.fillRect(position.getX()*10,position.getY()*10,10,10);
			g.setColor(white);
			g.fillRect(position.getX()*10+2,position.getY()*10+2,6,6);
			g.setColor(black);
			if(lastDirection.equals("down")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+9, position.getX()*10+5};
				int[] yPoints = {position.getY()*10+1, position.getY()*10+1, position.getY()*10+9};
				g.fillPolygon(xPoints,yPoints,3);
			}
			else if(lastDirection.equals("up")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+9, position.getX()*10+5};
				int[] yPoints = {position.getY()*10+9, position.getY()*10+9, position.getY()*10+1};
				g.fillPolygon(xPoints,yPoints,3);
			}
			else if(lastDirection.equals("left")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+9, position.getX()*10+9};
				int[] yPoints = {position.getY()*10+5, position.getY()*10+1, position.getY()*10+9};
				g.fillPolygon(xPoints,yPoints,3);
			}
			else if(lastDirection.equals("right")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+1, position.getX()*10+9};
				int[] yPoints = {position.getY()*10+1, position.getY()*10+9, position.getY()*10+5};
				g.fillPolygon(xPoints,yPoints,3);
			}
		}
		if(s.equals("land")) {
			g.setColor(brown);
			g.fillRect(position.getX()*10,position.getY()*10,10,10);
			g.setColor(black);
			if(lastDirection.equals("down")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+9, position.getX()*10+5};
				int[] yPoints = {position.getY()*10+1, position.getY()*10+1, position.getY()*10+9};
				g.fillPolygon(xPoints,yPoints,3);
			}
			else if(lastDirection.equals("up")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+9, position.getX()*10+5};
				int[] yPoints = {position.getY()*10+9, position.getY()*10+9, position.getY()*10+1};
				g.fillPolygon(xPoints,yPoints,3);
			}
			else if(lastDirection.equals("left")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+9, position.getX()*10+9};
				int[] yPoints = {position.getY()*10+5, position.getY()*10+1, position.getY()*10+9};
				g.fillPolygon(xPoints,yPoints,3);
			}
			else if(lastDirection.equals("right")) {
				int[] xPoints = {position.getX()*10+1, position.getX()*10+1, position.getX()*10+9};
				int[] yPoints = {position.getY()*10+1, position.getY()*10+9, position.getY()*10+5};
				g.fillPolygon(xPoints,yPoints,3);
			}
		}
	}
}