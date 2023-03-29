

public class Application {
    private Strings args[];

    public Application(Strings args[]){
        this.args = args;
    }
    /*
     * Je vous laisse rajouter des cases si vous vouler tester vos classe dans des cas spécifique (args sont les argument passer dans le terminal)
     */
    public void run(){
        switch(args[1]){
            case "normal":
            //nodéfinie pour le moment
                break;
            case "terminal":
                //nodéfinie pour le moment
                break;
            case "testLevel":
                if(args[2] == null){
                    throw new NullPointerException();
                }
                testLevel(args[2]);
                break;
            default:
                System.out.println("argument non reconnu");
                break;
        }
        System.out.println("Sortie Run()...");
    }
/*
 * 
 * Voila comment le loop principale du jeu ce derouleras dans les grandes ligne
 * 
 * ici on peut executer un niveaux pour tester le programmme ce ferme quand les condition de victiore son remplis
 */
    private void testLevel(String str){
        // Instantiation
        Memory load = new Memory();
        Level testlv = load.loadFromFile(str);
        Joueur j1 = new Joueur(/* arg constructeur */);
        Renderer rd = new Renderer();
        // Loop principale PEUT ETRE AJUSTER!!!!
        while(!testlv.winConditionMet()){
            rd.effacer();
        
            Direction dir = j1.getInput();
            if(dir != Direction.NODIRECTION){
                testlv.checkForMove(j1,dir);// On suppose ici que si checkFormove est vrai alors le deplacement a été effectuer(faisable avec recursivité)
            }
             rd.drawStaticObjects(testlv.getLevelData());
             rd.drawMovealble(testlv.getMoveableobjects());
             rd.tamponner();
             rd.effacer();
        }

    }

}