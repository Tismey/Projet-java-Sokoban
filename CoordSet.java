public class CoordSet {
	private int x;
	private int y;

	public CoordSet (int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
		Renvoie sa coordonnée x
	*/
	public int getX() {
		return this.x;
	}

	/**
		Renvoie sa coordonnée y
	*/
    public int getY() {
    	return this.y;
    }

    /**
		Ajoute à sa coordonnée x la valeur de p
	*/
    public void addToX(int p) {
    	this.x += p;
    }
    
    /**
		Ajoute à sa coordonnée y la valeur de p
	*/
    public void addToY(int p) {
    	this.y += p;
    }

    /**
		Ajoute 1 ou -1 à une de ses coordonnées en fonction de la direction d
	*/
    public void addToCoordWithDir(Direction d) {
    	if (d == Direction.HAUT)
			this.x += -1;
		else if (d == Direction.BAS)
			this.x += 1;
		else if (d == Direction.GAUCHE)
			this.y += -1;
		else if (d == Direction.DROITE)
			this.y += 1;
    }

    /**
		Renvoie sa coordonnée x avec un ajout de 1 ou -1 en fonction de la direction d
	*/
    public int getAddToXWithDir(Direction d) {
    	if (d == Direction.HAUT) {
			return this.x - 1;
		}
		if (d == Direction.BAS) {
			return this.x + 1;
		}
    	return this.x;
    }

    /**
		Renvoie sa coordonnée y avec un ajout de 1 ou -1 en fonction de la direction d
	*/
    public int getAddToYWithDir(Direction d) {
    	if (d == Direction.GAUCHE) {
			return this.y - 1;
		}
		if (d == Direction.DROITE) {
			return this.y + 1;
		}
    	return this.y;
    }

    /**
		Modifie ses coordonnées avec les valeurs x et y
    */
    public void chgCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

	/**
		Renvoie les coordonnées d'un ajout vers la direction d
	*/
	public static CoordSet dirToVec(Direction d) {
		if (d == Direction.HAUT) {
			return new CoordSet(-1, 0);
		}
		if (d == Direction.BAS) {
			return new CoordSet(1, 0);
		}
		if (d == Direction.GAUCHE) {
			return new CoordSet(0, -1);
		}
		if (d == Direction.DROITE) {
			return new CoordSet(0, 1);
		}
		return new CoordSet(0, 0);
	}

	/**
		Renvoie les coordonnées de la somme des coordonnées a et b
	*/
	public static CoordSet addVec(CoordSet a, CoordSet b) {
		return new CoordSet(a.getX() + b.getX(), a.getY() + b.getY());
	}

    /**
		Renvoie true si o est un type CoordSet et que les coordonnées de o sont égaux à ses coordonnées
		Sinon renvoie false
    */
    public boolean equals(Object o) {
    	if (!(o instanceof CoordSet)) {
    		return false;
    	}

    	CoordSet autre = (CoordSet) o;

    	return ((getX() == autre.getX()) && (getY() == autre.getY()));
    }

    /**
		Renvoie la direction opposée à la direction d
    */	
	public static Direction revDirection(Direction d) {
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