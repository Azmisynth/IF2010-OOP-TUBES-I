package model.station;

import model.chef.ChefPlayer;
import model.item.*;

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
        /*
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
        if (chefItem != null && itemOnStation == null) { // cek chef bawa sesuatu dan station kosong
            itemOnStation = chefItem; // taro item chef ke station
            chef.setInventory(null); // kosongin inventory chef
            System.out.println("Item diletakkan di Ingredient Station");
        }
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

        if (chefItem instanceof Preparable && itemOnStation instanceof Plate) {

            Preparable food = (Preparable) chefItem;
            Plate plate = (Plate) itemOnStation;

            // Syarat: Piring tidak boleh kotor & Bahan siap disajikan
            if (plate.isClean() && food.canBePlacedOnPlate()) {

                // Masukkan bahan ke dalam objek Plate
                plate.addComponent(food);

                // Hapus bahan dari tangan Chef
                chef.setInventory(null);

                System.out.println("Bahan berhasil ditambahkan ke atas Piring di meja!");
                return;
            } else {
                System.out.println("Piring kotor atau bahan belum siap.");
            }
        }

         */
    }

    public boolean receiveThrownItem(Item item) {
        // MEJA KOSONG
        if (itemOnStation == null) {
            // Bisa terima Piring atau Ingredient
            if (item instanceof Plate || item instanceof Ingredient) {
                this.itemOnStation = item;
                System.out.println("LOGIC: Assembly Station menangkap " + item.getName());
                return true;
            }
        }
        // SUDAH ADA PIRING
        else if (itemOnStation instanceof Plate) {
            // Kalau dilempar Ingredient, masukkan ke piring
            if (item instanceof Ingredient) {
                ((Plate) itemOnStation).addComponent((Ingredient) item);
                System.out.println("LOGIC: Lemparan masuk ke Piring di Assembly Station!");
                return true;
            }
        }

        return false;
    }


    public Class<? extends Ingredient> getIngredientType() {
        return ingredientType;
    }

    public Item getItemOnStation() {
        if (itemOnStation != null) {
            return itemOnStation;
        }
        try {
            return ingredientType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setItemOnStation(Item item) {
        this.itemOnStation = item;
    }

    @Override
    public boolean allowItem(Item item) {
        if (this.itemOnStation != null) return false;
        else return false;
    }

    public void removeItem() {
        if (this.itemOnStation != null) {
            this.itemOnStation = null;
        }
    }

    public String getIngredientName() {
        return ingredientType.getSimpleName();
    }

}