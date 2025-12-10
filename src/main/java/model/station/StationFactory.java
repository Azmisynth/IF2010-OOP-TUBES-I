package main.java.model.station;

import main.java.model.chef.Position;
import main.java.model.item.Tomato;

public class StationFactory {
    public static Station createStationObject(String symbol) {
        return switch(symbol) {
            case "C" -> new CuttingStation();
            case "R" -> new CookingStation();
            case "A" -> new AssemblyStation();
            case "S" -> new ServingCounter();
            case "W" -> new WashingStation();
            case "I" -> new IngredientStation(Tomato.class);
            case "P" -> new PlateStorage();
            case "T" -> new TrashStation();
            default -> null;
        };
    }

    public static String getName(String symbol, Position position) {
        return switch(symbol) {
            case "C" -> "cutting";
            case "R" -> {
                if(position.getX() == 12) { yield "cooking-right"; }
                else { yield "cooking-left"; }
            }
            case "A" -> {
                if(position.getY() == 9) { yield "assembly-bottom"; }
                else { yield "assembly-normal"; }
            }
            case "S" -> {
                if(position.getX() == 12 && position.getY() == 3) { yield "serving-bottom"; }
                else if  (position.getX() == 12 && position.getY() == 2) { yield "serving"; }
                else { yield "null"; }
            }
            case "W" -> "washing";
            case "I" -> "ingredient";
            case "P" -> "plate";
            case "T" -> "trash";
            default -> null;
        };
    }
}