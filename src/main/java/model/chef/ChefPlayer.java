package main.java.model.chef;

import main.java.model.item.Ingredient;
import main.java.model.item.Item;
import main.java.model.item.ItemState;
import main.java.model.map.*;
import main.java.model.station.Station;

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

    public void throwItem(Map map, java.util.List<ChefPlayer> allChefs) {
        if (!active || inventory == null) return;

        // Hanya Ingredient Mentah/Potong yang boleh dilempar
        if (inventory instanceof Ingredient) {
            Ingredient ing = (Ingredient) inventory;
            if (ing.getState() == ItemState.COOKED || ing.getState() == ItemState.BURNED) {
                return;
            }
        } else {
            return;
        }

        // Simpan item yang mau dilempar & kosongkan tangan
        Item projectile = this.inventory;
        this.inventory = null;

        // Kalkulasi Lintasan
        Position currentCheckPos = this.position;
        Tile landingTile = null;

        for (int i = 1; i <= THROW_DISTANCE; i++) {
            // Cek koordinat berikutnya
            Position nextPos = Position.getAdjacent(currentCheckPos, this.direction);
            Tile nextTile = map.getTile(nextPos.getX(), nextPos.getY());

            // Cek Tembok
            if (nextTile == null || nextTile.isWall(nextPos.getX(), nextPos.getY())) {
                break; // Stop di tile sebelumnya
            }

            // Cek Chef Lain (Fitur Tangkap)
            ChefPlayer catcher = null;
            for (ChefPlayer c : allChefs) {
                if (c != this && c.getPosition().getX() == nextPos.getX() && c.getPosition().getY() == nextPos.getY()) {
                    catcher = c;
                    break;
                }
            }

            if (catcher != null) {
                if (catcher.getInventory() == null) {
                    catcher.receiveItem(projectile); // Tangkap!
                    return; // Selesai, barang sudah di tangan chef lain
                } else {
                    landingTile = nextTile; // Jatuh di kaki chef itu kalau chef bawa ite,
                    break;
                }
            }

            // Cek Station (Bisa gak lempar masuk ke Wajan/Lantai?)
            if (nextTile.getStation() != null) {
                landingTile = nextTile;
                break;
            }

            // Kalau kosong (Lantai), lanjut terbang
            landingTile = nextTile;
            currentCheckPos = nextPos; // Maju selangkah
        }

        // Pendaratan
        if (landingTile != null) {
            // Cek apakah lantai sudah ada barang?
            if (landingTile.getItem() == null) {
                landingTile.setItem(projectile);
            }
        } else {
            //Lempar pas madep tembok
            Tile myTile = map.getTile(position.getX(), position.getY());
            if (myTile.getItem() == null) myTile.setItem(projectile);
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