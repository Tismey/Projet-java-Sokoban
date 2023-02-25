import java.io.*;
import java.util.*;
import java.lang.*;

public class LevelMove {
	private Cells[][] matrice;
	private int taille;
	private CoordSet playerTarget;
	private ArrayList<CoordSet> boxTarget;

	public LevelMove(int tailleMatrice) {
		this.matrice = new Cells[tailleMatrice][tailleMatrice];
		this.taille = tailleMatrice;
		this.boxTarget = new ArrayList<CoordSet>();
	}

	public void displayInTerminal() {
		int i, j;

		for (i = 0; i < this.getSizeMat(); i++) {
			for (j = 0; j < this.getSizeMat(); j++) {
				if (this.matrice[i][j] == Cells.JOUEUR) {
					if (getPlayerTarget().getX() == i && getPlayerTarget().getY() == j) {
						System.out.print("a");
						continue;
					}
					System.out.print("A");
				}
				if (this.matrice[i][j] == Cells.BOITE) {
					CoordSet coord1 = new CoordSet(i, j);
					if (getBoxTarget().contains(coord1)) {
						System.out.print("b");
						continue;
					}
					System.out.print("B");
				}

				if (this.matrice[i][j] == Cells.MUR) {
					System.out.print("#");
				}
				if (this.matrice[i][j] == Cells.VIDE) {
					CoordSet coord2 = new CoordSet(i, j);
					if (getPlayerTarget().getX() == i && getPlayerTarget().getY() == j) {
						System.out.print("&");
						continue;
					}
					if (getBoxTarget().contains(coord2)) {
						System.out.print("@");
						continue;
					}
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public void initMatrice() {
		int i, j;

		for (i = 0; i < this.getSizeMat(); i++) {
			for (j = 0; j < this.getSizeMat(); j++) {
				if (i == 0 || i == this.getSizeMat() - 1 || j == 0 || j == this.getSizeMat() - 1) {
					this.matrice[i][j] = Cells.MUR;
				}
				else
					this.matrice[i][j] = Cells.VIDE;
			}
		}

        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2 - 1] = Cells.JOUEUR;
        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2] = Cells.BOITE;
        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2 + 1] = Cells.BOITE;

        putPlayerTarget(5, 2);
        boxTarget.add(new CoordSet(1, 4));
	}

	public boolean checkForMove(CoordSet o, Direction d) {
		if (matrice[o.getX()][o.getY()] == Cells.MUR) {
			return false;
		}

		if (d == Direction.HAUT && (matrice[o.getX() - 1][o.getY()] == Cells.MUR)) {
			return false;
		}
		if (d == Direction.BAS && (matrice[o.getX() + 1][o.getY()] == Cells.MUR)) {
			return false;
		}
		if (d == Direction.GAUCHE && (matrice[o.getX()][o.getY() - 1] == Cells.MUR)) {
			return false;
		}
		if (d == Direction.DROITE && (matrice[o.getX()][o.getY() + 1] == Cells.MUR)) {
			return false;
		}

		return true;
	}

	public void move(CoordSet o, Direction d) {
		if (d == Direction.HAUT && matrice[o.getX() - 1][o.getY()] == Cells.BOITE) {
			o.addToX(-1);

			while (matrice[o.getX() - 1][o.getY()] == Cells.BOITE) {
				o.addToX(-1);
			}

			if (!checkForMove(o, d)) {
				return;
			}

			swapEnHaut(o);

			while (matrice[o.getX() + 1][o.getY()] != Cells.JOUEUR){
				swapEnBas(o);
				o.addToX(1);
			}
			
			swapEnBas(o);
		} else if (d == Direction.HAUT && checkForMove(o, d)) {
			swapEnHaut(o);
		}
		
		
		if (d == Direction.BAS && matrice[o.getX() + 1][o.getY()] == Cells.BOITE) {
			o.addToX(1);

			while (matrice[o.getX() + 1][o.getY()] == Cells.BOITE) {
				o.addToX(1);
			}

			if (!checkForMove(o, d)) {
				return;
			}

			swapEnBas(o);

			while (matrice[o.getX() - 1][o.getY()] != Cells.JOUEUR){
				swapEnHaut(o);
				o.addToX(-1);
			}
			
			swapEnHaut(o);
		} else if (d == Direction.BAS && checkForMove(o, d)) {
			swapEnBas(o);
		}
		

		if (d == Direction.GAUCHE && matrice[o.getX()][o.getY() - 1] == Cells.BOITE) {
			o.addToY(-1);

			while (matrice[o.getX()][o.getY() - 1] == Cells.BOITE) {
				o.addToY(-1);
			}

			if (!checkForMove(o, d)) {
				return;
			}

			swapAGauche(o);

			while (matrice[o.getX()][o.getY() + 1] != Cells.JOUEUR){
				swapADroite(o);
				o.addToY(1);
			}
			
			swapADroite(o);
		} else if (d == Direction.GAUCHE && checkForMove(o, d)) {
			swapAGauche(o);
		}

		if (d == Direction.DROITE && matrice[o.getX()][o.getY() + 1] == Cells.BOITE) {
			o.addToY(1);

			while (matrice[o.getX()][o.getY() + 1] == Cells.BOITE) {
				o.addToY(1);
			}

			if (!checkForMove(o, d)) {
				return;
			}

			swapADroite(o);

			while (matrice[o.getX()][o.getY() - 1] != Cells.JOUEUR){
				swapAGauche(o);
				o.addToY(-1);
			}
			
			swapAGauche(o);
		} else if (d == Direction.DROITE && checkForMove(o, d)) {
			swapADroite(o);
		}
	}


	public Cells[][] getLevelData() {
		return this.matrice;
	}

	public int getSizeMat() {
    	return this.taille;
    }

    public CoordSet playerSpawn() {
    	int i, j;

    	for (i = 0; i < getSizeMat(); i++) {
    		for (j = 0; j < getSizeMat(); j++) {
    			if (this.matrice[i][j] == Cells.JOUEUR) {
    				return new CoordSet(i, j);
    			}
    		}
    	}
    	return new CoordSet(-1, -1);
    }

    public boolean winConditionMet() {
    	int i;

    	for (i = 0; i < getBoxTarget().size(); i++) {
    		if (matrice[getBoxTarget().get(i).getX()][getBoxTarget().get(i).getY()] != Cells.BOITE) {
    			return false;
    		}
    	}
    	return playerSpawn().equals(getPlayerTarget());
    }

    public void putPlayerTarget(int x, int y) {
    	this.playerTarget = new CoordSet(x, y);  
    }

    public CoordSet getPlayerTarget() {
    	return this.playerTarget;
    }

    public ArrayList<CoordSet> getBoxTarget() {
    	return this.boxTarget;
    }

    public void swapEnHaut(CoordSet c) {
    	Cells tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX() - 1][c.getY()];
    	matrice[c.getX() - 1][c.getY()] = tmp;
    }

    public void swapEnBas(CoordSet c) {
    	Cells tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX() + 1][c.getY()];
    	matrice[c.getX() + 1][c.getY()] = tmp;
    }
    
    public void swapAGauche(CoordSet c) {
    	Cells tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX()][c.getY() - 1];
    	matrice[c.getX()][c.getY() - 1] = tmp;
    }
    
    public void swapADroite(CoordSet c) {
    	Cells tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX()][c.getY() + 1];
    	matrice[c.getX()][c.getY() + 1] = tmp;
    }
}