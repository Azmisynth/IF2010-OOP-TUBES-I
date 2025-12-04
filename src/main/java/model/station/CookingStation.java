package main.java.model.station;
import main.java.model.chef.ChefPlayer;
import java.util.Timer;
import java.util.TimerTask;

public class CookingStation extends Station {
    //private Oven oven; // Oven built-in di CookingStation
    private Timer cookingTimer; // Timer untuk mngiitung waktu sampe pizza COOKED (12 detik)
    private Timer burnTimer; // Timer untuk ngitung waktu sampe pizza BURNED (24 detik total)
    private boolean isCooking; // Status oven lagi memasak atau gak

    private static final int COOKING_DURATION = 12000; // 12 detik dalam milidetik - waktu untuk pizza jadi COOKED
    private static final int BURNING_DURATION = 24000; // 24 detik dalam milidetik - waktu untuk pizza jadi BURNED (dihitung dari awal masak)

    public CookingStation() {
        super("R"); // panggil constructor parent class Station dengan simbol R sebagai String
        //this.oven = new Oven(); // oven ada di station sejak awal (built-in)
        this.isCooking = false; // status cooking dimulai dari belum masak
    }

    @Override
    public void interact(ChefPlayer chef) {
//        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef
//
//        // kalo chef bawa Pizza (Dish) -> masukin pizza ke oven (chef jadi tangan kosong)
//        if (chefItem instanceof Pizza) { // cek chef mbawa Pizza
//            Pizza pizza = (Pizza) chefItem; // cast item jadi Pizza
//
//            // validasi: pizza harus bisa dimasak (semua ingredient RAW/CHOPPED, bukan COOKED/BURNED)
//            if (canCookPizza(pizza)) { // cek apakah pizza valid untuk dimasak
//                oven.setDish(pizza); // masukin pizza ke oven
//                chef.setInventory(null); // tangan chef jadi kosong
//                startCooking(); // mulai proses memasak (timer 12 detik dan 24 detik dimulai)
//                System.out.println("Pizza dimasukkan ke oven!");
//            }
//        }
//
//        // chef tangan kosong + oven ada pizza -> ambil pizza dari oven (baik matang atau gosong)
//        else if (chefItem == null && oven.hasDish()) { // tangan chef kosong dan oven ada pizza
//            Pizza pizza = (Pizza) oven.getDish(); // ambil pizza dari oven
//            chef.setInventory(pizza); // chef sekarang membawa pizza (matang atau gosong)
//            oven.setDish(null); // oven jadi kosong
//            stopCooking(); // hentikan semua timer cooking
//
//            // kasih feedback ke chef apakah pizza matang atau gosong
//            if (isPizzaBurned(pizza)) {
//                System.out.println("Pizza gosong diambil! Harus dibuang ke trash!");
//            } else {
//                System.out.println("Pizza matang diambil dari oven!");
//            }
//        }
    }

    // method untuk cek apakah pizza bisa dimasak (semua ingredient harus RAW/CHOPPED)
//    private boolean canCookPizza(Pizza pizza) {
//        for (Preparable ingredient : pizza.getIngredients()) { // loop semua ingredient di pizza
//            if (!ingredient.canBeCooked()) { // cek apakah ingredient bisa dimasak (bukan COOKED/BURNED)
//                return false; // kalau ada 1 ingredient yang ga bisa dimasak, pizza invalid
//            }
//        }
//        return true; // semua ingredient bisa dimasak
//    }
//
//    // method untuk cek apakah semua ingredient di pizza sudah COOKED
//    private boolean isPizzaCooked(Pizza pizza) {
//        for (Preparable ingredient : pizza.getIngredients()) { // loop semua ingredient di pizza
//            if (ingredient.getState() != IngredientState.COOKED) { // cek apakah ingredient sudah COOKED
//                return false; // kalau ada 1 aja yang belum COOKED, return false
//            }
//        }
//        return true; // semua ingredient sudah COOKED
//    }
//
//    // method untuk cek apakah ada ingredient di pizza yang BURNED
//    private boolean isPizzaBurned(Pizza pizza) {
//        for (Preparable ingredient : pizza.getIngredients()) { // loop semua ingredient di pizza
//            if (ingredient.getState() == IngredientState.BURNED) { // cek apakah ingredient gosong
//                return true; // kalau ada 1 aja yang BURNED, pizza dianggap gosong
//            }
//        }
//        return false; // tidak ada ingredient yang gosong
//    }

    private void startCooking() {
        if (isCooking){ // kalau sudah sedang memasak, jangan mulai lagi
            return; // prevent double cooking
        }

        isCooking = true;
        System.out.println("Oven memasak pizza...");

        // buat timer untuk COOKED (12 detik)
        cookingTimer = new Timer(); // buat timer baru
        cookingTimer.schedule(new TimerTask() { // schedule task yang akan dijalankan setelah delay
            @Override
            public void run() { // method yang akan dijalankan setelah 12 detik
                finishCooking(); // panggil method finishCooking untuk ubah ingredient jadi COOKED
            }
        }, COOKING_DURATION); // delay 12000 milidetik (12 detik)

        // buat timer untuk BURNED (24 detik dari mulai)
        burnTimer = new Timer(); // buat timer baru untuk burn
        burnTimer.schedule(new TimerTask() { // schedule task yang akan dijalankan setelah delay
            @Override
            public void run() { // method yang akan dijalankan setelah 24 detik
                burnFood(); // panggil method burnFood untuk ubah ingredient jadi BURNED
            }
        }, BURNING_DURATION); // delay 24000 milidetik (24 detik dari awal masak)
    }

    // method yang dipanggil setelah 12 detik - ubah semua ingredient jadi COOKED
    private void finishCooking() {
//        Pizza pizza = (Pizza) oven.getDish(); // ambil pizza dari oven
//        if (pizza != null) { // cek apakah oven ada pizza
//            for (Preparable ingredient : pizza.getIngredients()) { // loop semua ingredient di pizza
//                ingredient.cook(); // ubah state ingredient dari RAW/CHOPPED -> COOKED
//            }
//            System.out.println("Pizza sudah matang!");
//        }
    }

    // method yang dipanggil setelah 24 detik - ubah semua ingredient jadi BURNED
    private void burnFood() {
//        Pizza pizza = (Pizza) oven.getDish(); // ambil pizza dari oven
//        if (pizza != null) { // cek apakah oven ada pizza
//            for (Preparable ingredient : pizza.getIngredients()) { // loop semua ingredient di pizza
//                ingredient.burn(); // ubah state ingredient dari COOKED -> BURNED
//            }
//            System.out.println("PIZZA GOSONG!");
//        }
    }


    private void stopCooking() {
        if (cookingTimer != null) { // cek apakah cookingTimer ada
            cookingTimer.cancel(); // cancel timer COOKED
            cookingTimer = null; // set jadi null
        }
        if (burnTimer != null) { // cek apakah burnTimer ada
            burnTimer.cancel(); // cancel timer BURNED
            burnTimer = null; // set jadi null
        }
        isCooking = false; // set status jadi tidak sedang memasak
    }


//    public Oven getOven() {
//        return oven;
//    }

    public boolean isCooking() {
        return isCooking;
    }
}