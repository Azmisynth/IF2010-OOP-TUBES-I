package model.station;

import model.chef.ChefPlayer;
import model.item.Ingredient;
import model.item.Item;
import model.item.ItemState;
import model.item.Plate;
import model.kitchen.OrderManager;

import java.util.Timer;
import java.util.TimerTask;

public class ServingCounter extends Station {
    private static final int PLATE_RETURN_DELAY = 10000; // 10 detik (dalam milidetik) buat delay kembaliin piring

    public ServingCounter() {
        super("S"); // simbol S sebagai String
    }

    @Override // override method interactnya Station
    public void interact(ChefPlayer chef) {
    }

    @Override
    public boolean allowItem(Item item) {
        // Hanya terima Piring
        return item instanceof Plate;
    }

    @Override
    public void setItemOnStation(Item item) {
        if (item instanceof Plate) {
            Plate plate = (Plate) item;

            // Logic Kirim Order
            // Panggil OrderManager untuk cek resep
            boolean success = OrderManager.getInstance().deliverOrder(plate.getContents());

            if (success) {
                System.out.println("Order Terkirim!");
                returnPlate(plate); // Kembalikan piring kotor nanti
            } else {
                returnPlate(plate);
            }
        }
    }

    private void returnPlate(Plate plate) {
        plate.clearContents();
        plate.setClean(false); // Jadi kotor
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (PlateStorage.instance != null) {
                    PlateStorage.instance.returnPlate(plate);
                }
            }
        }, PLATE_RETURN_DELAY);
    }

    // Override getItemOnStation return null agar Chef tidak bisa ambil balik piring yg sdh ditaruh
    @Override
    public Item getItemOnStation() { return null; }
}