package model.station;

import model.chef.ChefPlayer;
import model.item.Item;
import model.item.Plate;

import java.util.Stack;

public class WashingClean extends Station {
    public static WashingClean instance; // Static agar bisa diakses WashingStation
    private Stack<Plate> cleanPlatesStack;

    public WashingClean() {
        super("H");
        instance = this;
        this.cleanPlatesStack = new Stack<>();
    }

    @Override
    public void interact(ChefPlayer chef) {
    }

    @Override
    public boolean allowItem(Item item) {
        return item instanceof Plate && ((Plate)item).isClean();
    }

    @Override
    public void setItemOnStation(Item item) {
        if (item instanceof Plate) {
            cleanPlatesStack.push((Plate) item);
        }
    }

    @Override
    public Item getItemOnStation() {
        if (!cleanPlatesStack.isEmpty()) {
            return cleanPlatesStack.peek();
        }
        return null;
    }

    @Override
    public void removeItem() {
        if (!cleanPlatesStack.isEmpty()) {
            cleanPlatesStack.pop();
        }
    }

    public void addCleanPlate(Plate plate) {
        cleanPlatesStack.push(plate);
    }

    public boolean hasPlates() {
        return !cleanPlatesStack.isEmpty();
    }
}
