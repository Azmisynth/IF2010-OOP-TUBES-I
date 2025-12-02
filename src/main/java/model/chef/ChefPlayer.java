package main.java.model.chef;

public class ChefPlayer implements Moveable {
    private String id;
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory; // item yang dibawa oleh chef dan bisa null
    private boolean isActive;

    public ChefPlayer(String id, String name, Position startPosition) {
        this.id = id;
        this.name = name;
        this.position = startPosition;
        this.direction = Direction.UP;
        this.inventory = null;
        this.isActive = false;
    }

    public Position getPosition() {
        return position;
    }

    public Item getInventory() {
        return inventory;
    }

    public void pickUpItem(Map map) {
        Position targetPosition = Position.getAdjacent(this.position, this.direction);

        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if(targetTile != null && targetTile.getItem() != null) {
            this.receiveItem(targetTile.removeItem());
        } else {
            System.out.println("No item found on the floor to pick up.");
        }
    }

    public void putDownItem(Map map) {
        if (this.inventory == null) {
            System.out.println("Cannot put down item: Inventory is empty.");
            return;
        }

        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if(targetTile != null && targetTile.getItem() == null) {
            targetTile.setItem(giveItem());
        } else {
            System.out.println("No item can be put down on the floor.");
        }
    }

    public void interact(Map map) {
        if (!isActive) {
            return;
        }

        Position targetPosition = Position.getAdjacent(this.position, this.direction);
        Tile targetTile = map.getTile(targetPosition.getX(), targetPosition.getY());

        if(targetTile == null) return;

        Station targetStation = targetTile.getStation();
        Item itemOnFloor = targetTile.getItem();

        if(targetStation != null) {
            // method station
        } else {
            // jika masuk ke sini, artinya untuk handle item
            if(this.inventory == null && itemOnFloor == null) {
                System.out.println("No interaction can be done");
            }
            else if(this.inventory == null & itemOnFloor != null) {
                this.receiveItem(map.removeItemOnMap(targetPosition.getX(), targetPosition.getY()));
            } else if(this.inventory != null && itemOnFloor != null) {
                /***
                 * kalo inventorynya adalah piring, lalu itemOnFloor adalah ingredients yang bisa diletakkan di piring, itemOnFloor akan diletakkan di piring
                 * kalo inventorynya adalah piring, lalu itemOnFloor adalah dish, itemOnFloor akan diletakkan di piring
                 * kalo inventorynya adalah kitchen utensils yang berisi ingredients, itemOnFloor adalah piring, lalu ingredients di KitchenUtensils bisa diletakkan di piring dan telah selesai diproses, ingredients akan berpindah ke piring di lantai, menyisakan kitchen utensils kosong di chef
                 */
            }
        }
    }

    public void receiveItem(Item item) {
        if(getInventory() == null) {
            this.inventory = item;
        } else {
            throw new IllegalStateException("Chef has held an item.");
        }
    }

    public Item giveItem() {
        if(getInventory() == null) {
            throw new IllegalStateException("Chef has no items to give");
        }
        Item itemToGive = this.inventory;
        this.inventory = null;
        return itemToGive;
    }

    public void activate() {
        isActive = true;
        System.out.println(this.name + " is now active.");
    }

    public void deactivate() {
        isActive = false;
        System.out.println(this.name + " is now inactive.");
    }

    private void attemptMove(Map map, Direction newDirection) {
        if(!isActive) {
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

    @Override
    public void moveUp(Map map) {
        attemptMove(map, Direction.UP);
    }

    @Override
    public void moveDown(Map map) {
        attemptMove(map, Direction.DOWN);
    }

    @Override
    public void moveLeft(Map map) {
        attemptMove(map, Direction.LEFT);
    }

    @Override
    public void moveRight(Map map) {
        attemptMove(map, Direction.RIGHT);
    }
}