package main.java.model.map;

import main.java.model.chef.Position;
import java.util.List;
import java.util.ArrayList;

public class PizzaMap extends MapType {
    private List<Position> chefPosition = new ArrayList<>();
    private static final String[] PIZZA_LAYOUT = {
            "XXXATACAAACAAAXX",
            "XXX...........XX",
            "XXX.....A.V...SX",
            "XXX...........SX",
            "XXXWWAIAIAIAIAPX",
            "XXX............X",
            "XXXXXX..A...XXXX",
            "XXXR...V......RX",
            "XXXXXX......XXXX",
            "XXXXXXAAIAAAXXXX",
    };

    public List<Position> getChefPositions() {
        for(int i = 0; i < getHeight(); i++) {
            String rowLayout = PIZZA_LAYOUT[i];
            for(int j = 0; j < getWidth(); j++) {
                String symbol = String.valueOf(rowLayout.charAt(j));
                if(symbol.equals("V")) {
                    chefPosition.add(new Position(j, i));
                }
            }
        }
        return java.util.Collections.unmodifiableList(this.chefPosition);
    }

    @Override
    protected String[] getLayoutData() {
        return PIZZA_LAYOUT;
    }

    @Override
    protected String getMapName() {
        return "Pizza Map";
    }

    @Override
    protected void initialLayout() {
        for(int i = 0; i < getHeight(); i++) {
            String rowLayout = PIZZA_LAYOUT[i];
            for(int j = 0; j < getWidth(); j++) {
                String symbol = String.valueOf(rowLayout.charAt(j));
                if(symbol.equals("V")) {
                    chefPosition.add(new Position(i, j));
                }
                tiles[i][j] = new Tile(i, j, symbol);
            }
        }
    }
}