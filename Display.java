import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;


public class Display extends JPanel {
    private LevelMove l;
    private Univers u;
    private ArrayList<Image> images;

    public static void main(String[] args) throws IOException {
        new Display();
    }

    public Display() throws IOException {
        l = new LevelMove(6);
        images = new ArrayList<>();
        u = null;
        add(new JLabel("rien"));
    }

    public Display(LevelMove level) throws IOException {
        l = level;
        u = null;
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")));
        images.add(ImageIO.read(new File("wall.png")));
        images.add(ImageIO.read(new File("player.png")));
        images.add(ImageIO.read(new File("target.png")));
    }

    public Display(Univers univ) throws IOException {
        u = univ;
        l = univ.getUnivers().get(univ.getPlayerSpawnWorld());
        
        images = new ArrayList<>();
        images.add(ImageIO.read(new File("box.png")));
        images.add(ImageIO.read(new File("wall.png")));
        images.add(ImageIO.read(new File("player.png")));
        images.add(ImageIO.read(new File("target.png")));
        images.add(ImageIO.read(new File("world.png")));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        g.setColor(Color.GRAY);
        Dimension d = this.getSize();
        int ofx =(int) d.getWidth()/2 - (l.getSizeMat()/2)* images.get(0).getWidth(null) ;
        int ofy =(int) d.getHeight()/2 - (l.getSizeMat()/2) * images.get(0).getHeight(null);
        g.drawRect(ofx, ofy,images.get(0).getWidth(null) * l.getSizeMat(), images.get(0).getHeight(null) * l.getSizeMat());
        for (int i = 0; i < l.getSizeMat(); i++) {
            for (int j = 0; j < l.getSizeMat(); j++) {
                if (l.getLevelData(i, j) == Cells.BOITE)
                    g.drawImage(images.get(0),ofx + j * images.get(0).getWidth(null), ofy + i * images.get(0).getHeight(null), null);
                else if (l.getLevelData(i, j) == Cells.MUR)
                    g.drawImage(images.get(1), ofx + j * images.get(1).getWidth(null), ofy + i * images.get(1).getHeight(null), null);
                else if (l.getLevelData(i, j) == Cells.JOUEUR) {
                    if (!l.isAPlayerWorld(i, j)) {
                        g.drawImage(images.get(2), ofx + j * images.get(2).getWidth(null), ofy + i * images.get(2).getHeight(null), null);
                    }
                    else if(u!= null) {
                        LevelMove l2 = u.getUnivers().get(LevelData.getNumPlayerWorld());
                        for (int t = 0; t < l2.getSizeMat();t++) {
                            for (int r = 0; r < l2.getSizeMat(); r++) {
                                if (l2.getLevelData(t, r) == Cells.BOITE)
                                    g.drawImage(images.get(0),  ofx +j*images.get(0).getWidth(null) + r*images.get(0).getWidth(null)/l2.getSizeMat(),  ofy +i * images.get(0).getHeight(null) + t*images.get(0).getHeight(null)/l2.getSizeMat(),images.get(0).getWidth(null)/l2.getSizeMat(),images.get(0).getHeight(null)/l2.getSizeMat(),null);
                                else if (l2.getLevelData(t, r) == Cells.MUR)
                                    g.drawImage(images.get(1),ofx + j*images.get(1).getWidth(null) + r*images.get(1).getWidth(null)/l2.getSizeMat(),  ofy +i * images.get(1).getHeight(null) + t*images.get(1).getHeight(null)/l2.getSizeMat(),images.get(1).getWidth(null)/l2.getSizeMat(),images.get(1).getHeight(null)/l2.getSizeMat(),null);
                                else if (l2.getLevelData(t, r) == Cells.JOUEUR)
                                    g.drawImage(images.get(2),  ofx +j*images.get(2).getWidth(null) + r*images.get(2).getWidth(null)/l2.getSizeMat(),  ofy +i * images.get(2).getHeight(null) + t*images.get(2).getHeight(null)/l2.getSizeMat(),images.get(2).getWidth(null)/l2.getSizeMat(),images.get(2).getHeight(null)/l2.getSizeMat(),null);
                                else if (l2.getListTarget().contains(new CoordSet(t, r)))
                                    g.drawImage(images.get(3),  ofx +j*images.get(3).getWidth(null) + r*images.get(3).getWidth(null)/l2.getSizeMat(),  ofy +i * images.get(3).getHeight(null) + t*images.get(3).getHeight(null)/l2.getSizeMat(),images.get(3).getWidth(null)/l2.getSizeMat(),images.get(3).getHeight(null)/l2.getSizeMat(),null);
                                else if(l2.getLevelData(t, r) >= 0)
                                    g.drawImage(images.get(4),  ofx +j*images.get(4).getWidth(null) + r*images.get(4).getWidth(null)/l2.getSizeMat(),  ofy +i * images.get(4).getHeight(null) + t*images.get(4).getHeight(null)/l2.getSizeMat(),images.get(4).getWidth(null)/l2.getSizeMat(),images.get(4).getHeight(null)/l2.getSizeMat(),null);
                            }
                        }
                    }                  
                }
                else if (l.getListTarget().contains(new CoordSet(i, j)) && !l.isAWorld(i, j))
                    g.drawImage(images.get(3), ofx +j * images.get(3).getWidth(null),ofy + i * images.get(3).getHeight(null), null);
                else if (l.getLevelData(i, j) >= 0 && u!= null){
                        g.drawRect(ofx +j * images.get(2).getWidth(null),ofy + i * images.get(3).getHeight(null), images.get(0).getWidth(null), images.get(0).getHeight(null));
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(ofx +j * images.get(2).getWidth(null),ofy + i * images.get(3).getHeight(null), images.get(0).getWidth(null), images.get(0).getHeight(null));
                        LevelMove l2 = u.getUnivers().get(l.getLevelData(i, j));
                        for (int t = 0; t < l2.getSizeMat();t++) {
                            for (int r = 0; r < l2.getSizeMat(); r++) {
                                if (l2.getLevelData(t, r) == Cells.BOITE)
                                    g.drawImage(images.get(0),ofx + j*images.get(0).getWidth(null) + r*images.get(0).getWidth(null)/l2.getSizeMat(),ofy + i * images.get(0).getHeight(null) + t*images.get(0).getHeight(null)/l2.getSizeMat(),images.get(0).getWidth(null)/l2.getSizeMat(),images.get(0).getHeight(null)/l2.getSizeMat(),null);
                                else if (l2.getLevelData(t, r) == Cells.MUR)
                                    g.drawImage(images.get(1), ofx +j*images.get(1).getWidth(null) + r*images.get(1).getWidth(null)/l2.getSizeMat(),ofy + i * images.get(1).getHeight(null) + t*images.get(1).getHeight(null)/l2.getSizeMat(),images.get(1).getWidth(null)/l2.getSizeMat(),images.get(1).getHeight(null)/l2.getSizeMat(),null);
                                else if (l2.getLevelData(t, r) == Cells.JOUEUR)
                                    g.drawImage(images.get(2),ofx + j*images.get(2).getWidth(null) + r*images.get(2).getWidth(null)/l2.getSizeMat(),ofy + i * images.get(2).getHeight(null) + t*images.get(2).getHeight(null)/l2.getSizeMat(),images.get(2).getWidth(null)/l2.getSizeMat(),images.get(2).getHeight(null)/l2.getSizeMat(),null);
                                else if (l2.getListTarget().contains(new CoordSet(t, r)) && !l2.isAWorld(t, r))
                                    g.drawImage(images.get(3),ofx + j*images.get(3).getWidth(null) + r*images.get(3).getWidth(null)/l2.getSizeMat(),ofy + i * images.get(3).getHeight(null) + t*images.get(3).getHeight(null)/l2.getSizeMat(),images.get(3).getWidth(null)/l2.getSizeMat(),images.get(3).getHeight(null)/l2.getSizeMat(),null);
                                else if(l2.getLevelData(t, r) >= 0)
                                    g.drawImage(images.get(4), ofx +j*images.get(4).getWidth(null) + r*images.get(4).getWidth(null)/l2.getSizeMat(),ofy + i * images.get(4).getHeight(null) + t*images.get(4).getHeight(null)/l2.getSizeMat(),images.get(4).getWidth(null)/l2.getSizeMat(),images.get(4).getHeight(null)/l2.getSizeMat(),null);
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

    public void maj(Univers univ){
        u = univ;
        l = univ.getUnivers().get(univ.getPlayerSpawnWorld());
        this.update(getGraphics());
        this.repaint();
    }
       
    public ArrayList<Image> getImages(){
        return images;
    }
}
