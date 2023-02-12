import java.io.*;
import java.util.*;

public class Level_data implements Memory {

	private Cells[][] data;

/*	public Level_data(int tailleMatrice) {
		this.data = new Cells[tailleMatrice][tailleMatrice];
	}
*/

	public void loadFromFile(String path) {
		try {
			File f = new File(path);
			FileInputStream s = new FileInputStream(f);

			s.read();
			s.read();
			
			int k = 0, taille = Character.getNumericValue((char)s.read()); 
			int i = 0, j = taille - 1;

			this.data = new Cells[taille][taille];

			s.read();
			
			while (k < (taille * taille) + taille) {
				int res = s.read();
				
				if (i >= 9) {
					j--;
					i = 0;
				}

				if (res == 35) {
					data[i][j] = Cells.MUR;
					i++;
				}
				if (res == 32) {
					data[i][j] = Cells.VIDE;
					i++;
				}
				if (res == 65) {
					data[i][j] = Cells.JOUEUR;
					i++;
				}
				if (res == 66) {
					data[i][j] = Cells.BOITE;
					i++;
				}
				if (res >= 67 && res <= 90) {
					data[i][j] = Cells.NIVEAUX;
					i++;
				}

				k++;
			}
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   	/** A faire plus tard

    public void rLoadFromFile(String path, int depth) {

    }
    */

	public int depthLevel(String path) {
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
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}


}