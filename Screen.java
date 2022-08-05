import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Screen extends JPanel implements KeyListener {

	//Tiles HashMap
	private Map<Coord,Tile> map = new HashMap<Coord,Tile>();

	//ArrayLists
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	//Projectile
	private Projectile projectile;

	//Player
	private Player p;

	//Timer
	private Timer t;

	//Booleans
	private boolean gameRunning;
	private boolean won = false;
	private boolean start = true;
	private boolean immortal = false;
	private boolean cheatsOn = false;

	//Threads
	private Thread playerThread;
	private Thread timerThread;
	private Thread projectileThread;
	private Thread[] enemyMovements;

	//Ints
	private int score = 0;
	private int lives = 3;
	private int reloadTimer = 0;
	
	//Music
	private Clip currentSongA;
	private Clip currentSongR;
	private Clip currentSongD;
	private Clip currentSong;

	//Constructor
	public Screen(){
		setLayout(null);
		setFocusable(true);
        addKeyListener(this);
		gameRunning = true;

		//Player
		p = new Player(new Coord(25,25));
		t = new Timer(50);

		//Setting up tiles
		for(int r = 0; r < 50; r++){
			for(int c = 0; c < 50; c++){
				map.put(new Coord(r,c), new Tile("water",false,false));
			}
		}

		//Terrain Gen
		for(int r = 0; r<50; r++) {
			for(int c = 0; c<50; c++) {
				int rand = (int)(Math.random()*55);
				if(rand == 0) {
					terrainGen("land", 8, r, c);
				} else if(rand >= 1 && rand <= 6) {
					terrainGen("water", 4, r, c);
				}
			}
		}

		//Creating treasures
		for(int i=0; i<30; i++) {
			createTreasures();
		}

		//Creating enemies
		for(int i = 0; i<40; i++) {
			createEnemies();
		}

		//Enemy Thread
		enemyMovements =  new Thread[enemies.size()];
		for(int i = 0; i<enemyMovements.length; i++) {
			enemyMovements[i] = new Thread(enemies.get(i));
			enemyMovements[i].start();
		}

		//Player Thread
		playerThread = new Thread(p);
		playerThread.start();

		timerThread = new Thread(t);
		timerThread.start();
		
		playSongR();
		currentSongR.stop();
		
		playSongD();
		currentSongD.stop();
		
		playSongA();
		currentSongA.stop();
		
		playSoundC();
	}

	//Restarting Game
	public void resetGame() {
		score = 0;
		lives = 3;
		enemies.clear();
		resetSongs();

		//Stopping enemies
		for(int i = 0; i<enemyMovements.length; i++) {
			enemyMovements[i].interrupt();
		}
		playerThread.interrupt();

		for(int r=0; r<50; r++) {
			for(int c=0; c<50; c++) {
				map.get(new Coord(r,c)).setEnemy(false);
				map.get(new Coord(r,c)).setTreasure(false);
			}
		}

		//Player
		p = new Player(new Coord(25,25));
		t = new Timer(50);

		//Setting up tiles
		for(int r = 0; r < 50; r++){
			for(int c = 0; c < 50; c++){
				map.put(new Coord(r,c), new Tile("water",false,false));
			}
		}

		//Terrain Gen
		for(int r = 0; r<50; r++) {
			for(int c = 0; c<50; c++) {
				int rand = (int)(Math.random()*55);
				if(rand == 0) {
					terrainGen("land", 8, r, c);
				} else if(rand >= 1 && rand <= 6) {
					terrainGen("water", 4, r, c);
				}
			}
		}

		//Creating treasures
		for(int i=0; i<30; i++) {
			createTreasures();
		}

		//Creating enemies
		for(int i = 0; i<40; i++) {
			createEnemies();
		}

		//Enemy Thread
		enemyMovements =  new Thread[enemies.size()];
		for(int i = 0; i<enemyMovements.length; i++) {
			enemyMovements[i] = new Thread(enemies.get(i));
			enemyMovements[i].start();
		}

		projectileThread = new Thread(projectile);

		//Player Thread
		playerThread = new Thread(p);
		playerThread.start();

		timerThread = new Thread(t);
		timerThread.start();
		
		currentSongA.start();

		won = false;
		immortal = false;
		cheatsOn = false;
		gameRunning = true;
		start = false;
	}

	//Terrain Generation
	public void terrainGen(String type, int maxDist, int x, int y) {
		Coord coord = new Coord(x, y);
		Tile t = new Tile(type,false,false);
		
		map.put(coord, t);
		int rand = (int)(Math.random()*maxDist);
		String newType = "";

		if(x != 0) { 
			coord.setX(x-1);
			newType = map.get(coord).getBackground();
		}
		if(x != 0 &&  rand != 0 && !newType.equals(type)) {
			terrainGen(type, maxDist-1, x-1, y);
		}

		if(x != 49) { 
			coord.setX(x+1);
			newType = map.get(coord).getBackground();
		}
		if(x != 49 && rand != 0 && !newType.equals(type)) {
			terrainGen(type, maxDist-1, x+1, y);
		}

		coord.setX(x);

		if(y != 0) { 
			coord.setY(y-1);
			newType = map.get(coord).getBackground();
		}
		if(y !=0 && rand != 0 && !newType.equals(type)) {
			terrainGen(type, maxDist-1, x, y-1);
		}

		if(y != 49) { 
			coord.setY(y+1);
			newType = map.get(coord).getBackground();
		}
		if(y != 49 && rand != 0 && !newType.equals(type)) {
			terrainGen(type, maxDist-1, x, y+1);
		}
	}
	
	//Creating Treasures
	public void createTreasures() {
		int randR = (int)(Math.random()*49);
		int randC = (int)(Math.random()*49);
		
		while(!map.get(new Coord(randR,randC)).getBackground().equals("land")) {
			randR = (int)(Math.random()*49);
			randC = (int)(Math.random()*49);
		}
		
		Coord newCoord = new Coord(randR,randC);
		if(map.get(newCoord).getBackground().equals("enemy") || map.get(newCoord).getBackground().equals("water") || map.get(newCoord).getBackground().equals("treasure") ) {
			createTreasures();
		}
		else {
			Tile t = new Tile(map.get(newCoord).getBackground(),false,true);
			map.put(newCoord,t);
		}
	}

	//Creating Enemies
	public void createEnemies() {

		int randX = (int)(Math.random()*48);
		int randY = (int)(Math.random()*48);
		while(randX>=22 && randX< 28 && randY>=22 && randY < 28 && randX>0 && randX<49 && randY>0 && randY<49) {
			randX = (int)(Math.random()*48);
			randY = (int)(Math.random()*48);
		}

		Coord newCoord = new Coord(randX,randY);
		if(map.get(newCoord).getBackground() != null) {
			Tile t = new Tile(map.get(newCoord).getBackground(),true,false);
			map.put(newCoord,t);
			enemies.add(new Enemy(map,newCoord,newCoord,true,map.get(newCoord).getBackground()));
		}
		else {
			createEnemies();
		}
	}

	//Dimensions of Panel
	public Dimension getPreferredSize() {
        return new Dimension(500,500);
	}

	//Play Bonk
	public void playSoundB() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/bonk.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}
	
	//Play Clash Royale
	public void playSoundC() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/clash_royale.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}
	
	//Play Yoda Death
	public void playSoundY() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/death_sound.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}
	
	//Resetting Songs
	public void resetSongs() {
		currentSongA.stop();
		currentSongD.stop();
		currentSongR.stop();
		
		currentSongA.setMicrosecondPosition(0);
		currentSongR.setMicrosecondPosition(0);
		currentSongD.setMicrosecondPosition(0);
	}
	
	//Play Vine Boom
	public void playSoundV() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/vine-boom.wav");
			currentSong = AudioSystem.getClip();
			currentSong.open(AudioSystem.getAudioInputStream(url));
			currentSong.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}
	
	//Play Among Us
	public void playSongA() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/among_us_music.wav");
			currentSongA = AudioSystem.getClip();
			currentSongA.open(AudioSystem.getAudioInputStream(url));
			currentSongA.loop(Clip.LOOP_CONTINUOUSLY);
			currentSongA.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}
	
	//Play Default Dance
	public void playSongD() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/fortnite.wav");
			currentSongD = AudioSystem.getClip();
			currentSongD.open(AudioSystem.getAudioInputStream(url));
			currentSongD.loop(Clip.LOOP_CONTINUOUSLY);
			currentSongD.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}
	
	//Play RickRoll
	public void playSongR() {
		try {
			URL url = this.getClass().getClassLoader().getResource("sound/rickroll.wav");
			currentSongR = AudioSystem.getClip();
			currentSongR.open(AudioSystem.getAudioInputStream(url));
			currentSongR.loop(Clip.LOOP_CONTINUOUSLY);
			currentSongR.start();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
	}

	//PaintComponent
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

		//Drawing Tiles
		for(int r = 0; r < 50; r++){
			for(int c = 0; c < 50; c++){
				map.get(new Coord(r,c)).drawMe(g,map.get(new Coord(r,c)).getBackground(),r*10,c*10);
			}
		}

		//Drawing Player
		Coord c = p.getPos();
		if(c.getX()<50 && c.getX()>=0 && c.getY()<50 && c.getY()>=0) {
			p.drawMe(g,map.get(c).getBackground());
		}

		//Drawing Projectile
		if(projectile != null) {
			projectile.drawMe(g);
		}

		//Drawing Text
		if(start) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 500, 500);
			g.setFont(new Font("Serif", Font.BOLD, 12));
			g.setColor(Color.WHITE);
			g.drawString("Press any key to start!", 200,200);
			g.drawString("Press SPACE to Shoot", 200, 300);
			g.drawString("Use WASD or Arrows to move", 175, 350);
			g.drawString("Press P to turn on cheats, L to turn off cheats, and K to die", 100,400);
			g.drawString("Get 20 treasures before the time reaches 0 and you win!",110,450);
		}
		else if(gameRunning && !won) {
			g.setFont(new Font("Serif", Font.BOLD, 12));
			g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 50, 50);
			g.drawString("Lives: " + lives, 50, 70);
			g.drawString(p.getPos().toString(),50,110);
			t.drawMe(g);
		}
		else if(!gameRunning && !start) {
			if(!won) {
				g.setFont(new Font("Serif", Font.BOLD, 12));
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 500, 500);
				g.setColor(Color.WHITE);
				g.drawString("GAME OVER", 220, 375);
			}
			else if(won) {
				g.setFont(new Font("Serif", Font.BOLD, 12));
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 500, 500);
				g.setColor(Color.WHITE);
				g.drawString("YOU WIN", 220, 375);
			}

			g.setFont(new Font("Serif", Font.BOLD, 12));
			g.setColor(Color.YELLOW);
			g.drawString("Score: " + score, 340, 420);
			g.setColor(Color.WHITE);
			g.drawString("Press SPACE to play again", 300, 450);
		}
	}

	//Animate
	public void animate() {
		while(true){ 
			synchronized(this) {
				if(gameRunning) {

					//Checking Projectile Collision
					if(projectile != null && map.get(projectile.getPos()) != null && map.get(projectile.getPos()).isEnemy()) {
						map.get(projectile.getPos()).getCurrEnemy().kill();
						projectile = null;
						projectileThread.interrupt();
						playSoundB();
					}

					//Checking Enemy Collision
					if(!immortal && !start) {
						Tile currentTile = map.get(p.getPos());
						if(p.getPos() != null && currentTile != null) {
							if(currentTile.isEnemy()) {
								lives-=1;
								map.get(p.getPos()).getCurrEnemy().reset();
								t.addTime(3);
								p.reset();
								playSoundY();
							}
						}
					}

					// Checking Treasure Collision
					if(map.get(p.getPos()) != null && map.get(p.getPos()).isTreasure()) {
						map.get(p.getPos()).setTreasure(false);
						score+=1;
						playSoundV();
					}

					//Stopping enemies
					if(lives <=0 && gameRunning) {
						for(int i = 0; i<enemyMovements.length; i++) {
							enemyMovements[i].interrupt();
						}
						playerThread.interrupt();
						
						gameRunning = false;
						currentSongA.stop();
						playSongR();
					}

					//Checking if projectiles go off the map
					if(projectile != null && (projectile.getPos().getX()>50 || projectile.getPos().getX()<0 || projectile.getPos().getY()>50 || projectile.getPos().getY()<0)) {
						projectileThread.interrupt();
						projectile = null;
					}

					//Time between each shot
					if(reloadTimer<150) {
						reloadTimer++;
					}

					//Stop game if time is zero
					if(t.getTime() == 0 && gameRunning) {
						gameRunning = false;
						currentSongA.stop();
						playSongR();
					}
					
					//Win if score >=25
					if(score >= 25 && gameRunning) {
						won = true;
						gameRunning = false;
						currentSongA.stop();
						playSongD();
					}

					repaint();
				}
			}

		try {
            Thread.sleep(10);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
		}
	}

	//KeyListener
	public void keyPressed(KeyEvent e) {

		//Right (right arrow or d
		if(e.getKeyCode() == 68 || e.getKeyCode() == 39){
			p.move("right");
		}

		//Left (left arrow or a)
		if(e.getKeyCode() == 65 || e.getKeyCode() == 37){
			p.move("left");
		}

		//Up (up arrow or w)
		if(e.getKeyCode() == 87 || e.getKeyCode() == 38){
			p.move("up");
		}

		//Down (down arrow or s)
		if(e.getKeyCode() == 83 || e.getKeyCode() == 40){
			p.move("down");
		}

		//Shoot Key (space)
		if(e.getKeyCode() == 32) {
			if(gameRunning && !start) {
				if(reloadTimer == 150) {
					projectile = new Projectile(map,p.getPos(),p.getDirection());
					projectileThread = new Thread(projectile);
					projectileThread.start();
					reloadTimer = 0;
				}
			}
			else {
				resetGame();
			}
		}

		//Cheat key (p)
		if(e.getKeyCode() == 80){
			if(!cheatsOn) {
				immortal = true;
				t.setTimer(false);
				cheatsOn = true;
				playSoundV();
			}
		}

		//AntiCheat (l)
		if(e.getKeyCode() == 76) {
			if(cheatsOn) {
				immortal = false;
				double curTime = t.getTime();
				t = new Timer(curTime);
				timerThread = new Thread(t);
				timerThread.start();
				cheatsOn = false;
				playSoundB();
			}
		}

		//Kill (k)
		if(e.getKeyCode() == 75) {
			gameRunning = false;

			resetSongs();
			currentSongR.start();
		}

		//Win (z)
		if(e.getKeyCode() == 90) {
			won = true;
			gameRunning = false;
			resetSongs();
			currentSongD.start();
		}

		//Any key starts game
		if(start) {
			start = false;
			playSongA();
		}

		repaint();
	}

	//Extra Stuff
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
