import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
    Classe permettant de lancer le jeu   
*/
public class ApplicationSokoban {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Argument non reconnu :(");
            //System.out.println("Il faut 2 argument : \n1 - le mode de jeu (classique ou récursive)\n2 - le nom du fichier de niveau (chemin absolu menant au fichier si nécessaire)");
            System.out.println("1 - le mode de jeu (classique ou récursive)");
            System.out.println("2 - le type d'affichage (ascii ou graphique)");
            System.out.println("3 - le nom du fichier de niveau (chemin absolu menant au fichier si nécessaire)");
            return;
        }

        LevelData data = new LevelData(args[2]);
        data.loadFromFile(); // on récupére les données du fichier

        /* Séléction du mode de jeu */
        switch(args[0]) {
            case "classique":
                jeuClassique(data, args[1]);
                break;
            case "récursive":
                jeuRecursive(data, args[1]);
                break;
            default:
                System.out.println("argument non reconnu");
                break;
        }
        System.out.println("Fin du jeu");
        System.exit(0);
    }

    /**
        Mode de jeu classique avec chainage
    */
    public static void jeuClassique(LevelData data, String typeAff) throws Exception {
        LevelMove l = data.getListData().get(0);
        Frame frame = null;
    
        switch(typeAff) {
            case "ascii":
                l.displayInTerminal(data);
                break;
            case "graphique":
                frame = new Frame(l);
                break;
            default:
                System.out.println("argument non reconnu");
                return;
        }
        /* On continue le jeu tant que le niveau n'est pas réussi */
        while (!l.winConditionMet()) {   
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Utiliser touche q pour aller à gauche, z (en haut), s (en bas), d (à droite)");

            System.out.print("--> ");

            String cl = br.readLine();
            System.out.println("");
        
            switch (cl) {
                case "q":
                    if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                        l.move(l.playerSpawn(), Direction.GAUCHE);
                    break;
                case "z":
                    if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                        l.move(l.playerSpawn(), Direction.HAUT);
                    break;
                case "d":
                    if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                        l.move(l.playerSpawn(), Direction.DROITE);
                    break;
                case "s":
                    if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                        l.move(l.playerSpawn(), Direction.BAS);
                    break;
                default:
                    System.out.println("Mauvaise touche !");
            }

            l.displayInTerminal(data);
            //frame.getDisplay().maj();
            
        /*    switch(typeAff) {
                case "ascii":
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                    System.out.println("Utiliser touche q pour aller à gauche, z (en haut), s (en bas), d (à droite)");

                    System.out.print("--> ");

                    String cl = br.readLine();
                    System.out.println("");
                
                    switch (cl) {
                        case "q":
                            if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                                l.move(l.playerSpawn(), Direction.GAUCHE);
                            break;
                        case "z":
                            if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                                l.move(l.playerSpawn(), Direction.HAUT);
                            break;
                        case "d":
                            if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                                l.move(l.playerSpawn(), Direction.DROITE);
                            break;
                        case "s":
                            if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                                l.move(l.playerSpawn(), Direction.BAS);
                            break;
                        default:
                            System.out.println("Mauvaise touche !");
                    }

                    l.displayInTerminal(data);
                    break;
                case "graphique":
                    break;
            }*/
        }
        /* Succès du niveau */
        System.out.println("Niveau réussi !");
    }

    /**
        Mode de jeu récursive avec les possibilités suivantes : chainage, poussette, absorbeur et le joueur est un monde
    */
    public static void jeuRecursive(LevelData data, String typeAff) throws Exception {
        Univers univ = new Univers(data.getListData(), data.depthLevel());
        Frame frame = null;
        
        int playerWorld, numRes;
        int numMove, initMove = 0;
        CoordSet resC = new CoordSet(0, 0);
        
        univ.initWorldAcces(); // initialisation des 2 tableaux pour les accès aux mondes 

        playerWorld = univ.getPlayerSpawnWorld(); // on récupère le numéro du monde où se trouve le joueur
        /*
        univ.getUnivers().get(playerWorld).displayInTerminal(data); // affichage de ce monde en ascii 
        //Frame frame = new Frame(univ.getUnivers().get(playerWorld));
        Frame frame = new Frame(univ);*/

        switch(typeAff) {
            case "ascii":
                univ.getUnivers().get(playerWorld).displayInTerminal(data);
                break;
            case "graphique":
                frame = new Frame(univ);
                break;
            default:
                System.out.println("argument non reconnu");
                return;
        }

        /* On continue le jeu tant que le niveau n'est pas réussi */
        while (!univ.winConditionMetUniv()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Utiliser touche q pour aller à gauche, z (en haut), s (en bas), d (à droite)");

            System.out.print("--> ");

            String cl = br.readLine();
            System.out.println("");

            switch (cl) {
                case "q" : 
                    /* On récupére un numéro de monde */
                    numRes = univ.getLevelPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), resC);
                    /* S'il est valable alors on essaye de faire un mouvement avec poussette */
                    if (numRes >= 0) {
                        if ((numMove = univ.getNumPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), initMove)) > 0)
                            univ.pockableMove(resC, Direction.GAUCHE, univ.getUnivers().get(numRes), numMove);
                    }
                    /* Mouvement pour un joueur monde */
                    else if (univ.getUnivers().get(playerWorld).playerIsAWorld() && univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld), initMove);
                        univ.playerWorldMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Mouvement pour un joueur simple */
                    else if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld), initMove);
                        univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.GAUCHE, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Sinon pas de mouvement possible */
                    break;
                case "z" : 
                    /* On récupére un numéro de monde */
                    numRes = univ.getLevelPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), resC);
                    /* S'il est valable alors on essaye de faire un mouvement avec poussette */
                    if (numRes >= 0) {
                        if ((numMove = univ.getNumPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), initMove)) > 0)
                            univ.pockableMove(resC, Direction.HAUT, univ.getUnivers().get(numRes), numMove);
                    }
                    /* Mouvement pour un joueur monde */
                    else if (univ.getUnivers().get(playerWorld).playerIsAWorld() && univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld), initMove);
                        univ.playerWorldMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Mouvement pour un joueur simple */
                    else if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld))) { 
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld), initMove);
                        univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.HAUT, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Sinon pas de mouvement possible */
                    break;
                case "d" : 
                    /* On récupére un numéro de monde */
                    numRes = univ.getLevelPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), resC);
                    /* S'il est valable alors on essaye de faire un mouvement avec poussette */
                    if (numRes >= 0) {
                        if ((numMove = univ.getNumPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), initMove)) > 0)
                            univ.pockableMove(resC, Direction.DROITE, univ.getUnivers().get(numRes), numMove);
                    }
                    /* Mouvement pour un joueur monde */
                    else if (univ.getUnivers().get(playerWorld).playerIsAWorld() && univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), initMove);
                        univ.playerWorldMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Mouvement pour un joueur simple */
                    else if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), initMove);
                        univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld), numMove);
                    }
                    /* Sinon pas de mouvement possible */
                    break;
                case "s" : 
                    /* On récupére un numéro de monde */
                    numRes = univ.getLevelPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), resC);
                    /* S'il est valable alors on essaye de faire un mouvement avec poussette */
                    if (numRes >= 0) {
                        if ((numMove = univ.getNumPockableMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld), univ.getUnivers().get(playerWorld), initMove)) > 0)
                            univ.pockableMove(resC, Direction.BAS, univ.getUnivers().get(numRes), numMove);   
                    }
                    /* Mouvement pour un joueur monde */
                    else if (univ.getUnivers().get(playerWorld).playerIsAWorld() && univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld), initMove);
                        univ.playerWorldMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Mouvement pour un joueur simple */
                    else if (univ.checkMovePossible(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld))) {
                        numMove = univ.getNumMove(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld), initMove);
                        univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.BAS, univ.getUnivers().get(playerWorld),numMove);                 
                    }
                    /* Sinon pas de mouvement possible */
                    break;
                default:
                    System.out.println("Mauvaise touche !");
            }

            playerWorld = univ.getPlayerSpawnWorld();
            univ.getUnivers().get(playerWorld).displayInTerminal(data);

            //frame.majFrame(univ);

            univ.resetWorldAcces(); // on réinitialise les accès aux mondes
        }
        /* Succès du niveau */
        System.out.println("Niveau réussi !");
    }
}



