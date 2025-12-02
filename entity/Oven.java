public class Oven extends KitchenUtensils implements CookingDevice {
    private static final int CAPACITY = 1;

    public Oven(String name) {
        super(name);
    }

    @Override
    public boolean isPortable() {
        return false;
    }

    @Override
    public int capacity() {
        return CAPACITY;
    }

    @Override
    public boolean canAccept(Preparable ingredient) {
        return contents.size() < CAPACITY;
    }

    @Override
    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            contents.add(ingredient);
        } else {
            throw new IllegalArgumentException("Oven cannot accept this ingredient.");
        }
    }

    @Override
    public void startCooking() {
        System.out.println("Oven starting to cook");
    }
}
