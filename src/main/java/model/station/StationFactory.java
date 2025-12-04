package main.java.model.station;

public class StationFactory {
    public static Station createStationObject(String symbol) {
        return switch(symbol) {
            case "C" -> new CuttingStation();
            case "R" -> new CookingStation();
            case "A" -> new AssemblyStation();
//            case "S" -> new ServingCounter();
            case "W" -> new WashingStation();
//            case "I" -> new IngredientStorage();
//            case "P" -> new PlateStorage();
            case "T" -> new TrashStation();
            default -> null;
        };
    }

    public static String getName(String symbol) {
        return switch(symbol) {
            case "C" -> "cutting";
            case "R" -> "cooking";
            case "A" -> "assembly";
            case "S" -> "serving";
            case "W" -> "washing";
//            case "I" -> new IngredientStorage();
//            case "P" -> new PlateStorage();
            case "T" -> "trash";
            default -> null;
        };
    }
}