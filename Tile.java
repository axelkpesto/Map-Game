import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Tile {
	
	//BackGround
	private String backGround;
	
	//Colors
	private Color green = new Color(0,154,29);
	private Color blue = new Color(0,157,196);

	//Boolean
	private boolean enemy;
	private boolean treasure;
	
	//Images
	private Image waterEnemyImage;
	private Image landEnemyImage;
	
	//Objects
	private Enemy currEnemy = null;
	
	//Constructor
	public Tile(String backGround, boolean enemy, boolean treasure) {
		this.backGround = backGround;
		this.enemy = enemy;
		this.treasure = treasure;
		
		
		//water
		float h = 0.533333333333f;
		float s = 1f;
		float b = .77f;
		
		b += (float)Math.random()/5 -0.1;
		s -= (float)Math.random()/8;
		
		blue = new Color(Color.HSBtoRGB(h,s,b));
		
		
		//grass
		h = 0.3638888888889f;
		s = 1f;
		b = 0.6f;
		
		b += (float)Math.random()/5 -0.1;
		s -= (float)Math.random()/8;

		green = new Color(Color.HSBtoRGB(h,s,b));
		
		
		try {
			waterEnemyImage = new ImageIcon("images/Water_Enemy.png").getImage();
			landEnemyImage = new ImageIcon("images/Land_Enemy.gif").getImage();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	//Getting BackGround
	public String getBackground() {
		return backGround;
	}

	//Getting if Enemy
	public boolean isEnemy() {
		return enemy;
	}

	//Setting if Enemy
	public void setEnemy(boolean e) {
		enemy = e;
	}

	public void setCurrEnemy(Enemy e) {
		this.currEnemy = e;
	}

	public Enemy getCurrEnemy() {
		return currEnemy;
	}
	
	//Getting if Treasure
	public boolean isTreasure() {
		return treasure;
	}
	
	//Setting if Treasure
	public void setTreasure(boolean t) {
		treasure = t;
	}
	
	//Drawing
	public void drawMe(Graphics g, String s, int x, int y) {
		if(s.equals("water")) {
			g.setColor(blue);
			g.fillRect(x,y,10,10);
		}
		else if(s.equals("land")) {
			g.setColor(green);
			g.fillRect(x,y,10,10);
		}
		if(treasure){
			g.setColor(Color.yellow);
			g.fillRect(x+3, y+3, 5, 5);
		}
		if(enemy) {
			if(backGround.equals("land")) {
				g.drawImage(landEnemyImage,x,y,null);
			}
			else if(backGround.equals("water")) {
				g.drawImage(waterEnemyImage,x,y,null);
			}
		}
		g.setColor(Color.BLACK);
		g.drawRect(x,y,10,10);
	}
}