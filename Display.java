import javax.swing.*;
import java.util.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.util.Scanner;

public class Display extends JPanel {
    private JFrame frame;
    private LevelMove l;
    private ArrayList<Image> images;

    public static void main(String[] args) throws IOException {
        new Display();
    }

    public Display() throws IOException {
        l = new LevelMove(6);
        //l.initMatrice();
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")).getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        images.add(ImageIO.read(new File("wall.jpg")).getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        images.add(ImageIO.read(new File("player.jpg")).getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        this.setLayout(new GridLayout(l.getSizeMat(), l.getSizeMat()));
        this.frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.add(this);
        afficher();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(images.get(0), 0, 0, null);
    }

    public void afficher() {
        this.setBackground(Color.DARK_GRAY);
        for (int i = 0; i < l.getSizeMat(); i++) {
            for (int j = 0; j < l.getSizeMat(); j++) {
                if (l.getLevelData(j, i) == Cells.BOITE)
                    this.add(new JLabel(new ImageIcon(images.get(0))));
                if (l.getLevelData(j, i) == Cells.MUR)
                    this.add(new JLabel(new ImageIcon(images.get(1))));
                if (l.getLevelData(j, i) == Cells.JOUEUR)
                    this.add(new JLabel(new ImageIcon(images.get(2))));
            }
        }
    }
}
