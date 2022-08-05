public class Coord {
	
	//Integers
	private int x;
	private int y;
	
	//Constructor
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//Getting X
	public int getX() {
		return x;
	}
	
	//Getting Y
	public int getY() {
		return y;
	}
	
	//Setting X
	public void setX(int x) {
		this.x = x;
	}
	
	//Setting Y
	public void setY(int y) {
		this.y = y;
	}
	
	//HashCode
	@Override
	public int hashCode() {
		int hashCode = (x*100) + y;
		return hashCode;
	}

	//Equals
	public boolean equals(Object o) {
		Coord t = (Coord)o;
		if(t.getX() == x && t.getY() == y) {
			return true;
		}
		return false;
	}
	
	//ToString
	public String toString() {
		return "X: " + x + ", Y: " + y;
	}
}