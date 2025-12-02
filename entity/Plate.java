public class Plate extends KitchenUtensils {
    private PlateState state;
    private int capacity;

    public Plate(String name) {
        super(name);
    }

    public PlateState getState() {
        return state;
    }

    public boolean isClean() {
        return state == PlateState.CLEAN;
    }

    public void setClean() {
        this.state = PlateState.CLEAN;
    }
}