package main.java.model.map;

public class PizzaMap extends MapType {
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
                tiles[i][j] = new Tile(i, j, symbol);
            }
        }
    }
}