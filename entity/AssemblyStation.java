public class AssemblyStation extends Station { // class untuk station tempat rakit dish/ingredient
    private Item itemOnStation; // item yang lagi ada di atas assembly station (bisa ingredient, plate, dish, apapun)
         
    public AssemblyStation(Tile position) { // constructor untuk bikin assembly station baru
        super(position, 'A'); // panggil constructor parent dengan simbol A
        this.itemOnStation = null; // awalnya station kosong, ga ada item
    }
         
    @Override
    public void interact(Chef chef) { // method yang dipanggil waktu chef pencet tombol interact di station ini
        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef di tangan
                 
        // plating: chef bawa plate bersih dan station ada ingredient/dish
        if (chefItem instanceof Plate && itemOnStation != null) { // cek chef bawa plate dan station ada item
            Plate plate = (Plate) chefItem; // cast item chef jadi plate
            if (!plate.isDirty() && itemOnStation instanceof Preparable) { // plate harus bersih dan item di station harus bisa dimasak (ingredient/dish)
                Preparable prep = (Preparable) itemOnStation; // cast item di station jadi preparable
                if (prep.canBePlacedOnPlate()) { // cek apakah item ini boleh ditaro di plate (harus COOKED atau sesuai rule)
                    plate.addComponent(prep); // masukin ingredient/dish ke dalam plate
                    itemOnStation = plate; // sekarang yang ada di station adalah plate berisi dish
                    chef.setInventory(null); // tangan chef jadi kosong
                    System.out.println("Plating berhasil di Assembly Station!");
                }
            }
        }
        
        // chef bawa kitchen utensils berisi ingredient, station ada plate bersih
        if (chefItem instanceof KitchenUtensils && itemOnStation instanceof Plate) { // cek chef bawa utensil dan station ada plate
            KitchenUtensils utensil = (KitchenUtensils) chefItem; // cast item chef jadi kitchen utensils
            Plate plate = (Plate) itemOnStation; // cast item di station jadi plate
                         
            if (!plate.isDirty() && utensil.hasContents()) { // plate harus bersih dan utensil harus ada isinya
                // transfer ingredients ke plate
                for (Preparable ingredient : utensil.getContents()) { // loop semua ingredient yang ada di utensil
                    if (ingredient.canBePlacedOnPlate()) { // cek apakah ingredient boleh ditaro di plate
                        plate.addComponent(ingredient); // masukin ingredient ke plate
                    }
                }
                utensil.clear(); // utensil jadi kosong setelah semua ingredient dipindah
                chef.setInventory(utensil); // utensil kosong kembali ke tangan chef
                System.out.println("Ingredients dipindah dari utensils ke plate!");
            }
        }

        // letakkan item di station (case umum: taruh item apapun)
        if (chefItem != null && itemOnStation == null) { // chef bawa item dan station kosong
            itemOnStation = chefItem; // item yang dibawa chef sekarang ada di station
            chef.setInventory(null); // tangan chef jadi kosong
            System.out.println("Item diletakkan di Assembly Station");
        // ambil item dari station (case umum: ambil item apapun)
        }

        if (itemOnStation != null && chefItem == null) { // station ada item dan tangan chef kosong
            chef.setInventory(itemOnStation); // item di station pindah ke tangan chef
            itemOnStation = null; // station jadi kosong
            System.out.println("Item diambil dari Assembly Station");
        }
    }
         
    public Item getItemOnStation() { // getter untuk ambil item yang ada di station
        return itemOnStation; // return item yang ada di station (atau null kalo kosong)
    }
         
    public void setItemOnStation(Item item) { // setter untuk set item di station secara manual (buat testing/load game)
        this.itemOnStation = item; // set item di station jadi item yang dikasih
    }
}