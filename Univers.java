import java.util.*;

public class Univers {
	private ArrayList<LevelMove> univ;
	private int taille;
	private int [] worldAcces; /** Tableau qui va nous permettre de savoir si on peut entrer ou non dans un monde (>0 oui : 0 non) */

	public Univers() {
		this.univ = new ArrayList<LevelMove>();
		this.taille = 0;
	}

	public Univers(ArrayList<LevelMove> u, int taille) {
		this.univ = new ArrayList<LevelMove>(u);
		this.taille = taille;
	}

	/**
		Renvoie true si le mouvement à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d est possible sur un ou éventuellement plusieurs niveaux.
	*/
	public boolean checkMovePossible(CoordSet o, Direction d, LevelMove l) {
		int [][] tmpMat = l.getLevelData();
		LevelMove tmpL;
		CoordSet tmpCoord, tmpCoord2 = CoordSet.addVec(o, CoordSet.dirToVec(CoordSet.revDirection(d)));

		/* On réctifie les coordonnées de o (cas quand on veut sortir d'un niveau 
			mais que le niveau courant se situe sur les extrémités du niveau extérieur) */
		if (o.getX() == -1)
			o.addToX(1);
		if (o.getX() == l.getSizeMat())
			o.addToX(-1);
		if (o.getY() == -1)
			o.addToY(1);
		if (o.getY() == l.getSizeMat())
			o.addToY(-1);

		/* Cas d'arrêt */
		if (tmpMat[o.getX()][o.getY()] == Cells.MUR)
			return false;
		else if (tmpMat[o.getX()][o.getY()] == Cells.VIDE)
			return true;

		/* Si les coordonnées o se situe sur les extrémités du niveau */
		if (l.onEdge(o, d)) {
			/* On récupère le monde extérieur du niveau courant */
			tmpL = univ.get(l.getOutsideWorld());
			/* On récupère les coordonnées du monde courant dans le niveau extérieur */
			if (isWorldInThisLevel(tmpL, l.getWorldNum()))
				tmpCoord = tmpL.getPosWorld(l.getWorldNum());
			else
				tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
			/* On positionne les coordonnées tmpCoord sur la sortie du monde courant */
			tmpCoord.addToCoordWithDir(d);
			/* Si le mouvement n'est pas possible on s'arrête (on renvoie false) */
			if (!checkMovePossible(tmpCoord, d, tmpL))
				return false;
			else /* Sinon on continue de vérifier avec ces nouvelles coordonnées */
				return checkMovePossible(tmpCoord, d, tmpL);
		}
		/* Si mouvement avec chaînage */
		if (tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] != Cells.VIDE) {
			while (tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] != Cells.VIDE) {
				/* On avance les coordonnées de o tant qu'on tombe pas sur le vide */
				o.addToCoordWithDir(d);
				/* Si on tombe sur un mur on s'arrête et on revient d'un cran sur les coordonnées o */
				if (tmpMat[o.getX()][o.getY()] == Cells.MUR) {
					o.addToCoordWithDir(o.revDirection(d));
					break;
				}
				/* Si les coordonnées o se situe sur les extrémités du niveau on fait comme précedemment */
				if (l.onEdge(o, d)) {
					tmpL = univ.get(l.getOutsideWorld());
					if (isWorldInThisLevel(tmpL, l.getWorldNum()))
						tmpCoord = tmpL.getPosWorld(l.getWorldNum());
					else
						tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
					tmpCoord.addToCoordWithDir(d);
					if (!checkMovePossible(tmpCoord, d, tmpL))
						break;
					else
						return checkMovePossible(tmpCoord, d, tmpL);
				}
				/* Si on tombe sur un vide on s'arrête (on renvoie true) */
				if (tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] == Cells.VIDE)
					return true;
			}
			/* On repasse sur toutes les cases traversées avec la précedente boucle while */
			while (!o.equals(tmpCoord2)) {	
				/* Si c'est un monde on vérifie qu'il soit possible de faire un mouvement dans ce monde */
				if (l.isAWorld(o.getX(), o.getY())) {
					tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					if (checkMovePossible(tmpL.enterPos(d), d, tmpL)) {
						worldAcces[tmpMat[o.getX()][o.getY()]]++; // on donne l'accès à ce monde
						return checkMovePossible(tmpL.enterPos(d), d, tmpL);
					}
				}
				o.addToCoordWithDir(o.revDirection(d));
			}
			return false;
		}
		return true;
	}

	/**
		Renvoie le nombre de mouvements à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d sur un ou éventuellement plusieurs niveaux.
	*/
	public int getNumMove(CoordSet o, Direction d, LevelMove l, int n) {
		int [][] m = l.getLevelData();
		LevelMove next;
		CoordSet tmpCoord;

		/* Cas d'arrêt */
		if (m[o.getX()][o.getY()] == Cells.VIDE || m[o.getX()][o.getY()] == Cells.MUR)
			return n;

		/* Si c'est un monde et qu'on peut y entrer, on continue de compter le nombre de mouvement dans ce monde 
			(on ne fait pas d'incrémentation du nombre de mouvement quand on rentre dans un monde !) */
		if (l.isAWorld(o.getX(), o.getY()) && worldAcces[m[o.getX()][o.getY()]] >= 1) {
			next = univ.get(m[o.getX()][o.getY()]);
			return getNumMove(next.enterPos(d), d, next, n);
		}
		else if (l.onEdge(o, d)) { /* Si les coordonnées o se situe sur les extrémités du niveau, on continue de compter le nombre de mouvement dans le monde extérieur */
			next = univ.get(l.getOutsideWorld());
			if (isWorldInThisLevel(next, l.getWorldNum()))
				tmpCoord = next.getPosWorld(l.getWorldNum());
			else
				tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
			tmpCoord.addToCoordWithDir(d);
			n++;
			return getNumMove(tmpCoord, d, next, n);
		}
		else { /* Sinon on continue de compter le nombre de mouvement dans ce monde */
			o.addToCoordWithDir(d);
			n++;
			return getNumMove(o, d, l, n);
		}
	}

	/**
		Effectue les mouvements à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d en fonction du nombre de mouvement nMove (renvoyé par la fonction getNumMove) 
		sur un ou éventuellement plusieurs niveaux.
	*/
	public void move(CoordSet o, Direction d, LevelMove l, int nMove) {
		int [][] tmpMat = l.getLevelData();
		int n = nMove, tmpCell1 = tmpMat[o.getX()][o.getY()], tmpCell2; // initialement tmpCell1 = Cells.JOUEUR
		LevelMove tmpL = l, tmpL2 = l; // initialement tmpL et tmpL2 correspondent au niveau du joueur

		tmpMat[o.getX()][o.getY()] = Cells.VIDE; // on déplace le joueur donc met aux coordonnées initiales -> Cells.VIDE

		while (n > 0) {
			/* Si on doit faire le mouvement dans le monde extérieur du niveau courant */
			if (tmpL.onEdge(o, d)) {
				/* On récupère le monde extérieur du niveau courant */
				tmpL = univ.get(tmpL2.getOutsideWorld());
				/* On conserve le Cell positionné à la sortie du niveau courant */
				if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
					tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(tmpL2.getWorldNum()).getAddToXWithDir(d), tmpL.getPosWorld(tmpL2.getWorldNum()).getAddToYWithDir(d));
				else
					tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getAddToXWithDir(d), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getAddToYWithDir(d));
				/* On change les coordonnées de o avec celles de la sortie du monde courant */
				if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
					o.chgCoord(tmpL.getPosWorld(tmpL2.getWorldNum()).getAddToXWithDir(d), tmpL.getPosWorld(tmpL2.getWorldNum()).getAddToYWithDir(d));
				else
					o.chgCoord(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getAddToXWithDir(d), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getAddToYWithDir(d));
				tmpMat = tmpL.getLevelData();
				/* On fait le mouvement */
				tmpMat[o.getX()][o.getY()] = tmpCell1;
				/* Si on a déplacé un monde, on change le numéro de son monde extérieur */
				if (tmpL.isAWorld(o.getX(), o.getY()))
					univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
				/* On récupère le Cell conservé précédemment */
				tmpCell1 = tmpCell2;
				/* On conserve le niveau courant */
				tmpL2 = tmpL;
				n--; // prochain mouvement
				continue;
			}
			else // sinon on conserve le Cell situé juste après les coordonnées o
				tmpCell2 = tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)];
			/* Si le Cell positionné après les coordonnées o est un monde et qu'on peut y entrer */
			if (tmpL.isAWorld(o.getAddToXWithDir(d), o.getAddToYWithDir(d)) && worldAcces[tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)]] >= 1) {
				/* On récupère le monde où on veut entrer du niveau courant */
				tmpL = univ.get(tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)]);
				/* On conserve le Cell positionné à l'entrée de ce monde */
				tmpCell2 = univ.get(tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)]).getLevelData(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
				tmpMat = tmpL.getLevelData();
				/* On change les coordonnées de o avec celles de l'entrée du monde */
				o.chgCoord(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
				/* On vérifie si le Cell positionné à l'entrée du monde est aussi un monde et qu'on peut y entrer */
				while (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
					/* On change le numéro du monde extérieur du niveau où on va (potentiellement) entrer */
					univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL2.getWorldNum());
					/* On récupère le monde où on veut entrer du niveau courant */
					tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					/* On conserve le Cell positionné à l'entrée de ce monde */
					tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
					tmpMat = tmpL.getLevelData();
					/* On change les coordonnées de o avec celles de l'entrée du monde */
					o.chgCoord(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
				}
				/* On fait le mouvement */
				tmpMat[o.getX()][o.getY()] = tmpCell1;
				/* Si on a déplacé un monde, on change le numéro de son monde extérieur */
				if (tmpL.isAWorld(o.getX(), o.getY()))
					univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
				/* On récupère le Cell conservé précédemment */
				tmpCell1 = tmpCell2;
				/* On conserve le niveau courant */
				tmpL2 = tmpL;
			}
			else { /* Sinon mouvement simple */
				/* On fait le mouvement */
				tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] = tmpCell1;
				/* On récupère le Cell conservé précédemment */
				tmpCell1 = tmpCell2;
				/* On avance les coordonnées o */
				o.addToCoordWithDir(d);
			}
			n--; // prochain mouvement
		}
	}

	/**
		Renvoie true si toutes les cibles sont atteintes
		Sinon renvoie false
	*/
	public boolean winConditionMetUniv() {
		int i, k;
		int [][] tmpMat;
		LevelMove tmp;

		for (i = 0; i < getTaille(); i++) {
			tmp = univ.get(i);
			tmpMat = tmp.getLevelData();
			for (k = 0; k < tmp.getListTarget().size(); k++)
    			if (tmpMat[tmp.getListTarget().get(k).getX()][tmp.getListTarget().get(k).getY()] == Cells.VIDE)
    				return false;
    	}
    	return true;
	}

	/**
		Renvoie le numéro du monde où le joueur est situé
	*/
	public int getPlayerSpawnWorld() {
		int k, i, j;
		int [][] tmp;

		for (k = 0; k < getTaille(); k++) {
			tmp = univ.get(k).getLevelData();
			for (i = 0; i < univ.get(k).getSizeMat(); i++)
				for (j = 0; j < univ.get(k).getSizeMat(); j++)
					if (tmp[i][j] == Cells.JOUEUR)
						return k;
		}
		return -1;
	}

	/**
		Initialise à 0 le tableau déclaré au début de la classe
	*/
	public void initWorldAcces() {
		int i;
		
		this.worldAcces = new int[this.getTaille()];
		for (i = 0; i < this.getTaille(); i++)
			this.worldAcces[i] = 0;
	}

	/**
		Remet à 0 le tableau déclaré au début de la classe
	*/
	public void resetWorldAcces() {
		int i;
		
		for (i = 0; i < this.getTaille(); i++)
			this.worldAcces[i] = 0;
	}

    /**
		Renvoie le numéro du monde qui contient le numéro du monde numWorld se trouvant dans le niveau l
	*/
    public int whereWorldIs(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	for (i = 0; i < l.getSizeMat(); i++)
    		for (j = 0; j < l.getSizeMat(); j++)
    			if (l.isAWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld)
    					return l.getWorldNum();
    				else if (isWorldIn(univ.get(tmpMat[i][j]), numWorld))
    					return tmpMat[i][j];
    			}
    	return -5;
    }

    /**
		Renvoie true si le numéro du monde numWorld se trouve dans le niveau l ou même récursivement dans un autre monde qui le contient ...
		Sinon renvoie false
	*/
    public boolean isWorldIn(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	for (i = 0; i < l.getSizeMat(); i++)
    		for (j = 0; j < l.getSizeMat(); j++)
    			if (l.isAWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld)
    					return true;
    				else if (isWorldIn(univ.get(tmpMat[i][j]), numWorld))
    					return isWorldIn(univ.get(tmpMat[i][j]), numWorld);
    			}
    	return false;
    }

    /**
		Renvoie true si le numéro du monde numWorld se trouve dans le niveau l
		Sinon renvoie false
	*/
    public boolean isWorldInThisLevel(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	for (i = 0; i < l.getSizeMat(); i++)
    		for (j = 0; j < l.getSizeMat(); j++) {
    			if (tmpMat[i][j] == numWorld)
    				return true;
    		}
    	return false;
    }

	/**
		Renvoie une liste des mondes (ou niveaux) présent dans l'univers
	*/
	public ArrayList<LevelMove> getUnivers() {
		return this.univ;
	}

	/**
		Renvoie le nombre de monde (ou niveau) présent dans l'univers
	*/
	public int getTaille() {
		return this.taille;
	}
}