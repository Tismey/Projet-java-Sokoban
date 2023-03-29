import java.*;
import java.util.ArrayList;

public interface LevelMoveInterface {

    /*
        Affiche sur le terminal une représentation en ASCII du niveau
    */
    public void displayInTerminal(LevelData data);

    /**
        Renvoie true si le mouvement du Cell sur les coordonnées o vers la direction d est possible
        Sinon renvoie false
    */
    public boolean checkForMove(CoordSet o, Direction d);

    /**
        Effectue les mouvements à partir de les coordonnées o (position du joueur) vers la direction d
    */
    public void move(CoordSet o, Direction d);

    /**
        Renvoie les coordonnées du joueur dans la matrice de ce niveau  
    */
    public CoordSet playerSpawn();

    /**
        Renvoie true si toutes les cibles sont atteintes
        Sinon renvoie false
    */
    public boolean winConditionMet();

    /**
        Renvoie true si le Cell en coordonnées (x, y) de la matrice de ce niveau est un monde
        Sinon renvoie false 
    */
    public boolean isAWorld(int x, int y);

    /**
        Renvoie true si le joueur est un monde (pas forcément dans ce niveau)
        Sinon renvoie false
    */
    public boolean playerIsAWorld();

    /**
        Renvoie true si le Cell en coordonnées (x, y) de la matrice de ce niveau est un joueur et un monde
        Sinon renvoie false 
    */
    public boolean isAPlayerWorld(int x, int y);

    /**
        Renvoie les coordonnées du monde correspondant au numéro numWorld dans ce niveau
    */
    public CoordSet getPosWorld(int numWorld);

    /**
        Effectue un mouvement vers la direction d du Cell situé sur les coordonnées c
    */
    public void swapToDir(CoordSet c, Direction d);

    /** 
        Renvoie les coordonnées correspondant à l'entrée de ce niveau par la direction d 
    */
    public CoordSet enterPos(Direction d);

    /**
        Renvoie true si les coordonnées o si situe sur les extrémités de la matrice de ce nivreau 
        et que la direction d se dirige vers l'extérieur de ce niveau.
        Sinon renvoie false
    */
    public boolean onEdge(CoordSet o, Direction d);

    /**
        Renvoie la matrice de ce niveau
    */
    public int[][] getLevelData();

    /**
        Renvoie le Cell situé sur les coordonnées (x, y) de la matrice 
    */
    public int getLevelData(int x, int y);

    /**
        Renvoie la taille de la matrice de ce niveau
    */
    public int getSizeMat();

    /**
        Renvoie la liste des coordonnées des cibles dans ce niveau
    */
    public ArrayList<CoordSet> getListTarget();

    /**
        Renvoie le numéro du monde qui contient ce niveau 
    */
    public int getOutsideWorld();

    /**
        Initialise la variable du numéro du monde qui contient ce niveau à val
    */
    public void setOutsideWorld(int val);

    /**
        Renvoie le numéro de ce niveau
    */
    public int getWorldNum();
}