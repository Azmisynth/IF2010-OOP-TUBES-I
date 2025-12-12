package model.station;
import helper.SoundPlayer;
import model.chef.ChefPlayer;
import model.item.*;
import model.kitchen.Recipe;
import model.kitchen.RecipeBook;

import java.util.Timer;
import java.util.TimerTask;

public class CookingStation extends Station {
    private Item itemOnStation; // Item yang ada di kompor (Harusnya Plate)
    private Recipe currentRecipe; // Resep apa yang sedang dimasak

    private Timer cookingTimer; // Timer untuk mngiitung waktu sampe pizza COOKED (12 detik)
    private volatile boolean isCooking; // Status oven lagi memasak atau gak
    private volatile int timeElapsed;

    private static final int TICK_RATE = 100;
    private static final int COOKING_DURATION = 12000; // 12 detik dalam milidetik - waktu untuk pizza jadi COOKED
    private static final int BURNING_DURATION = 24000; // 24 detik dalam milidetik - waktu untuk pizza jadi BURNED (dihitung dari awal masak)
    private SoundPlayer cookingSound = new SoundPlayer("/sound/cooking.wav");

    public CookingStation() {
        super("R"); // panggil constructor parent class Station dengan simbol R sebagai String
        this.isCooking = false; // status cooking dimulai dari belum masak
        this.itemOnStation = null;
        this.timeElapsed = 0;
    }

    @Override
    public void interact(ChefPlayer chef) {
        if (!isCooking && itemOnStation != null) {
            if (canBeCooked(itemOnStation)) {
                startCooking();
                cookingSound.play();
                System.out.println("Action: Kompor dinyalakan manual.");
            } else {
                System.out.println("Action: Item ini tidak bisa dimasak/sudah matang.");
            }
        }
    }

    public void startCooking() {
        if (isCooking) return;

        isCooking = true;
        timeElapsed = 0;

        cookingTimer = new Timer();
        cookingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateProcess();
            }
        }, 0, TICK_RATE);
    }

    public void updateProcess() {
        timeElapsed += TICK_RATE;

        // FMATANG (Tepat di 12 detik)
        if (timeElapsed >= COOKING_DURATION && timeElapsed < COOKING_DURATION + TICK_RATE) {
            processItem(ItemState.COOKED); // Ubah jadi Matang
            System.out.println("LOGIC: Makanan MATANG! (Cepat angkat sebelum gosong)");
        }

        // GOSONG (Tepat di 24 detik)
        if (timeElapsed >= BURNING_DURATION) {
            processItem(ItemState.BURNED); // Ubah jadi Gosong
            System.out.println("LOGIC: Makanan GOSONG! (Burned)");
            stopCooking();
        }
    }

    public void processItem(ItemState targetState) {
        if (itemOnStation instanceof Plate) {
            Plate plate = (Plate) itemOnStation;

            // Loop semua isi piring dan ubah statusnya (COOKED/BURNED)
            if (plate.getContents() != null) {
                for (Object obj : plate.getContents()) {
                    if (obj instanceof Ingredient) {
                        ((Ingredient) obj).setState(targetState);
                    }
                }
            }
        }
    }

    @Override
    public void setItemOnStation(Item item) {
        this.itemOnStation = item;

        // RESET STATE TAPI JANGAN MULAI TIMER
        this.timeElapsed = 0;
        this.isCooking = false;

        System.out.println("Logistik: " + item.getName() + " diletakkan di kompor. (Tekan V untuk masak)");
    }

    @Override
    public boolean allowItem(Item item) {
        // Syarat 1: Kompor harus kosong
        if (itemOnStation != null) return false;

        // Syarat 2: Cek apakah item valid untuk ditaruh (Mentah)
        return canBeCooked(item);
    }

    @Override
    public Item getItemOnStation() {
        return itemOnStation;
    }

    @Override
    public void removeItem() {
        // Kalau diambil pas lagi masak, matikan kompor
        stopCooking();
        this.itemOnStation = null;
        this.timeElapsed = 0;
    }

    private void stopCooking() {
        if (cookingTimer != null) {
            cookingTimer.cancel();
            cookingTimer = null;
        }
        isCooking = false;
    }

    private boolean canBeCooked(Item item) {
        if (item instanceof Plate) {
            // Cek isi piring, harus ada yang mentah
            Plate p = (Plate) item;
            if (p.getContents().isEmpty()) return false;
            for (Object obj : p.getContents()) {
                if (obj instanceof Ingredient && ((Ingredient)obj).getState() == ItemState.CHOPPED) {
                    return true;
                }
            }
        }
        return false;
    }

    public double getProgress() {
        return (double) timeElapsed / COOKING_DURATION;
    }
}