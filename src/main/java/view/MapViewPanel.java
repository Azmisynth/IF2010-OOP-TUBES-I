package main.java.view;

import main.java.model.chef.ChefPlayer;
import main.java.model.chef.Position;
import main.java.model.map.Map;
import main.java.model.map.Tile;
import main.java.model.chef.Direction;
import main.java.model.station.Station;
import main.java.model.station.StationFactory;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MapViewPanel extends JPanel {
    private final Map gameMap;
    private ChefPlayer activeChef;
    private List<ChefPlayer> allChefs;
    private BufferedImage tileImage;
    //private BufferedImage chefImage;
    private BufferedImage chef1UpImage;
    private BufferedImage chef1DownImage;
    private BufferedImage chef1LeftImage;
    private BufferedImage chef1RightImage;
    private BufferedImage chef2UpImage;
    private BufferedImage chef2DownImage;
    private BufferedImage chef2LeftImage;
    private BufferedImage chef2RightImage;
    private BufferedImage assemblyStation;
    private BufferedImage cookingStation;
    private BufferedImage cuttingStation;
    private BufferedImage ingredientStation;
    private BufferedImage plateStorage;
    private BufferedImage servingCounter;
    private BufferedImage trashStation;
    private BufferedImage washingStation;
    private static final int TILE_SIZE = 50;
    private static final int WIDTH = 14;
    private static final int HEIGHT = 10;
    private java.util.Map<String, BufferedImage> stationImages;

//    public MapViewPanel(main.java.model.map.Map map, List<Chef> chefs) {
//        this.gameMap = map;
//        this.allChefs = chefs;
//        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
//    }

    public MapViewPanel(Map gameMap, List<ChefPlayer> allChefs) {
        this.gameMap = gameMap;
        this.allChefs = allChefs;
        this.stationImages = new java.util.HashMap<>();
        try {
            URL imageUrl = getClass().getResource("/images/map/tile.png");
            if (imageUrl == null) {
                System.err.println("FATAL ERROR: File 'tile.png' not found in resources!");
            } else {
                tileImage = ImageIO.read(imageUrl);
            }
            this.chef1UpImage = ImageIO.read(getClass().getResource("/images/chef/chef1_up.png"));
            this.chef1DownImage = ImageIO.read(getClass().getResource("/images/chef/chef1_down.png"));
            this.chef1RightImage = ImageIO.read(getClass().getResource("/images/chef/chef1_right.png"));
            this.chef1LeftImage = ImageIO.read(getClass().getResource("/images/chef/chef1_left.png"));
            this.chef2LeftImage = ImageIO.read(getClass().getResource("/images/chef/chef2_left.png"));
            this.chef2DownImage = ImageIO.read(getClass().getResource("/images/chef/chef2_down.png"));
            this.chef2RightImage = ImageIO.read(getClass().getResource("/images/chef/chef2_right.png"));
            this.chef2UpImage = ImageIO.read(getClass().getResource("/images/chef/chef2_up.png"));
//            this.assemblyStation = ImageIO.read(getClass().getResource("/images/assembly_station_normal.png"));
//            this.cookingStation = ImageIO.read(getClass().getResource("/images/cooking_station_right.png"));
//            this.cuttingStation = ImageIO.read(getClass().getResource("/images/cutting_station.png"));
////            this.ingredientStation = ImageIO.read(getClass().getResource("/images/ingredient_station.png"));
////            this.plateStorage = ImageIO.read(getClass().getResource("/images/plate_storage.png"));
//            this.servingCounter = ImageIO.read(getClass().getResource("/images/serving_counter.png"));
//            this.trashStation = ImageIO.read(getClass().getResource("/images/trash_station.png"));
//            this.washingStation = ImageIO.read(getClass().getResource("/images/washing_station.png"));
            stationImages.put("assembly-normal", ImageIO.read(getClass().getResource("/images/station/assembly_station_normal.png")));
            stationImages.put("assembly-bottom", ImageIO.read(getClass().getResource("/images/station/assembly_station_bottom.png")));
            stationImages.put("cooking-right", ImageIO.read(getClass().getResource("/images/station/cooking_station_right.png")));
            stationImages.put("cooking-left", ImageIO.read(getClass().getResource("/images/station/cooking_station_left.png")));
            stationImages.put("cutting", ImageIO.read(getClass().getResource("/images/station/cutting_station.png")));
//            stationImages.put("ingredient", ImageIO.read(getClass().getResource("/images/ingredient_station.png")));
//            stationImages.put("plate", ImageIO.read(getClass().getResource("/images/plate_station.png")));
            stationImages.put("serving", ImageIO.read(getClass().getResource("/images/station/serving_counter.png")));
            stationImages.put("trash", ImageIO.read(getClass().getResource("/images/station/trash_station.png")));
            stationImages.put("washing", ImageIO.read(getClass().getResource("/images/station/washing_station.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE;
                Tile tileModel = gameMap.getTile(x, y);
                BufferedImage imageToDraw = tileImage;
                boolean usingImage = (tileImage != null && !tileModel.isWall(x, y));
                Color color = Color.LIGHT_GRAY;

                if (tileModel.isWall(getX(), getY())) {
                    color = Color.BLACK;
                    g2d.setColor(color);
                    g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else if (tileModel.getStation() != null) {
                    Station currentStation = tileModel.getStation();
                    String stationKey = StationFactory.getName(currentStation.getSymbol(), new Position(x, y));

                    imageToDraw = stationImages.get(stationKey);
                    if(imageToDraw != null) {
                        g2d.drawImage(imageToDraw, px, py, TILE_SIZE, TILE_SIZE, this);
                    }
//
//                    Station currentStation = tileModel.getStation();
//                    imageToDraw = assemblyStation;
//                    if(imageToDraw != null) {g2d.drawImage(imageToDraw, px, py, TILE_SIZE, TILE_SIZE, this);}
                }
//                else if (tileModel.getItem() != null) {
//                    color = Color.YELLOW; // Item di lantai
//                }
                else {
                    imageToDraw = tileImage;
                    g2d.drawImage(imageToDraw, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    g2d.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
//                g2d.setColor(color);
//                g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//

            }
        }
        for(ChefPlayer chef : allChefs) {
            int px = chef.getPosition().getX() * TILE_SIZE;
            int py = chef.getPosition().getY() * TILE_SIZE;

            BufferedImage imageToDraw = null;
            Direction currentDir = chef.getDirection();

            switch (currentDir) {
                case UP:
                    if(chef.getName().equals("Kebin")) { imageToDraw = chef1UpImage; }
                    else if(chef.getName().equals("Stewart")) { imageToDraw = chef2UpImage; }
                    break;
                case DOWN:
                    if(chef.getName().equals("Kebin")) { imageToDraw = chef1DownImage; }
                    else if(chef.getName().equals("Stewart")) { imageToDraw = chef2DownImage; }
                    //imageToDraw = chef1DownImage;
                    break;
                case LEFT:
                    if(chef.getName().equals("Kebin")) { imageToDraw = chef1LeftImage; }
                    else if(chef.getName().equals("Stewart")) { imageToDraw = chef2LeftImage; }
//                    imageToDraw = chef1LeftImage;
                    break;
                case RIGHT:
                    if(chef.getName().equals("Kebin")) { imageToDraw = chef1RightImage; }
                    else if(chef.getName().equals("Stewart")) { imageToDraw = chef2RightImage; }
//                    imageToDraw = chef1RightImage;
                    break;
                default:
                    if(chef.getName().equals("Kebin")) { imageToDraw = chef1DownImage; }
                    else if(chef.getName().equals("Stewart")) { imageToDraw = chef2DownImage; }
//                    imageToDraw = chef1DownImage;
            }

            if (imageToDraw != null) {
                g2d.drawImage(imageToDraw, px, py, TILE_SIZE, TILE_SIZE, this);
            }
//        else {
//            // Fallback: Jika gambar gagal dimuat, gambar lingkaran berwarna
//            g2d.setColor(chef.isActive() ? Color.BLUE : Color.RED);
//            g2d.fillOval(px, py, TILE_SIZE, TILE_SIZE);
//        }

            g2d.setColor(Color.WHITE);
        }
    }

    public void refreshView() {
        this.repaint();
    }
}