public class CoordSet {
	private int x;
	private int y;

	public CoordSet (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

    public int getY() {
    	return this.y;
    }

    public void addToX(int p) {
    	this.x += p;
    }
    
    public void addToY(int p) {
    	this.y += p;
    }

    public void chgCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
    	if (!(o instanceof CoordSet)) {
    		return false;
    	}

    	CoordSet autre = (CoordSet) o;

    	return ((getX() == autre.getX()) && (getY() == autre.getY()));
    }
}