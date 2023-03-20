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

public class Panel extends JPanel {
    private LevelMove l;
    private BufferedImage box;
    private BufferedImage walls;
    private BufferedImage player;

    public Panel() throws IOException{
        l = new LevelMove(6);
        box = ImageIO.read(new File("box.png"));
        this.setLayout(new GridLayout(l.getTaille(), l.getTaille()));
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(box, 0, 0, null);
    }
}
