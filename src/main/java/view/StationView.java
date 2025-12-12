package view;

import model.chef.Position;
import model.item.Item;
import model.item.Plate;
import model.item.Preparable;
import model.item.Ingredient;
import model.item.ItemState;
import model.map.Tile;
import model.station.*;
import model.kitchen.Recipe;
import model.kitchen.RecipeBook;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class StationView {
    private static final int TILE_SIZE = 50;

    private final BufferedImage wall;
    private final BufferedImage tileImage;
    private final Map<String, BufferedImage> stationImages;
    private final Map<String, BufferedImage> itemImages;

    public StationView(BufferedImage tileImage, BufferedImage wall, Map<String, BufferedImage> stationImages, Map<String, BufferedImage> itemImages) {
        this.tileImage = tileImage;
        this.stationImages = stationImages;
        this.itemImages = itemImages;
        this.wall = wall;
    }

    public void drawTile(Graphics2D g2d, Tile tileModel, int x, int y) {
        int px = x * TILE_SIZE;
        int py = y * TILE_SIZE;

        //Tembok
        if (tileModel.isWall(x, y)) {
            g2d.drawImage(wall, px, py, TILE_SIZE, TILE_SIZE, null);
        } else {
            if (tileImage != null) {
                g2d.drawImage(tileImage, px, py, TILE_SIZE, TILE_SIZE, null);
            }
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRect(px, py, TILE_SIZE, TILE_SIZE); // Grid line
        }

        //Item dilantai
        if (tileModel.getItem() != null) {
            // Gambar item di lantai (agak kecil 30x30)
            drawItemAt(g2d, tileModel.getItem(), px + 10, py + 10, 30);
        }

        //Station
        if (tileModel.getStation() != null) {
            Station currentStation = tileModel.getStation();

            // Gambar Meja Dasar
            String stationKey = StationFactory.getName(currentStation.getSymbol(), new Position(x, y));
            BufferedImage imageToDraw = stationImages.get(stationKey);

            if(imageToDraw != null) {
                g2d.drawImage(imageToDraw, px, py, TILE_SIZE, TILE_SIZE, null);
            } else {
                g2d.setColor(Color.GRAY);
                g2d.fillRect(px, py, TILE_SIZE, TILE_SIZE);
            }

            // Logika Khusus per Station

            if (currentStation instanceof IngredientStation) {
                drawIngredientStation(g2d, (IngredientStation) currentStation, px, py);
            }
            else if (currentStation instanceof CuttingStation) {
                drawCuttingStation(g2d, (CuttingStation) currentStation, px, py);
            }
            else if (currentStation instanceof PlateStorage) {
                drawPlateStorage(g2d, (PlateStorage) currentStation, px, py);
            }
            else if (currentStation instanceof AssemblyStation) {
                drawAssemblyStation(g2d, (AssemblyStation) currentStation, px, py);
            }
            else if (currentStation instanceof CookingStation) {
                drawCookingStation(g2d, (CookingStation) currentStation, px, py);
            }
            else if (currentStation instanceof WashingStation) {
                drawWashingStation(g2d, (WashingStation) currentStation, px, py);
            }
        }
    }


    private void drawIngredientStation(Graphics2D g2d, IngredientStation station, int px, int py) {
        String ingredientName = station.getIngredientName();
        BufferedImage icon = itemImages.get(ingredientName);
        if (icon != null) {
            g2d.drawImage(icon, px, py, 50, 50, null);
        }
    }

    private void drawCuttingStation(Graphics2D g2d, CuttingStation station, int px, int py) {
        // 1. Item
        Item item = station.getItemOnStation();
        if (item != null) {
            drawItemAt(g2d, item, px + 5, py + 5, 40);
        }

        // 2. Progress Bar (Hijau)
        if (station.isBusy()) {
            double progress = station.getProgress();
            int barY = py + 38;

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(px + 5, barY, 40, 6);
            g2d.setColor(Color.CYAN);
            g2d.fillRect(px + 5, barY, (int)(40 * progress), 6);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(px + 5, barY, 40, 6);
        }
    }

    private void drawPlateStorage(Graphics2D g2d, PlateStorage storage, int px, int py) {
        if (storage.hasPlates()) {
            String plateName = storage.hasDirtyPlateOnTop() ? "Plate_DIRTY" : "Plate";
            BufferedImage plateImg = itemImages.get(plateName);
            if (plateImg == null) plateImg = itemImages.get("Plate");

            if (plateImg != null) {
                g2d.drawImage(plateImg, px + 10, py + 8, 30, 30, null);
                g2d.drawImage(plateImg, px + 10, py + 4, 30, 30, null);
            }
        }
    }

    private void drawAssemblyStation(Graphics2D g2d, AssemblyStation station, int px, int py) {
        Item item = station.getItemOnStation();
        if (item != null) {
            // Gambar Piring atau Bahan
            drawItemAt(g2d, item, px + 5, py + 5, 40);

            // Jika Piring, gambar isinya (Overlay / Pizza Utuh)
            if (item instanceof Plate) {
                drawPlateContent(g2d, (Plate) item, px, py);
            }
        }
    }

    private void drawCookingStation(Graphics2D g2d, CookingStation station, int px, int py) {
        // 1. Item di Kompor
        Item item = station.getItemOnStation();
        if (item != null) {
            drawItemAt(g2d, item, px + 5, py + 5, 40);

            if (item instanceof Plate) {
                drawPlateContent(g2d, (Plate) item, px, py);
            }
        }

        // 2. Dual Progress Bar (Hijau -> Merah)
        if (item != null) {
            double progress = station.getProgress();
            int barY = py - 8;

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(px + 7, barY, 40, 6);

            if (progress <= 1.0) {
                g2d.setColor(Color.GREEN);
                g2d.fillRect(px + 7, barY, (int)(40 * progress), 6);
            } else {
                g2d.setColor(Color.RED);
                double burnProgress = Math.min(progress - 1.0, 1.0);
                g2d.fillRect(px + 7, barY, (int)(40 * burnProgress), 6);
            }
            g2d.setColor(Color.WHITE);
            g2d.drawRect(px + 7, barY, 40, 6);
        }
    }


    private void drawItemAt(Graphics2D g2d, Item item, int x, int y, int size) {
        String itemName = item.getName();

        // 1. Cek Status Ingredient
        if (item instanceof Ingredient) {
            Ingredient ing = (Ingredient) item;
            if (ing.getState() == ItemState.CHOPPED) itemName += "_CHOPPED";
            else if (ing.getState() == ItemState.BURNED) itemName = "Burnt";
            else if (ing.getState() == ItemState.RAW) itemName += "pick";
        }

        // 2. Cek Status Plate
        if (item instanceof Plate) {
            if (!((Plate) item).isClean()) itemName = "Plate_DIRTY";
        }

        // 3. Cari Gambar (Prioritas: Spesifik -> Pick -> Asli)
        BufferedImage img = itemImages.get(itemName);
        if (img == null) img = itemImages.get(itemName + "pick");
        if (img == null) img = itemImages.get(item.getName());

        if (img != null) {
            g2d.drawImage(img, x, y, size, size, null);
        }
    }

    private void drawWashingStation(Graphics2D g2d, WashingStation station, int px, int py) {
        if (station.hasDirtyPlates()) {
            BufferedImage dirtyImg = itemImages.get("Plate_DIRTY");
            if (dirtyImg != null) {
                // Gambar di kiri agak bawah
                g2d.drawImage(dirtyImg, px + 2, py + 10, 25, 25, null);
                // Efek tumpukan
                g2d.drawImage(dirtyImg, px + 2, py + 7, 25, 25, null);
            }
        }

        if (station.hasCleanPlates()) {
            BufferedImage cleanImg = itemImages.get("Plate");
            if (cleanImg != null) {
                // Gambar di kanan agak bawah
                g2d.drawImage(cleanImg, px + 22, py + 10, 25, 25, null);
                // Efek tumpukan
                g2d.drawImage(cleanImg, px + 22, py + 7, 25, 25, null);
            }
        }

        if (station.isBusy()) {
            double progress = station.getProgress();
            int barY = py - 8; // Di atas wastafel

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(px + 5, barY, 40, 6);
            g2d.setColor(Color.CYAN); // Pakai Cyan (Air) biar beda sama Cutting
            g2d.fillRect(px + 5, barY, (int)(40 * progress), 6);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(px + 5, barY, 40, 6);
        }
    }

    private void drawPlateContent(Graphics2D g2d, Plate plate, int px, int py) {
        if (plate.getContents() == null || plate.getContents().isEmpty()) return;

        boolean hasDough = false;
        boolean hasTomato = false;
        boolean hasCheese = false;
        // Cek GOSONG
        boolean isBurned = false;
        boolean isAllCooked = true;
        for (Object obj : plate.getContents()) {
            if (obj instanceof Ingredient) {
                Ingredient ing = (Ingredient) obj;
                String name = ing.getName();
                if (ing.getState() == ItemState.BURNED) isBurned = true;
                if (ing.getState() != ItemState.COOKED) isAllCooked = false;

                if (name.equals("Dough")) hasDough = true;
                if (name.equals("Tomato")) hasTomato = true;
                if (name.equals("Cheese")) hasCheese = true;
            }
        }

        if (isBurned) {
            BufferedImage burntImg = itemImages.get("Burnt");
            if (burntImg != null) g2d.drawImage(burntImg, px + 5, py + 5, 40, 40, null);
            return;
        }
        Recipe recipe = RecipeBook.findRecipe(plate.getContents());
        if (recipe != null) {
            String recipeName = recipe.getName().replace(" ", "_");
            String imageKey = isAllCooked ? recipeName : recipeName + "_RAW";

            BufferedImage dishImg = itemImages.get(imageKey);
            if (dishImg != null) {
                g2d.drawImage(dishImg, px + 5, py + 5, 40, 40, null);
                return;
            }
        }

        if (hasDough && hasTomato && !hasCheese) {
            BufferedImage intermediateImg = itemImages.get("Pizza_Tomat_RAW");

            if (intermediateImg != null) {
                g2d.drawImage(intermediateImg, px + 5, py + 5, 40, 40, null);
                return;
            }
        }

        int offset = 0;
        for (Object obj : plate.getContents()) {
            if (obj instanceof Ingredient) {
                Ingredient ing = (Ingredient) obj;
                String ingName = ing.getName();

                if (ing.getState() == ItemState.CHOPPED) ingName += "_CHOPPED";

                BufferedImage img = itemImages.get(ingName + "pick");
                if (img == null) img = itemImages.get(ingName);

                if (img != null) {
                    g2d.drawImage(img, px + 10 + offset, py + 10, 25, 25, null);
                    offset += 8;
                }
            }
        }
    }
}