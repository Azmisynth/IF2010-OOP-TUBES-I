package main.java.view;

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
    private BufferedImage starImage;
    private final Rectangle playButtonRect;
    private final Rectangle backButtonRect;
    private static final int PANEL_WIDTH = 231;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 35;
    private static final int TOP_PADDING = 50;
    private static final int VERTICAL_PADDING = 30;
    private static final int BUTTON_GAP = 20;
    private static final int LABEL_HEIGHT = 30;
    private static final int LABEL_PADDING = 20;
    private boolean backHover;
    private boolean playHover;

    public StageInfoPanel(String stageId, StageInfoDialog dialog) {
        this.dialog = dialog;

        this.setPreferredSize(new Dimension(231, 240));
        this.setLayout(null);

        this.setOpaque(false);
        try {
            if(stageId.equals("Stage1")) this.previewStage = ImageIO.read(getClass().getResource("/images/stage/preview_stage1.png"));
            else if(stageId.equals("Stage2")) this.previewStage = ImageIO.read(getClass().getResource("/images/stage/preview_stage2.png"));
            else this.previewStage = ImageIO.read(getClass().getResource("/images/stage/preview_stage3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel titleLabel = new JLabel("  KrustyCooked");
        titleLabel.setForeground(new Color(90, 43, 12));
        titleLabel.setFont(new Font("Red Hat Text", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        int titleWidth = titleLabel.getFontMetrics(titleLabel.getFont()).stringWidth(titleLabel.getText()) + LABEL_PADDING;
        int titleX = (231 - titleWidth) / 2;
        titleLabel.setBounds(titleX, 40, titleWidth, 50);
        this.add(titleLabel);
        JLabel targetTime;
        if(stageId.equals("Stage1")) {
            targetTime = new JLabel("Target time      120 s");
        } else if(stageId.equals("Stage2")) {
            targetTime = new JLabel("Target time      90 s");
        } else if(stageId.equals("Stage3")) {
            targetTime = new JLabel("Target time      60 s");
        } else {
            targetTime = new JLabel("No target time");
        }

//        JLabel targetTime = new JLabel("Target time      90 s");
        targetTime.setForeground(new Color(90, 43, 12));
        targetTime.setFont(new Font("Red Hat Text", Font.BOLD, 15));
        targetTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        int timeWidth = targetTime.getFontMetrics(targetTime.getFont()).stringWidth(targetTime.getText()) + LABEL_PADDING;
        int timeX = (231 - timeWidth) / 2;
        targetTime.setBounds(timeX, 70, timeWidth, 50);
        this.add(targetTime);

        int totalWidth = BUTTON_WIDTH;
        int startX = (PANEL_WIDTH - totalWidth) / 2;
        int startY = 140;
        int playButtonY = 120;
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

        g2.setColor(new Color(216, 146, 33));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        g2.setFont(new Font("Red Had Text", Font.BOLD, 15));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int tx = rect.x + (rect.width - textWidth) / 2;
        int ty = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(text.equals("PLAY") ? new Color(255, 251, 202) : new Color(216, 146, 33));
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

        Color playColor = new Color(216, 146, 33);
        Color playHoverColor = new Color(226, 169, 78);
        drawButton(g2d, playButtonRect, "PLAY", playHover, playColor, playHoverColor);

        Color backColor = new Color(255, 251, 202);
        Color backHoverColor = Color.LIGHT_GRAY;
        drawButton(g2d,  backButtonRect, "BACK", backHover, backColor, backHoverColor);
    }
}