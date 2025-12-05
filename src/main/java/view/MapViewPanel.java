package main.java.view;

import main.java.controller.KeyHandler;
import main.java.model.chef.ChefPlayer;
import main.java.model.map.Map;
import main.java.model.map.Tile;
import main.java.model.chef.Direction;
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

public class MapViewPanel extends JPanel implements Runnable {
    private final Map gameMap;
//    private ChefPlayer activeChef;
    private List<ChefPlayer> allChefs;
    private BufferedImage tileImage;
    //private BufferedImage chefImage;
    private BufferedImage chefUpImage;
    private BufferedImage chefDownImage;
    private BufferedImage chefLeftImage;
    private BufferedImage chefRightImage;
    private static final int TILE_SIZE = 50;
    private static final int WIDTH = 14;
    private static final int HEIGHT = 10;
    Thread gameThread;
    int FPS = 60;
    KeyHandler keyHandler;

    double playerSpeed = 0.02;

    public MapViewPanel(Map gameMap, List<ChefPlayer> allChefs) {
        this.gameMap = gameMap;
        this.allChefs = allChefs;
        try {
            URL imageUrl = getClass().getResource("/images/tile.png");
            if (imageUrl == null) {
                System.err.println("FATAL ERROR: File 'tile.png' not found in resources!");
            } else {
                tileImage = ImageIO.read(imageUrl);
            }
            URL chefImageUrl = getClass().getResource("/images/chef_player_down.png");
            if(chefImageUrl != null) {
                this.chefDownImage = ImageIO.read(chefImageUrl);
            }
            chefImageUrl = getClass().getResource("/images/chef_player_up.png");
            if (chefImageUrl != null) {
                this.chefUpImage = ImageIO.read(chefImageUrl);
            }
            chefImageUrl = getClass().getResource("/images/chef_player_right.png");
            if(chefImageUrl != null) {
                this.chefRightImage = ImageIO.read(chefImageUrl);
            }
            chefImageUrl = getClass().getResource("/images/chef_player_left.png");
            if(chefImageUrl != null) {
                this.chefLeftImage = ImageIO.read(chefImageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        this.setFocusable(true);
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
        this.addKeyListener(keyHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tileModel = gameMap.getTile(x, y);
                BufferedImage imageToDraw = tileImage;
                boolean usingImage = (tileImage != null && !tileModel.isWall(x, y));
                Color color = Color.LIGHT_GRAY;

                if (tileModel.isWall(getX(), getY())) {
                    color = Color.BLACK;
                    g2d.setColor(color);
                    g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else {
                    imageToDraw = tileImage;
                    g2d.drawImage(imageToDraw, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    g2d.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
//                else if (tileModel.getStation() != null) {
//                    color = Color.ORANGE; // Stasiun
//                } else if (tileModel.getItem() != null) {
//                    color = Color.YELLOW; // Item di lantai
//                }

//                g2d.setColor(color);
//                g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//

            }
        }
        for(ChefPlayer chef : allChefs) {
            double px = chef.getPosition().getX() * TILE_SIZE;
            double py = chef.getPosition().getY() * TILE_SIZE;

            BufferedImage imageToDraw;
            Direction currentDir = chef.getDirection();

            switch (currentDir) {
                case UP:
                    imageToDraw = chefUpImage;
                    break;
                case DOWN:
                    imageToDraw = chefDownImage;
                    break;
                case LEFT:
                    imageToDraw = chefLeftImage;
                    break;
                case RIGHT:
                    imageToDraw = chefRightImage;
                    break;
                default:
                    imageToDraw = chefDownImage;
            }

            if (imageToDraw != null) {
                g2d.drawImage(imageToDraw, (int) px, (int) py, TILE_SIZE, TILE_SIZE, this);
            }
//        else {
//            // Fallback: Jika gambar gagal dimuat, gambar lingkaran berwarna
//            g2d.setColor(chef.isActive() ? Color.BLUE : Color.RED);
//            g2d.fillOval(px, py, TILE_SIZE, TILE_SIZE);
//        }

            g2d.setColor(Color.WHITE);
            g2d.drawString(chef.getName(), ((int) px) + TILE_SIZE/4,((int) py) + 2);
        }

        // 2. Gambar Chef (Didasarkan pada posisi Model)
//        for (Chef chef : allChefs) {
//            int px = chef.getPosition().getX() * TILE_SIZE;
//            int py = chef.getPosition().getY() * TILE_SIZE;
//
//            g2d.setColor(chef.isActive() ? Color.BLUE : Color.RED);
//            g2d.fillOval(px, py, TILE_SIZE, TILE_SIZE);
//
//            g2d.setColor(Color.WHITE);
//            g2d.drawString(chef.getName(), px + 5, py + TSIZE / 2);
//        }
    }

    public void refreshView() {
        this.repaint();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS; // 0.01666 sec
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            // update information such as character position
            update();
            // draw the screen with the updated information (player gerak dan berpindah posisi, artinya update informasi koordinat secara terus menerus)
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        ChefPlayer chef = gameMap.getActiveChef();
        //for(ChefPlayer chef : allChefs) {
            if(keyHandler.upPressed == true) {
                chef.setPosition(Direction.UP, playerSpeed);
            } else if(keyHandler.downPressed == true) {
                chef.setPosition(Direction.DOWN, playerSpeed);
            } else if(keyHandler.leftPressed == true) {
                chef.setPosition(Direction.LEFT, playerSpeed);
            } else if(keyHandler.rightPressed == true) {
                chef.setPosition(Direction.RIGHT, playerSpeed);
            }
        //}
    }
}