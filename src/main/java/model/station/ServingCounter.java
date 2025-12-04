//package main.java.model.station;
//
//import main.java.model.chef.Chef;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class ServingCounter extends Station {
//    private PlateStorage plateStorage; // simpen referensi ke PlateStorage buat nanti kembaliin piring kotor
//
//    private static final int PLATE_RETURN_DELAY = 10000; // 10 detik (dalam milidetik) buat delay kembaliin piring
//
//    public ServingCounter(PlateStorage plateStorage) {
//        super("S"); // simbol S sebagai String
//        this.plateStorage = plateStorage; // simpen referensi PlateStorage yang dikasih
//    }
//
//    @Override // override method interactnya Station
//    public void ChefPlayer(Chef chef) { // method yang dipanggil pas chef berinteraksi sama counter ini
//        Item chefItem = chef.getInventory(); // ambil item yang lagi dibawa chef
//
//        if (chefItem instanceof Plate) { // cek apakah item yang dibawa adalah Plate
//            Plate plate = (Plate) chefItem; // cast item jadi Plate biar bisa akses method Plate
//            if (!plate.isDirty() && plate.hasDish()) { // cek piring bersih dan ada dish-nya
//                boolean success = serveDish(plate); // coba sajiin dish-nya, simpen hasilnya
//                if (success) { // kalau berhasil (dish sesuai order)
//                    plate.setDirty(true); // tandain piring jadi kotor
//                    chef.setInventory(null); // kosongin inventory chef karena piringnya udah diserahin
//
//                    schedulePlateReturn(plate); // jadwalin piring ini buat dikembaliin ke storage nanti
//
//                    System.out.println("Dish berhasil disajikan!");
//                } else { // kalau gagal (dish gak sesuai order)
//                    // dish tetep dimakan tapi dapet penalty
//                    plate.setDirty(true); // piring tetep jadi kotor
//                    chef.setInventory(null); // inventory chef tetep dikosongin
//
//                    schedulePlateReturn(plate); // jadwalin piring dikembaliin juga
//
//                    System.out.println("Dish tidak sesuai order! Penalty diberikan.");
//                }
//            } else { // kalau piring kotor atau gak ada dish
//                System.out.println("Tidak ada dish yang dapat disajikan!");
//            }
//        } else { // kalau yang dibawa bukan Plate
//            System.out.println("Tidak bisa melayani tanpa plate!");
//        }
//    }
//
//    private void schedulePlateReturn(Plate plate) { // method buat jadwalin kembaliin piring setelah delay
//        Timer returnTimer = new Timer(); // bikin Timer baru
//        returnTimer.schedule(new TimerTask() { // jadwalin TimerTask baru
//            @Override // override method run dari TimerTask
//            public void run() { // ini yang dijalanin pas timer selesai
//                if (plateStorage != null) { // cek plateStorage ada gak
//                    plateStorage.returnDirtyPlate(plate); // kembaliin piring kotor ke storage
//                    System.out.println("Piring kotor dikembalikan ke Plate Storage");
//                }
//                returnTimer.cancel(); // matiin timer karena udah kelar tugasnya
//            }
//        }, PLATE_RETURN_DELAY); // eksekusi setelah delay 10 detik
//    }
//
//    public boolean serveDish(Plate dishOnPlate) { // method buat validasi dan sajiin dish
//        Dish dish = dishOnPlate.getDish(); // ambil dish dari plate
//
//        if (dish != null) { // cek dish-nya ada gak
//            boolean isValidOrder = validateOrder(dish); // cek apakah dish sesuai order yang ada
//
//            if (isValidOrder) { // kalau sesuai order
//                // nanti tambahin score dan hapus order
//                // GameManager.getInstance().addScore(order.getReward());
//                // OrderManager.getInstance().removeOrder(order);
//                return true; // return true artinya berhasil
//            } else { // kalau gak sesuai order
//                // nanti kurangin score (penalty)
//                // GameManager.getInstance().addScore(-order.getPenalty());
//                return false; // return false artinya gagal
//            }
//        }
//        return false; // kalau dish null, return false
//    }
//
//    private boolean validateOrder(Dish dish) { // method utk validasi dish sama order aktif
//        // cek apakah dish cocok sama salah satu order yang lagi aktif
//        return true; // sementara selalu return true (placeholder)
//    }
//
//    public void setPlateStorage(PlateStorage plateStorage) {
//        this.plateStorage = plateStorage; // set plateStorage dengan yang baru
//    }
//}