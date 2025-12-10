package main.java.view;

import main.java.model.chef.ChefPlayer;
import main.java.model.chef.Direction;
import main.java.model.item.Item; // Sesuaikan package Item kamu

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ChefView {
    private static final int TILE_SIZE = 50;

    private final Map<String, BufferedImage> itemImages;
    private final BufferedImage chef1UpImage, chef1DownImage, chef1LeftImage, chef1RightImage;
    private final BufferedImage chef2UpImage, chef2DownImage, chef2LeftImage, chef2RightImage;

    public ChefView(Map<String, BufferedImage> itemImages,
                        BufferedImage c1U, BufferedImage c1D, BufferedImage c1L, BufferedImage c1R,
                        BufferedImage c2U, BufferedImage c2D, BufferedImage c2L, BufferedImage c2R) {
        this.itemImages = itemImages;
        this.chef1UpImage = c1U; this.chef1DownImage = c1D; this.chef1LeftImage = c1L; this.chef1RightImage = c1R;
        this.chef2UpImage = c2U; this.chef2DownImage = c2D; this.chef2LeftImage = c2L; this.chef2RightImage = c2R;
    }

    public void drawChef(Graphics2D g2d, ChefPlayer chef) {
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
                break;
            case LEFT:
                if(chef.getName().equals("Kebin")) { imageToDraw = chef1LeftImage; }
                else if(chef.getName().equals("Stewart")) { imageToDraw = chef2LeftImage; }
                break;
            case RIGHT:
                if(chef.getName().equals("Kebin")) { imageToDraw = chef1RightImage; }
                else if(chef.getName().equals("Stewart")) { imageToDraw = chef2RightImage; }
                break;
            default:
                if(chef.getName().equals("Kebin")) { imageToDraw = chef1DownImage; }
                else if(chef.getName().equals("Stewart")) { imageToDraw = chef2DownImage; }
        }

        if (imageToDraw != null) {
            g2d.drawImage(imageToDraw, px, py, TILE_SIZE, TILE_SIZE, null);
        }

        // Reset warna
        g2d.setColor(Color.WHITE);

        //Penanda chef aktif
        if (chef.isActive()) {
            int centerX = px + (TILE_SIZE / 2);
            int baseY = py - 15;
            //Bouncing
            long time = System.currentTimeMillis();
            int bounce = (int) (Math.sin(time / 150.0) * 5);

            int triSize = 8; // Ukuran segitiga
            int triY = baseY + bounce; // Posisi Y + Animasi

            int[] xPoints = {
                    centerX - triSize, // Sudut Kiri Atas
                    centerX + triSize, // Sudut Kanan Atas
                    centerX            // Sudut Bawah
            };

            int[] yPoints = {
                    triY - triSize,    // Y Atas
                    triY - triSize,    // Y Atas
                    triY               // Y Bawah
            };

            g2d.setColor(Color.CYAN);
            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 3);
        }

        //Cooldown Dash
        float dashProgress = chef.getDashCooldownProgress();
        if (dashProgress < 1.0f) {
            int barWidth = 40;
            int barHeight = 4;
            int barX = px + 5;
            int barY = py + 45;

            g2d.setColor(Color.GRAY);
            g2d.fillRect(barX, barY, barWidth, barHeight);

            g2d.setColor(Color.CYAN);
            int fillWidth = (int) (barWidth * dashProgress);
            g2d.fillRect(barX, barY, fillWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(barX, barY, barWidth, barHeight);
        }

        if (chef.getInventory() != null) {
            String baseName = chef.getInventory().getName();
            BufferedImage itemImg = itemImages.get(baseName + "pick"); // Cek suffix "pick"

            if (itemImg == null) {
                itemImg = itemImages.get(baseName); // Fallback ke nama asli
            }

            if (itemImg != null) {
                g2d.drawImage(itemImg, px + 15, py + 20, 20, 20, null);
            }
        }
    }
}