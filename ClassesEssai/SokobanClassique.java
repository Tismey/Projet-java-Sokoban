import java.io.*;

public class SokobanClassique {
	public static void main (String args []) throws Exception {
		LevelMove mat = new LevelMove(13);

		mat.initMatrice();

		mat.displayInTerminal();

		while (!mat.winConditionMet()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Utiliser touche h pour aller à gauche, k (en haut), j (en bas), l (à droite)");

			System.out.print("--> ");

			String cl = br.readLine();
			System.out.println("");			

			switch (cl) {
				case "h" : 
					if (mat.checkForMove(mat.playerSpawn(), Direction.GAUCHE))
						mat.move(mat.playerSpawn(), Direction.GAUCHE);
					break;
				case "k" : 
					if (mat.checkForMove(mat.playerSpawn(), Direction.HAUT)) 
						mat.move(mat.playerSpawn(), Direction.HAUT);
					break;
				case "l" : 
					if (mat.checkForMove(mat.playerSpawn(), Direction.DROITE))
						mat.move(mat.playerSpawn(), Direction.DROITE);
					break;
				case "j" : 
					if (mat.checkForMove(mat.playerSpawn(), Direction.BAS)) 
						mat.move(mat.playerSpawn(), Direction.BAS);					
					break;
				default : System.out.println("Mauvaise touche !");	
			}
		
			mat.displayInTerminal();
		}
		System.out.println("Niveau réussi !");
	}
}