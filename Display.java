import javax.swing.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private Frame frame;
    private LevelMove l;
    private LevelData d;
    private  Univers u;
    private ArrayList<Image> images;

    public static void main(String[] args) throws IOException {
        new Display();
    }

    public Display() throws IOException {
        l = new LevelMove(6);
        d = new LevelData("nivClassique");
        images = new ArrayList<>();
    }

    public Display(LevelMove level, LevelData data) throws IOException {
        l = level;
        d = data;
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")));
        images.add(ImageIO.read(new File("wall.jpg")));
        images.add(ImageIO.read(new File("player.jpg")));
        images.add(ImageIO.read(new File("target.png")));
    }

    public Display(Univers univ) throws IOException {
        u = univ;
        l = univ.getUnivers().get(univ.getPlayerSpawnWorld());
        
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")));
        images.add(ImageIO.read(new File("wall.jpg")));
        images.add(ImageIO.read(new File("player.jpg")));
        images.add(ImageIO.read(new File("target.png")));
    }

    

    

    

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.DARK_GRAY);
        for (int i = 0; i < l.getSizeMat(); i++) {
            for (int j = 0; j < l.getSizeMat(); j++) {
                if (l.getLevelData(i, j) == Cells.BOITE)
                    g.drawImage(images.get(0), j * images.get(0).getWidth(null), i * images.get(0).getHeight(null), null);
                else if (l.getLevelData(i, j) == Cells.MUR)
                    g.drawImage(images.get(1), j * images.get(1).getWidth(null), i * images.get(1).getHeight(null), null);
                else if (l.getLevelData(i, j) == Cells.JOUEUR)
                    g.drawImage(images.get(2), j * images.get(2).getWidth(null), i * images.get(2).getHeight(null), null);
                else if (l.getListTarget().contains(new CoordSet(i, j)))
                    g.drawImage(images.get(3), j * images.get(3).getWidth(null), i * images.get(3).getHeight(null), null);
                // PARTIE POUR DESSINER LES SOUS NIVEAUX
                else if (l.getLevelData(i, j) >= 0){
                    LevelMove l2 = u.getUnivers().get(l.getLevelData(i, j));
                    for (int t = 0; t < l2.getSizeMat();t++) {
                        for (int r = 0; r < l2.getSizeMat(); r++) {
                            if (l2.getLevelData(i, j) == Cells.BOITE)
                                g.drawImage(images.get(0), j*images.get(0).getWidth(null) + r*images.get(0).getWidth(null)/l2.getSizeMat(), i * images.get(0).getHeight(null) + t*images.get(0).getHeight(null)/l2.getSizeMat(), images.get(0).getWidth(null)/l2.getSizeMat(),images.get(0).getHeight(null)/l2.getSizeMat(),null);
                            else if (l2.getLevelData(i, j) == Cells.MUR)
                                g.drawImage(images.get(1), j*images.get(1).getWidth(null) + r*images.get(1).getWidth(null)/l2.getSizeMat(), i * images.get(1).getHeight(null) + t*images.get(1).getHeight(null)/l2.getSizeMat(), images.get(1).getWidth(null)/l2.getSizeMat(),images.get(1).getHeight(null)/l2.getSizeMat(),null);
                            else if (l2.getLevelData(i, j) == Cells.JOUEUR)
                                g.drawImage(images.get(2), j*images.get(2).getWidth(null) + r*images.get(2).getWidth(null)/l2.getSizeMat(), i * images.get(2).getHeight(null) + t*images.get(2).getHeight(null)/l2.getSizeMat(), images.get(2).getWidth(null)/l2.getSizeMat(),images.get(2).getHeight(null)/l2.getSizeMat(),null);
                            else if (l2.getListTarget().contains(new CoordSet(i, j)))
                                g.drawImage(images.get(3), j*images.get(3).getWidth(null) + r*images.get(3).getWidth(null)/l2.getSizeMat(), i * images.get(3).getHeight(null) + t*images.get(3).getHeight(null)/l2.getSizeMat(), images.get(3).getWidth(null)/l2.getSizeMat(),images.get(3).getHeight(null)/l2.getSizeMat(),null);
                        }
                    }
                }
                   
            }
        }
    }

    public void maj(){
        this.update(getGraphics());
        this.repaint();
    }
       
    public ArrayList<Image> getImages(){
        return images;
    }
}

