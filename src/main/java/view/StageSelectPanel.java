package main.java.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StageSelectPanel extends JPanel {
    private final GameFrame controller;
    private BufferedImage background;
    private int hoverIndex = -1;


    private static class Circle {
        int x, y, r;
        String stage;
        String label;

        Circle(int x, int y, int r, String stage, String label) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.stage = stage;
            this.label = label;
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
        circles.add(new Circle(148, 188, r, "Stage1", "1"));
        circles.add(new Circle(402, 309, r, "Stage2", "2"));
        circles.add(new Circle(670, 187, r, "Stage3", "3"));

        OutlineLabel backLabel = new OutlineLabel("< Back to Homepage");
        backLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 40, 0));
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                controller.showMainMenu();
            }
        });


        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 450));
        linkPanel.setOpaque(false);
        linkPanel.add(backLabel);
        setLayout(new BorderLayout());

        setLayout(new BorderLayout());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));

        bottom.add(backLabel, BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);


        this.addMouseListener(new java.awt.event.MouseAdapter() {
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

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                int newHover = -1;
                for (int i = 0; i < circles.size(); i++) {
                    if (circles.get(i).contains(mx, my)) {
                        newHover = i;
                        break;
                    }
                }

                if (newHover != hoverIndex) {
                    hoverIndex = newHover;
                    repaint();
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

    private void drawStageCircle(Graphics2D g2d, Circle c, int index) {
        Color fill = (hoverIndex == index)
                ? new Color(90, 43, 12)
                : new Color(130, 228, 255);

        Color border = (hoverIndex == index)
                ? new Color(255, 240, 200)
                : new Color(31, 123, 192);

        drawCircleWithBorder(g2d, c.x, c.y, c.r, fill, border, 3);

        Color textColor = (hoverIndex == index)
                ? new Color(255, 240, 200)
                : new Color(31, 123, 192);

        g2d.setColor(textColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.setFont(new Font("Red Hat Text", Font.BOLD, 40));
        drawCenteredString(g2d, c.label, c.x, c.y);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(background != null) {
            g2d.drawImage(background, 0, 0, 800, 500, null);
        }

        for (int i = 0; i < circles.size(); i++) {
            drawStageCircle(g2d, circles.get(i), i);
        }

        g2d.dispose();
    }
}
