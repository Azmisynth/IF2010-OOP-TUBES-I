package main.java.model.item;

public interface CookingDevice {
    boolean isPortable();
    int capacity();
    boolean canAccept(Preparable ingredient);

    void addIngredient(Preparable ingredient);
    void startCooking();
    java.util.Set<Preparable> getContents();
}