package model.chef;

import model.item.*;
import model.kitchen.EventManager;
import model.map.*;
import model.station.*;

import java.util.List;

public class ChefPlayer implements Moveable {
    private String id;
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory; // item yang dibawa oleh chef dan bisa null
    private boolean active;
    private volatile boolean busy = false;
    private ChefAction currentAction;
    private static final int THROW_DISTANCE = 4;
    private static final long DASH_COOLDOWN_MS = 3000; // Cooldown 3 Detik
    private static final int DASH_DISTANCE = 3;         // Jarak 3 Kotak
    private long lastDashTime = 0;                      // Waktu terakhir dash

    public ChefPlayer(String id, String name, Position startPosition) {
        this.id = id;
        this.name = name;
        this.position = startPosition;
        this.direction = Direction.DOWN;
        this.inventory = null;
        this.active = false;
        this.currentAction = ChefAction.IDLE;
    }

    public ChefAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(ChefAction action) {
        this.currentAction = action;
    }

    public void startWorking(ChefAction action) {
        this.currentAction = action;
        this.setBusy(true); // setBusy tetap dipertahankan untuk mengontrol thread stasiun
    }

    public void finishWorking() {
        this.currentAction = ChefAction.IDLE;
        this.setBusy(false);
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() { return direction;}

    public Item getInventory() {
        return inventory;
    }
    public void pickUpOrDrop(Map map) {
        if (!active) return;

        // Dapatkan Target di depan Chef
        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if (targetTile == null) return;

        Station station = targetTile.getStation();

        // Cabang Logika
        if (station != null) {
            handleStationInteraction(station, targetTile);
        } else if (targetTile.isWalkable(targetPosition.getX(), targetPosition.getY())) {
            handleTileInteraction(targetTile);
        }
    }


    public void interact(Map map) {
        if (!active) return;

        this.currentAction = ChefAction.INTERACTING;

        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if (targetTile == null) {
            this.currentAction = ChefAction.IDLE;
            return;
        }

        Station targetStation = targetTile.getStation();
        if (targetStation != null) {
            targetStation.interact(this);
        }
    }

    public void dash(Map map) {
        if (!active) return;
        boolean isUnlimited = EventManager.getInstance().getActiveEvent() == EventManager.EventType.UNLIMITED_DASH;

        // Cek  Cooldown
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDashTime < DASH_COOLDOWN_MS) {
            this.currentAction = ChefAction.IDLE;
            return;
        }

        // logic utama dash
        boolean dashed = false;
        for (int i = 0; i < DASH_DISTANCE; i++) {
            Position nextPos = Position.getAdjacent(this.position, this.direction);
            int nx = nextPos.getX();
            int ny = nextPos.getY();
            // Cek apakah kotak di depan bisa diinjak
            if (map.isWalkable(nextPos.getX(), nextPos.getY())) {
                Tile nextTile = map.getTile(nextPos.getX(), nextPos.getY());
                if (nextTile != null && nextTile.getStation() != null) {
                    break;
                }
                boolean chefCollision = map.isChefAt(nx, ny);
                if (!chefCollision) {
                    dashed = true;
                    this.position = nextPos;
                }
            } else {
                break; // Berhenti kalau nabrak tembok
            }
        }

        // 3. Set Waktu Cooldown
        if (dashed) {
            this.currentAction = ChefAction.DASHING;
            if (!isUnlimited) {
                lastDashTime = currentTime;
            }
        } else {
            this.currentAction = ChefAction.IDLE;
        }
    }

    // Visual Cooldown Bar (0.0 - 1.0)
    public float getDashCooldownProgress() {
        long timePassed = System.currentTimeMillis() - lastDashTime;
        if (timePassed >= DASH_COOLDOWN_MS) return 1.0f; // Ready
        return (float) timePassed / DASH_COOLDOWN_MS;
    }

    private void handleStationInteraction(Station station, Tile targetTile) {
        Item handItem = this.inventory;
        Item stationItem = station.getItemOnStation();

        // (Menggabungkan item di tangan dengan item di station)
        if (handItem != null && stationItem != null) {
            boolean success = tryPlating(handItem, stationItem, station, null);
            if (success) return; // Jika berhasil plating, berhenti.
        }

        // (Tangan Penuh -> Station Kosong)
        if (handItem != null && stationItem == null) {
            // Cek apakah station mengizinkan item ini?
            if (station.allowItem(handItem)) {
                if (station instanceof TrashStation) {
                    station.setItemOnStation(handItem);
                } else {
                    station.setItemOnStation(handItem);
                    this.inventory = null; // Tangan kosong
                }
                System.out.println("Drop " + handItem.getName() + " ke " + station.getClass().getSimpleName());
            }
            return;
        }

        // (Tangan Kosong -> Station Ada Barang)
        if (handItem == null && stationItem != null) {
            this.inventory = stationItem; // Ambil item
            station.removeItem(); // Hapus dari station (Kecuali infinite source)
            System.out.println("Pick up " + stationItem.getName() + " dari " + station.getClass().getSimpleName());
        }
    }

    private void handleTileInteraction(Tile targetTile) {
        Item handItem = this.inventory;
        Item floorItem = targetTile.getItem();
        if (floorItem instanceof EventItem) {
            EventItem potion = (EventItem) floorItem;

            // 1. Aktifkan Efek (Durasi 15 detik)
            EventManager.getInstance().triggerEvent(potion.getType(), 10);

            // 2. Hapus Item dari Lantai
            targetTile.setItem(null);

            System.out.println("GLUK GLUK! Efek " + potion.getType() + " aktif!");
            return;
        }

        // PLATING / ASSEMBLY
        if (handItem != null && floorItem != null) {
            boolean success = tryPlating(handItem, floorItem, null, targetTile);
            if (success) return;
        }

        // (Tangan Penuh -> Lantai Kosong)
        if (handItem != null && floorItem == null) {
            targetTile.setItem(handItem);
            this.inventory = null;
        }

        // (Tangan Kosong -> Lantai Ada Barang)
        else if (handItem == null && floorItem != null) {
            this.inventory = floorItem;
            targetTile.setItem(null);
        }
    }

    public void throwItem(Map map, List<ChefPlayer> allChefs) {
        // Validasi dasar
        if (!active || inventory == null) return;
        this.currentAction = ChefAction.THROWING;

        // Validasi Item: Hanya Ingredient Mentah/Potong
        if (inventory instanceof Ingredient) {
            Ingredient ing = (Ingredient) inventory;
            if (ing.getState() == ItemState.COOKED ||
                    ing.getState() == ItemState.BURNED) {
                System.out.println("LOGIC: Makanan panas gaboleh dilempar!");
                return;
            }
        } else {
            return;
        }

        System.out.println(name + " MELEMPAR " + inventory.getName() + "!");

        // Simpan projectile & kosongkan tangan
        Item projectile = this.inventory;
        this.inventory = null;

        // Posisi terakhir yang valid untuk mendarat (Awalnya posisi chef sendiri)
        Position validLandingPos = new Position(this.position.getX(), this.position.getY());

        // LOOPING LINTASAN
        for (int i = 1; i <= THROW_DISTANCE; i++) {
            // Hitung koordinat target
            Position nextPos = Position.getAdjacent(validLandingPos, this.direction);
            if (!map.isValidCoordinate(nextPos.getX(), nextPos.getY())) {
                System.out.println("-> Out of Bounds (Luar Map). Jatuh di posisi terakhir.");

                // Jatuh di tile terakhir yang aman (validLandingPos)
                dropOnFloor(map, validLandingPos, projectile);
                return;
            }

            // Khusus untuk logic fly over, kita butuh tahu posisi 'calon' tile ini
            // Jika validLandingPos tidak update (karena fly over), kita hitung manual dari posisi chef
            if (i > 1) {
                nextPos = getPositionAtDistance(this.position, this.direction, i);
            }

            model.map.Tile nextTile = map.getTile(nextPos.getX(), nextPos.getY());

            if (nextTile == null || nextTile.isWall(nextPos.getX(), nextPos.getY())) {
                System.out.println("-> Nabrak Tembok. Jatuh di " + validLandingPos.getX() + "," + validLandingPos.getY());
                dropOnFloor(map, validLandingPos, projectile); // Jatuh di posisi valid terakhir
                return;
            }

            for (ChefPlayer c : allChefs) {
                if (c != this && c.getPosition().getX() == nextPos.getX() && c.getPosition().getY() == nextPos.getY()) {
                    // Chef Kosong -> Terima
                    if (c.getInventory() == null) {
                        c.setInventory(projectile);
                        System.out.println("-> DITANGKAP oleh " + c.getName());
                        return;
                    }
                    // Chef Penuh -> Jatuh DI TILE CHEF TERSEBUT
                    else {
                        System.out.println("-> Nabrak Chef (Penuh). Jatuh di kaki chef.");
                        dropOnFloor(map, nextPos, projectile);
                        return;
                    }
                }
            }

            if (nextTile.getStation() != null) {
                boolean mustLandHere = (i == THROW_DISTANCE);
                if (!mustLandHere) {
                    Position peekPos = getPositionAtDistance(this.position, this.direction, i + 1);
                    model.map.Tile peekTile = map.getTile(peekPos.getX(), peekPos.getY());

                    // Jika depannya tembok, maka station ini jadi terminal terakhir
                    if (peekTile == null || peekTile.isWall(peekPos.getX(), peekPos.getY())) {
                        mustLandHere = true;
                    }
                }

                // LOGIC FLY OVER
                if (!mustLandHere) {
                    validLandingPos = nextPos;

                    continue; // Lanjut ke loop berikutnya (i+1)
                }

                // Coba masukkan ke station
                boolean accepted = nextTile.getStation().receiveThrownItem(projectile);

                if (accepted) {
                    // Station Kosong/Cocok -> Masuk Station
                    System.out.println("-> Mendarat di Station!");
                    return;
                } else {
                    // Logic Fly Over vs Drop Before
                    dropOnFloor(map, validLandingPos, projectile);
                    return;
                }
            }

            validLandingPos = nextPos;

            // Jika sudah jarak maksimal, jatuhkan di sini
            if (i == THROW_DISTANCE) {
                System.out.println("-> Jarak Max. Jatuh di lantai.");
                dropOnFloor(map, validLandingPos, projectile); // [Kondisi 1]
                return;
            }
        }
        this.currentAction = ChefAction.IDLE;
    }

    private Position getPositionAtDistance(Position start, Direction direction, int distance) {
        int x = start.getX();
        int y = start.getY();

        switch (direction) {
            case UP    -> y -= distance; // Y makin kecil ke atas
            case RIGHT -> x += distance; // X makin besar ke kanan
            case DOWN  -> y += distance; // Y makin besar ke bawah
            case LEFT  -> x -= distance; // X makin kecil ke kiri
        }
        return new Position(x, y);
    }

    private void dropOnFloor(Map map, Position pos, Item item) {
        Tile tile = map.getTile(pos.getX(), pos.getY());
        if (tile == null) return;

        Item floorItem = tile.getItem();

        // Kasus A: Lantai Kosong
        if (floorItem == null) {
            tile.setItem(item);
        }
        // Kasus B: Ada Piring -> Coba Merge (Kondisi 1: Tertumpuk)
        else if (floorItem instanceof Plate) {
            Plate plate = (Plate) floorItem;
            if (item instanceof Ingredient) {
                plate.addComponent((Ingredient) item);
                System.out.println("LOGIC: Lemparan masuk ke piring di lantai!");
            }
        }
        // Kasus C: Lantai Penuh bukan piring -> Item hilang/mental (atau timpa, tergantung desainmu)
        else {
            System.out.println("LOGIC: Lantai penuh, item hilang.");
        }
    }

    /**
     * Mencoba menggabungkan item di tangan dengan item target.
     * Return true jika berhasil melakukan aksi (Plating/Merging).
     */
    private boolean tryPlating(Item handItem, Item targetItem, Station station, Tile tile) {

        // SKENARIO A: CHEF BAWA PIRING
        if (handItem instanceof Plate) {
            Plate handPlate = (Plate) handItem;

            // 1. Piring ambil Ingredient (Piring -> Ingredient)
            if (targetItem instanceof Ingredient) {
                Ingredient targetIng = (Ingredient) targetItem;
                handPlate.addComponent(targetIng); // Masukkan bahan ke piring

                // Piring (yang sudah isi) ditaruh di Target
                updateTargetItem(handPlate, station, tile);

                this.inventory = null; // Tangan jadi kosong
                System.out.println("ACTION: Plate Scoop (Piring membungkus bahan)");
                return true;
            }

            // Merge: Piring ambil isi Piring lain (Piring -> Piring)
            if (targetItem instanceof Plate) {
                Plate targetPlate = (Plate) targetItem;
                // Jika target punya isi, pindahkan ke piring di tangan
                if (!targetPlate.getContents().isEmpty()) {
                    for (Object obj : targetPlate.getContents()) {
                        if (obj instanceof Ingredient) {
                            handPlate.addComponent((Ingredient) obj);
                        }
                    }
                    targetPlate.clearContents(); // Target jadi kosong
                    System.out.println("ACTION: Plate Merge (Gabung isi piring)");
                    return true;
                }
            }
        }

        // SKENARIO B: CHEF BAWA INGREDIENT
        else if (handItem instanceof Ingredient) {
            Ingredient handIng = (Ingredient) handItem;

            // Place: Ingredient masuk ke Piring (Ingredient -> Piring)
            if (targetItem instanceof Plate) {
                Plate targetPlate = (Plate) targetItem;

                if (targetPlate.isClean()) {targetPlate.addComponent(handIng);}

                this.inventory = null; // Bahan pindah ke piring
                System.out.println("ACTION: Ingredient Place (Bahan masuk piring)");
                return true;
            }
        }

        return false; // Tidak ada aksi plating yang cocok
    }

    private void updateTargetItem(Item newItem, Station station, Tile tile) {
        if (station != null) {
            station.setItemOnStation(newItem);
        } else if (tile != null) {
            tile.setItem(newItem);
        }
    }

    // Helper untuk fitur tangkap
    public void receiveItem(Item item) {
        this.inventory = item;
    }

    public boolean isBusy() { return busy; }

    public void setBusy(boolean busy) { this.busy = busy; }

    public void setInventory(Item inventory) { this.inventory = inventory; }

    public void activate() {
        active = true;
        System.out.println(this.name + " is now active.");
    }

    public void deactivate() {
        active = false;
        System.out.println(this.name + " is now inactive.");
    }

    public boolean isActive() {
        return active;
    }

    private void attemptMove(Map map, Direction newDirection) {
        if(!active) {
            return;
        }

        if(this.direction != newDirection) {
            this.direction = newDirection;
            this.currentAction = ChefAction.IDLE;
            return;
        }

        Position targetPosition = Position.getAdjacent(position, newDirection);
        boolean chefCollision = map.isChefAt(targetPosition.getX(), targetPosition.getY());

        if(map.isWalkable(targetPosition.getX(), targetPosition.getY()) && !chefCollision) {
            this.position = targetPosition;
            this.currentAction = ChefAction.MOVING;
        } else {
            System.out.println(name + " movement blocked.");
        }
        this.direction = newDirection;
    }

    public String getName() { return this.name; }

    public void moveUp(Map map) {
        attemptMove(map, Direction.UP);
    }

    public void moveDown(Map map) {
        attemptMove(map, Direction.DOWN);
    }

    public void moveLeft(Map map) {
        attemptMove(map, Direction.LEFT);
    }

    public void moveRight(Map map) {
        attemptMove(map, Direction.RIGHT);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void stopInteract(Map map) {
        if (!active) return;

        // Cari station di depan muka Chef
        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if (targetTile != null && targetTile.getStation() != null) {
            targetTile.getStation().stopCutting();
        }
    }
}