import javax.swing.*;
import java.io.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Frame extends JFrame implements KeyListener{
    private Display display;
    private LevelMove l;
    private int playerWorld, numRes;
    private int numMove, initMove = 0;
    private CoordSet resC = new CoordSet(0, 0);

    public Frame() throws IOException{
        super();
        display = new Display();
        l = new LevelMove(1);
        add(display);
        addKeyListener(this);
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public Frame(LevelMove level) throws IOException{
        super();
        display = new Display(level);
        l = level;
        add(display);
        this.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e){
                if(e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT)
                    System.out.println("Mauvaise touche pressée, les mouvements se font avec les touches directionnelles.");
            };
        
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT: 
                        if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                            l.move(l.playerSpawn(), Direction.GAUCHE);
                        display.maj();
                        if (l.winConditionMet()){
                            System.out.println("Niveau réussi !\nFin du jeu");
                            System.exit(0);
                        }
                        break;
                    case KeyEvent.VK_RIGHT: 
                        if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                            l.move(l.playerSpawn(), Direction.DROITE);
                        display.maj();
                        if (l.winConditionMet()){
                            System.out.println("Niveau réussi !\nFin du jeu");
                            System.exit(0);
                        }
                        break;
                    case KeyEvent.VK_UP: 
                        if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                            l.move(l.playerSpawn(), Direction.HAUT);
                        display.maj();
                        if (l.winConditionMet()){
                            System.out.println("Niveau réussi !\nFin du jeu");
                            System.exit(0);
                        }
                        break;
                    case KeyEvent.VK_DOWN: 
                        if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                            l.move(l.playerSpawn(), Direction.BAS);
                        display.maj();
                        if (l.winConditionMet()){
                            System.out.println("Niveau réussi !\nFin du jeu");
                            System.exit(0);
                        }
                        break;
                }
            };
        
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT: 
                        if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                            System.out.println("Gauche");
                        break;
                    case KeyEvent.VK_RIGHT: 
                        if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                            System.out.println("Droite");
                        break;
                    case KeyEvent.VK_UP: 
                        if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                            System.out.println("Haut");
                        break;
                    case KeyEvent.VK_DOWN: 
                        if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                            System.out.println("Bas");
                        break;
                }
            }
        });
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public Frame(Univers univ) throws IOException{
        super();
        display = new Display(univ);
        add(display);
        univ.initWorldAcces(); // initialisation des 2 tableaux pour les accès aux mondes 
        playerWorld = univ.getPlayerSpawnWorld(); // on récupère le numéro du monde où se trouve le joueur
        this.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e){
                if(e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT)
                    System.out.println("Mauvaise touche pressée, les mouvements se font avec les touches directionnelles.");
            };
        
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT: 
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
                    if(univ.winConditionMetUniv()){
                        System.out.println("Niveau réussi !\nFin du jeu");
                        System.exit(0);
                    }
                    try {
                            majFrame(univ);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    break;

                    case KeyEvent.VK_RIGHT: 
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
                        univ.move(univ.getUnivers().get(playerWorld).playerSpawn(), Direction.DROITE, univ.getUnivers().get(playerWorld),numMove);
                    }
                    /* Sinon pas de mouvement possible */
                    if(univ.winConditionMetUniv()){
                        System.out.println("Niveau réussi !\nFin du jeu");
                        System.exit(0);
                    }
                    try {
                        majFrame(univ);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                    case KeyEvent.VK_UP: 
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
                    if(univ.winConditionMetUniv()){
                        System.out.println("Niveau réussi !\nFin du jeu");
                        System.exit(0);
                    }
                    try {
                        majFrame(univ);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                    case KeyEvent.VK_DOWN: 
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
                    if(univ.winConditionMetUniv()){
                        System.out.println("Niveau réussi !\nFin du jeu");
                        System.exit(0);
                    }
                        try {
                            majFrame(univ);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    break;
                }
            };
        
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    System.out.println("ESPACE !");
            }
        });
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void keyTyped(KeyEvent e){
        if(e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT)
            System.out.println("Mauvaise touche pressée, les mouvements se font avec les touches directionnelles.");
    }

    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT: 
                if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                    l.move(l.playerSpawn(), Direction.GAUCHE);
                display.maj();
                if (l.winConditionMet()){
                    System.out.println("Niveau réussi !\nFin du jeu");
                    System.exit(0);
                }
                break;
            case KeyEvent.VK_RIGHT: 
                if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                    l.move(l.playerSpawn(), Direction.DROITE);
                display.maj();
                if (l.winConditionMet()){
                    System.out.println("Niveau réussi !\nFin du jeu");
                    System.exit(0);
                }
                break;
            case KeyEvent.VK_UP: 
                if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                    l.move(l.playerSpawn(), Direction.HAUT);
                display.maj();
                if (l.winConditionMet()){
                    System.out.println("Niveau réussi !\nFin du jeu");
                    System.exit(0);
                }
                break;
            case KeyEvent.VK_DOWN: 
                if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                    l.move(l.playerSpawn(), Direction.BAS);
                display.maj();
                if (l.winConditionMet()){
                    System.out.println("Niveau réussi !\nFin du jeu");
                    System.exit(0);
                }
                break;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT: 
                if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                    System.out.println("Gauche");
                break;
            case KeyEvent.VK_RIGHT: 
                if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                    System.out.println("Droite");
                break;
            case KeyEvent.VK_UP: 
                if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                    System.out.println("Haut");
                break;
            case KeyEvent.VK_DOWN: 
                if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                    System.out.println("Bas");
                break;
        }
    }

    public Display getDisplay(){
        return this.display;
    }

    public void majFrame(Univers univ) throws IOException{
        setVisible(false);
        remove(display);
        display = new Display(univ);
        add(display);
        setVisible(true);
        univ.resetWorldAcces();
        playerWorld = univ.getPlayerSpawnWorld();
    }
}
