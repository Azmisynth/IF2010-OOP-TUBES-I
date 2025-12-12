package model.map;

import model.item.Item;
import model.station.Station;
import model.station.StationFactory;

public class Tile {
    private final int x;
    private final int y;
    private String symbol;
    private String state;
    private Station stationContained;
    private Item itemContained;     // Item yang diletakkan chef di suatu tile, bisa jadi null

    public Tile(int x, int y, String symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.state = TileState.fromSymbol(symbol);
        this.itemContained = null;
//
        if(isWall(x, y) || isWalkable(x, y)) {
            this.stationContained = null;
        } else {
            this.stationContained = StationFactory.createStationObject(symbol);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getState() {
        return state;
    }

    public void setState(String symbol) {
        this.state = TileState.fromSymbol(symbol);
    }

    public boolean isWall(int x, int y) {
        return getState().equals("WALL");
    }

    public boolean isWalkable(int x, int y) {
        return getState().equals("WALKABLE");
    }

    public boolean isStation(int x, int y) {
        return getStation() != null;
    }

    public Station getStation() {
        return stationContained;
    }

    public void setStation(Station station) {
        this.stationContained = station;
    }


    public Item getItem() {
        return itemContained;
    }

    public void setItem(Item item) {
        itemContained = item;
    }
//
    public boolean hasItem() {
        return itemContained != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}