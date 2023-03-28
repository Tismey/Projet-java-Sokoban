import javax.swing.*;
import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Frame extends JFrame implements KeyListener{
    private Display panel;
    private LevelMove l;
    private LevelData d;

    public Frame(){
        super();
        l = new LevelMove(1);
        d = new LevelData(getName());
        add(panel);
        addKeyListener(this);
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public Frame(LevelMove level, LevelData data) throws IOException{
        super();
        panel = new Display(level, data);
        l = level;
        d = data;
        add(panel);
        this.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT: 
                        if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                            l.move(l.playerSpawn(), Direction.GAUCHE);
                        System.out.println("sex");
                        panel.maj();
                        break;
                    case KeyEvent.VK_RIGHT: 
                        if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                            l.move(l.playerSpawn(), Direction.DROITE);
                        panel.maj();
                        break;
                    case KeyEvent.VK_UP: 
                        if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                            l.move(l.playerSpawn(), Direction.HAUT);
                        panel.maj();
                        break;
                    case KeyEvent.VK_DOWN: 
                        if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                            l.move(l.playerSpawn(), Direction.BAS);
                        panel.maj();
                        break;
                }
            };
        
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.KEY_PRESSED)
                    System.out.println("Key pressed");
            };
        
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.KEY_RELEASED)
                    System.out.println("Key released");
            };
        });
        setSize(panel.getImages().get(0).getWidth(null)*l.getSizeMat(), panel.getImages().get(0).getWidth(null)*l.getSizeMat());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public boolean moveUp(KeyEvent e){
        l = d.getListData().get(0);
        
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                l.move(l.playerSpawn(), Direction.HAUT);
            panel.maj();
        } 
        return true;                    
    }

    public void moveDown(KeyEvent e){
        l = d.getListData().get(0);
        
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                l.move(l.playerSpawn(), Direction.BAS);
            panel.maj();
        }                     
    }

    public void moveRight(KeyEvent e){
        l = d.getListData().get(0);
        
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                l.move(l.playerSpawn(), Direction.DROITE);
            panel.maj();
        }                     
    }

    public boolean moveLeft(KeyEvent e){
        l = d.getListData().get(0);
        
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                l.move(l.playerSpawn(), Direction.GAUCHE);
            panel.maj();
        } 
        return true;                    
    }

    @Override
    public void keyTyped(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT: 
                if (l.checkForMove(l.playerSpawn(), Direction.GAUCHE))
                    l.move(l.playerSpawn(), Direction.GAUCHE);
                panel.maj();
                break;
            case KeyEvent.VK_RIGHT: 
                if (l.checkForMove(l.playerSpawn(), Direction.DROITE))
                    l.move(l.playerSpawn(), Direction.DROITE);
                panel.maj();
                break;
            case KeyEvent.VK_UP: 
                if (l.checkForMove(l.playerSpawn(), Direction.HAUT))
                    l.move(l.playerSpawn(), Direction.HAUT);
                panel.maj();
                break;
            case KeyEvent.VK_DOWN: 
                if (l.checkForMove(l.playerSpawn(), Direction.BAS))
                    l.move(l.playerSpawn(), Direction.BAS);
                panel.maj();
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.KEY_PRESSED)
            System.out.println("Key pressed");
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.KEY_RELEASED)
            System.out.println("Key released");
    }

    public Display getDisplay(){
        return this.panel;
    }
}
