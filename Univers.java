import java.util.*;

/**
	Classe permettant de definir un ensemble de plusieurs niveaux avec ses caractéristique,
	les mouvements dans cette classe ne sont utilisés que pour un jeu récursive
*/
public class Univers {
	private ArrayList<LevelMove> univ;
	private int taille;
	private int [] worldAcces; /** Tableau qui va nous permettre de savoir si on peut entrer ou non dans un monde (>0 oui : 0 non) */
	private int [] worldAccesAbs; /** Même tableau que précédemment mais avec un mouvement absorbeur (>0 oui : 0 non) */

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
		/* Sinon on avance les coordonnées de o tant que on ne tombe pas sur un vide ou un mur */
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
				if (l.isAWorld(o.getX(), o.getY()) || l.isAPlayerWorld(o.getX(), o.getY())) {
					if (tmpMat[o.getX()][o.getY()] == Cells.JOUEUR)
						tmpL = univ.get(LevelData.getNumPlayerWorld());
					else	
						tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
					/* Mouvement par absorption */
					if (l.onEdge(o, d)) {
						if (checkMovePossible(tmpL.enterPos(d), d, tmpL)) {
							worldAcces[tmpMat[o.getX()][o.getY()]]++; // on donne l'accès à ce monde
							return checkMovePossible(tmpL.enterPos(d), d, tmpL);
						}
					}
					else if (checkMovePossible(tmpL.enterPos(CoordSet.revDirection(d)), CoordSet.revDirection(d), tmpL) && tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] != Cells.MUR) {
						if (tmpMat[o.getX()][o.getY()] == Cells.JOUEUR)
							worldAccesAbs[LevelData.getNumPlayerWorld()]++; // on donne l'accès à ce monde par l'absorption
						else
							worldAccesAbs[tmpMat[o.getX()][o.getY()]]++; // on donne l'accès à ce monde par l'absorption
						return checkMovePossible(tmpL.enterPos(CoordSet.revDirection(d)), CoordSet.revDirection(d), tmpL);
					} 
					/* Mouvement non absorption */
					else if (checkMovePossible(tmpL.enterPos(d), d, tmpL) && tmpMat[o.getX()][o.getY()] != Cells.JOUEUR) {
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
		Renvoie true si un mouvement avec poussette est potentiellement possible à partir de la coordonnée o (position donné dans la méthode getLevelPockableMove) 
		du niveau l (niveau donné dans la méthode getLevelPockableMove) vers la direction d est possible sur un ou éventuellement plusieurs niveaux.
		Le niveau lCrte nous sert de "sauvegarde", il correspond au même niveau que l.
	*/
	public boolean checkPockableMovePossible(CoordSet o, Direction d, LevelMove l, LevelMove lCrte) {
		int [][] tmpMat = l.getLevelData();
		LevelMove tmpL;
		CoordSet tmpCoord;

		/* Cas d'arrêt */
		if (tmpMat[o.getX()][o.getY()] == Cells.MUR || tmpMat[o.getX()][o.getY()] == Cells.VIDE)
			return false;
	
		/* Tant que les coordonnées o se situe pas à l'extrémité de ce niveau on avance */
		while (!l.onEdge(o, d)) {
			o.addToCoordWithDir(d);
			/* Cas d'arrêt */
			if (tmpMat[o.getX()][o.getY()] == Cells.MUR || tmpMat[o.getX()][o.getY()] == Cells.VIDE)
				return false;
		}

		/* Cas d'arrêt sur les coordonnées o se situant sur les extrémités du niveau */
		if (tmpMat[o.getX()][o.getY()] == Cells.MUR || tmpMat[o.getX()][o.getY()] == Cells.VIDE)
			return false;

		/* On récupère le monde extérieur du niveau courant */
		tmpL = univ.get(l.getOutsideWorld());
		
		/* Si le monde extérieur de ce niveau ne correspond au niveau de l'appel initial de cette méthode */
		if (tmpL.getWorldNum() != lCrte.getWorldNum()) {
			/* On récupère les coordonnées du niveau courant dans le monde extérieur */
			if (isWorldInThisLevel(tmpL, l.getWorldNum()))
				tmpCoord = tmpL.getPosWorld(l.getWorldNum());
			else
				tmpCoord = tmpL.getPosWorld(whereWorldIs(tmpL, l.getWorldNum()));
			tmpCoord.addToCoordWithDir(d); // on situe les coordonnées devant le niveau courant
			return checkPockableMovePossible(tmpCoord, d, tmpL, lCrte);
		}
		/* Sinon on a trouvé un monde extérieur qui correspond au niveau de l'appel initial de cette méthode */
		return true;
	}

	/** 	Avant d'utiliser cette méthode il est nécessaire de vérifier au préalable qu'un mouvement est possible (méthode checkMovePossible)
		Renvoie le nombre de mouvements à effectuer à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d sur un ou éventuellement plusieurs niveaux.
		Le paramètre n correspondra au nombre de mouvements à effectuer, il est initialement à 0.
	*/
	public int getNumMove(CoordSet o, Direction d, LevelMove l, int n) {
		int [][] tmpMat = l.getLevelData();
		LevelMove next;
		CoordSet tmpCoord;

		/* Cas d'arrêt */
		if (tmpMat[o.getX()][o.getY()] == Cells.VIDE /*|| tmpMat[o.getX()][o.getY()] == Cells.MUR*/)
			return n;

		/* Si c'est un monde et qu'on peut y entrer, on continue de compter le nombre de mouvement dans ce monde 
			(on ne fait pas d'incrémentation du nombre de mouvement quand on entre dans un monde) */
		if (l.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
			next = univ.get(tmpMat[o.getX()][o.getY()]); // on récupère le niveau
			return getNumMove(next.enterPos(d), d, next, n);
		} 
		/* Absorption avec un joueur monde ou un monde (on fait une incrémentation du nombre de mouvement pour le déplacement du monde qui absorbe),
		   puis on continue de compter le nombre de mouvement dans ce monde */
		else if (l.isAPlayerWorld(o.getX(), o.getY()) && worldAccesAbs[LevelData.getNumPlayerWorld()] >= 1) {
			next = univ.get(LevelData.getNumPlayerWorld());
			n++;
			return getNumMove(next.enterPos(CoordSet.revDirection(d)), CoordSet.revDirection(d), next, n);
		}
		else if (l.isAWorld(o.getX(), o.getY()) && worldAccesAbs[tmpMat[o.getX()][o.getY()]] >= 1) {
			next = univ.get(tmpMat[o.getX()][o.getY()]);
			n++;
			return getNumMove(next.enterPos(CoordSet.revDirection(d)), CoordSet.revDirection(d), next, n);
		}
		/* Si les coordonnées o se situe sur les extrémités du niveau, on continue de compter le nombre de mouvement dans le monde extérieur */
		else if (l.onEdge(o, d)) {
			next = univ.get(l.getOutsideWorld());
			/* On récupère les coordonnées du niveau courant dans le monde extérieur */
			if (isWorldInThisLevel(next, l.getWorldNum()))
				tmpCoord = next.getPosWorld(l.getWorldNum());
			else
				tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
			tmpCoord.addToCoordWithDir(d); // on situe les coordonnées devant le niveau courant
			n++;
			return getNumMove(tmpCoord, d, next, n);
		}
		/* Sinon on continue de compter le nombre de mouvement dans ce monde */
		else {
			o.addToCoordWithDir(d);
			n++;
			return getNumMove(o, d, l, n);
		}
	}

	/**
		Renvoie le numéro du niveau où un mouvement avec poussette est potentiellement possible à partir de la coordonnée o (position du joueur) 
		du niveau l (niveau où se trouve le joueur) vers la direction d sur un ou éventuellement plusieurs niveaux.
		Le niveau lCrte nous sert de "sauvegarde", au début il correspond au même niveau que l (il peut changer en cours de route), 
		c'est son numéro de niveau qu'on va potentiellement renvoyer.
		De plus la méthode change les valeurs des coordonnées cRes en fonction de se qu'on trouve.
	*/
	public int getLevelPockableMove(CoordSet o, Direction d, LevelMove l, LevelMove lCrte, CoordSet cRes) {
		int [][] tmpMat = l.getLevelData();
		LevelMove next;
		CoordSet tmpCoord;
		int lRes;

		/* Cas d'arrêt */
		if (tmpMat[o.getX()][o.getY()] == Cells.MUR || tmpMat[o.getX()][o.getY()] == Cells.VIDE)
			return -1;

		/* Si c'est un monde et qu'un mouvement est potentiellement possible dans le niveau courant */
		if (l.isAWorld(o.getX(), o.getY()) && checkPockableMovePossible(new CoordSet(o.getX(), o.getY()), CoordSet.revDirection(d), l, l)) {
			/* On change les coordonnées de cRes avec celle correspondant à l'entrée de ce niveau par la direction d */
			cRes.chgCoord(l.enterPos(d).getX(), l.enterPos(d).getY());
			lRes = l.getWorldNum();
			return lRes;
		}
		/* Si les coordonnées o se situe sur les extrémités du niveau */
		else if (l.onEdge(o, d)) {
			next = univ.get(l.getOutsideWorld());
			/* On vérifie que le monde extérieur ce trouve dans le niveau courant (même récursivement dans un autre) */
			if (!isWorldIn(l, next.getWorldNum())) {
				/* Si c'est pas le cas on modifie le niveau de "sauvegarde" */
				lCrte = next;
				/* On récupère les coordonnées du niveau courant dans le monde extérieur */
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				
				/* Pour éviter de dépasser la taille de la matrice du monde extérieur */
				if (next.onEdge(tmpCoord, d))
					return getLevelPockableMove(tmpCoord, d, next, lCrte, cRes);
				/* Sinon on peut se positionner devant le niveau courant */
				tmpCoord.addToCoordWithDir(d);
				return getLevelPockableMove(tmpCoord, d, next, lCrte, cRes);
			}
			/* Si le monde extérieur est le niveau de "sauvegarde" */
			else if (lCrte.getWorldNum() == next.getWorldNum())	{
				lRes = lCrte.getWorldNum();
				/* On récupère les coordonnées du niveau courant dans le monde extérieur */
				tmpCoord = lCrte.getPosWorld(l.getWorldNum());
				/* Pour éviter de dépasser la taille de la matrice du monde extérieur */
				if (lCrte.onEdge(tmpCoord, d)) 
					return -1;
				/* Sinon on peut se positionner devant le niveau courant */
				tmpCoord.addToCoordWithDir(d);
				/* On change les coordonnées de cRes avec celle correspondant à la sortie de ce niveau dans le niveau de sauvegarde par la direction d */
				cRes.chgCoord(tmpCoord.getX(), tmpCoord.getY());
				tmpMat = lCrte.getLevelData();
				
				/* Cas d'arrêt */
				if (tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.MUR || tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.VIDE)
					return -1;

				return lRes;
			}
			/* Sinon on cherche dans le monde extérieur */
			else {
				/* On récupère les coordonnées du niveau courant dans le monde extérieur */
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				
				/* Pour éviter de dépasser la taille de la matrice du monde extérieur */
				if (next.onEdge(tmpCoord, d))
					return getLevelPockableMove(tmpCoord, d, next, lCrte, cRes);
				/* Sinon on peut se positionner devant le niveau courant */
				tmpCoord.addToCoordWithDir(d);
				return getLevelPockableMove(tmpCoord, d, next, lCrte, cRes);
			}
		}
		/* Sinon on continue de chercher dans ce monde */
		else { 
			o.addToCoordWithDir(d);
			return getLevelPockableMove(o, d, l, lCrte, cRes);
		}
	}

	/**
		Renvoie le nombre de mouvements avec poussette possible à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) 
		vers la direction d sur un ou éventuellement plusieurs niveaux.
		Le niveau lCrte nous sert de "sauvegarde", au début il correspond au même niveau que l (il peut changer en cours de route).
		Le paramètre n correspondra au nombre de mouvements à effectuer, il est initialement à 0.
	*/
	public int getNumPockableMove(CoordSet o, Direction d, LevelMove l, LevelMove lCrte, int n) {
		int [][] tmpMat = l.getLevelData();
		LevelMove next;
		CoordSet tmpCoord;

		/* Cas d'arrêt */
		if (tmpMat[o.getX()][o.getY()] == Cells.MUR || tmpMat[o.getX()][o.getY()] == Cells.VIDE)
			return 0;

		/* Si on tombe sur le monde */
		if (tmpMat[o.getX()][o.getY()] == lCrte.getWorldNum()) {
			/* On récupère les coordonnées du niveau de sauvegarde dans le niveau courant */
			tmpCoord = l.getPosWorld(lCrte.getWorldNum());
				/* Pour éviter de dépasser la taille de la matrice du monde extérieur (dans ce cas là le mouvement est impossible)*/
				if (l.onEdge(tmpCoord, CoordSet.revDirection(d)))
					return 0;
				/* Sinon on peut se positionner devant le niveau courant */
				tmpCoord.addToCoordWithDir(CoordSet.revDirection(d));
				/* Cas d'arrêt */
				if (tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.MUR || tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.VIDE)
					return 0;
				/* On change les coordonnées o avec celles de l'entrée de ce niveau courant par la direction d */
				o.chgCoord(l.enterPos(d).getX(), l.enterPos(d).getY());
				/* On compte le nombre potentiel de mouvement */
				while (!o.equals(tmpCoord) || lCrte.getWorldNum() != l.getWorldNum()) {
					n++;
					/* On change de monde */
					if (l.onEdge(tmpCoord, CoordSet.revDirection(d))) {
						next = univ.get(l.getOutsideWorld());
						if (isWorldInThisLevel(next, l.getWorldNum()))
							tmpCoord = next.getPosWorld(l.getWorldNum());
						else
							tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
						if (!next.onEdge(tmpCoord, CoordSet.revDirection(d))) {
							tmpCoord.addToCoordWithDir(CoordSet.revDirection(d));
						}
						lCrte = next;
						tmpMat = next.getLevelData();
					}
					else
						tmpCoord.addToCoordWithDir(CoordSet.revDirection(d));
					
					/* Cas d'arrêt */
					if (tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.MUR || tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.VIDE)
						return 0;
				}
				n++; // correspond au dernier mouvement du Cell situé l'extrémité du niveau
				return n;
		}
		/* Si les coordonnées o se situe sur les extrémités du niveau, on continue de compter le nombre de mouvement dans le monde extérieur 
			même chose que la méthode précédente (getLevelPockableMove) */
		else if (l.onEdge(o, d)) {
			next = univ.get(l.getOutsideWorld());
				if (!isWorldIn(l, next.getWorldNum())) {
				lCrte = next;
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				if (next.onEdge(tmpCoord, d))
					return getNumPockableMove(tmpCoord, d, next, lCrte, n);
				tmpCoord.addToCoordWithDir(d);
				return getNumPockableMove(tmpCoord, d, next, lCrte, n);
			}
			if (lCrte.getWorldNum() == next.getWorldNum()) {
				tmpCoord = lCrte.getPosWorld(l.getWorldNum());
				if (lCrte.onEdge(tmpCoord, d))
					return 0;
				tmpCoord.addToCoordWithDir(d);
				tmpMat = lCrte.getLevelData();
				
				/* Cas d'arrêt */
				if (tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.MUR || tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.VIDE)
					return 0;
				/* Cas si le joueur n'est pas un monde */
				if (lCrte.getWorldNum() != LevelData.getNumPlayerWorld())		
					o.chgCoord(l.enterPos(CoordSet.revDirection(d)).getX(), l.enterPos(CoordSet.revDirection(d)).getY());
				
				while (!o.equals(tmpCoord) || lCrte.getWorldNum() != l.getWorldNum()) {
					n++;
					if (lCrte.onEdge(tmpCoord, d)) {
						next = univ.get(lCrte.getOutsideWorld());
						if (isWorldInThisLevel(next, lCrte.getWorldNum()))
							tmpCoord = next.getPosWorld(lCrte.getWorldNum());
						else
							tmpCoord = next.getPosWorld(whereWorldIs(next, lCrte.getWorldNum()));
						if (!next.onEdge(tmpCoord, d)) {
							tmpCoord.addToCoordWithDir(d);
						}
						lCrte = next;
						tmpMat = next.getLevelData();
					}
					else
						tmpCoord.addToCoordWithDir(d);
					
					/* Cas d'arrêt */
					if (tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.MUR || tmpMat[tmpCoord.getX()][tmpCoord.getY()] == Cells.VIDE)
						return 0;
				}
				n++;
				return n;
			}
			else {
				if (isWorldInThisLevel(next, l.getWorldNum()))
					tmpCoord = next.getPosWorld(l.getWorldNum());
				else
					tmpCoord = next.getPosWorld(whereWorldIs(next, l.getWorldNum()));
				if (next.onEdge(tmpCoord, d))
					return getNumPockableMove(tmpCoord, d, next, lCrte, n);
				tmpCoord.addToCoordWithDir(d);
				return getNumPockableMove(tmpCoord, d, next, lCrte, n);
			}
		}
		/* Sinon on continue de compter le nombre de mouvement dans ce monde */
		else {
			o.addToCoordWithDir(d);
			return getNumPockableMove(o, d, l, lCrte, n);
		}
	}

	/**							Méthode utilisé avec un joueur simple pour les mouvements récursive avec chainage + absorption		

		Effectue les mouvements à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) vers la direction d en fonction du nombre 
		de mouvement nMove (renvoyé par la méthode getNumMove) sur un ou éventuellement plusieurs niveaux.
	*/
	public void move(CoordSet o, Direction d, LevelMove l, int nMove) {
		int [][] tmpMat = l.getLevelData(), tmpMat2;
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
			/* sinon on conserve le Cell situé juste après les coordonnées o */
			else
				tmpCell2 = tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)];
			
			/* Si le Cell positionné après les coordonnées o est un monde et qu'on peut y entrer (par absorption ou non) */
			if (tmpL.isAWorld(o.getAddToXWithDir(d), o.getAddToYWithDir(d)) && (worldAcces[tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)]] >= 1 || worldAccesAbs[tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)]] >= 1)) {	
				/* On se déplace sur la position de ce monde */
				o.chgCoord(o.getAddToXWithDir(d), o.getAddToYWithDir(d));
				
				/* Boucle utilisée pour vérifier la possibilité d'entrée dans un monde présent à l'entrée de ce monde ... (entrée successive) 
				   (On entre au moins une fois dans le boucle car on a déjà vérifié dans le if précédant qu'on peut accéder à un monde) */
				while (tmpL.isAWorld(o.getX(), o.getY())) {
					/* Entrée direct */
					if (worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
						/* On récupère le monde où on veut entrer du niveau courant */
						tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
						/* On conserve le Cell positionné à l'entrée de ce monde */
						tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
						/* On change les coordonnées de o avec celles de l'entrée du monde */
						o.chgCoord(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
						/* On récupère la matrice de ce monde */
						tmpMat = tmpL.getLevelData();
						/* On change le numéro du monde extérieur du niveau où on va (potentiellement) entrer */
						if (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1)	
							univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL2.getWorldNum());
					} 
					/* Entrée par absorption */
					else if (worldAccesAbs[tmpMat[o.getX()][o.getY()]] >= 1) {
						tmpL2 = tmpL;
						/* On garde la matrice du monde courant */
						tmpMat2 = tmpL2.getLevelData();
						/* On récupère le monde où on veut entrer du niveau courant */
						tmpL = univ.get(tmpMat2[o.getX()][o.getY()]);
						/* On récupère la matrice de ce monde */
						tmpMat = tmpL.getLevelData();
						/* On conserve le Cell positionné après ce monde */
						tmpCell2 = tmpMat2[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)];
						/* On fait le mouvement de ce monde */
						tmpMat2[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] = tmpMat2[o.getX()][o.getY()];
						/* On fait le mouvement du Cell précédent ce monde */
						tmpMat2[o.getX()][o.getY()] = tmpCell1;
						/* On change les coordonnées de o avec celles de l'entrée du monde */
						o.chgCoord(tmpL.enterPos(CoordSet.revDirection(d)).getX(), tmpL.enterPos(CoordSet.revDirection(d)).getY());
						/* On récupère le Cell conservé précédemment (celui qui était après le monde) */
						tmpCell1 = tmpCell2;
						/* On conserve le Cell positionné à l'entrée de ce monde */
						tmpCell2 = tmpMat[o.getX()][o.getY()];
						/* On inverse la direction */
						d = CoordSet.revDirection(d);
					}
					/* Accès impossible au monde on s'arrête */
					else 
						break;

					/* Si on ne tombe pas sur un monde ou que c'est un monde mais sans accès possible on fait le mouvement */
					if (!tmpL.isAWorld(o.getX(), o.getY())) {
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
					else if (tmpL.isAWorld(o.getX(), o.getY()) && (worldAcces[tmpMat[o.getX()][o.getY()]] == 0 && worldAccesAbs[tmpMat[o.getX()][o.getY()]] == 0)) {
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
				}
			}
			/* Sinon mouvement simple */
			else {
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

	/**													Méthode utilisé pour les mouvements avec poussettes

		Effectue les mouvements à partir de la coordonnée o (position donné par la méthode getLevelPockableMove) du niveau l (niveau donné par la méthode getLevelPockableMove) 
		vers la direction d en fonction du nombre de mouvement nMove (renvoyé par la méthode getNumPockableMove).
	*/
	public void pockableMove(CoordSet o, Direction d, LevelMove l, int nMove) {
		int [][] tmpMat = l.getLevelData();
		LevelMove tmpL = l, tmpL2 = l;
		int n = nMove, tmpCell1 = tmpMat[o.getX()][o.getY()], tmpCell2;

		tmpMat[o.getX()][o.getY()] = Cells.VIDE;
		
		while (n > 0) {
			/* Si on sort du niveau courant */
			if (tmpL.onEdge(o, d)) {
				/* On récupère le monde extérieur du niveau courant */
				tmpL = univ.get(tmpL2.getOutsideWorld());
				
				/* On conserve le Cell positionné à la sortie du niveau courant */
				if (isWorldInThisLevel(tmpL, tmpL2.getWorldNum()))
					tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(tmpL2.getWorldNum()).getAddToXWithDir(d), tmpL.getPosWorld(tmpL2.getWorldNum()).getAddToYWithDir(d));
				else
					tmpCell2 = univ.get(tmpL2.getOutsideWorld()).getLevelData(tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getAddToXWithDir(d), tmpL.getPosWorld(whereWorldIs(tmpL, tmpL2.getWorldNum())).getAddToYWithDir(d));
				
				/* On change les coordonnées de o avec celles de la sortie du niveau courant */
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
			} 
			/* Sinon si on tombe sur le monde correspond au niveau courant alors on place le Cell conservé à l'entrée de ce niveau par la direction d */
			else if (tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] == l.getWorldNum()) {
				tmpMat[l.enterPos(d).getX()][l.enterPos(d).getY()] = tmpCell1;
			}
			/* Sinon mouvement simple */
			else {
				/* On conserve le Cell situé juste après les coordonnées o */
				tmpCell2 = tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)];
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

	/**							Méthode utilisé avec un joueur monde pour les mouvements récursive avec chainage + absorption	

		Effectue les mouvements à partir de la coordonnée o (position du joueur) du niveau l (niveau où se trouve le joueur) vers la direction d en fonction du nombre 
		de mouvement nMove (renvoyé par la méthode getNumMove) sur un ou éventuellement plusieurs niveaux.
	*/
	public void playerWorldMove(CoordSet o, Direction d, LevelMove l, int nMove) {
		int [][] tmpMat = l.getLevelData(), tmpMat2;
		LevelMove tmpL = l, tmpL2 = l;
		int n = nMove, tmpCell1 = Cells.VIDE, tmpCell2;

		/* Joueur à l'autorisation pour absorber */
		if (worldAccesAbs[LevelData.getNumPlayerWorld()] >= 1) {
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
			
				/* Si le Cell positionné sur les coordonnées o est un monde (ou un joueur monde) et qu'on peut y entrer (par absorption ou non) */
				if (tmpL.isAWorld(o.getX(), o.getY()) || tmpL.isAPlayerWorld(o.getX(), o.getY())) {	
					/* Même principe que la méthode move avec une condition en plus (celle avec le joueur monde) */
					while (tmpL.isAWorld(o.getX(), o.getY())|| tmpL.isAPlayerWorld(o.getX(), o.getY())) {
						/* Non absorption */
						if (tmpL.isAPlayerWorld(o.getX(), o.getY())) {
							/* On garde la matrice du monde courant */
							tmpMat2 = tmpL2.getLevelData();
							/* On récupère le monde où on veut entrer du niveau courant */
							tmpL = univ.get(LevelData.getNumPlayerWorld());
							/* On récupère la matrice de ce monde */
							tmpMat = tmpL.getLevelData();
							/* On conserve le Cell positionné après ce monde */
							tmpCell2 = tmpMat2[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)];
							/* On fait le mouvement de ce monde */
							tmpMat2[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] = tmpMat2[o.getX()][o.getY()];
							/* On fait le mouvement du Cell précédent ce monde */
							tmpMat2[o.getX()][o.getY()] = tmpCell1;
							/* On change les coordonnées de o avec celles de l'entrée du monde */
							o.chgCoord(tmpL.enterPos(CoordSet.revDirection(d)).getX(), tmpL.enterPos(CoordSet.revDirection(d)).getY());
							/* On récupère le Cell conservé précédemment (celui qui était après le monde) */
							tmpCell1 = tmpCell2;
							/* On conserve le Cell positionné à l'entrée de ce monde */
							tmpCell2 = tmpMat[o.getX()][o.getY()];
							/* On inverse la direction */
							d = CoordSet.revDirection(d);
						}	
						else if (worldAcces[tmpMat[o.getX()][o.getY()]] >= 1) {
							/* On récupère le monde où on veut entrer du niveau courant */
							tmpL = univ.get(tmpMat[o.getX()][o.getY()]);
							/* On conserve le Cell positionné à l'entrée de ce monde */
							tmpCell2 = univ.get(tmpMat[o.getX()][o.getY()]).getLevelData(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
							/* On change les coordonnées de o avec celles de l'entrée du monde */
							o.chgCoord(tmpL.enterPos(d).getX(), tmpL.enterPos(d).getY());
							/* On récupère la matrice de ce monde */
							tmpMat = tmpL.getLevelData();
							/* On change le numéro du monde extérieur du niveau où on va (potentiellement) entrer */
							if (tmpL.isAWorld(o.getX(), o.getY()) && worldAcces[tmpMat[o.getX()][o.getY()]] >= 1)	
								univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL2.getWorldNum());
						} 
						/* Absorption */
						else if (worldAccesAbs[tmpMat[o.getX()][o.getY()]] >= 1) {
							tmpL2 = tmpL;
							/* On garde la matrice du monde courant */
							tmpMat2 = tmpL2.getLevelData();
							/* On récupère le monde où on veut entrer du niveau courant */
							tmpL = univ.get(tmpMat2[o.getX()][o.getY()]);
							/* On récupère la matrice de ce monde */
							tmpMat = tmpL.getLevelData();
							/* On conserve le Cell positionné après ce monde */
							tmpCell2 = tmpMat2[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)];
							/* On fait le mouvement de ce monde */
							tmpMat2[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] = tmpMat2[o.getX()][o.getY()];
							/* On fait le mouvement du Cell précédent ce monde */
							tmpMat2[o.getX()][o.getY()] = tmpCell1;
							/* On change les coordonnées de o avec celles de l'entrée du monde */
							o.chgCoord(tmpL.enterPos(CoordSet.revDirection(d)).getX(), tmpL.enterPos(CoordSet.revDirection(d)).getY());
							/* On récupère le Cell conservé précédemment (celui qui était après le monde) */
							tmpCell1 = tmpCell2;
							/* On conserve le Cell positionné à l'entrée de ce monde */
							tmpCell2 = tmpMat[o.getX()][o.getY()];
							/* On inverse la direction */
							d = CoordSet.revDirection(d);
						}
						/* Sinon mouvement simple */
						else {
							/* On fait le mouvement */
							tmpMat[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] = tmpCell1;
							/* On récupère le Cell conservé précédemment */
							tmpCell1 = tmpCell2;
							/* On avance les coordonnées o */
							o.addToCoordWithDir(d);
							break;
						}

						if (!tmpL.isAWorld(o.getX(), o.getY())) {
							/* On fait le mouvement */
							tmpMat[o.getX()][o.getY()] = tmpCell1;
							/* Si on a déplacé un monde, on change le numéro de son monde extérieur */
							if (tmpL.isAWorld(o.getX(), o.getY()))
								univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
							/* On récupère le Cell conservé précédemment */
							tmpCell1 = tmpCell2;
							/* On conserve le niveau courant */
							tmpL2 = tmpL;
							break;
						}
						else if (tmpL.isAWorld(o.getX(), o.getY()) && (worldAcces[tmpMat[o.getX()][o.getY()]] == 0 && worldAccesAbs[tmpMat[o.getX()][o.getY()]] == 0)) {
							/* On fait le mouvement */
							tmpMat[o.getX()][o.getY()] = tmpCell1;
							/* Si on a déplacé un monde, on change le numéro de son monde extérieur */
							if (tmpL.isAWorld(o.getX(), o.getY()))
								univ.get(tmpMat[o.getX()][o.getY()]).setOutsideWorld(tmpL.getWorldNum());
							/* On récupère le Cell conservé précédemment */
							tmpCell1 = tmpCell2;
							/* On conserve le niveau courant */
							tmpL2 = tmpL;
							break;
						}
					}
				}
				/* Sinon mouvement simple */
				else {
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
		/* Sinon le joueur se comporte comme un joueur simple */
		else
			move(o, d, l, nMove);
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
		return -1; // par défaut valeur quelconque
	}

	/**
		Initialise à 0 le tableau déclaré au début de la classe
	*/
	public void initWorldAcces() {
		int i;
		
		this.worldAcces = new int[this.getTaille()];
		this.worldAccesAbs = new int[this.getTaille()];
		for (i = 0; i < this.getTaille(); i++) {
			this.worldAcces[i] = 0;
			this.worldAccesAbs[i] = 0;
		}
	}

	/**
		Remet à 0 le tableau déclaré au début de la classe
	*/
	public void resetWorldAcces() {
		int i;
		
		for (i = 0; i < this.getTaille(); i++) {
			this.worldAcces[i] = 0;
			this.worldAccesAbs[i] = 0;
		}
	}

    /**
		Renvoie le numéro du monde qui contient le numéro du monde numWorld se trouvant dans le niveau l
	*/
    public int whereWorldIs(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	/* Si le numéro numWorld correspond au numéro du monde du joueur on modifie numWorld avec la valeur identifiant le joueur */
    	if (numWorld == LevelData.getNumPlayerWorld())
    		numWorld = Cells.JOUEUR;

    	for (i = 0; i < l.getSizeMat(); i++)
    		for (j = 0; j < l.getSizeMat(); j++) {
    			if (l.isAPlayerWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld)
    					return l.getWorldNum();
    				else if (isWorldIn(univ.get(LevelData.getNumPlayerWorld()), numWorld))
    					return LevelData.getNumPlayerWorld();
    			}
    			else if (l.isAWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld)
    					return l.getWorldNum();
    				else if (isWorldIn(univ.get(tmpMat[i][j]), numWorld))
    					return tmpMat[i][j];
    			}
    		}
    	return -5; // par défaut valeur quelconque
    }

    /**
		Renvoie true si le numéro du monde numWorld se trouve dans le niveau l ou même récursivement dans un autre monde qui le contient ...
		Sinon renvoie false
	*/
    public boolean isWorldIn(LevelMove l, int numWorld) {
    	int i, j;
    	int [][] tmpMat = l.getLevelData();
    	
    	/* Si le numéro numWorld correspond au numéro du monde du joueur on modifie numWorld avec la valeur identifiant le joueur */
    	if (numWorld == LevelData.getNumPlayerWorld())
    		numWorld = Cells.JOUEUR;

    	for (i = 0; i < l.getSizeMat(); i++)
    		for (j = 0; j < l.getSizeMat(); j++) {
    			if (l.isAPlayerWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld)
    					return true;
    				else if (isWorldIn(univ.get(LevelData.getNumPlayerWorld()), numWorld))
    					return isWorldIn(univ.get(LevelData.getNumPlayerWorld()), numWorld);
    			}	
				else if (l.isAWorld(i, j)) {
    				if (tmpMat[i][j] == numWorld)
    					return true;
    				else if (isWorldIn(univ.get(tmpMat[i][j]), numWorld))
    					return isWorldIn(univ.get(tmpMat[i][j]), numWorld);
    			}
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
    	
    	/* Si le numéro numWorld correspond au numéro du monde du joueur on modifie numWorld avec la valeur identifiant le joueur */
    	if (numWorld == LevelData.getNumPlayerWorld())
    		numWorld = Cells.JOUEUR;

    	for (i = 0; i < l.getSizeMat(); i++)
    		for (j = 0; j < l.getSizeMat(); j++)
    			if (tmpMat[i][j] == numWorld)
    				return true;
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