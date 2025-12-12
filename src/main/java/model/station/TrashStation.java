package model.station;

import model.chef.ChefPlayer;
import model.item.*;

public class TrashStation extends Station {
    public TrashStation() {
        super("T"); // simbol T sebagai String
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef

        if (chefItem != null) { // cek chef bawa sesuatu
            // kalau yang dibawa adalah kitchen utensils (kayak panci, wajan)
            if (chefItem instanceof KitchenUtensils) { // cek apakah item adalah KitchenUtensils
                KitchenUtensils utensil = (KitchenUtensils) chefItem; // cast item jadi KitchenUtensils biar bisa akses method-nya
                utensil.clearContents(); // bersihin isi utensil-nya aja (buang ingredient/makanan yang ada di dalem)
                System.out.println("Isi kitchen utensils dibuang, utensil masih di tangan");
            }
            // kalau item biasa (ingredient, plate, dll)
            else { // kalau bukan KitchenUtensils
                String deletedName = chefItem.getName();
                chef.setInventory(null); // buang item dari inventory chef (item hilang sepenuhnya)
                System.out.println("Item dibuang ke trash");
            }
        } else { // kalau chef ngga bawa apa-apa
            System.out.println("Tidak ada item untuk dibuang!");
        }
    }
}