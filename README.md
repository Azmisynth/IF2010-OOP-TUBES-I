# KrustyCooked Game

## Deskripsi Singkat

KrustyCooked adalah game 2D berbasis Java (Swing) yang mensimulasikan aktivitas memasak dengan sistem waktu (timer), menu utama, halaman how-to-play.

---
## Fitur Utama

### Kontrol Chef
- Mengendalikan **dua chef** dalam satu dapur.
- Hanya satu chef aktif dalam satu waktu.
- Dapat berpindah chef kapan saja tanpa menghentikan aksi chef lain.

### Pergerakan & Navigasi
- Pergerakan berbasis grid (atas, bawah, kiri, kanan).
- Chef tidak dapat melewati dinding, station, atau chef lain.
- Arah hadap menentukan objek yang dapat diinteraksikan.

### Manajemen Inventori
- Setiap chef hanya memiliki **1 slot inventori**.
- Chef dapat mengambil, membawa, dan meletakkan item.
- Mendukung kitchen utensils yang berisi ingredient.
- Plate bersih dapat melakukan **plating otomatis**.

### Pengolahan Bahan
- Mengambil bahan mentah dari Ingredient Storage.
- Memotong bahan di Cutting Station.
- Memasak bahan di Cooking Station.
- State ingredient:
    - `RAW`
    - `CHOPPED`
    - `COOKED`
    - `BURNED`

### Memasak & Alat Masak
- Menggunakan kitchen utensils berupa Oven dan Plate.
- Proses memasak berjalan otomatis berbasis waktu.
- Masakan yang dibiarkan terlalu lama akan gosong.

### Perakitan & Penyajian
- Menggabungkan ingredient menjadi dish di atas plate.
- Menyajikan dish ke Serving Counter.
- Sistem memvalidasi kecocokan dish dengan order pelanggan.

### Manajemen Piring
- Plate bersih tersedia di Plate Storage.
- Plate menjadi kotor setelah penyajian.
- Plate kotor harus dicuci di Washing Station sebelum digunakan kembali.

### Sistem Order
- Order pelanggan muncul secara terus-menerus dengan jeda 15 detik.
- Setiap order memiliki:
    - Resep
    - Batas waktu
    - Reward dan penalty skor
- Order diselesaikan berdasarkan urutan kedatangan.

### 🗺️ Map & Stage
- Map berbentuk grid **16 × 10** dengan tipe map **Pizza**

### Aksi Berbasis Waktu & Concurrency
- Aksi seperti memotong, memasak, dan mencuci membutuhkan waktu.
- Aksi tetap berjalan walaupun chef tidak sedang dikendalikan.

### Skor & Kondisi Akhir
- Mendapat skor dari order yang berhasil.
- Penalti jika order gagal atau salah penyajian.
- Stage berakhir jika:
    - Waktu habis, atau
    - Terlalu banyak order gagal berturut-turut.


---
## Fitur Bonus

### Dash
Chef dapat melakukan **dash** menggunakan kombinasi tombol tertentu untuk bergerak maju lebih dari satu kotak (2–4 kotak, tergantung konfigurasi).  
Dash memiliki **cooldown**, sehingga tidak dapat digunakan secara terus-menerus dan harus digunakan secara taktis saat bermain.

---

### Lempar (Throw Item)
Chef dapat **melempar ingredient yang belum dimasak** ke arah tertentu.  
Mekanisme lempar memiliki ketentuan sebagai berikut:
- Jarak lemparan sebanyak 4 kotak.
- Lemparan akan divalidasi terhadap map.
- Jika lintasan lempar melewati tembok, item akan jatuh tepat di kotak terakhir sebelum tembok.
- Item yang dilempar dapat **ditangkap oleh chef lain**, mendukung kerja sama antar pemain.

---

### Random Level Generator
Game menggunakan **Random Level Generator berbasis Cellular Automata** untuk menghasilkan layout dapur secara acak namun tetap valid.

Tahapan umum generator:
1. Inisialisasi map secara acak (lantai dan tembok).
2. Penerapan aturan Cellular Automata dalam beberapa iterasi untuk membentuk struktur ruang yang lebih natural.
3. Validasi konektivitas untuk memastikan area map saling terhubung.
4. Penempatan station pada area yang dapat diakses.
5. Validasi akhir untuk memastikan:
   - Tidak ada station yang terisolasi.
   - Semua dish yang tersedia dapat diselesaikan.
6. Fitur ini dapat diakses pada pemilihan stage dengan button **?** (tanda tanya)

---

## Alur Game
1. Pemain memilih stage.
2. Order pelanggan mulai bermunculan.
3. Chef mengolah bahan, memasak, dan menyajikan hidangan.
4. Stage berakhir dan skor ditampilkan.

---

## Prasyarat

Sebelum melakukan kompilasi dan menjalankan program, pastikan:

* Java Development Kit (JDK) **versi 8 atau lebih baru** sudah terpasang
* Variabel lingkungan `JAVA_HOME` telah terkonfigurasi
* Sistem operasi mendukung Java Swing (Windows / Linux / macOS)

Untuk memastikan Java terpasang:

```bash
java -version
javac -version
```

---

## Struktur Proyek

```
├── 📂 src/
│   └── 📂 main/
│       ├── 📂 java/
│       │   ├── 📂 controller/
│       │   │   └── ChefInputListener.java
│       │   ├── 📂 helper/
│       │   │   └── SoundPlayer.java
│       │   ├── 📂 model/
│       │   │   ├── 📂 chef/
│       │   │   │   ├─ ChefAction.java
│       │   │   │   ├─ ChefPlayer.java
│       │   │   │   ├─ Direction.java
│       │   │   │   ├─ Moveable.java
│       │   │   │   └─ Position.java
│       │   │   ├── 📂 item/
│       │   │   │   ├─ Cheese.java
│       │   │   │   ├─ Chicken.java
│       │   │   │   ├─ CookingDevice.java
│       │   │   │   ├─ Dish.java
│       │   │   │   ├─ Dough.java
│       │   │   │   ├─ Ingredient.java
│       │   │   │   ├─ Item.java
│       │   │   │   ├─ ItemState.java
│       │   │   │   ├─ KitchenUtensils.java
│       │   │   │   ├─ Oven.java
│       │   │   │   ├─ Plate.java
│       │   │   │   ├─ Preparable.java
│       │   │   │   ├─ Sausage.java
│       │   │   │   └─ Tomato.java
│       │   │   ├── 📂 kitchen/
│       │   │   │   ├─ Order.java
│       │   │   │   ├─ OrderManager.java
│       │   │   │   ├─ Recipe.java
│       │   │   │   └─ RecipeBook.java
│       │   │   ├── 📂 map/
│       │   │   │   ├─ LevelGenerator.java
│       │   │   │   ├─ Map.java
│       │   │   │   ├─ MapType.java
│       │   │   │   ├─ PizzaMap.java
│       │   │   │   ├─ Tile.java
│       │   │   │   └─ TileState.java
│       │   │   └── 📂 station/
│       │   │       ├─ AssemblyStation.java
│       │   │       ├─ CookingStation.java
│       │   │       ├─ CuttingStation.java
│       │   │       ├─ IngredientStation.java
│       │   │       ├─ PlateStorage.java
│       │   │       ├─ ServingCounter.java
│       │   │       ├─ Station.java
│       │   │       ├─ StationFactory.java
│       │   │       ├─ TrashStation.java
│       │   │       └─ WashingStation.java
│       │   └── 📂 view/
│       │       ├─ ChefView.java
│       │       ├─ DetailStagePanel.java
│       │       ├─ ExitConfirmDialog.java
│       │       ├─ ExitConfirmPanel.java
│       │       ├─ GameFrame.java
│       │       ├─ GamePanel.java
│       │       ├─ HowToPlayDialog.java
│       │       ├─ HowToPlayPanel.java
│       │       ├─ MainMenuPanel.java
│       │       ├─ MapViewPanel.java
│       │       ├─ OutlineLabel.java
│       │       ├─ ResultScreenDialog.java
│       │       ├─ ResultScreenPanel.java
│       │       ├─ SettingsDialog.java
│       │       ├─ SettingsPanel.java
│       │       ├─ StageInfoDialog.java
│       │       ├─ StageInfoPanel.java
│       │       ├─ StageSelectPanel.java
│       │       └─ StationView.java
│       └── 📂 resources/
│           ├── 📂 images/
│           │   ├── 📂 chef/
│           │   │   ├─ chef1_down.png
│           │   │   ├─ chef1_left.png
│           │   │   ├─ chef1_right.png
│           │   │   ├─ chef1_up.png
│           │   │   ├─ chef2_down.png
│           │   │   ├─ chef2_left.png
│           │   │   ├─ chef2_right.png
│           │   │   └─ chef2_up.png
│           │   ├── 📂 item/
│           │   │   ├─ burnt.png
│           │   │   ├─ Cheese_CHOPPED.png
│           │   │   ├─ cheese_station.png
│           │   │   ├─ chicken_station.png
│           │   │   ├─ dough.png
│           │   │   ├─ dough_station.png
│           │   │   ├─ ingredient_ayam_chopped.png
│           │   │   ├─ ingredient_ayam_raw.png
│           │   │   ├─ ingredient_keju_raw.png
│           │   │   ├─ ingredient_sosis_chopped.png
│           │   │   ├─ ingredient_sosis_raw.png
│           │   │   ├─ ingredient_tomato_raw.png
│           │   │   ├─ pizza_ayam.png
│           │   │   ├─ pizza_margherita.png
│           │   │   ├─ pizza_polosan.png
│           │   │   ├─ pizza_sosis.png
│           │   │   ├─ pizza_tomat.png
│           │   │   ├─ plate.png
│           │   │   ├─ plate_DIRTY.png
│           │   │   ├─ sausage_station.png
│           │   │   ├─ tomat.png
│           │   │   └─ tomat_slice.png
│           │   ├── 📂 main_menu/
│           │   │   ├─ back_button.png
│           │   │   ├─ back_button_hover.png
│           │   │   ├─ background.png
│           │   │   ├─ background_transparent.png
│           │   │   ├─ exit.png
│           │   │   ├─ exit_button.png
│           │   │   ├─ exit_button_hover.png
│           │   │   ├─ exit_confirmation.png
│           │   │   ├─ exit_hover.png
│           │   │   ├─ how_to_play.png
│           │   │   ├─ how_to_play_hover.png
│           │   │   ├─ how_to_play_screen.png
│           │   │   ├─ start_game.png
│           │   │   └─ start_game_hover.png
│           │   ├── 📂 stage/
│           │   │   ├─ complete_stage1.png
│           │   │   ├─ complete_stage2.png
│           │   │   ├─ complete_stage3.png
│           │   │   ├─ game_over.png
│           │   │   ├─ preview_map.png
│           │   │   ├─ preview_stage1.png
│           │   │   ├─ preview_stage2.png
│           │   │   ├─ preview_stage3.png
│           │   │   ├─ select_stage.png
│           │   │   ├─ star.png
│           │   │   └─ times_up.png
│           │   └── 📂 station/
│           │       ├─ assembly_station_bottom.png
│           │       ├─ assembly_station_normal.png
│           │       ├─ cooking_station_left.png
│           │       ├─ cooking_station_right.png
│           │       ├─ cutting_station.png
│           │       ├─ ingredient_station.png
│           │       ├─ plate_storage.png
│           │       ├─ serving_atas.png
│           │       ├─ serving_bawah.png
│           │       ├─ serving_counter.png
│           │       ├─ trash_station.png
│           │       └─ washing_station.png
│           └── 📂 sound/
│               ├─ bgm.wav
│               ├─ complete_order.wav
│               ├─ cooking.wav
│               ├─ cutting.wav
│               ├─ game_over.wav
│               └─ placement.wav
├── .gitignore
└── README.md
```

---

## Tahapan Kompilasi dan Menjalankan Program

### 1. Clone Repository

Buka terminal atau command prompt, lalu jalankan perintah berikut untuk meng-clone repository game ini:

```bash
git clone <IF2010-OOP-TUBES-I>
cd <IF2010-OOP-TUBES-I>
```

### 2. Compile dan Jalankan Game

Buka terminal di root project (atau terminal bawaan IDE), lalu jalankan:

Pada Windows (PowerShell):

```powershell
.\gradlew.bat run
```

3. Game akan terbuka dalam jendela aplikasi

## Kontrol Game

### Gerak Pemain

* **W / Panah Atas** : Bergerak ke atas
* **S / Panah Bawah** : Bergerak ke bawah
* **A / Panah Kiri** : Bergerak ke kiri
* **D / Panah Kanan** : Bergerak ke kanan

### Aksi & Interaksi

* **V** : Action di station
* **C** : Pick up / Drop item
* **B** : Switch chef
* **T** : Throw item
* **Space** : Dash
