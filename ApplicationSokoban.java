import java.io.*;
import java.util.*;

public class ApplicationSokoban{
    public static void main(String [] args) throws Exception {
        boolean erreur = true;
        if (args.length != 2) {
            System.out.println("Argument non reconnu :(");
            System.out.println("Il faut 2 argument : \n1 - le mode de jeu (classique, récursive ...)\n2 - le nom du fichier de niveau (chemin absolu menant au fichier)");
            return;
        }

        LevelData data = new LevelData(args[1]);
        data.loadFromFile();

        do {
            switch(args[0]){
                case "classique":
                    erreur = false;
                    jeuClassique(data);
                    break;
                case "récursive":
                    erreur = false;
                    jeuRecursive(data);
                    break;
                default:
                    System.out.println("argument non reconnu");
                    break;
            }
        }
        while (erreur);
        System.out.println("Fin du jeu");
    }

    public static void jeuClassique(LevelData data) throws Exception {
        LevelMove l = data.getListData().get(0);
        l.displayInTerminal(data);

        while (!l.winConditionMet()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Utiliser touche h pour aller à gauche, k (en haut), j (en bas), l (à droite)");

            System.out.print("--> ");

            String cl = br.readLine();
            System.out.println("");         

            switch (cl) {
                case "h" : 
                    if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                        l.move(l.playerSpawn(), Direction.GAUCHE);
                    break;
                case "k" : 
                    if (l.checkForMove(l.playerSpawn(), Direction.HAUT)) 
                        l.move(l.playerSpawn(), Direction.HAUT);
                    break;
                case "l" : 
                    if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                        l.move(l.playerSpawn(), Direction.DROITE);
                    break;
                case "j" : 
                    if (l.checkForMove(l.playerSpawn(), Direction.BAS)) 
                        l.move(l.playerSpawn(), Direction.BAS);                 
                    break;
                default : System.out.println("Mauvaise touche !");  
            }
        
            l.displayInTerminal(data);
        }
        System.out.println("Niveau réussi !");
    }

    public static void jeuRecursive(LevelData data) throws Exception {
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