import java.util.*;

public class Enemy implements Runnable {
	
	//Coordinates
	private Coord c;
	private Coord originalSpot;
	
	//Booleans
	private boolean alive;
	
	//Strings
	private String background;
	
	//HashMap
	private Map<Coord,Tile> map;
	
	//Constructor
	public Enemy(Map<Coord, Tile> map, Coord c, Coord originalSpot, boolean alive, String background) {
		this.c = c;
		this.originalSpot = originalSpot;
		this.alive = alive;
		this.background = background;
		this.map = map;
		map.get(c).setCurrEnemy(this);
	}
	
	//Getting Position
	public Coord getPos() {
		return c;
	}

	//Getting if Alive
	public boolean isAlive() {
		return alive;
	}

	//Setting Alive
	public void setAlive(boolean a) {
		alive = a;
	}
	
	//Resetting Enemy
	public void reset() {
		map.get(new Coord(c.getX(), c.getY())).setEnemy(false);
		c = new Coord(originalSpot.getX(), originalSpot.getY());
		map.get(new Coord(originalSpot.getX(), originalSpot.getY())).setEnemy(true);
	}

	//Moving Enemy
	public void move() {
		try{
			int rand = (int)(Math.random()*4);
			if(alive) {
				if(rand == 0) {
					if(c.getY() - 1 > 1 && map.get(new Coord(c.getX(), c.getY() - 1)).getBackground().equals(background) && !map.get(new Coord(c.getX(), c.getY() - 1)).isEnemy()) {
						map.get(c).setEnemy(false);
						map.get(c).setCurrEnemy(null);
						c = new Coord(c.getX(), c.getY() - 1);
						map.get(c).setEnemy(true);
						map.get(c).setCurrEnemy(this);
					}
					else {
						move();
					}
				}
				if(rand == 1) {
					if(c.getY() + 1 < 50 && map.get(new Coord(c.getX(), c.getY() + 1)).getBackground().equals(background) && !map.get(new Coord(c.getX(), c.getY() + 1)).isEnemy()) {
						map.get(c).setEnemy(false);
						map.get(c).setCurrEnemy(null);
						c = new Coord(c.getX(), c.getY() + 1);
						map.get(c).setEnemy(true);
						map.get(c).setCurrEnemy(this);
					}
					else {
						move();
					}
				}
				if(rand == 2) {
					if(c.getX() - 1 > 1 && map.get(new Coord(c.getX() - 1, c.getY())).getBackground().equals(background) && !map.get(new Coord(c.getX() - 1, c.getY())).isEnemy()) {
						map.get(c).setEnemy(false);
						map.get(c).setCurrEnemy(null);
						c = new Coord(c.getX() - 1, c.getY());
						map.get(c).setEnemy(true);
						map.get(c).setCurrEnemy(this);
					}
					else {
						move();
					}
				}
				if(rand == 3) {
					if(c.getX() + 1 < 50 && map.get(new Coord(c.getX() + 1, c.getY())).getBackground().equals(background) && !map.get(new Coord(c.getX() + 1, c.getY())).isEnemy()) {
						map.get(c).setEnemy(false);
						map.get(c).setCurrEnemy(null);
						c = new Coord(c.getX() + 1, c.getY());
						map.get(c).setEnemy(true);
						map.get(c).setCurrEnemy(this);
					}
					else {
						move();
					}
				}
			}
        }
		
		//Killing if stackoverflow
        catch(StackOverflowError e){
			kill();
        }
	}

	//Required Run Method
	@Override
	public void run() {

		Thread t = Thread.currentThread();

		while(alive) {

			move();

			sleep(400);

			if(t.isInterrupted()) {
				break;
			}
		}
	}

	//Sleeping
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	//Killing an Enemy
	public void kill() {
		map.get(c).setEnemy(false);
		map.get(c).setCurrEnemy(null);
		alive = false;
	}
	
	public String toString() {
		return "Enemy Coordinates; " + c;
	}
}