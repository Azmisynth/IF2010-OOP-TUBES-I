package main.java.model.item;

public class Plate extends KitchenUtensils<Preparable> {

    private boolean isClean;

    public Plate(String name) {
        super(name);
        this.isClean = true;
    }

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean isClean) {
        this.isClean = isClean;
    }

    public boolean canBeUsedForPlating() {
        return isClean && contents.isEmpty();
    }

    public void addComponent(Preparable ingredient) {
        if (canBeUsedForPlating()) {
            contents.add(ingredient);
        } else {
            throw new IllegalStateException("main.java.model.item.Plate cannot be used for plating.");
        }
    }
}