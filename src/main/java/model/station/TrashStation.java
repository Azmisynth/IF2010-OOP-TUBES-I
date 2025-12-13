package model.station;

import model.chef.ChefPlayer;
import model.item.*;

public class TrashStation extends Station {
    public TrashStation() {
        super("T"); // simbol T sebagai String
    }

    @Override
    public void interact(ChefPlayer chef) {
    }

    @Override
    public boolean allowItem(Item item) {
        return true; // Sampah terima SEGALA JENIS barang
    }

    @Override
    public void setItemOnStation(Item item) {
        // Logika "Membuang" terjadi di sini
        if (item != null) {
            if (item instanceof Plate) {
                ((Plate) item).clearContents(); // Hanya buang isinya
                System.out.println("Isi panci dibuang.");
            } else {
                System.out.println(item.getName() + " dibuang ke tempat sampah.");
            }
        }
    }

    @Override
    public Item getItemOnStation() {
        return null; // Tidak bisa ambil balik dari sampah
    }
}