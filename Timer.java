import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Timer implements Runnable {
	
	//Double
	private double d = 0;
	
	//Booleans
	private boolean on;
	
	//Constructor
	public Timer(double d) {
		this.d = d;
		
		on = true;
	}
	
	//Drawing
	public void drawMe(Graphics g) {
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.setColor(Color.WHITE);
		g.drawString("Time: " + String.format("%.2f%n",d), 50, 90);
	}
	
	//Changing Time
	public void changeTime() {
		if(d>0) {
			d-=.05;
		}
	}
	
	//Required Run Method
	@Override
	public void run() {

		Thread thisThread = Thread.currentThread();

		while(on) {

			changeTime();

			sleep(50);
			
			if(thisThread.isInterrupted()) {
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
	
	//Getting Time
	public double getTime() {
		return Math.round(d);
	}
	
	//Setting Timer
	public void setTimer(boolean status) {
		on = status;
	}
	
	//Adding Time
	public void addTime(double t) {
		d += t;
	}
}