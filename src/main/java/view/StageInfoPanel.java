package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StageInfoPanel extends JPanel {
    private final StageInfoDialog dialog;
    private BufferedImage previewStage;
    private final Rectangle playButtonRect;
    private final Rectangle backButtonRect;
    private static final int PANEL_WIDTH = 231;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 35;
    private static final int BUTTON_GAP = 10;
    private boolean backHover;
    private boolean playHover;

    public StageInfoPanel(String stageId, StageInfoDialog dialog) {
        this.dialog = dialog;

        this.setPreferredSize(new Dimension(233, 240));
        this.setLayout(null);

        this.setOpaque(false);
        try {
            if(stageId.equals("Stage1")) this.previewStage = ImageIO.read(getClass().getResource("/images/stage/preview_stage1.png"));
            else if(stageId.equals("Stage2")) this.previewStage = ImageIO.read(getClass().getResource("/images/stage/preview_stage2.png"));
            else this.previewStage = ImageIO.read(getClass().getResource("/images/stage/preview_stage3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalWidth = BUTTON_WIDTH;
        int startX = (PANEL_WIDTH - totalWidth) / 2;
        int playButtonY = 150;
        int backButtonY = playButtonY + BUTTON_HEIGHT + BUTTON_GAP;


        this.playButtonRect = new Rectangle(startX, playButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.backButtonRect = new Rectangle(startX, backButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (backButtonRect.contains(p)) {
                    dialog.cancelPlay();
                } else if (playButtonRect.contains(p)) {
                    dialog.confirmPlay();
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                boolean newBackHover = backButtonRect.contains(p);
                boolean newPlayHover = playButtonRect.contains(p);

                if (newBackHover != backHover || newPlayHover != playHover) {
                    backHover = newBackHover;
                    playHover = newPlayHover;
                    repaint();
                }
            }
        });
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text, boolean hover, Color baseColor, Color hoverColor) {
        g2.setColor(hover ? hoverColor : baseColor);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        if(hover) g2.setColor(new Color(255, 251, 202));
        else g2.setColor(new Color(216, 48, 33));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        g2.setFont(new Font("Red Had Text", Font.BOLD, 15));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        int tx = rect.x + (rect.width - textWidth) / 2;
        int ty = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();

        if(hover) {
            g2.setColor(new Color(255, 251, 202));
        } else {
            g2.setColor(text.equals("Play") ? new Color(255, 251, 202) : new Color(216, 48, 33));
        }
        g2.setStroke(new BasicStroke(2));
        g2.drawString(text, tx, ty);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (previewStage != null) {
            g2d.drawImage(previewStage, 0, 0, getWidth(), getHeight(), this);
        }

        Color playColor = new Color(216, 48, 33);
        Color playHoverColor = new Color(90, 43, 12);
        drawButton(g2d, playButtonRect, "Play", playHover, playColor, playHoverColor);

        Color backColor = new Color(255, 251, 202);
        Color backHoverColor = new Color(90, 43, 12);
        drawButton(g2d,  backButtonRect, "Back", backHover, backColor, backHoverColor);
    }
}