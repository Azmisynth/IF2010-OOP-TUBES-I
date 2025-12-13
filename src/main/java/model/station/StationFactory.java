package model.station;

import model.chef.Position;
import model.item.*;

public class StationFactory {
    public static Station createStationObject(String symbol) {
        return switch(symbol) {
            case "C" -> new CuttingStation();
            case "R" -> new CookingStation();
            case "A" -> new AssemblyStation();
            case "S" -> new ServingCounter();
            case "W" -> new WashingStation();
            case "P" -> new PlateStorage();
            case "T" -> new TrashStation();
            case "H" -> new WashingClean();

            case "O" -> new IngredientStation(Tomato.class);
            case "D" -> new IngredientStation(Dough.class);
            case "K" -> new IngredientStation(Cheese.class);
            case "U" -> new IngredientStation(Sausage.class);
            case "Y" -> new IngredientStation(Chicken.class);
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
            case "S" -> {
                if(position.getX() == 14 && position.getY() == 3) { yield "serving-bottom"; }
                else if  (position.getX() == 14 && position.getY() == 2) { yield "serving"; }
                else { yield "serving"; }
            }
            case "W" -> "washing";
            case "H" -> "washing-clean";
            case "P" -> "plate";
            case "T" -> "trash";

            case "O" -> "Tomato";
            case "D" -> "Dough";
            case "K" -> "Cheese";
            case "U" -> "Sausage";
            case "Y" -> "Chicken";
            default -> null;
        };
    }
}