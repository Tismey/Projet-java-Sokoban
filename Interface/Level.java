import java.*;
import java.util.ArrayList;

public interface level{

    /*
     * Classe niveaux qui contiendra : Cells[][]
     * et une array list avec les moveables
     */

    public static int LEVEL_SIZE;

    public void DisplayInTerminal();

    public Cells[][] getLevelData();

    public ArrayList<Moveable> getMoveableobjects();

    public boolean checkForMove(Moveable mv, Direction d);

    public boolean addMoveable(Moveable mv);

    public boolean remove(Moveable mv);

    public boolean winConditionMet();

    public Coords playerSpawn();



}