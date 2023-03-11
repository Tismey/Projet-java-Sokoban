import java.io.*;
import java.util.*;

public class RecursiveSokoban {
	public static void main(String [] args) throws Exception {
		LevelData data = new LevelData("/Users/lyes/Desktop/outlevels/carry.txt");
		data.loadFromFile();
//		data.printNameWorld();
		Univers univ = new Univers(data.getListData(), data.depthLevel());
//		LevelMove main = new LevelMove(7);
/*		LevelMove boite1 = new LevelMove(7);
		LevelMove boite2 = new LevelMove(7);
		LevelMove boite3 = new LevelMove(7);*/
		int playerWorld;
		int numMove, initMove = 0;
		
//		main.initMatrice();
/*		boite1.initBoite(1);
		boite2.initBoite(2);
		boite3.initBoite(3);*/
		//boite2.putPlayerTarget(boite2.getSizeMat() - 2, boite2.getSizeMat() - 2);

//		univ.addLevel(main);
/*		univ.addLevel(boite1);
		univ.addLevel(boite2);
		univ.addLevel(boite3);
*/
//		System.out.println("Taille = "+univ.getTaille());
		univ.initWorldAcces();

		playerWorld = univ.getPlayerSpawnWorld();

		univ.getUnivers().get(playerWorld).displayInTerminal(data);
	//	univ.getUnivers().get(4).displayInTerminal();

		/*
		System.out.println("Main");
		univ.getUnivers().get(0).displayInTerminal();

		System.out.println("Boite");
		univ.getUnivers().get(1).displayInTerminal();
		*/
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
		/*	univ.getUnivers().get(0).displayInTerminal(data);
			univ.getUnivers().get(3).displayInTerminal(data);
			univ.getUnivers().get(6).displayInTerminal(data);
			univ.getUnivers().get(2).displayInTerminal();
			univ.getUnivers().get(3).displayInTerminal();*/
			univ.resetWorldAcces();
		}
		System.out.println("Niveau réussi !");
	}
}