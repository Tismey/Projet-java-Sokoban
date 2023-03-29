import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;


public class Display extends JPanel {
    private LevelMove l;
    private ArrayList<Image> images;

    public static void main(String[] args) throws IOException {
        new Display();
    }

    public Display() throws IOException {
        l = new LevelMove(6);
        images = new ArrayList<>();
        add(new JLabel("rien"));
    }

    public Display(LevelMove level) throws IOException {
        l = level;
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")));
        images.add(ImageIO.read(new File("wall.jpg")));
        images.add(ImageIO.read(new File("player.jpg")));
        images.add(ImageIO.read(new File("target.png")));
        images.add(createImage(new Display()).getScaledInstance(100, 100, ABORT));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
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
                else if (l.getLevelData(i, j) >= 0)
                    g.drawImage(images.get(4), j * images.get(4).getWidth(null), i * images.get(4).getHeight(null), null);
            }
        }
    }

    public Image createImage(Display d){
        int w = d.getWidth();
        int h = d.getHeight();
        BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = im.createGraphics();
        d.paint(g);
        g.dispose();
        return im;
    }

    public void maj(){
        this.update(getGraphics());
        this.repaint();
    }
       
    public ArrayList<Image> getImages(){
        return images;
    }
}


