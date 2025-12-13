package model.map;

import model.chef.Position;
import model.station.*;
import model.item.*;

import java.util.List;
import java.util.ArrayList;

public class PizzaMap extends MapType {
    private List<Position> chefPosition = new ArrayList<>();
    private String[] currentLayout;
    private String mapName;

    private static final String[] PIZZA_LAYOUT = {
            "XXXATACAAACAAAXX",
            "XXX...........XX",
            "XXX.....A.V...SX",
            "XXX...........SX",
            "XXXHWADAYAKAUAPX",
            "XXX............X",
            "XXXXXX..A...XXXX",
            "XXXR...V......RX",
            "XXXXXX......XXXX",
            "XXXXXXAAOAAAXXXX",
    };

    public PizzaMap(int level) {
        if (level == 4) {
            // JIKA LEVEL 4 -> GENERATE RANDOM MAP
            this.mapName = "Random Kitchen";
            System.out.println("Generating Random Map...");

            // 1. Panggil Generator (Ukuran 16x10)
            LevelGenerator generator = new LevelGenerator(16, 10);
            String rawMap = generator.generate();
            System.out.println("DEBUG: Hasil Generator:\n" + rawMap);

            // 2. Convert String panjang menjadi Array String per baris
            this.currentLayout = rawMap.split("\n");

        } else {
            this.mapName = "Pizza Map";
            this.currentLayout = PIZZA_LAYOUT;
        }
    }

    public List<Position> getChefPositions() {
        chefPosition.clear();
        for(int i = 0; i < getHeight(); i++) {
            String rowLayout = currentLayout[i];
            for(int j = 0; j < getWidth(); j++) {
                if(rowLayout.charAt(j) == 'V') {
                    chefPosition.add(new Position(j, i));
                }
            }
        }

        if (chefPosition.isEmpty()) chefPosition.add(new Position(8, 5));
        return java.util.Collections.unmodifiableList(this.chefPosition);
    }

    @Override
    protected String[] getLayoutData() {
        return currentLayout;
    }

    @Override
    protected String getMapName() {
        return mapName;
    }

    @Override
    protected void initialLayout() {
        tiles = new Tile[getHeight()][getWidth()];
        for(int i = 0; i < getHeight(); i++) {
            String rowLayout = currentLayout[i];
            for(int j = 0; j < getWidth(); j++) {
                String symbol = String.valueOf(rowLayout.charAt(j));
                if(symbol.equals("V")) {
                    chefPosition.add(new Position(i, j));
                    symbol = ".";
                }
                tiles[i][j] = new Tile(i, j, symbol);

                Station station = StationFactory.createStationObject(symbol);

                if (station != null) {
                    tiles[i][j].setStation(station);
                }
            }
        }
    }
}