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

public class Display{
    private JFrame frame;
    private Panel panel;

    public static void main(String[] args) throws IOException{
        new Display();
    }

    public Display() throws IOException{
        this.frame = new JFrame();
        this.panel = new Panel();
        frame.setSize(700, 700);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        afficherMonde();
    }

    public void afficherMonde(){
        this.frame.setVisible(true);
        
    }

}