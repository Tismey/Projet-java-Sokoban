import java.lang.System.Logger.Level;

public interface Memory{
    /*
     * A changer a votre discretion ( remplacer path par un class qui encapsule tout le fichier je l'est connais pas :/)
     * interface pour la partie memoire
     * 
     * Note : Cette class pourra etre abstract pour avoir une autre classe niveaux
     */
    public Level loadFromFile(String path);

    /*
     * Interessant pour la recurence(niveaux dans des niveaux)
     */

    public Level rLoadFromFile(String path, int depth);

     /*
      * Savoir combien de niveaux dans ce niveaux
      */

    public int depthLevel(String path);

    /*
     *  ajout possible
     */
}