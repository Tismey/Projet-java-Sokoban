
import java.util.ArrayList;

public class Maps{
    private ArrayList<Level> levels;
    private int numDepth;
    private int incr;

    public Maps(){
        levels = new ArrayList<Level>();
        numDepth = 0;
        incr = 0;
    }

    public Maps(ArrayList<Level> ar){
        levels = ar;
        numDepth = ar.size();
        incr = 0;
    }

    /**
     * 
     * Définition methode
     */
}