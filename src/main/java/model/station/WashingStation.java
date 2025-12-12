package model.station;

import model.chef.ChefAction;
import model.chef.ChefPlayer;
import model.chef.Direction;
import model.item.Item;
import model.item.Plate;

import javax.swing.*;
import java.util.Stack; // utk nyimpen piring kotor dan bersih dengan sistem LIFO (Last In First Out)
import java.util.Timer;
import java.util.TimerTask;

public class WashingStation extends Station {
    private Stack<Plate> dirtyPlatesStack; // stack buat nyimpen piring-piring kotor yang nunggu dicuci
    private Stack<Plate> cleanPlatesStack; // stack buat nyimpen piring-piring yang udah dicuci
    private boolean isBusy; // buat tandain station lagi sibuk cuci atau ngga
    private double progress; // simpen progress pencucian dari 0.0 (belum mulai) sampai 1.0 (selesai)
    private Timer washingTimer; // timer buat ngatur durasi proses cuci
    private ChefPlayer busyChef; // simpen referensi chef yang lagi cuci piring

    private static final int TICK_RATE = 100;
    private static final int WASHING_DURATION = 3000; // konstanta durasi cuci 3 detik (3000 milidetik) per piring
    private static final double PROGRESS_STEP = (double) TICK_RATE / WASHING_DURATION;

    public WashingStation() {
        super("W"); // simbol W sebagai String
        this.dirtyPlatesStack = new Stack<>(); // bikin stack baru buat piring kotor
        this.cleanPlatesStack = new Stack<>(); // bikin stack baru buat piring bersih
        this.isBusy = false; // awalnya station ngga sibuk
        this.progress = 0.0; // progress awalnya 0%
        this.busyChef = null; // belum ada chef yang cuci
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef

        // chef bawa piring kotor mau ditaro buat dicuci (drop)
//        if (chefItem instanceof Plate) { // cek chef bawa Plate
//            Plate plate = (Plate) chefItem; // cast item jadi Plate
//            if (!plate.isClean()) { // cek piringnya kotor apa ngga
//                dirtyPlatesStack.push(plate); // masukin piring kotor ke stack
//                chef.setInventory(null); // kosongin inventory chef
//                System.out.println("Piring kotor diletakkan untuk dicuci. Total: " + dirtyPlatesStack.size());
//            }
//            return;
//        }
        // chef mau ambil piring bersih (pick up)
//        if (chefItem == null && !cleanPlatesStack.isEmpty()) { // cek chef ngga bawa apa-apa dan ada piring bersih
//            stopWashing(); // Stop nyuci kalau mau ambil
//            Plate cleanPlate = cleanPlatesStack.pop(); // ambil piring bersih dari stack
//            chef.setInventory(cleanPlate); // kasih piring bersih ke chef
//            System.out.println("Piring bersih diambil"); // kasih tau berhasil ambil
//            return;
//        }
        // chef mau mulai cuci piring
        if (chefItem == null && !dirtyPlatesStack.isEmpty() && !isBusy) { // cek chef kosong, ada piring kotor, dan station ngga lagi sibuk
            startOrContinueWashing(chef); // mulai proses cuci
       }
    }

    private void startOrContinueWashing(ChefPlayer chef) { // method buat mulai atau lanjutin proses cuci piring
        if (isBusy) { // kalau station lagi sibuk
            System.out.println("Sedang mencuci..."); // kasih tau lagi cuci
            return;
        }

        if (dirtyPlatesStack.isEmpty()) { // kalau ngga ada piring kotor
            System.out.println("Tidak ada piring kotor untuk dicuci"); // kasih tau ngga ada yang bisa dicuci
            return;
        }

        isBusy = true; // tandain station jadi sibuk
        busyChef = chef; // simpen chef yang lagi cuci
        chef.startWorking(ChefAction.BUSY_WORKING); // tandain chef jadi sibuk

        if (progress >= 1.0) progress = 0.0;

        System.out.println("Mencuci piring... Progress: " + (int)(progress * 100) + "%");
        washingTimer = new Timer();
        washingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateProgress();
            }
        }, 0, TICK_RATE);
    }

    private void finishWashingOnePlate() { // method yang dipanggil pas selesai cuci satu piring
        if (washingTimer != null) { // kalau timer lagi jalan
            washingTimer.cancel(); // cancel timer
            washingTimer = null; // hapus referensi timer
        }

        if (!dirtyPlatesStack.isEmpty()) { // cek masih ada piring kotor
            Plate plate = dirtyPlatesStack.pop(); // ambil piring kotor dari stack
            plate.setClean(true); // tandain piring jadi bersih
            plate.clearContents(); // bersihin isi piring (dish/component)
            cleanPlatesStack.push(plate); // masukin piring bersih ke stack piring bersih
            System.out.println("1 piring selesai dicuci! Sisa piring kotor: " + dirtyPlatesStack.size());
        }

        // reset progress buat piring berikutnya (kalau mau cuci lagi)
        progress = 0.0; // mulai dari 0% lagi

        if (busyChef != null) { // kalau ada chef yang lagi sibuk
            busyChef.finishWorking(); // bebasin chef dari status sibuk
            busyChef = null; // hapus referensi chef
        }
        isBusy = false;
    }

    public void stopWashing() {
        if (washingTimer != null) { // kalau timer lagi jalan
            washingTimer.cancel(); // cancel timer
            washingTimer = null; // hapus referensi timer
        }
        isBusy = false;
        System.out.println("Pencucian dihentikan. Progress tersimpan: " + (int)(progress * 100) + "%");
    }

    private void updateProgress() {
        if (busyChef == null || !busyChef.isBusy() || busyChef.getCurrentAction() != ChefAction.BUSY_WORKING) {
            stopWashing();
            System.out.println("Pencucian dihentikan karena Chef menjauh atau status diubah.");
            return;
        }

        progress += PROGRESS_STEP;

        if (progress >= 1.0) {
            progress = 1.0;
            // Panggil penyelesaian di SwingUtilities.invokeLater karena ini memengaruhi Model
            SwingUtilities.invokeLater(() -> finishWashingOnePlate());
        }
    }

    public boolean hasDirtyPlates() {
        return !dirtyPlatesStack.isEmpty();
    }

    public boolean hasCleanPlates() {
        return !cleanPlatesStack.isEmpty();
    }

    public boolean isBusy() {
        return isBusy;
    }

    public double getProgress() {
        return progress;
    }
}