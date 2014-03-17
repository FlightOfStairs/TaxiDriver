public abstract class MovingMapItem extends MapItem {

    // Distance moved per turn (in Pixels?)
    protected int speed;

    protected State state;

    // If we want to remove this item at the end of it's cycle
    protected boolean dying;


    public MovingMapItem(String name, int x, int y, int newSpeed) {
        super(name, x, y);
        speed = newSpeed;
        dying = false;
    }

    public abstract void act();

    public boolean dying() { return dying; }
        public void kill() {
          if (dying) throw new RuntimeException("Already dying.");
          dying = true;
        }
}
