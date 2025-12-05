package main.java.model.map;

import java.util.List;
import main.java.model.chef.ChefPlayer;

public class Map {
    private static final int WIDTH = 14;
    private static final int HEIGHT = 10;
    private static final int TILE_SIZE = 50;
    private static final int CHEF_SIZE = TILE_SIZE;
    private final Tile[][] tiles;
    private final MapType mapConfig;
    private List<ChefPlayer> allChefs;

    public Map(MapType mapConfig, List<ChefPlayer> allChefs) {
        this.mapConfig = mapConfig;
        mapConfig.initialLayout();
        this.tiles = mapConfig.getTiles();
        this.allChefs = allChefs;
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            return tiles[y][x];
        }
        throw new IndexOutOfBoundsException("Coordinates (" + x + ", " + y + ") are outside the map bounds.");
    }

//    public boolean isWalkable(int x, int y) {
//        try {
//            getTile(x, y);
//        } catch (IndexOutOfBoundsException e) {
//            return false;
//        }
//
//        Tile targetTile = tiles[y][x];
//        if(targetTile.isWall(x, y)) {
//            return false;
//        }
////        if(targetTile.isWall(x, y)) {
////            return false;
////        } else if(targetTile.isStation(x, y)) {
////            return false;
////        }
//        return true;
//    }

    public boolean isWalkable(double x, double y) {
        final double CHEF_COLLISION_WIDTH = CHEF_SIZE - 1;
//        System.out.println(allChefs.get(0).getPosition().getX() + " " + allChefs.get(0).getPosition().getY());
//        int leftX = (int) x;
        double leftX = x;
//        int topY = (int) y;
        double topY = y;
//        System.out.println("Left X: " + leftX +  " Top Y: " + topY + " " + CHEF_SIZE);
//
//        int rightX = (int) (x + CHEF_SIZE);
        double rightX = x + CHEF_COLLISION_WIDTH;
//        int bottomY = (int) (y + CHEF_SIZE);
        double bottomY = y + CHEF_COLLISION_WIDTH;
//
        double leftTile = leftX / TILE_SIZE;
//        int leftTile = (int) Math.floor(leftX);
        double rightTile = (rightX - 1) / TILE_SIZE;
//        int topTile = (int) Math.floor(topY);
        double topTile = topY / TILE_SIZE;
        double bottomTile = (bottomY - 1) / TILE_SIZE;
//        int rightTile = (int) Math.floor(rightX-1);
//        int bottomTile = (int) Math.floor(bottomY);

//        if (leftTile < 0 || rightTile >= WIDTH || topTile < 0 || bottomTile >= HEIGHT) {
//            return false;
//        }

//        if (!isTilePassable((int) leftTile,(int) topTile)) return false;
//        if (leftTile != rightTile && !isTilePassable(rightTile, topTile)) return false;
//        if (topTile != bottomTile && !isTilePassable(leftTile, bottomTile)) return false;
//        if (leftTile != rightTile && topTile != bottomTile && !isTilePassable(rightTile, bottomTile)) return false;

        return true;
    }

    public ChefPlayer getActiveChef() {
        for (ChefPlayer chef : allChefs) {
            if (chef.isActive()) {
                return chef;
            }
        }
        return null; // tidak ada chef yang aktif
    }

    private boolean isTilePassable(int tileX, int tileY) {
//        if (tileX < 0 || tileX >= WIDTH || tileY < 0 || tileY >= HEIGHT) {
//            return false;
//        }

        Tile targetTile = tiles[tileY][tileX];

        if(targetTile.isWall(tileX, tileY)) {
            return false;
        }
        // if(targetTile.getStation() != null) { return false; }

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