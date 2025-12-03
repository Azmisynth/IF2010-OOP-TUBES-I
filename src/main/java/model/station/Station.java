package main.java.model.station;
import main.java.model.chef.Chef;

public abstract class Station {
    protected String symbol; // simbol dari stationnya untuk identifikasi (konsisten dengan TileState)

    public Station(String symbol) {
        this.symbol = symbol; // simbol dari station
    }

    public abstract void ChefPlayer(Chef chef);

    public String getSymbol() {
        return symbol;
    }
}