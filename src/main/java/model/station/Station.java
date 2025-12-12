package model.station;
import model.chef.ChefPlayer;
import model.item.Item;

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

    public boolean receiveThrownItem(Item item) {
        return false; // Default: Menolak semua lemparan
    }
}