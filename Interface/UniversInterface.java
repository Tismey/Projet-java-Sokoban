public interface UniversInterface {

	/**
		Renvoie true si le mouvement à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d est possible sur un ou éventuellement plusieurs niveaux.
	*/
	public boolean checkMovePossible(CoordSet o, Direction d, LevelMove l);

	/**
		Renvoie true si un mouvement avec poussette est potentiellement possible à partir de la coordonnée o (position donné dans la méthode getLevelPockableMove) 
		du niveau l (niveau donné dans la méthode getLevelPockableMove) vers la direction d est possible sur un ou éventuellement plusieurs niveaux.
		Le niveau lCrte nous sert de "sauvegarde", il correspond au même niveau que l.
	*/
	public boolean checkPockableMovePossible(CoordSet o, Direction d, LevelMove l, LevelMove lCrte);

	/** 	Avant d'utiliser cette méthode il est nécessaire de vérifier au préalable qu'un mouvement est possible (méthode checkMovePossible)
		Renvoie le nombre de mouvements à effectuer à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d sur un ou éventuellement plusieurs niveaux.
		Le paramètre n correspondra au nombre de mouvements à effectuer, il est initialement à 0.
	*/
	public int getNumMove(CoordSet o, Direction d, LevelMove l, int n);

	/**
		Renvoie le numéro du niveau où un mouvement avec poussette est potentiellement possible à partir de la coordonnée o (position du joueur) 
		du niveau l (niveau où se trouve le joueur) vers la direction d sur un ou éventuellement plusieurs niveaux.
		Le niveau lCrte nous sert de "sauvegarde", au début il correspond au même niveau que l (il peut changer en cours de route), 
		c'est son numéro de niveau qu'on va potentiellement renvoyer.
		De plus la méthode change les valeurs des coordonnées cRes en fonction de se qu'on trouve.
	*/
	public int getLevelPockableMove(CoordSet o, Direction d, LevelMove l, LevelMove lCrte, CoordSet cRes);

	/**
		Renvoie le nombre de mouvements avec poussette possible à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d sur un ou éventuellement plusieurs niveaux.
		Le niveau lCrte nous sert de "sauvegarde", au début il correspond au même niveau que l (il peut changer en cours de route).
		Le paramètre n correspondra au nombre de mouvements à effectuer, il est initialement à 0.
	*/
	public int getNumPockableMove(CoordSet o, Direction d, LevelMove l, LevelMove lCrte, int n);

	/**							Méthode utilisé avec un joueur simple pour les mouvements récursive avec chainage + absorption		

		Effectue les mouvements à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) vers la direction d en fonction du nombre 
		de mouvement nMove (renvoyé par la méthode getNumMove) sur un ou éventuellement plusieurs niveaux.
	*/
	public void move(CoordSet o, Direction d, LevelMove l, int nMove);

	/**													Méthode utilisé pour les mouvements avec poussettes

		Effectue les mouvements à partir de la coordonnée o (position donné par la méthode getLevelPockableMove) du niveau l (niveau donné par la méthode getLevelPockableMove) 
		vers la direction d en fonction du nombre de mouvement nMove (renvoyé par la méthode getNumPockableMove).
	*/
	public void pockableMove(CoordSet o, Direction d, LevelMove l, int nMove);

	/**							Méthode utilisé avec un joueur monde pour les mouvements récursive avec chainage + absorption	

		Effectue les mouvements à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) vers la direction d en fonction du nombre 
		de mouvement nMove (renvoyé par la méthode getNumMove) sur un ou éventuellement plusieurs niveaux.
	*/
	public void playerWorldMove(CoordSet o, Direction d, LevelMove l, int nMove);

	/**
		Renvoie true si toutes les cibles sont atteintes
		Sinon renvoie false
	*/
	public boolean winConditionMetUniv();

	/**
		Renvoie le numéro du monde où le joueur est situé
	*/
	public int getPlayerSpawnWorld();

	/**
		Initialise à 0 le tableau déclaré au début de la classe
	*/
	public void initWorldAcces();

	/**
		Remet à 0 le tableau déclaré au début de la classe
	*/
	public void resetWorldAcces();

	/**
		Renvoie le numéro du monde qui contient le numéro du monde numWorld se trouvant dans le niveau l
	*/
    public int whereWorldIs(LevelMove l, int numWorld);

    /**
		Renvoie true si le numéro du monde numWorld se trouve dans le niveau l ou même récursivement dans un autre monde qui le contient ...
		Sinon renvoie false
	*/
    public boolean isWorldIn(LevelMove l, int numWorld);

    /**
		Renvoie true si le numéro du monde numWorld se trouve dans le niveau l
		Sinon renvoie false
	*/
    public boolean isWorldInThisLevel(LevelMove l, int numWorld);

    /**
		Renvoie une liste des mondes (ou niveaux) présent dans l'univers
	*/
	public ArrayList<LevelMove> getUnivers();

	/**
		Renvoie le nombre de monde (ou niveau) présent dans l'univers
	*/
	public int getTaille();
}