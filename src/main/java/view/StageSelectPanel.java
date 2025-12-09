package main.java.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StageSelectPanel extends JPanel {
    private final GameFrame controller;
    private BufferedImage background;

    private static class Circle {
        int x, y, r;
        String stage;

        Circle(int x, int y, int r, String stage) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.stage = stage;
        }

        boolean contains(int mx, int my) {
            int dx = mx - x;
            int dy = my - y;
            return dx * dx + dy * dy <= r * r;
        }
    }

    private final java.util.List<Circle> circles = new java.util.ArrayList<>();

    public StageSelectPanel(GameFrame controller) {
        this.controller = controller;
        this.setPreferredSize(new Dimension(800, 500));

        try {
            this.background = ImageIO.read(getClass().getResource("/images/stage/select_stage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int r = 35;
        circles.add(new Circle(146, 195, r, "Stage1"));
        circles.add(new Circle(397, 300, r, "Stage2"));
        circles.add(new Circle(670, 195, r, "Stage3"));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                for (Circle c : circles) {
                    if (c.contains(mx, my)) {
                        controller.startGame(c.stage);
                        break;
                    }
                }
            }
        });
    }

    private void drawCenteredString(Graphics2D g2d, String text, int centerX, int centerY) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = centerX - (textWidth / 2);
        int textY = centerY + (fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
    }

    private void drawCircleWithBorder(Graphics2D g2d, int x, int y, int radius, Color fillColor, Color borderColor, int strokeWidth) {
        int diameter = radius * 2;
        int startX = x - radius;
        int startY = y - radius;
        g2d.setColor(fillColor);
        g2d.fillOval(startX, startY, diameter, diameter);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawOval(startX, startY, diameter, diameter);
        g2d.setStroke(new BasicStroke(1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(background != null) {
            g2d.drawImage(background, 0, 0, 800, 500, null);
        }

        int circleRadius = 35;

        drawCircleWithBorder(g2d, 146, 195, circleRadius, new Color(255,240,200), new Color(90,43,12), 3);
        drawCircleWithBorder(g2d, 397, 300, circleRadius, new Color(255,240,200), new Color(90,43,12), 3);
        drawCircleWithBorder(g2d, 670, 195, circleRadius, new Color(255,240,200), new Color(90,43,12), 3);

        g2d.setColor(new Color(90,43,12));
        g2d.setFont(new Font("Red Hat Text", Font.BOLD, 40));
        drawCenteredString(g2d, "1", 146, 195);
        drawCenteredString(g2d, "2", 397, 300);
        drawCenteredString(g2d, "3", 670, 195);

        g2d.dispose();
    }
}
