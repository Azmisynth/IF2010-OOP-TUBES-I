package main.java.model.station;

import main.java.model.chef.ChefPlayer;
import main.java.model.item.*;

public class IngredientStation extends Station {
    private Class<? extends Ingredient> ingredientType; // simpen tipe ingredient apa yang ada di station ini (misalnya Lettuce, Tomato, dll)
    private Item itemOnStation;
//
    public IngredientStation(Class<? extends Ingredient> ingredientType) { // constructor buat bikin IngredientStation baru
        super("I"); // panggil constructor Station dengan simbol 'I' sebagai String
        this.ingredientType = ingredientType; // set tipe ingredient yang disediain station ini
        this.itemOnStation = null; // awalnya station kosong gak ada item
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef

        // skenario 1: plating - chef bawa plate bersih dan di station ada ingredient
        if (chefItem instanceof Plate && itemOnStation != null) { // cek chef bawa Plate dan station ada item
            Plate plate = (Plate) chefItem; // cast item jadi Plate biar bisa akses method Plate
            if (plate.isClean() && itemOnStation instanceof Preparable) { // cek plate bersih dan item di station bisa disiapkan
                Preparable prep = (Preparable) itemOnStation; // cast item jadi Preparable biar bisa cek status
                if (prep.canBePlacedOnPlate()) { // cek apakah item udah siap buat ditaro di plate
                    plate.addComponent(prep); // tambahin ingredient/component ke plate
                    itemOnStation = plate; // sekarang yang di station jadi plate-nya
                    chef.setInventory(null); // kosongin inventory chef karena plate udah ditaro di station
                    System.out.println("Plating berhasil di Ingredient Station!");
                    return;
                }
            }
        }

        // skenario 2: chef mau taro item di station kosong
        //if (chefItem != null && itemOnStation == null) { // cek chef bawa sesuatu dan station kosong
        //    itemOnStation = chefItem; // taro item chef ke station
        //    chef.setInventory(null); // kosongin inventory chef
         //   System.out.println("Item diletakkan di Ingredient Station");
        //}
        // skenario 3: chef mau ambil item dari station
        if (itemOnStation != null && chefItem == null) { // cek station ada item dan chef gak bawa apa-apa
            chef.setInventory(itemOnStation); // kasih item dari station ke chef
            itemOnStation = null; // kosongin station
            System.out.println("Item diambil dari Ingredient Station");
        }
        // skenario 4: ambil ingredient baru dari station (stok unlimited)
        else if (chefItem == null && itemOnStation == null) { // cek chef dan station sama-sama kosong
            try { // coba bikin ingredient baru pake reflection
                Ingredient newIngredient = ingredientType.getDeclaredConstructor().newInstance(); // bikin instance baru dari tipe ingredient yang disimpen di station
                chef.setInventory(newIngredient); // kasih ingredient baru ke chef
                System.out.println("Ingredient diambil: " + newIngredient.getClass().getSimpleName()); // kasih tau ingredient apa yang diambil
            } catch (Exception e) { // kalau ada error pas bikin ingredient
                System.out.println("Error mengambil ingredient!");
                e.printStackTrace(); // print detail error-nya
            }
        }
    }

    public Class<? extends Ingredient> getIngredientType() {
        return ingredientType;
    }

    public Item getItemOnStation() {
        return itemOnStation;
    }

    public void setItemOnStation(Item item) {
        this.itemOnStation = item;
    }

    public String getIngredientName() {
        return ingredientType.getSimpleName();
    }

}