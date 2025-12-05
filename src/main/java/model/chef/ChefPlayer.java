package main.java.model.chef;

import main.java.model.map.*;
import main.java.model.station.Station;

public class ChefPlayer implements Moveable {
    private String id;
    private String name;
    private Position position;
    private Direction direction;
    //private Item inventory; // item yang dibawa oleh chef dan bisa null
    private boolean active;

    public ChefPlayer(String id, String name, Position startPosition) {
        this.id = id;
        this.name = name;
        this.position = startPosition;
        this.direction = Direction.DOWN;
        //this.inventory = null;
        this.active = false;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() { return direction;}

//    public Item getInventory() {
//        return inventory;
//    }
//
//    public void pickUpItem(Map map) {
//        Position targetPosition = Position.getAdjacent(this.position, this.direction);
//
//        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());
//
//        Item itemOnTile =  targetTile.getItem();
//
//        if(itemOnTile != null) { // Jika tangan kosong → ambil item di depan
//            this.receiveItem(targetTile.removeItem());
//            System.out.println(name + " picked up " + inventory.getName());
//        }
//    }
//
//    public void dropItem(Map map) {
//        if (this.inventory == null) {
//            System.out.println("Cannot put down item: Inventory is empty.");
//            return;
//        }
//
//        Position targetPosition = Position.getAdjacent(this.position, this.direction);
//        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());
//
//        if(targetTile != null && targetTile.getItem() == null) {
//            targetTile.setItem(giveItem());
//            System.out.println(name + " placed " + targetTile.getItem().getName() + " on the floor.");
//        }
//    }

    public void interact(Map map) {
        if (!active) {
            return;
        }

        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if(targetTile == null) return;

        Station targetStation = targetTile.getStation();
//        Item itemOnTile = targetTile.getItem();
//
//        if(targetStation != null) { // interaksi dengan station
//            if (targetStation instanceof IngredientStorage) {
//                handleIngredientStorageInteraction(targetTile, (IngredientStorage) targetStation);
//            } else {
//                targetStation.interact(this);
//            }
//            return;
//        }
//
//        if (this.inventory instanceof Plate) {
//            Plate plateInHand = (Plate) this.inventory;
//            if (plateInHand.isClean() && itemOnTile instanceof Ingredient) {
//                handlePlating(targetTile, plateInHand, itemOnTile);
//                return;
//            }
//        }
//
//        if (this.inventory == null) {
//            if(itemonTile != null) {
//                pickUpItem(map);
//            } else {
//                System.out.println("No interaction can be done");
//            }
//        } else if (itemOnTile == null) {
//            dropItem(map);
//        }
    }

    //private void handlePlating(Tile targetTile, Plate plateInHand, Item itemOnTile) {
        // plating item yang ada di kitchen utensils dan ada di lantai
//        if (itemOnTile instanceof KitchenUtensils) {
//            KitchenUtensils utensils = (KitchenUtensils) itemOnTile;
//
//            if (!utensils.getContents().isEmpty()) {
//                Item itemToPlate = (Item) utensils.getContents().remove(0);
//                plateInHand.addComponent((Preparable) itemToPlate);
//                System.out.println(name + " plated item from utensil to plate in hand.");
//            }
//
//        } else { // item bukan di kitchen utensils dan ada di lantai
//            targetTile.removeItem();
//
//            if (itemOnTile instanceof Preparable) {
//                plateInHand.addComponent((Preparable) itemOnTile);
//            } else {
//                System.err.println("Item cannot be added to plate.");
//                targetTile.setItem(itemOnTile); // balikin ke tile
//                return;
//            }
//
//            targetTile.setItem(this.giveItem()); // berikan item yang ada di chef ke tile
//            System.out.println(name + " plated item and placed the filled plate on the floor.");
//        }
    //}

//    private void handleIngredientStationInteraction(Tile targetTile, IngredientStation storage) {
//        Item itemOnStorage = targetTile.getItem();
//
//        if (itemOnStorage != null) {
//            if (this.inventory == null) {
//                this.receiveItem(targetTile.removeItem());
//            } else {
//                System.out.println(name + " inventory full, cannot take item on top.");
//            }
//            return;
//        }
//
//        if (this.inventory != null && itemOnStorage == null) {
//            targetTile.setItem(this.giveItem());
//            System.out.println(name + " placed item on the empty storage.");
//            return;
//        }
//
//        if (this.inventory == null && itemOnStorage == null) {
//            storage.interact(this);
//        } else {
//            System.out.println(name + " inventory full, cannot take ingredient.");
//        }
//    }

//
//    public void receiveItem(Item item) {
//        if(getInventory() == null) {
//            this.inventory = item;
//        } else {
//            System.err.println("Chef has held an item.");
//        }
//    }
//
//    public Item giveItem() {
//        if(getInventory() == null) {
//            return null;
//        }
//        Item itemToGive = this.inventory;
//        this.inventory = null;
//        return itemToGive;
//    }

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
}