package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExitConfirmPanel extends JPanel {
    private final ExitConfirmDialog dialog;

    private final Rectangle backButtonRect;
    private final Rectangle exitButtonRect;

    private boolean backHover = false;
    private boolean exitHover = false;

    private static final int BUTTON_WIDTH = 140;
    private static final int BUTTON_HEIGHT = 35;
    private static final int HORIZONTAL_PADDING = 30;
    private static final int BUTTON_GAP = 20;

    public ExitConfirmPanel(ExitConfirmDialog dialog) {
        this.dialog = dialog;

        this.setPreferredSize(new Dimension(360, 150));

        this.setLayout(null);
        this.setOpaque(false);

        JLabel customLabel = new JLabel("Are you sure you want to exit?");
        customLabel.setForeground(new Color(90, 52, 12));
        customLabel.setFont(new Font("Poppins", Font.BOLD, 16));

        FontMetrics fm = customLabel.getFontMetrics(customLabel.getFont());
        int labelWidth = fm.stringWidth(customLabel.getText());
        int labelX = (360 - labelWidth) / 2;

        customLabel.setBounds(labelX, 30, labelWidth, 20);
        this.add(customLabel);

        int buttonY = 85;
        int totalWidth = 2 * BUTTON_WIDTH + BUTTON_GAP;
        int startX = (360 - totalWidth) / 2;

        this.backButtonRect = new Rectangle(startX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.exitButtonRect = new Rectangle(startX + BUTTON_WIDTH + BUTTON_GAP, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (backButtonRect.contains(p)) {
                    dialog.cancelExit();
                } else if (exitButtonRect.contains(p)) {
                    dialog.confirmExit();
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                boolean newBackHover = backButtonRect.contains(p);
                boolean newExitHover = exitButtonRect.contains(p);

                if (newBackHover != backHover || newExitHover != exitHover) {
                    backHover = newBackHover;
                    exitHover = newExitHover;
                    repaint();
                }
            }
        });
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text, boolean hover, Color baseColor, Color hoverColor) {
        g2.setColor(hover ? hoverColor : baseColor);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        g2.setColor(new Color(90, 52, 12));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        g2.setFont(new Font("Poppins", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int tx = rect.x + (rect.width - textWidth) / 2;
        int ty = rect.y + (rect.height + textHeight) / 2 - (fm.getDescent() / 2);

        g2.setColor(text.equals("BACK") ? Color.WHITE : Color.RED);
        g2.setStroke(new BasicStroke(2));
        g2.drawString(text, tx, ty);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color backgroundColor = new Color(250, 240, 210);
        int arcRadius = 25;

        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcRadius, arcRadius);

        Color backColor = Color.RED;
        Color backHoverColor = Color.PINK;
        drawButton(g2d, backButtonRect, "BACK", backHover, backColor, backHoverColor);

        Color exitColor = new Color(255, 255, 202);
        Color exitHoverColor = new Color(212, 212, 204);
        drawButton(g2d, exitButtonRect, "EXIT", exitHover, exitColor, exitHoverColor);

        g2d.dispose();
    }
}
