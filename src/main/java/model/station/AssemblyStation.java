package model.station;
import helper.SoundPlayer;
import model.chef.ChefPlayer;
import model.item.Item;
import model.item.KitchenUtensils;
import model.item.Plate;
import model.item.Preparable;
import model.item.Ingredient;

public class AssemblyStation extends Station { // class untuk station tempat rakit dish/ingredient
    private Item itemOnStation; // item yang lagi ada di atas assembly station (bisa ingredient, plate, dish, apapun)
    private SoundPlayer placementSound = new SoundPlayer("/sound/placement.wav");

    public AssemblyStation() { // constructor untuk bikin assembly station baru
        super("A"); // panggil constructor parent dengan simbol A (String)
        this.itemOnStation = null; // awalnya station kosong, ga ada item
    }

    @Override
    public void interact(ChefPlayer chef) {
    }

    @Override
    public boolean receiveThrownItem(Item item) {
        // MEJA KOSONG
        if (itemOnStation == null) {
            // Bisa terima Piring atau Ingredient
            if (item instanceof Plate || item instanceof Ingredient) {
                placementSound.play();
                this.itemOnStation = item;
                System.out.println("LOGIC: Assembly Station menangkap " + item.getName());
                return true;
            }
        }
        // SUDAH ADA PIRING
        else if (itemOnStation instanceof Plate) {
            // Kalau dilempar Ingredient, masukkan ke piring
            placementSound.play();
            if (item instanceof Ingredient) {
                ((Plate) itemOnStation).addComponent((Ingredient) item);
                System.out.println("LOGIC: Lemparan masuk ke Piring di Assembly Station!");
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean allowItem(Item item) {
        return true;
    }

    public void removeItem() {
        this.itemOnStation = null;
    }


    public Item getItemOnStation() { // getter untuk ambil item yang ada di station
        return itemOnStation; // return item yang ada di station (atau null kalo kosong)
    }

    public void setItemOnStation(Item item) { // setter untuk set item di station secara manual (buat testing/load game)
        this.itemOnStation = item; // set item di station jadi item yang dikasih
    }
}