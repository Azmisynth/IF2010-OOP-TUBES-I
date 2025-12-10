package main.java.model.station;
import main.java.model.chef.ChefPlayer;

public abstract class Station {
    protected String symbol; // simbol dari stationnya untuk identifikasi (konsisten dengan TileState)

    public Station(String symbol) {
        this.symbol = symbol; // simbol dari station
    }

    public abstract void interact(ChefPlayer chef);

    public String getSymbol() {
        return symbol;
    }

    public void stopCutting(){}
}