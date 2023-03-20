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

	public static CoordSet DirToVec(Direction d){
		if (d == Direction.HAUT) {
			return new CoordSet(-1, 0);
		}
		if (d == Direction.BAS) {
			return new CoordSet(1,0);
		}
		if (d == Direction.GAUCHE) {
			return new CoordSet(0, -1);
		}
		if (d == Direction.DROITE) {
			return new CoordSet(0, 1);
		}
		return new CoordSet(0, 0);
	}

	public static CoordSet AddVec(CoordSet a, CoordSet b){
		return new CoordSet(a.getX() + b.getX(), a.getY() + b.getY());
	}

    public boolean equals(Object o) {
    	if (!(o instanceof CoordSet)) {
    		return false;
    	}

    	CoordSet autre = (CoordSet) o;

    	return ((getX() == autre.getX()) && (getY() == autre.getY()));
    }
	public static Direction revDirection(Direction d){
        if (d == Direction.HAUT) {
			return Direction.BAS;
		}
		if (d == Direction.BAS) {
			return Direction.HAUT;
		}
		if (d == Direction.GAUCHE) {
			return Direction.DROITE;
		}
		if (d == Direction.DROITE) {
			return Direction.GAUCHE;
		}
		return Direction.NODIRECTION;
    } 
}