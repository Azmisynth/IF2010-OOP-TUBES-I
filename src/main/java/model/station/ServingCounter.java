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
    public void interact(ChefPlayer chef) { // method yang dipanggil pas chef berinteraksi sama counter ini
        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef

//        if (chefItem != null && chefItem instanceof Plate) { // cek apakah item yang dibawa adalah Plate
//            Plate plate = (Plate) chefItem; // cast item jadi Plate biar bisa akses method Plate
//            if (!isPlateReadyToServe(plate)) {
//                System.out.println("LOGIC: Gagal Serve! Ada bahan yang belum matang (COOKED).");
//                return;
//            }
//            boolean isSuccess = OrderManager.getInstance().deliverOrder(plate.getContents());
//
//            chef.setInventory(null);
//            returnPlate(plate);
//        }
    }

    private boolean isPlateReadyToServe(Plate plate) {
        if (plate.getContents() == null || plate.getContents().isEmpty()) {
            return false;
        }

        // Cek satu per satu
        for (Object obj : plate.getContents()) {
            if (obj instanceof Ingredient) {
                Ingredient ing = (Ingredient) obj;

                // Syarat Mutlak: Harus COOKED
                // (Kecuali ada resep salad/sushi mentah, tapi di Pizza Map semua harus Cooked)
                if (ing.getState() != ItemState.COOKED) {
                    // Kalau Gosong -> Boleh disajikan tapi nanti kena penalti di OrderManager (opsional)
                    // Tapi biasanya game Overcooked menolak barang gosong juga di sini.
                    if (ing.getState() == ItemState.BURNED) {
                        System.out.println("Info: Ada bahan GOSONG!");
                        return false;
                    }

                    System.out.println("Info: " + ing.getName() + " masih " + ing.getState());
                    return false;
                }
            }
        }
        return true; // Semua lolos seleksi
    }

    private void returnPlate(Plate plate) {
        plate.clearContents();
        plate.setClean(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Kembalikan ke PlateStorage (Static Instance)
                if (PlateStorage.instance != null) {
                    PlateStorage.instance.returnPlate(plate);
                    System.out.println("LOGIC: Piring kotor muncul kembali di Storage!");
                } else {
                    System.err.println("ERROR: PlateStorage instance belum di-set!");
                }
            }
        }, PLATE_RETURN_DELAY);
    }
}