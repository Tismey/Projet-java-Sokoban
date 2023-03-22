import java.io.*;
import java.util.*;
import java.lang.*;

public class LevelMove {
	private int[][] matrice;
	private int taille;
	private ArrayList<CoordSet> listTarget;
	private int outsideWorld;
	private int worldNum;

	public LevelMove(int tailleMatrice) {
		this.matrice = new int[tailleMatrice][tailleMatrice];
		this.taille = tailleMatrice;
		this.outsideWorld = 0;
		this.worldNum = 0;
	}

	public LevelMove(int [][] data, int tailleMatrice, int numMonde, int outsideWorld, ArrayList<CoordSet> listTarget) {
		this.matrice = new int[tailleMatrice][tailleMatrice];
		this.taille = tailleMatrice;
		this.listTarget = new ArrayList<CoordSet>(listTarget);
		this.outsideWorld = outsideWorld;
		this.worldNum = numMonde;

		int i, j;

		for (i = 0; i < tailleMatrice; i++) {
			for (j = 0; j < tailleMatrice; j++) {
				matrice[i][j] = data[i][j];
			}
		}
	}

	/*
		Affiche sur le terminal une représentation en ASCII du niveau
	*/
	public void displayInTerminal(LevelData data) {
		int i, j;
		CoordSet coord;

		for (i = 0; i < this.getSizeMat(); i++) {
			for (j = 0; j < this.getSizeMat(); j++) {
				if (this.matrice[i][j] == Cells.JOUEUR) {
					coord = new CoordSet(i, j);
					if (getListTarget().contains(coord)) {
						System.out.print("a");
						continue;
					}
					System.out.print("A");
				}
				if (this.matrice[i][j] == Cells.BOITE) {
					coord = new CoordSet(i, j);
					if (getListTarget().contains(coord)) {
						System.out.print("b");
						continue;
					}
					System.out.print("B");
				}

				if (this.matrice[i][j] == Cells.MUR) {
					System.out.print("#");
				}
				if (this.matrice[i][j] == Cells.VIDE) {
					coord = new CoordSet(i, j);
					if (getListTarget().contains(coord)) {
						System.out.print("@");
						continue;
					}
					System.out.print(" ");
				}
				if (isAWorld(i, j)) {
					coord = new CoordSet(i, j);
					if (getListTarget().contains(coord)) {
						System.out.print((char) (data.getName(matrice[i][j]) + 32));
						continue;
					}
					System.out.print(data.getName(matrice[i][j]));
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}

	/**
		Renvoie true si le mouvement du Cell sur les coordonnées o vers la direction d est possible
		Sinon renvoie false
	*/
	public boolean checkForMove(CoordSet o, Direction d) {
		if (matrice[o.getX()][o.getY()] == Cells.MUR)
			return false;

		if (matrice[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] == Cells.MUR)
			return false;
		return true;
	}

	/**
		Effectue les mouvements à partir de les coordonnées o (position du joueur) vers la direction d
	*/
	public void move(CoordSet o, Direction d) {
		if (matrice[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] == Cells.BOITE) {
			while (matrice[o.getAddToXWithDir(d)][o.getAddToYWithDir(d)] == Cells.BOITE)
				o.addToCoordWithDir(d);
			if (!checkForMove(o, d))
				return;
			swapToDir(o, d);
			while (matrice[o.getAddToXWithDir(CoordSet.revDirection(d))][o.getAddToYWithDir(CoordSet.revDirection(d))] != Cells.JOUEUR){
				swapToDir(o, CoordSet.revDirection(d));
				o.addToCoordWithDir(CoordSet.revDirection(d));
			}
			swapToDir(o, CoordSet.revDirection(d));
		} 
		else if (checkForMove(o, d))
			swapToDir(o, d);
	}
	
	/**
		Renvoie la matrice du niveau
	*/
	public int[][] getLevelData() {
		return this.matrice;
	}

	/**
		Renvoie le Cell situé sur les coordonnées (x, y) de la matrice 
	*/
	public int getLevelData(int x, int y) {
		return this.matrice[x][y];
	}

	/**
		Renvoie la taille de la matrice du niveau
	*/
	public int getSizeMat() {
    	return this.taille;
    }

    /**
		Renvoie les coordonnées du joueur dans la matrice du niveau 	
	*/
    public CoordSet playerSpawn() {
    	int i, j;

    	for (i = 0; i < getSizeMat(); i++) 
    		for (j = 0; j < getSizeMat(); j++) 
    			if (this.matrice[i][j] == Cells.JOUEUR) 
    				return new CoordSet(i, j);
    	return new CoordSet(-2, -2);
    }

    /**
		Renvoie true si toutes les cibles sont atteintes
		Sinon renvoie false
	*/
    public boolean winConditionMet() {
    	int i;

		for (i = 0; i < getListTarget().size(); i++) 
    		if (matrice[getListTarget().get(i).getX()][getListTarget().get(i).getY()] == Cells.VIDE)
    			return false;
    	return true;
    }

    /**
		Renvoie la liste des coordonnées des cibles
	*/
    public ArrayList<CoordSet> getListTarget() {
    	return this.listTarget;
    }

    /**
		Renvoie true si le Cell en coordonnées (x, y) de la matrice du niveau est un monde
		Sinon renvoie false 
	*/
    public boolean isAWorld(int x, int y) {
    	if (this.matrice[x][y] >= 0)
    		return true;
    	return false;
    }

    /**
		Renvoie le numéro du monde qui contient ce niveau 
	*/
    public int getOutsideWorld() {
    	return this.outsideWorld;
    }

    /**
		Initialise la variable du numéro du monde qui contient ce niveau à val
	*/
    public void setOutsideWorld(int val) {
    	this.outsideWorld = val;
    }

   	/**
		Renvoie le numéro de ce niveau
	*/
    public int getWorldNum() {
    	return this.worldNum;
    }

    /**
		Renvoie les coordonnées du monde avec le numéro numWorld dans ce niveau
	*/
    public CoordSet getPosWorld(int numWorld) {
    	int i, j;

    	for (i = 0; i < getSizeMat(); i++) 
    		for (j = 0; j < getSizeMat(); j++) 
    			if (matrice[i][j] == numWorld)
    				return new CoordSet(i, j);
    	return new CoordSet(-3, -3);
    }
    
	/**
    	Effectue un mouvement vers la direction d du Cell situé sur les coordonnées c
    */
	public void swapToDir(CoordSet c, Direction d) {
		int tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getAddToXWithDir(d)][c.getAddToYWithDir(d)];
    	matrice[c.getAddToXWithDir(d)][c.getAddToYWithDir(d)] = tmp;
	}

	/** 
		Renvoie les coordonnées correspondant à l'entrée de ce niveau par la direction d 
	*/
	public CoordSet enterPos(Direction d) {
		if (d == Direction.HAUT) {
			return new CoordSet(getSizeMat() - 1, getSizeMat()/2);
		}
		if (d == Direction.BAS) {
			return new CoordSet(0, getSizeMat()/2);
		}
		if (d == Direction.GAUCHE) {
			return new CoordSet(getSizeMat()/2, getSizeMat() - 1);
		}
		if (d == Direction.DROITE) {
			return new CoordSet(getSizeMat() /2, 0);
		}
		return new CoordSet(-7, -7);
	}

	/**
		Renvoie true si les coordonnées o si situe sur les extrémités de la matrice de ce nivreau 
		et que la direction d se dirige vers l'extérieur de ce niveau.
		Sinon renvoie false
	*/
	public boolean onEdge(CoordSet o, Direction d) {
		if (d == Direction.HAUT && o.getX() == 0)
			return true;
		else if (d == Direction.BAS && o.getX() == getSizeMat() - 1) 
			return true;
		else if (d == Direction.GAUCHE && o.getY() == 0) 
			return true;
		else if (d == Direction.DROITE && o.getY() == getSizeMat() - 1)
			return true;
		return false;
	}
}