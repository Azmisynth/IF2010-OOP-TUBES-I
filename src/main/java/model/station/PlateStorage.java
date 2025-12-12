package model.station;

import model.chef.ChefPlayer;
import model.item.Item;
import model.item.Plate;
import java.util.Stack;

public class PlateStorage extends Station{
    public static PlateStorage instance;
    private Stack<Plate> plates;

    public PlateStorage() {
        super("P");
        instance = this;
        this.plates = new Stack<>();

        for (int i = 0; i < 3; i++) {
            plates.push(new Plate("Plate"));
        }
    }

    public void interact(ChefPlayer chef) {
        Item chefItem = chef.getInventory();

        //Chef Ambil Piring
        if (chefItem == null) {
            if (!plates.isEmpty()) {
                // Ambil piring paling atas
                Plate p = plates.pop();
                chef.setInventory(p);
            }
        }
    }

    public boolean hasPlates() {
        return !plates.isEmpty();
    }

    public boolean hasDirtyPlateOnTop() {
        if (plates.isEmpty()) return false;
        return !plates.peek().isClean();
    }

    public void returnPlate(Plate dirtyPlate) {
        plates.push(dirtyPlate);
        System.out.println("LOGIC: Piring kotor ditambahkan ke tumpukan.");
    }

}
