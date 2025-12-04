package main.java.model.station;

import main.java.model.chef.ChefPlayer;

import java.util.Stack; // utk nyimpen piring kotor dan bersih dengan sistem LIFO (Last In First Out)
import java.util.Timer;
import java.util.TimerTask;

public class WashingStation extends Station {
//    private Stack<Plate> dirtyPlatesStack; // stack buat nyimpen piring-piring kotor yang nunggu dicuci
//    private Stack<Plate> cleanPlatesStack; // stack buat nyimpen piring-piring yang udah dicuci
    private boolean isBusy; // buat tandain station lagi sibuk cuci atau ngga
    private double progress; // simpen progress pencucian dari 0.0 (belum mulai) sampai 1.0 (selesai)
    private Timer washingTimer; // timer buat ngatur durasi proses cuci
    private ChefPlayer busyChef; // simpen referensi chef yang lagi cuci piring

    private static final int WASHING_DURATION = 3000; // konstanta durasi cuci 3 detik (3000 milidetik) per piring

    public WashingStation() {
        super("W"); // simbol W sebagai String
//        this.dirtyPlatesStack = new Stack<>(); // bikin stack baru buat piring kotor
//        this.cleanPlatesStack = new Stack<>(); // bikin stack baru buat piring bersih
        this.isBusy = false; // awalnya station ngga sibuk
        this.progress = 0.0; // progress awalnya 0%
        this.busyChef = null; // belum ada chef yang cuci
    }

    @Override
    public void interact(ChefPlayer chef) {
//        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef
//
//        // chef bawa piring kotor mau ditaro buat dicuci
//        if (chefItem instanceof Plate) { // cek chef bawa Plate
//            Plate plate = (Plate) chefItem; // cast item jadi Plate
//            if (plate.isDirty()) { // cek piringnya kotor apa ngga
//                dirtyPlatesStack.push(plate); // masukin piring kotor ke stack
//                chef.setInventory(null); // kosongin inventory chef
//                System.out.println("Piring kotor diletakkan untuk dicuci. Total: " + dirtyPlatesStack.size());
//            }
//        }
//        // chef mau ambil piring bersih
//        else if (chefItem == null && !cleanPlatesStack.isEmpty()) { // cek chef ngga bawa apa-apa dan ada piring bersih
//            Plate cleanPlate = cleanPlatesStack.pop(); // ambil piring bersih dari stack
//            chef.setInventory(cleanPlate); // kasih piring bersih ke chef
//            System.out.println("Piring bersih diambil"); // kasih tau berhasil ambil
//        }
//        // chef mau mulai cuci piring
//        else if (chefItem == null && !dirtyPlatesStack.isEmpty() && !isBusy) { // cek chef kosong, ada piring kotor, dan station ngga lagi sibuk
//            startOrContinueWashing(chef); // mulai proses cuci
//        }
    }

    private void startOrContinueWashing(ChefPlayer chef) { // method buat mulai atau lanjutin proses cuci piring
        if (isBusy) { // kalau station lagi sibuk
            System.out.println("Sedang mencuci..."); // kasih tau lagi cuci
            return;
        }

//        if (dirtyPlatesStack.isEmpty()) { // kalau ngga ada piring kotor
//            System.out.println("Tidak ada piring kotor untuk dicuci"); // kasih tau ngga ada yang bisa dicuci
//            return;
//        }

        isBusy = true; // tandain station jadi sibuk
        busyChef = chef; // simpen chef yang lagi cuci
        //chef.setBusy(true); // tandain chef jadi sibuk

        // hitung sisa waktu berdasarkan progress yang udah ada (buat kalau pencucian dilanjutin)
        int remainingTime = (int) ((1.0 - progress) * WASHING_DURATION); // kalau progress 0.5 berarti sisa 50% dari 3000ms = 1500ms

        System.out.println("Mencuci piring... Progress: " + (int)(progress * 100) + "%"); // kasih tau progress saat ini dalam persen

        washingTimer = new Timer(); // bikin Timer baru
        washingTimer.schedule(new TimerTask() { // jadwalin TimerTask baru
            @Override // override method run dari TimerTask
            public void run() { // ini yang dijalanin pas timer selesai
                finishWashingOnePlate(); // panggil method buat selesaiin cuci satu piring
            }
        }, remainingTime); // jalanin setelah sisa waktu yang dihitung tadi
    }

    private void finishWashingOnePlate() { // method yang dipanggil pas selesai cuci satu piring
//        if (!dirtyPlatesStack.isEmpty()) { // cek masih ada piring kotor
//            Plate plate = dirtyPlatesStack.pop(); // ambil piring kotor dari stack
//            plate.setDirty(false); // tandain piring jadi bersih
//            plate.clear(); // bersihin isi piring (dish/component)
//            cleanPlatesStack.push(plate); // masukin piring bersih ke stack piring bersih
//            progress = 1.0; // set progress jadi 100% (selesai)
//            System.out.println("1 piring selesai dicuci! Sisa piring kotor: " + dirtyPlatesStack.size());
//        }

        // reset progress buat piring berikutnya (kalau mau cuci lagi)
        progress = 0.0; // mulai dari 0% lagi

        if (busyChef != null) { // kalau ada chef yang lagi sibuk
            //busyChef.setBusy(false); // bebasin chef dari status sibuk
            busyChef = null; // hapus referensi chef
        }
        isBusy = false; // station jadi ngga sibuk lagi
    }

    public void stopWashing() {
        if (washingTimer != null) { // kalau timer lagi jalan
            washingTimer.cancel(); // cancel timer
            washingTimer = null; // hapus referensi timer
        }

        if (busyChef != null) { // kalau ada chef yang sibuk
            //busyChef.setBusy(false); // bebasin chef
            busyChef = null; // hapus referensi chef
        }

        isBusy = false; // station jadi ngga sibuk
        System.out.println("Pencucian dihentikan. Progress tersimpan: " + (int)(progress * 100) + "%");
    }

//    public Stack<Plate> getDirtyPlatesStack() {
//        return dirtyPlatesStack;
//    }
//
//    public Stack<Plate> getCleanPlatesStack() {
//        return cleanPlatesStack;
//    }

    public boolean isBusy() {
        return isBusy;
    }

    public double getProgress() {
        return progress;
    }
}