import java.awt.Graphics;
import java.awt.Color;
import java.util.*;

public class Projectile implements Runnable {
	
	//Coordinates
	private Coord c;
	
	//Strings
	private String direction;

	//Colors
	private Color pink;

	//Player

	//Booleans
	private boolean connected = false;

	//Constructor
	public Projectile(Map<Coord, Tile> map, Coord c, String direction) {
		this.c = c;
		this.direction = direction;

		pink = new Color(255,192,203);
	}
	
	//Moving Projectile
	public void move() {
		try {
			if(!connected) {
				if(direction.equals("up")) {
					c = new Coord(c.getX(), c.getY() - 1);
				}
				if(direction.equals("down")) {
					c = new Coord(c.getX(), c.getY() + 1);
				}
				if(direction.equals("left")) {
					c = new Coord(c.getX() - 1, c.getY());
				}
				if(direction.equals("right")) {
					c = new Coord(c.getX() + 1, c.getY());
				}
			}
			while(c.getX()<50 && c.getX()>=0 && c.getY()<50 && c.getY()>=0) {
				sleep(45);
				move();
			}
		}
		catch(StackOverflowError e){}
	}

	//Required Run Method
	@Override
	public void run() {
		Thread t = Thread.currentThread();
		while(!connected) {
			try {
				move();
			}
			catch(StackOverflowError e){}

			sleep(1);

			if(t.isInterrupted()) {
				break;
			}
		}
	}

	//Draw Bullet
	public void drawMe(Graphics g) {
		if(!connected) {
			g.setColor(pink);
			g.fillOval(c.getX()*10+2,c.getY()*10+2,5,5);
		}
	}

	//Sleep
	public void sleep(int ms) {
		try {
			Thread.sleep(ms); //16
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	//Getting Coordinate
	public Coord getPos() {
		return c;
	}
}