public interface LevelDataInterface {

	/**
		Permet de récupèrer les niveaux présent dans le nom fichier (chemin absolu) donné dans l'attribut path (grace au constructeur)
		afin de les ajouter dans l'attribut de type liste (listData) 
	*/
	public void loadFromFile();

	/**
		Renvoie le nombre de niveau dans le fichier
	*/
	public int depthLevel();

	/**
		Renvoie un tableau à deux dimensions avec tous les noms des mondes présent dans le fichier 
		avec chacun le nom des mondes qu'ils contiennent 
	*/
	public char [][] getNameWorld(int nbMonde);

	/**
		Renvoie le numéro du monde avec le nom nameWorld	
	*/
	public int getNumWorld(char nameWorld, int nbMonde);

	/**
		Renvoie le numéro du monde extérieur du niveau avec le nom nameWorld	
	*/
	public int getOutsideWorld(char nameWorld, int nbMonde);

	/**
		Renvoie le nom du monde avec le numéro du monde numWorld	
	*/
	public char getName(int numWorld);

	/**
		Renvoie la liste avec tous les niveaux du fichier	
	*/
	public ArrayList<LevelMove> getListData();

	/**
		Renvoie le numéro du monde du joueur si le joueur est un monde
		Si le joueur n'est pas un monde renvie par défaut -8 (valeur quelconque) 
	*/
	public static int getNumPlayerWorld();
}