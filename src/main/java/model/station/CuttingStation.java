package model.station;

import helper.SoundPlayer;
import model.chef.ChefAction;
import model.chef.ChefPlayer;
import model.item.*;
import model.map.Map;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class CuttingStation extends Station {
    private Item itemOnStation; // item yg ada di atas station
    private boolean isBusy; // status station (lagi dipake atau engga)
    private double progress; // progress motong
    private Timer cuttingTimer; // timer untuk ngitung waktu motong
    private ChefPlayer busyChef; // chef yg lagi motong
    private static final int CUTTING_DURATION = 3000; // durasi motong dalam mili detik
    private static final int TICK_RATE = 100;         // Update setiap 0.1 detik biar bar jalan bar
    private static final double PROGRESS_STEP = (double) TICK_RATE / CUTTING_DURATION;
    private SoundPlayer cuttingSound = new SoundPlayer("/sound/cutting.wav");

    public CuttingStation(){
        super("C"); // simbol C sebagai String
        this.itemOnStation = null;
        this.isBusy = false;
        this.progress = 0.0;
        this.busyChef = null;
    }

    @Override
    public void interact(ChefPlayer chef){
      Item chefItem = chef.getInventory();
//
//        if (chefItem != null && itemOnStation == null && !isBusy){ // kalo chef bawa item dan di station cutting ga ada item dan lgi g sibuk
//            if (chefItem instanceof Preparable || chefItem instanceof Plate) {
//                itemOnStation = chefItem; // maka taro itemnya
//                chef.setInventory(null); // jadi set inventory alias bawaansi chef jadi kosong lagi karena udah ditaro
//                this.progress = 0.0;
//                System.out.println("Menaruh " + itemOnStation.getName() + " di Cutting Station.");
//            }
//            return;
//        }

        if (itemOnStation != null && chefItem == null) {
            if (itemOnStation instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) itemOnStation;
                if (ingredient.getState() == ItemState.RAW) {
                    if (!isBusy) {
                        cuttingSound.play();
                        startOrContinueCutting(chef);
                    } else {
                        System.out.println("Sedang memotong... " + (int) (progress * 100) + "%");
                    }
                }

            }
        }

//        if (chefItem instanceof Preparable && itemOnStation instanceof Plate) {
//
//            Preparable food = (Preparable) chefItem;
//            Plate plate = (Plate) itemOnStation;
//
//            // Syarat: Piring tidak boleh kotor & Bahan siap disajikan
//            if (plate.isClean() && food.canBePlacedOnPlate()) {
//
//                // Masukkan bahan ke dalam objek Plate
//                plate.addComponent(food);
//
//                // Hapus bahan dari tangan Chef
//                chef.setInventory(null);
//
//                System.out.println("Bahan berhasil ditambahkan ke atas Piring di meja!");
//                return;
//            } else {
//                System.out.println("Piring kotor atau bahan belum siap.");
//            }
//        }
//
//        if (chefItem instanceof Plate && itemOnStation instanceof Preparable && !isBusy){
//            Plate plate = (Plate) chefItem;
//            Preparable prep = (Preparable) itemOnStation;
//            if (plate.isClean() && prep.canBeCooked()){ // ini yang fungsi assembly
//                plate.addComponent(prep);
//                itemOnStation = plate;
//                chef.setInventory(null); // tangan chef kosong
//                return;
//            }
//        }

    }

    private void takeItem(ChefPlayer chef) {
        chef.setInventory(itemOnStation);
        itemOnStation = null;
        progress = 0.0;
        System.out.println("Mengambil item dari Cutting Station.");
    }

    public void startOrContinueCutting(ChefPlayer chef){
        if (isBusy){ // klo station lgi motong jangan mulai apa2
            return;
        }

        isBusy = true;
        busyChef = chef;
        chef.startWorking(ChefAction.BUSY_WORKING);
        chef.setBusy(true);
        // set status chef dan station lgi dipakai alias sibuk

        if (progress >= 1.0) progress = 0.0;

        // buat timer untuk proses motong
        cuttingTimer = new Timer();
        cuttingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run(){
                updateProgress(); // panggil method ini kalo waktunya dah abis
            }
        }, 0, TICK_RATE);
    }

    private void updateProgress(){
        if(busyChef == null || !busyChef.isBusy() || busyChef.getCurrentAction() != ChefAction.BUSY_WORKING){
            finishCutting();
            System.out.println("Pemotongan dihentikan karena Chef menjauh atau status diubah.");
            return;
        }
        progress += PROGRESS_STEP; // Nambah dikit-dikit

        if (progress >= 1.0) {
            progress = 1.0;
            SwingUtilities.invokeLater(() -> finishCutting());
        }
    }

    public void finishCutting(){
        if (itemOnStation instanceof Ingredient) {
            Ingredient ingredient = (Ingredient) itemOnStation;
            if (progress >= 1.0 && ingredient.getState() == ItemState.RAW) {
                ingredient.setState(ItemState.CHOPPED);
                System.out.println(ingredient.getName() + " berhasil dipotong!");
            }
        }

        cuttingSound.stop();

        SwingUtilities.invokeLater(() -> {
            if (cuttingTimer != null){
                cuttingTimer.cancel();
                cuttingTimer = null;
            }

            if (busyChef != null) {
                busyChef.setCurrentAction(ChefAction.IDLE);
                busyChef = null;
            }

            isBusy = false;
//            progress = 0.0; // Reset progress bar setelah selesai
        });
    }

    public Item getItemOnStation(){
        return itemOnStation;
    }

    public void setItemOnStation(Item item){
        this.itemOnStation = item;
    }

    public boolean isBusy(){
        return isBusy;
    }

    public double getProgress(){
        return progress;
    }

    @Override
    public boolean allowItem(Item item) {
        if (this.itemOnStation != null) return false;
        else {
            return true;
        }
    }

    public void removeItem() {
        this.itemOnStation = null;
    }
}