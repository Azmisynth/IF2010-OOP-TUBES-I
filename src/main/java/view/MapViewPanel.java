package view;

import model.chef.ChefPlayer;
import model.map.Map;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MapViewPanel extends JPanel {
    private final Map gameMap;
    private final GameFrame gameFrame;
    private final List<ChefPlayer> allChefs;

    // Renderer Modules
    private StationView stationRenderer;
    private ChefView chefRenderer;
    private UIView UI;

    // Resource Variables
    private BufferedImage tileImage;
    private BufferedImage wall;
    private BufferedImage chef1UpImage, chef1DownImage, chef1LeftImage, chef1RightImage;
    private BufferedImage chef2UpImage, chef2DownImage, chef2LeftImage, chef2RightImage;
    private BufferedImage settingsIconImage;
    private boolean hoveringSettings = false;
    private BufferedImage settingsHoverImage;

    // Maps
    private java.util.Map<String, BufferedImage> stationImages;
    private java.util.Map<String, BufferedImage> itemImages;

    private Rectangle settingsButtonRect;

    private static final int TILE_SIZE = 50;
    private static final int WIDTH = 16;
    private static final int HEIGHT = 10;
    private static final int SETTINGS_BTN_SIZE = 40;

    public MapViewPanel(Map gameMap, List<ChefPlayer> allChefs, GameFrame gameFrame) {
        this.gameMap = gameMap;
        this.allChefs = allChefs;
        this.stationImages = new HashMap<>();
        this.itemImages = new HashMap<>();
        this.gameFrame = gameFrame;
        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));

        settingsButtonRect = new Rectangle(723, 14, 64, 53);

        loadResources();
        initRenderers();

        addSettingsButtonListener();
    }

    private void loadResources() {
        try {
            URL imageUrl = getClass().getResource("/images/map/tile.png");
            if (imageUrl == null) {
                System.err.println("FATAL ERROR: File 'tile.png' not found!");
            } else {
                tileImage = ImageIO.read(imageUrl);
            }

            this.wall = ImageIO.read(getClass().getResource("/images/map/wall.png"));
            this.chef1UpImage = ImageIO.read(getClass().getResource("/images/chef/chef1_up.png"));
            this.chef1DownImage = ImageIO.read(getClass().getResource("/images/chef/chef1_down.png"));
            this.chef1RightImage = ImageIO.read(getClass().getResource("/images/chef/chef1_right.png"));
            this.chef1LeftImage = ImageIO.read(getClass().getResource("/images/chef/chef1_left.png"));
            this.chef2LeftImage = ImageIO.read(getClass().getResource("/images/chef/chef2_left.png"));
            this.chef2DownImage = ImageIO.read(getClass().getResource("/images/chef/chef2_down.png"));
            this.chef2RightImage = ImageIO.read(getClass().getResource("/images/chef/chef2_right.png"));
            this.chef2UpImage = ImageIO.read(getClass().getResource("/images/chef/chef2_up.png"));
            this.settingsIconImage = ImageIO.read(getClass().getResource("/images/map/settings.png"));
            this.settingsHoverImage = ImageIO.read(getClass().getResource("/images/map/settings_hover.png"));


            stationImages.put("assembly-normal", ImageIO.read(getClass().getResource("/images/station/assembly_station_normal.png")));
            stationImages.put("assembly-bottom", ImageIO.read(getClass().getResource("/images/station/assembly_station_bottom.png")));
            stationImages.put("cooking-right", ImageIO.read(getClass().getResource("/images/station/cooking_station_right.png")));
            stationImages.put("cooking-left", ImageIO.read(getClass().getResource("/images/station/cooking_station_left.png")));
            stationImages.put("cutting", ImageIO.read(getClass().getResource("/images/station/cutting_station.png")));
            stationImages.put("serving", ImageIO.read(getClass().getResource("/images/station/serving_atas.png")));
            stationImages.put("trash", ImageIO.read(getClass().getResource("/images/station/trash_station.png")));
            stationImages.put("washing", ImageIO.read(getClass().getResource("/images/station/washing_station.png")));
            stationImages.put("ingredient", ImageIO.read(getClass().getResource("/images/station/ingredient_station.png")));
            stationImages.put("ingredient-bottom", ImageIO.read(getClass().getResource("/images/station/assembly_station_bottom.png")));
            stationImages.put("serving-bottom", ImageIO.read(getClass().getResource("/images/station/serving_bawah.png")));
            stationImages.put("plate", ImageIO.read(getClass().getResource("/images/station/plate_storage.png")));

            itemImages.put("Jam", ImageIO.read(getClass().getResource("/images/map/jam.png")));
            itemImages.put("Tomato", ImageIO.read(getClass().getResource("/images/ingredient_tomato_raw.png")));
            itemImages.put("Tomatopick",  ImageIO.read(getClass().getResource("/images/tomat.png"))); // Ini penting buat ChefRenderer
            itemImages.put("Cheese",  ImageIO.read(getClass().getResource("/images/cheese_station.png")));
            itemImages.put("Cheesepick", ImageIO.read(getClass().getResource("/images/ingredient_keju_raw.png")));
            itemImages.put("Chicken", ImageIO.read(getClass().getResource("/images/chicken_station.png")));
            itemImages.put("Chickenpick", ImageIO.read(getClass().getResource("/images/ingredient_ayam_raw.png")));
            itemImages.put("Sausage",   ImageIO.read(getClass().getResource("/images/sausage_station.png")));
            itemImages.put("Sausagepick", ImageIO.read(getClass().getResource("/images/ingredient_sosis_raw.png")));
            itemImages.put("Dough", ImageIO.read(getClass().getResource("/images/dough_station.png")));
            itemImages.put("Doughpick",  ImageIO.read(getClass().getResource("/images/dough.png")));
            itemImages.put("Cheese_CHOPPED", ImageIO.read(getClass().getResource("/images/Cheese_CHOPPED.png")));
            itemImages.put("Sausage_CHOPPED", ImageIO.read(getClass().getResource("/images/ingredient_sosis_chopped.png")));
            itemImages.put("Tomato_CHOPPED", ImageIO.read(getClass().getResource("/images/tomat_slice.png")));
            itemImages.put("Chicken_CHOPPED", ImageIO.read(getClass().getResource("/images/ingredient_ayam_chopped.png")));
            itemImages.put("Dough_CHOPPED", ImageIO.read(getClass().getResource("/images/pizza_polosan.png")));
            itemImages.put("Plate", ImageIO.read(getClass().getResource("/images/plate.png")));
            itemImages.put("Plate_DIRTY", ImageIO.read(getClass().getResource("/images/plate_DIRTY.png")));
            itemImages.put("Pizza_Margherita_RAW", ImageIO.read(getClass().getResource("/images/pizza_margherita.png")));
            itemImages.put("Pizza_Ayam_RAW", ImageIO.read(getClass().getResource("/images/pizza_ayam.png")));
            itemImages.put("Pizza_Sosis_RAW",  ImageIO.read(getClass().getResource("/images/pizza_sosis.png")));
            itemImages.put("Pizza_Tomat_RAW",   ImageIO.read(getClass().getResource("/images/pizza_tomat.png")));
            itemImages.put("Pizza_Margherita",  ImageIO.read(getClass().getResource("/images/pizza_margherita.png")));
            itemImages.put("Pizza_Sosis", ImageIO.read(getClass().getResource("/images/pizza_sosis.png")));
            itemImages.put("Pizza_Ayam",  ImageIO.read(getClass().getResource("/images/pizza_ayam.png")));
            itemImages.put("Burnt", ImageIO.read(getClass().getResource("/images/burnt.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRenderers() {
        // Mengirim aset yang sudah di-load ke Renderer
        this.stationRenderer = new StationView(tileImage, wall, stationImages, itemImages);
        this.chefRenderer = new ChefView(itemImages,
                chef1UpImage, chef1DownImage, chef1LeftImage, chef1RightImage,
                chef2UpImage, chef2DownImage, chef2LeftImage, chef2RightImage
        );
        this.UI = new UIView(itemImages);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (stationRenderer != null) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    stationRenderer.drawTile(g2d, gameMap.getTile(x, y), x, y);
                }
            }
        }

        if (chefRenderer != null) {
            for (ChefPlayer chef : allChefs) {
                chefRenderer.drawChef(g2d, chef);
            }
        }

        if (UI != null) {
            UI.drawUI(g2d, getWidth(), getHeight());
        }

        if (settingsButtonRect != null && settingsIconImage != null) {
            g2d.setColor(new Color(216, 48, 33, 0));
            g2d.fillRoundRect(settingsButtonRect.x, settingsButtonRect.y, settingsButtonRect.width, settingsButtonRect.height, 10, 10);

            BufferedImage img = hoveringSettings ? settingsHoverImage : settingsIconImage;

            g2d.drawImage(
                    img,
                    settingsButtonRect.x,
                    settingsButtonRect.y,
                    settingsButtonRect.width,
                    settingsButtonRect.height,
                    null
            );
        }
    }

    private void addSettingsButtonListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean hover = settingsButtonRect.contains(e.getPoint());
                if (hover != hoveringSettings) {
                    hoveringSettings = hover;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (settingsButtonRect.contains(e.getPoint())) {
                    if (gameFrame != null) {
                        gameFrame.showSettingsMenu();
                    }
                }
            }
        });
    }


    public void refreshView() {
        this.repaint();
    }
}