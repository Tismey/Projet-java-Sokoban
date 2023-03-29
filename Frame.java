import javax.swing.*;
import java.io.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Frame extends JFrame implements KeyListener{
    private Display display;
    private LevelMove l;

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
}
