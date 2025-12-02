import main.java.model.map.Map;
import main.java.model.map.Tile;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class MapViewPanel extends JPanel {
    private final Map gameMap;
    // private final List<Chef> allChefs;
    private BufferedImage tileImage;
    private static final int TILE_SIZE = 50;
    private static final int WIDTH = 14;
    private static final int HEIGHT = 10;

//    public MapViewPanel(main.java.model.map.Map map, List<Chef> chefs) {
//        this.gameMap = map;
//        this.allChefs = chefs;
//        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
//    }

    public MapViewPanel(Map gameMap) {
        this.gameMap = gameMap;
        try {
            URL imageUrl = ClassLoader.getSystemResource("images/tile.png");
            if (imageUrl == null) {
                System.err.println("FATAL ERROR: File 'tile.png' not found in resources!");
            } else {
                tileImage = ImageIO.read(imageUrl);
            }
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
                Tile tileModel = gameMap.getTile(x, y);
                BufferedImage imageToDraw = tileImage;

                //Color color = Color.LIGHT_GRAY;

                if (tileModel.isWall(getX(), getY())) {
//                    color = Color.BLACK; // Dinding
                    imageToDraw = tileImage;
                }
//                else if (tileModel.getStation() != null) {
//                    color = Color.ORANGE; // Stasiun
//                } else if (tileModel.getItem() != null) {
//                    color = Color.YELLOW; // Item di lantai
//                }

//                g2d.setColor(color);
//                g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//
                g2d.drawImage(imageToDraw, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // 2. Gambar Chef (Didasarkan pada posisi Model)
//        for (Chef chef : allChefs) {
//            int px = chef.getPosition().getX() * TILE_SIZE;
//            int py = chef.getPosition().getY() * TILE_SIZE;
//
//            // Gambar Chef sebagai lingkaran (simulasi ImageView)
//            g2d.setColor(chef.isActive() ? Color.BLUE : Color.RED);
//            g2d.fillOval(px, py, TILE_SIZE, TILE_SIZE);
//
//            // Tambahkan nama untuk debugging
//            g2d.setColor(Color.WHITE);
//            g2d.drawString(chef.getName(), px + 5, py + TSIZE / 2);
//        }
    }

    public void refreshView() {
        this.repaint();
    }
}