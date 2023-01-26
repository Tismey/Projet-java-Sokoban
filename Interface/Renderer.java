import java.util.ArrayList;

public interface Renderer{
    /**
     * Partie graphique du projet
     */

    public void drawStaticObjects(Cells[][] cells);

    public void drawMovealble(ArrayList<Moveable> mv);

    /**
     * fonction pour controller la taille a laquelle dessiner et la position de tout un niveaux
     * (pour la recursivité et pour des annimations)
     */

     public void drawStaticObjectswScale(Cells[][] cells, double scale, Coords pos);

    public void tamponner();

    public void effacer();



}