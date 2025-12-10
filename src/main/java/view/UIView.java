package main.java.view;

import main.java.model.kitchen.Order;
import main.java.model.kitchen.OrderManager;
import main.java.model.kitchen.Recipe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class UIView {
    private final Map<String, BufferedImage> itemImages;

    private final Font scoreFont = new Font("Arial", Font.BOLD, 20);
    private final Font timerFont = new Font("Arial", Font.BOLD, 16);

    public UIView(Map<String, BufferedImage> itemImages) {
        this.itemImages = itemImages;
    }

    public void drawUI(Graphics2D g2d) {
        drawScoreAndStageTimer(g2d);
        drawLeftWallOrders(g2d);
    }

    private void drawScoreAndStageTimer(Graphics2D g2d) {
        int score = OrderManager.getInstance().getScore();

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(5, 5, 120, 50, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(5, 5, 120, 50, 10, 10);

        g2d.setFont(scoreFont);
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Score: " + score, 15, 28);

    }

    private void drawLeftWallOrders(Graphics2D g2d) {
        List<Order> orders = OrderManager.getInstance().getActiveOrders();

        int startX = 5;      // Jarak dari kiri layar
        int startY = 70;     // Mulai di bawah kotak skor
        int boxSize = 50;    // Ukuran kotak order (persegi)
        int gap = 10;        // Jarak antar order

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            int y = startY + (i * (boxSize + gap));

            // 1. Background Order (Kotak Putih Transparan)
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillRoundRect(startX, y, boxSize, boxSize, 10, 10);

            // Border (Hitam)
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(startX, y, boxSize, boxSize, 10, 10);

            // 2. Gambar Pizza (Ikon)
            // Ambil nama resep -> Ubah jadi Key Gambar (misal "Pizza Margherita" -> "Pizza_Margherita")
            String recipeKey = order.getRecipe().getName().replace(" ", "_");

            BufferedImage icon = itemImages.get(recipeKey);

            if (icon != null) {
                g2d.drawImage(icon, startX + 5, y + 5, 40, 40, null);
            } else {
                // Fallback (Tanda Tanya Merah) jika gambar belum diload
                g2d.setColor(Color.RED);
                g2d.drawString("?", startX + 20, y + 30);
            }

            // 3. Progress Bar Waktu (Di Bawah Kotak)
            float progress = order.getProgress(); // 0.0 s/d 1.0

            // Warna Bar: Hijau -> Kuning -> Merah
            if (progress > 0.5f) g2d.setColor(Color.GREEN);
            else if (progress > 0.2f) g2d.setColor(Color.ORANGE);
            else g2d.setColor(Color.RED);

            // Bar mengecil ke kiri
            int barHeight = 6;
            int barWidth = (int) ((boxSize - 4) * progress); // Sedikit lebih kecil dari kotak

            // Gambar Bar
            g2d.fillRect(startX + 2, y + boxSize - 8, barWidth, barHeight);

            // Border Bar Hitam Tipis
            g2d.setColor(Color.BLACK);
            g2d.drawRect(startX + 2, y + boxSize - 8, boxSize - 4, barHeight);
        }
    }
}
