public interface CoordSetInterface {
    /*
     * classe pour exprimer les coordonnes
     */

    
    /**
		Renvoie sa coordonnée x
	*/
	public int getX();

	/**
		Renvoie sa coordonnée y
	*/
    public int getY();
    
    /**
		Ajoute à sa coordonnée x la valeur de p
	*/
    public void addToX(int p);

    /**
		Ajoute à sa coordonnée y la valeur de p
	*/
    public void addToY(int p);

    /**
		Ajoute 1 ou -1 à une de ses coordonnées en fonction de la direction d
	*/
    public void addToCoordWithDir(Direction d);

    /**
		Renvoie sa coordonnée x avec un ajout de 1 ou -1 en fonction de la direction d
	*/
    public int getAddToXWithDir(Direction d);

    /**
		Renvoie sa coordonnée y avec un ajout de 1 ou -1 en fonction de la direction d
	*/
    public int getAddToYWithDir(Direction d);

    /**
		Modifie ses coordonnées avec les valeurs x et y
    */
    public void chgCoord(int x, int y);

    /**
		Renvoie les coordonnées d'un ajout vers la direction d
	*/
	public static CoordSet dirToVec(Direction d);

	/**
		Renvoie les coordonnées de la somme des coordonnées a et b
	*/
	public static CoordSet addVec(CoordSet a, CoordSet b);

	/**
		Renvoie la direction opposée à la direction d
    */	
	public static Direction revDirection(Direction d);

	/**
		Renvoie true si o est un type CoordSet et que les coordonnées de o sont égaux à ses coordonnées
		Sinon renvoie false
    */
    public boolean equals(Object o);

}