package main.java.model.chef;

import main.java.model.item.Ingredient;
import main.java.model.item.Item;
import main.java.model.item.ItemState;
import main.java.model.item.Plate;
import main.java.model.map.*;
import main.java.model.station.Station;

import java.util.List;

public class ChefPlayer implements Moveable {
    private String id;
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory; // item yang dibawa oleh chef dan bisa null
    private boolean active;
    private volatile boolean busy = false;
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

        // Cari Tile di depan Chef
        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if (targetTile != null && targetTile.getStation() == null && targetTile.isWalkable(targetPosition.getX(), targetPosition.getY())) {
            Item floorItem = targetTile.getItem();
            // SKENARIO 1: DROP (Tangan Penuh -> Lantai Kosong)
            if (this.inventory != null && floorItem == null) {
                targetTile.setItem(this.inventory); // Pindahkan item ke lantai
                this.inventory = null; // Tangan jadi kosong
            }

            // SKENARIO 2: PICK UP (Tangan Kosong -> Lantai Ada Barang)
            else if (this.inventory == null && floorItem != null) {
                this.inventory = floorItem; // Ambil item ke tangan
                targetTile.setItem(null); // Lantai jadi kosong
            }
        }
    }

    public void interact(Map map) {
        if (!active) {
            return;
        }

        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if (targetTile == null) {
            return;
        }

        Station targetStation = targetTile.getStation();
        if (targetStation != null) {
            targetStation.interact(this);
        }
    }

    public void dash(Map map) {
        if (!active) return;

        // Cek  Cooldown
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDashTime < DASH_COOLDOWN_MS) {
            return;
        }

        // logic utama dash
        for (int i = 0; i < DASH_DISTANCE; i++) {
            Position nextPos = Position.getAdjacent(this.position, this.direction);

            // Cek apakah kotak di depan bisa diinjak
            if (map.isWalkable(nextPos.getX(), nextPos.getY())) {
                Tile nextTile = map.getTile(nextPos.getX(), nextPos.getY());
                if (nextTile != null && nextTile.getStation() != null) {
                    break;
                }
                this.position = nextPos;
            } else {
                break; // Berhenti kalau nabrak tembok
            }
        }

        // 3. Set Waktu Cooldown
        lastDashTime = currentTime;
    }

    // Visual Cooldown Bar (0.0 - 1.0)
    public float getDashCooldownProgress() {
        long timePassed = System.currentTimeMillis() - lastDashTime;
        if (timePassed >= DASH_COOLDOWN_MS) return 1.0f; // Ready
        return (float) timePassed / DASH_COOLDOWN_MS;
    }

    public void throwItem(Map map, List<ChefPlayer> allChefs) {
        // Validasi dasar
        if (!active || inventory == null) return;

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

            main.java.model.map.Tile nextTile = map.getTile(nextPos.getX(), nextPos.getY());

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
                    main.java.model.map.Tile peekTile = map.getTile(peekPos.getX(), peekPos.getY());

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
            return;
        }

        Position targetPosition = Position.getAdjacent(position, newDirection);

        if(map.isWalkable(targetPosition.getX(), targetPosition.getY())) {
            this.position = targetPosition;
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