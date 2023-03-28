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
    private ArrayList<Image> images;

    public static void main(String[] args) throws IOException {
        new Display();
    }

    public Display() throws IOException {
        l = new LevelMove(6);
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")).getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        images.add(ImageIO.read(new File("wall.jpg")).getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        images.add(ImageIO.read(new File("player.jpg")).getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        this.setLayout(new GridLayout(l.getSizeMat(), l.getSizeMat()));
        frame = new Frame();
        frame.add(this);
    }

    public Display(LevelMove level, LevelData data) throws IOException {
        l = level;
        d = data;
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")));
        images.add(ImageIO.read(new File("wall.jpg")));
        images.add(ImageIO.read(new File("player.jpg")));
        images.add(ImageIO.read(new File("aim.jpg")));
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

