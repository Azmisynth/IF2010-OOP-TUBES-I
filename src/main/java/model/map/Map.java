package main.java.model.map;

import main.java.model.chef.ChefPlayer;

public class Map {
    private static final int width = 14;
    private static final int height = 10;
    private final Tile[][] tiles;
    private final MapType mapConfig;
    private ChefPlayer chef;

    public Map(MapType mapConfig, ChefPlayer chef) {
        this.mapConfig = mapConfig;
        mapConfig.initialLayout();
        this.tiles = mapConfig.getTiles();
        this.chef = chef;
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[y][x];
        }
        throw new IndexOutOfBoundsException("Coordinates (" + x + ", " + y + ") are outside the map bounds.");
    }

    public boolean isWalkable(int x, int y) {
        try {
            getTile(x, y);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        Tile targetTile = tiles[y][x];
        if(targetTile.isWall(x, y)) {
            return false;
        }
//        if(targetTile.isWall(x, y)) {
//            return false;
//        } else if(targetTile.isStation(x, y)) {
//            return false;
//        }
        return true;
    }

//    public void placeItemOnMap(int x, int y, Item item) {
//        getTile(x, y).setItem(item);
//    }
//
//    public Item removeItemOnMap(int x, int y) {
//        return getTile(x, y).removeItem();
//    }
}