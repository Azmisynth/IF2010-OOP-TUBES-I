package main.java.model.station;

import main.java.model.chef.ChefPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class CuttingStation extends Station {
    //private Item itemOnStation; // item yg ada di atas station
    private boolean isBusy; // status station (lagi dipake atau engga)
    private double progress; // progress motong
    private Timer cuttingTimer; // timer untuk ngitung waktu motong
    private ChefPlayer busyChef; // chef yg lagi motong
    private static final int CUTTING_DURATION = 3000; // durasi motong dalam mili detik

    public CuttingStation(){
        super("C"); // simbol C sebagai String
        //this.itemOnStation = null;
        this.isBusy = false;
        this.progress = 0.0;
        this.busyChef = null;
    }

    @Override
    public void interact(ChefPlayer chef){
//        Item chefItem = chef.getInventory();
//
//        if (chefItem != null && itemOnStation == null && !isBusy){ // kalo chef bawa item dan di station cutting ga ada item dan lgi g sibuk
//            itemOnStation = chefItem; // maka taro itemnya
//            chef.setInventory(null); // jadi set inventory alias bawaansi chef jadi kosong lagi karena udah ditaro
//            return;
//        }
//
//        if (itemOnStation != null && chefItem == null && !isBusy ){ // kalo di station ada item dan chef ga bawa apa2 dan lgi ga sibuk
//            chef.setInventory(itemOnStation); // maka ambil item, dan isi inventory alias tangan chef penuh
//            itemOnStation = null; // jadi board stationnya kosong
//            progress = 0.0; // progress balik ke 0.0 (reset)
//            return;
//        }
//
//        if (itemOnStation instanceof Preparable && chefItem == null) {
//            // kalau ada ingredient yg bisa di potong dan tangan chef kosong sok di potong
//            Preparable prep = (Preparable) itemOnStation;
//            if (prep.canbeChopped() && progress < 1.0){ // kalo bisa di potong trus belum selesai di potong
//                startOrContinueCutting(chef); //maka potong
//                return;
//            }
//        }
//
//        if (chefItem instanceof Plate && itemOnStation instanceof Preparable && !isBusy){
//            Plate plate = (Plate) chefItem;
//            Preparable prep = (Preparable) itemOnStation;
//            if (!plate.isDirty() && prep.getState() == IngredientState.CHOPPED){ // ini yang fungsi assembly
//                plate.addComponent(prep);
//                itemOnStation = plate;
//                chef.setInventory(null); // tangan chef kosong
//                return;
//            }
//        }
//
//        if (chefItem instanceof Preparable && itemOnStation instanceof Plate && !isBusy){
//            Plate plate = (Plate) itemOnStation;
//            Preparable prep = (Preparable) chefItem;
//            if (!plate.isDirty() && prep.getState() == IngredientState.CHOPPED){ // ini yang fungsi assembly
//                plate.addComponent(prep);
//                chef.setInventory(null); // tangan chef kosong
//                return;
//            }
//        }

    }

    public void startOrContinueCutting(ChefPlayer chef){
        if (isBusy){ // klo station lgi motong jangan mulai apa2
            return;
        }

        isBusy = true;
        busyChef = chef;
        //chef.setBusy(true);
        // set status chef dan station lgi dipakai alias sibuk

        int remainingTime = (int) ((1.0 - progress) * CUTTING_DURATION);
        // ngitung sisa waktu berdasar progress
        // jadi kek klo progress misalnya 50% maka sisa waktu = 50% x 3000ms = 1500ms

        // buat timer untuk proses motong
        cuttingTimer = new Timer();
        cuttingTimer.schedule(new TimerTask() {
            @Override
            public void run(){
                finishCutting(); // panggil method ini kalo waktunya dah abis
            }
        }, remainingTime);
    }

    public void finishCutting(){
//        if (itemOnStation instanceof Preparable){
//            Preparable prep = (Preparable) itemOnStation;
//            prep.chop(); // ubah state jdi CHOPPED
//            progress = 1.0; // progress dah penuh
//        }

        if (busyChef != null){
            //busyChef.setBusy(false);
            busyChef = null;
        }

        isBusy = false;
    }

    public void stopCutting(){ // untuk kalau player berhenti hold tombol
        if (cuttingTimer != null){ // timer di hentiin dlu
            cuttingTimer.cancel();
            cuttingTimer = null;
        }
        if (busyChef != null){ // chef dibuat jdi ga sibuk
            //busyChef.setBusy(false);
            busyChef = null;
        }

        isBusy = false; // progress cuma berhenti ga di reset okeng
    }

//    public Item getItemOnStation(){
//        return itemOnStation;
//    }
//
//    public void setItemOnStation(Item item){
//        this.itemOnStation = item;
//    }

    public boolean isBusy(){
        return isBusy;
    }

    public double getProgress(){
        return progress;
    }
}