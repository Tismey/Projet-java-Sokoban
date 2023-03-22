import java.io.*;
import java.util.*;

public class LevelData {
	private ArrayList<LevelMove> listData;
	private String path;

	public LevelData(String path) {
		this.listData = new ArrayList<LevelMove>();
		this.path = path;
	}

	/**
		Permet de récupèrer les niveaux présent dans le nom fichier (chemin absolu) donné dans l'attribut path (grace au constructeur)
		afin de les ajouter dans l'attribut de type liste (listData) 
	*/
	public void loadFromFile() {
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);

			int nbMonde = depthLevel(); // nbre de niveau
			int numWorld = 0;

			while (numWorld < nbMonde) {	
				int [][] data;
				int taille;
				int outsideWorld = 0;
				ArrayList<CoordSet> listTarget = new ArrayList<CoordSet>();

				outsideWorld = getOutsideWorld((char) s.read(), nbMonde);

				s.read(); // espace

				taille = Character.getNumericValue((char)s.read()); // taille du niveau de numéro numWorld
				
				int res, k = 0; 
				int i = 0, j = 0;

				data = new int[taille][taille];

				s.read(); // saut de ligne
				
				/* Lecture et initialisation de la matrice du niveau de numéro numWorld */
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

				s.read(); // saut de ligne

				listData.add(new LevelMove(data, taille, numWorld, outsideWorld, listTarget));
				numWorld++;
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
		Renvoie le nombre de niveau dans le fichier
	*/
	public int depthLevel() {
		int res = 0;
		
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);
			int i, taille;
			
			while(s.read() != -1) {
				s.read(); // espace
				i = 0;
				taille = Character.getNumericValue((char)s.read());

				s.read(); // saut de ligne
				
				/* Lecture de la matrice du niveau */
				while (i < (taille * taille) + taille) {
					s.read();
					i++;
				}
				s.read(); // saut de ligne
				res++;
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
		Renvoie un tableau à deux dimensions avec tous les noms des mondes présent dans le fichier 
		avec chacun le nom des mondes qu'ils contiennent 
	*/
	public char [][] getNameWorld(int nbMonde) {
		char [][] tabName = new char[nbMonde][nbMonde];
		
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);
			int tmp, i, j, k = 0, taille;
			
			while(k < nbMonde) {
				j = 1;
				tabName[k][0] = (char) s.read(); // initialisation avec le nom du niveau k

				if (nbMonde == 1) {
					return tabName;
				}

				s.read(); // espace
				i = 0;
				taille = Character.getNumericValue((char)s.read());

				s.read(); // saut de ligne
				
				/* Lecture et initialisation du tableau avec le nom des mondes présents dans le niveau k */
				while (i < (taille * taille) + taille) {
					tmp = s.read();
					if (tmp >= 67 && tmp <= 90 && j < nbMonde) {
						tabName[k][j] = (char) tmp;
						j++;
					}
					i++;
				}
				s.read(); // saut de ligne
				
				/* On remplit le reste non utilisé par un caractère par défaut */
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

	/**
		Renvoie le numéro du monde avec le nom nameWorld	
	*/
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

	/**
		Renvoie le numéro du monde extérieur du niveau avec le nom nameWorld	
	*/
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

	/**
		Renvoie le nom du monde avec le numéro du monde numWorld	
	*/
	public char getName(int numWorld) {
		char [][] tabName = getNameWorld(depthLevel());

		return tabName[numWorld][0];
	}

	/**
		Renvoie la liste avec tous les niveaux du fichier	
	*/
	public ArrayList<LevelMove> getListData() {
		return this.listData;
	}
}