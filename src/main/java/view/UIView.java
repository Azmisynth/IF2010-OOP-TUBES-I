package main.java.view;

import main.java.model.kitchen.Order;
import main.java.model.kitchen.OrderManager;
import main.java.model.kitchen.Recipe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class UIView {
    private final Map<String, BufferedImage> itemImages;

    private final Font scoreFont = new Font("Red Hat Text", Font.BOLD, 14);
    private final Font timerFont = new Font("Red Hat Text", Font.BOLD, 24);
    private final Font orderFont = new Font("Red Hat Text", Font.BOLD, 10);
    private final Font failedFont = new Font("Red Hat Text", Font.BOLD, 11);

    public UIView(Map<String, BufferedImage> itemImages) {
        this.itemImages = itemImages;
    }

    public void drawUI(Graphics2D g2d, int width, int height) {
        drawScore(g2d);
        drawTimer(g2d, width, height); // Timer di pojok kiri bawah
        drawFailedCOunter(g2d, height);
        drawOrders(g2d); // Order List
    }

    private void drawScore(Graphics2D g2d) {
        int score = OrderManager.getInstance().getScore();
        int target = OrderManager.getInstance().getTargetScore();

        // Background Score (Pojok Kiri Atas)
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(20, 20, 125, 40, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(20, 20, 125, 40, 10, 10);

        g2d.setFont(scoreFont);
        g2d.setColor(Color.WHITE);
        // Tampilkan Score / Target
        g2d.drawString("Score: " + score + "/" + target, 30, 47);
    }

    private void drawTimer(Graphics2D g2d, int screenWidth, int screenHeight) {
        int time = OrderManager.getInstance().getTimeRemaining();
        String timeText = String.format("%02d:%02d", time / 60, time % 60);

        int timerBoxWidth = 140;
        int margin = 20;

        int timerX = screenWidth - timerBoxWidth - margin;
        int timerY = screenHeight - 55;

        // Background
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(timerX, timerY, timerBoxWidth, 50, 15, 15);

        // Border
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(timerX, timerY, timerBoxWidth, 50, 15, 15);

        // Text Timer
        g2d.setFont(timerFont);

        // Efek Kedip Merah jika waktu < 30 detik
        if (time <= 30 && time % 2 == 0) g2d.setColor(Color.RED);
        else g2d.setColor(Color.WHITE);

        g2d.drawString(timeText, timerX + 50, timerY + 34);
        BufferedImage jampasir = itemImages.get("Jam");
        g2d.drawImage(jampasir, timerX + 7  , timerY + 4, 40, 40, null);
    }

    private void drawFailedCOunter(Graphics2D g2d, int screenHeight) {
        int currentFail = OrderManager.getInstance().getFailedOrdersCount();
        int maxFail = OrderManager.getInstance().getMaxFailedOrders();
        String failText = String.format("%d", currentFail);
        String maxText = String.format("%d", maxFail);
        String combined = failText + "/" + maxText;
        int boxWidth = 100;
        int boxHeight = 45;
        int x = 20;

        int y = screenHeight - 50;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, boxWidth, boxHeight, 10, 10);

        g2d.setFont(failedFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString("FAILED COUNT", x + 9, y + 12);
        g2d.drawString(combined, x + 40, y + 30);
    }

    private void drawOrders(Graphics2D g2d) {
        List<Order> orders = OrderManager.getInstance().getActiveOrders();

        int startX = 20;       // Margin kiri
        int startY = 75;       // Di bawah Score
        int boxWidth = 125;
        int boxHeight = 65;
        int gap = 10;

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            int y = startY + (i * (boxHeight + gap));

            // 1. Background Kotak Memanjang
            g2d.setColor(new Color(255, 255, 255, 220));
            g2d.fillRoundRect(startX, y, boxWidth, boxHeight, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(startX, y, boxWidth, boxHeight, 10, 10);

            // 2. Gambar Pizza Utama (Kiri)
            String recipeName = order.getRecipe().getName();
            String recipeKey = recipeName.replace(" ", "_");
            BufferedImage icon = itemImages.get(recipeKey);

            if (icon != null) {
                g2d.drawImage(icon, startX + 6, y + 10, 40, 40, null);
            }

            // 3. Nama Resep (Kanan Atas)
            g2d.setColor(Color.BLACK);
            g2d.setFont(orderFont);
            g2d.drawString(recipeName, startX + 42, y + 20);

            // 4. Ingredients List (Kanan Bawah - Asset "pick")
            int ingX = startX + 42;
            int ingY = y + 26;

            for (String ingName : order.getRecipe().getRequiredIngredients()) {
                // Cari asset dengan suffix "pick" (misal: Tomato_CHOPPEDpick)
                String pickKey = ingName + "_CHOPPED";
                BufferedImage ingImg = itemImages.get(pickKey);

                // Fallback: kalau versi pick ga ada, coba nama asli
                if (ingImg == null) ingImg = itemImages.get(ingName);

                if (ingImg != null) {
                    g2d.drawImage(ingImg, ingX, ingY, 22, 22, null);
                    ingX += 18; // Geser ke kanan untuk bahan berikutnya
                }
            }

            // 5. Progress Bar (Bawah)
            float progress = order.getProgress();
            int barWidth = (int) ((boxWidth - 20) * progress);

            // Warna Bar
            if (progress > 0.5f) g2d.setColor(Color.GREEN);
            else if (progress > 0.2f) g2d.setColor(Color.ORANGE);
            else g2d.setColor(Color.RED);

            // Gambar Bar
            g2d.fillRoundRect(startX + 6, y + 54, barWidth, 4, 2, 2);

            // Border Bar
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(startX + 6, y + 54, boxWidth - 12, 4, 2, 2);
        }
    }
}
