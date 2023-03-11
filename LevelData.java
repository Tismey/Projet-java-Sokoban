import java.io.*;
import java.util.*;

public class LevelData {
	private ArrayList<LevelMove> listData;
	private String path;

	public LevelData(String path) {
		this.listData = new ArrayList<LevelMove>();
		this.path = path;
	}


	public void loadFromFile() {
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);
			
			//ArrayList<LevelMove> res = new ArrayList<LevelMove>();

			int nbMonde = depthLevel();
			int numWorld = 0;
	/*		
			int [][] data;
			int taille;
			int outsideWorld = 0;
			ArrayList<CoordSet> listTarget = new ArrayList<CoordSet>();
*/
			while (numWorld < nbMonde) {	
				int [][] data;
				int taille;
				int outsideWorld = 0;
				ArrayList<CoordSet> listTarget = new ArrayList<CoordSet>();

				outsideWorld = getOutsideWorld((char) s.read(), nbMonde);

				s.read();

				taille = Character.getNumericValue((char)s.read());
				
				int res, k = 0; 
				int i = 0, j = 0;

				data = new int[taille][taille];

				s.read();
				
				while (k < (taille * taille) + taille) {
					res = s.read();
					
					if (j >= taille) {
						i++;
						j = 0;
					}

					
					if (res == 64) {
						data[i][j] = Cells.VIDE;
						listTarget.add(new CoordSet(i, j));
						j++;
					}
					else if (res == 35) {
						data[i][j] = Cells.MUR;
						j++;
					}
					else if (res == 32) {
						data[i][j] = Cells.VIDE;
						j++;
					}
					else if (res == 65) {
						data[i][j] = Cells.JOUEUR;
						j++;
					}
					else if (res == 66) {
						data[i][j] = Cells.BOITE;
						j++;
					}
					else if (res >= 67 && res <= 90) {
						data[i][j] = Cells.mondeNum(getNumWorld((char) res, nbMonde));
						j++;
					}

					k++;
				}

				s.read();

				listData.add(new LevelMove(data, taille, numWorld, outsideWorld, listTarget));
				numWorld++;
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//return res;
	}

	public int depthLevel() {
		int res = 0;
		
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);
			int i, taille;
			
			while(s.read() != -1) {
				s.read();
				i = 0;
				taille = Character.getNumericValue((char)s.read());

				s.read();
				
				while (i < (taille * taille) + taille) {
					s.read();
					i++;
				}
				s.read();
				res++;
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public char [][] getNameWorld(int nbMonde) {
		char [][] tabName = new char[nbMonde][nbMonde];
		
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);
			int tmp, i, j, k = 0, taille;
			
			while(k < nbMonde) {
				j = 1;
				tabName[k][0] = (char) s.read();

				if (nbMonde == 1) {
					return tabName;
				}

				s.read();
				i = 0;
				taille = Character.getNumericValue((char)s.read());

				s.read();
				
				while (i < (taille * taille) + taille) {
					tmp = s.read();
					if (tmp >= 67 && tmp <= 90 && j < nbMonde) {
						tabName[k][j] = (char) tmp;
						j++;
					}
					i++;
				}
				s.read();
				
				while (j < nbMonde) {
					tabName[k][j] = '&';
					j++;
				}

				k++;
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tabName;
	}

	public int getNumWorld(char nameWorld, int nbMonde) {
		int i;
		char [][] tabName = getNameWorld(nbMonde);

		for (i = 0; i < nbMonde; i++) {
			if (nameWorld == tabName[i][0]) {
				return i;
			}
		}

		return -1;
	}

	public int getOutsideWorld(char nameWorld, int nbMonde) {
		int i, j;
		char [][] tabName = getNameWorld(nbMonde);

		for (i = 0; i < nbMonde; i++) {
			for (j = 1; j < nbMonde; j++) {
				if (tabName[i][j] == nameWorld) {
					return i;
				}
			}
		}
		return 0;
	} 

	public char getName(int numWorld) {
		char [][] tabName = getNameWorld(depthLevel());

		return tabName[numWorld][0];
	}

	public ArrayList<LevelMove> getListData() {
		return this.listData;
	}

	public void printNameWorld() {
		int i, j, n = depthLevel();
		char [][] tabName = getNameWorld(n);

		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				System.out.print(tabName[i][j]);
			}
			System.out.println("");
		}
	}
}