public abstract class Station {
    protected Tile position;
    protected char symbol; // simbol dari stationnya

    public Station(Tile position, char symbol){
        this.position = position; // posisi si station dimana
        this.symbol = symbol; // simbol dari station kali? gatau azmi
    }

    public abstract void interact(Chef chef);

    public boolean isAdjacent(Tile pos){
        int dx = Math.abs(position.getX() - pos.getX());
        int dy = Math.abs(position.getY() - pos.getY());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
        // posisi saat ini dikurang posisi station (karena jarak harus cuma 1 tile)
    }

    public Tile getPosition(){
        return position;
    }

    public char getSymbol(){
        return symbol;
    }
}
