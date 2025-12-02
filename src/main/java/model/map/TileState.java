package main.java.model.map;

public enum TileState {
    WALL("X"),
    CUTTING_STATION("C"),
    COOKING_STATION("R"),
    ASSEMBLY_STATION("A"),
    SERVING_COUNTER("S"),
    WASHING_STATION("W"),
    INGREDIENT_STORAGE("I"),
    PLATE_STORAGE("P"),
    TRASH_STATION("T"),
    WALKABLE("."),
    SPAWN("V");

    private final String symbol;

    TileState(String symbol) {
        this.symbol = symbol;
    }

    public static String fromSymbol(String symbol) {
        for(TileState state: TileState.values()) {
            if(state.symbol.equals(symbol)) {
                return state.getName();
            }
        }
        return null;
    }

    public String getName() {
        return this.name();
    }
}