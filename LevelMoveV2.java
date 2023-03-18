import java.io.*;
import java.util.*;
import java.lang.*;

public class LevelMoveV2 {
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
				
				
				
			}
		}

		this.outsideWorld = 0;
		this.worldNum = numVal;

		
	}

	public boolean checkForMove(CoordSet o, Direction d) {
		if (matrice[o.getX()][o.getY()] == Cells.MUR) {
			return false;
		}

		CoordSet nextSpot = CoordSet.AddVec(o, CoordSet.DirToVec(d));

		if (matrice[nextSpot.getX()][nextSpot.getY()] == Cells.MUR) {
			return false;
		}
		if (matrice[nextSpot.getX()][nextSpot.getY()] == Cells.BOITE) {
			if(!checkForMove(nextSpot, d)){
				return false;
			}
		}
		
		move(o, d);
		return true;
	}

	public void move(CoordSet o, Direction d) {
		swapCoords(o,d);
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
	public void swapCoords(CoordSet c, Direction d){
		CoordSet nextSpot = CoordSet.AddVec(c, CoordSet.DirToVec(d));
		int tmp = matrice[c.getX()][c.getY()];
    	matrice[c.getX()][c.getY()] = matrice[nextSpot.getX()][nextSpot.getY()];
    	matrice[nextSpot.getX()][nextSpot.getY()] = tmp;
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
}