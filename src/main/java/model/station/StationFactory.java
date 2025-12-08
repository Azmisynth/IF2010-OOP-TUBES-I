package main.java.model.station;

import main.java.model.chef.Position;

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

    public static String getName(String symbol, Position position) {
        return switch(symbol) {
            case "C" -> "cutting";
            case "R" -> {
                if(position.getX() == 14) { yield "cooking-right"; }
                else { yield "cooking-left"; }
            }
            case "A" -> {
                if(position.getY() == 9) { yield "assembly-bottom"; }
                else { yield "assembly-normal"; }
            }
            case "S" -> "serving";
            case "W" -> "washing";
//            case "I" -> new IngredientStorage();
//            case "P" -> new PlateStorage();
            case "T" -> "trash";
            default -> null;
        };
    }
}