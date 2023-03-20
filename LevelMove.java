import java.io.*;
import java.util.*;
import java.lang.*;

public class LevelMove {
	private int[][] matrice;
	private int taille;
//	private CoordSet playerTarget;
//	private ArrayList<CoordSet> boxTarget;
	private ArrayList<CoordSet> listTarget;
	private int outsideWorld;
	private int worldNum;

	public LevelMove(int tailleMatrice) {
		this.matrice = new int[tailleMatrice][tailleMatrice];
		this.taille = tailleMatrice;
	//	this.boxTarget = new ArrayList<CoordSet>();
	//	this.playerTarget = new CoordSet(-1, -1);
		this.outsideWorld = 0;
		this.worldNum = 0;
	}

	public LevelMove(int [][] data, int tailleMatrice, int numMonde, int outsideWorld, ArrayList<CoordSet> listTarget) {
		this.matrice = new int[tailleMatrice][tailleMatrice];
		this.taille = tailleMatrice;
	//	this.boxTarget = new ArrayList<CoordSet>(listTarget);
	//	this.playerTarget = new CoordSet(-1, -1);
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

	public void initMatrice() {
		int i, j;

		for (i = 0; i < this.getSizeMat(); i++) {
			for (j = 0; j < this.getSizeMat(); j++) {
				if (i == getSizeMat() / 2 && j == 0) {
					this.matrice[i][j] = Cells.VIDE;
					continue;
				}

				if (i == 0 || i == this.getSizeMat() - 1 || j == 0 || j == this.getSizeMat() - 1) {
					this.matrice[i][j] = Cells.MUR;
				}
				else
					this.matrice[i][j] = Cells.VIDE;
				
			}
		}

        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2] = Cells.JOUEUR;
        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2 + 1] = Cells.mondeNum(0);
/*
        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2 - 1] = Cells.BOITE;
        this.matrice[this.getSizeMat()/2 + 1][this.getSizeMat()/2 - 1] = Cells.mondeNum(3);
        this.matrice[this.getSizeMat()/2][this.getSizeMat()/2 - 1] = Cells.BOITE;
        
        this.matrice[this.getSizeMat()/2 - 1][this.getSizeMat()/2] = Cells.mondeNum(1);
        this.matrice[this.getSizeMat()/2 - 2][this.getSizeMat()/2 - 1] = Cells.mondeNum(2);
*/
        //putPlayerTarget(1, 1);
        //boxTarget.add(new CoordSet(1, 4));
	}

	public void initBoite(int numVal) {
		int i, j, n = this.getSizeMat();

		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				if (i == n/2 && (j == 0 || j == n - 1)) {
					this.matrice[i][j] = Cells.VIDE;
					continue; 
				}
				if (i == 0 && j == n/2) {
					this.matrice[i][j] = Cells.VIDE;
					continue;
				}
				if (i == n - 1 && j == n/2) {
					this.matrice[i][j] = Cells.VIDE;
					continue;
				}

				if (i == 0 || i == n - 1 || j == 0 || j == n - 1) {
					this.matrice[i][j] = Cells.MUR;
				}
				else
					this.matrice[i][j] = Cells.VIDE;
				
				/*
				if (i == getSizeMat() / 2 - 1 && (j == getSizeMat()/2 - 1 || j == getSizeMat()/2 || j == getSizeMat()/2 + 1)) {
					matrice[i][j] = Cells.VIDE;
				}
				else if (i == getSizeMat() / 2 && (j == getSizeMat()/2 - 1 || j == getSizeMat()/2 || j == getSizeMat()/2 + 1)) {
					matrice[i][j] = Cells.VIDE;
				}
				else if (i == getSizeMat() / 2 + 1 && (j == getSizeMat()/2 - 1 || j == getSizeMat()/2 || j == getSizeMat()/2 + 1)) {
					matrice[i][j] = Cells.VIDE;
				}
				else if (i == getSizeMat() / 2 || j == getSizeMat() / 2) {
					matrice[i][j] = Cells.VIDE;
				}
				else
					matrice[i][j] = Cells.MUR;*/
				/*
				if (i == getSizeMat() - 1 && j == getSizeMat() / 2) {
					this.matrice[i][j] = Cells.VIDE;
				}
				else
					this.matrice[i][j] = Cells.MUR;*/
			}
		}

		this.outsideWorld = 0;
		this.worldNum = numVal;

		//putPlayerTarget(n - 2, n - 2);
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


	public int[][] getLevelData() {
		return this.matrice;
	}

	public int getLevelData(int x, int y) {
		return this.matrice[x][y];
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
    	return new CoordSet(-2, -2);
    }

    public boolean winConditionMet() {
    	int i;

		for (i = 0; i < getListTarget().size(); i++) {
    		if (matrice[getListTarget().get(i).getX()][getListTarget().get(i).getY()] == Cells.VIDE) {
    			return false;
    		}
    	}
    	return true;
    }
/*
    public void putPlayerTarget(int x, int y) {
    	this.playerTarget = new CoordSet(x, y);  
    }

    public CoordSet getPlayerTarget() {
    	return this.playerTarget;
    }

    public ArrayList<CoordSet> getBoxTarget() {
    	return this.boxTarget;
    }
*/
    public ArrayList<CoordSet> getListTarget() {
    	return this.listTarget;
    }

    public boolean isAWorld(int x, int y) {
    	if (this.matrice[x][y] >= 0) {
    		return true;
    	}
    	return false;
    }

    public int getOutsideWorld() {
    	return this.outsideWorld;
    }

     public void setOutsideWorld(int val) {
    	this.outsideWorld = val;
    }

    public int getWorldNum() {
    	return this.worldNum;
    }

    public CoordSet getPosWorld(int numWorld) {
    	int i, j;

    	for (i = 0; i < getSizeMat(); i++) {
    		for (j = 0; j < getSizeMat(); j++) {
    			if (matrice[i][j] == numWorld) {
    				return new CoordSet(i, j);
    			}
    		}
    	}
    	return new CoordSet(-3, -3);
    }

    public void swapEnHaut(CoordSet c) {
    	int tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX() - 1][c.getY()];
    	matrice[c.getX() - 1][c.getY()] = tmp;
    }

    public void swapEnBas(CoordSet c) {
    	int tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX() + 1][c.getY()];
    	matrice[c.getX() + 1][c.getY()] = tmp;
    }
    
    public void swapAGauche(CoordSet c) {
    	int tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX()][c.getY() - 1];
    	matrice[c.getX()][c.getY() - 1] = tmp;
    }
    
    public void swapADroite(CoordSet c) {
    	int tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[c.getX()][c.getY() + 1];
    	matrice[c.getX()][c.getY() + 1] = tmp;
    }

	public CoordSet EnterPos(Diretion d){
		if (d == Direction.HAUT) {
			return CoordSet(getSizeMat() - 1, getSizeMat()/2);
		}
		if (d == Direction.BAS) {
			return CoordSet(0, getSizeMat()/2);
		}
		if (d == Direction.GAUCHE) {
			return CoordSet(getSizeMat()/2, getSizeMat() - 1);
		}
		if (d == Direction.DROITE) {
			return CoordSet(getSizeMat() /2, 0);
		}
	}
}