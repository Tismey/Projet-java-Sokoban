public interface Moveable{

    /*
     * Class abstract dont les futures classe joueur et boite pourront heriter
     */

    public boolean move(Direction d);

    public Coords getCoords();

    public boolean equals(Object o);


}