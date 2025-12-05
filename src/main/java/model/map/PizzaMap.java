package main.java.model.map;

import main.java.model.chef.Position;
import java.util.List;
import java.util.ArrayList;

public class PizzaMap extends MapType {
    private List<Position> chefPosition = new ArrayList<>();
    private static final String[] PIZZA_LAYOUT = {
            "XATACAAACAAAXX",
            "X...........XX",
            "X.....A.V...SX",
            "X...........SX",
            "XWWAIAIAIAIAPX",
            "X............X",
            "XXXX..A...XXXX",
            "XR...V......RX",
            "XXXX......XXXX",
            "XXXXAAIAAAXXXX",
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