package main.java.model.item;

public class Oven extends KitchenUtensils<Preparable> implements CookingDevice {
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
            throw new IllegalArgumentException("main.java.model.item.Oven cannot accept this ingredient.");
        }
    }

    @Override
    public void startCooking() {
        System.out.println("main.java.model.item.Oven starting to cook");
    }

    @Override
    public java.util.Set<Preparable> getContents() {
        return contents;
    }
}