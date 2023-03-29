import java.io.*;
import java.util.*;

public class RecursiveSokoban {
	public static void main(String [] args) throws Exception {
		LevelData data = new LevelData(args[1]);
		data.loadFromFile();
		Univers univ = new Univers(data.getListData(), data.depthLevel());
		int playerWorld;
		int numMove, initMove = 0;
		
		univ.initWorldAcces();

		playerWorld = univ.getPlayerSpawnWorld();

		univ.getUnivers().get(playerWorld).displayInTerminal(data);

		while (!univ.winConditionMetUniv()) {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Utiliser touche h pour aller à gauche, k (en haut), j (en bas), l (à droite)");

			System.out.print("--> ");

			String cl = br.readLine();
			System.out.println("");			

			switch (cl) {
				case "h" : 
					if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld))) {
						numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld), initMove);
						univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld),numMove);
					}
					break;
				case "k" : 
					if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld))) { 
						numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld), initMove);
						univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld),numMove);
					}
					break;
				case "l" : 
					if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld))) {
						numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), initMove);
						univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), numMove);
					}
					break;
				case "j" : 
					if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld))) {
						numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld), initMove);
						univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld),numMove);					
					}
					break;
				default : System.out.println("Mauvaise touche !");	
			}

			playerWorld = univ.getPlayerSpawnWorld();
			univ.getUnivers().get(playerWorld).displayInTerminal(data);
			univ.resetWorldAcces();
		}
		System.out.println("Niveau réussi !");
	}
}