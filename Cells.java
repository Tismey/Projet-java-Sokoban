/*
 * Enum pour stocker les niveaux au runtime (representation des niveaux avec un double tableau
 * d enum)
 * 
 */

/*
public enum Cells {

    MUR, 
    BOITE, 
    JOUEUR, 
    VIDE,
    MONDE,
}
*/

public abstract class Cells {
	public static final int MUR = -1; 
	public static final int BOITE = -2; 
	public static final int VIDE = -3; 
	public static final int JOUEUR = -4;

	public static int mondeNum(int val) {
		return val;
	} 
}