package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class ResultScreenPanel extends JPanel {
    private final ResultScreenDialog dialog;
    private final int finalScore;
    private final boolean passed;
    private BufferedImage backgroundPopup;

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 380;

    private final Rectangle continueRect = new Rectangle(299, 319, 153, 35);

    private final Rectangle tryAgainRect = new Rectangle(451, 311, 50, 50);

    private static final int SCORE_X = 370;
    private static final int SCORE_Y = 265;
    private static final int SCORE_FONT_SIZE = 30;

    public ResultScreenPanel(ResultScreenDialog dialog, int finalScore, boolean passed) {
        this.dialog = dialog;
        this.finalScore = finalScore;
        this.passed = passed;

        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLayout(null);
        this.setOpaque(false);

        loadResources();
        addMouseListeners();
    }

    private void loadResources() {
        try {
            URL url = getClass().getResource("/images/stage/complete_stage1.png");
            if (url != null) {
                backgroundPopup = ImageIO.read(url);
            } else {
                System.err.println("FATAL ERROR: Result Popup background not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMouseListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (continueRect.contains(p)) {
                    if (passed) {
                        dialog.showStageSelect();
                    } else {
                        dialog.restartGame();
                    }
                } else if (tryAgainRect.contains(p)) {
                    dialog.restartGame();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundPopup != null) {
            g2d.drawImage(backgroundPopup, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        }

        g2d.setFont(new Font("Arial", Font.BOLD, SCORE_FONT_SIZE));
        g2d.setColor(Color.RED);

        String scoreText = String.valueOf(finalScore);

        g2d.drawString(scoreText, SCORE_X, SCORE_Y);

        drawButton(g2d, continueRect, "Continue", passed);

        drawRestartButton(g2d, tryAgainRect);

        g2d.dispose();
    }

    private void drawButton(Graphics2D g2d, Rectangle rect, String text, boolean isContinue) {
        Color bgColor = isContinue ? new Color(216, 48, 33) : new Color(255, 102, 0);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fm = g2d.getFontMetrics();
        int tx = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int ty = rect.y + (rect.height + fm.getAscent()) / 2 - (fm.getDescent() / 2);
        g2d.drawString(text, tx, ty);
    }

    private void drawRestartButton(Graphics2D g2d, Rectangle rect) {
        g2d.setColor(new Color(255, 102, 0));
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 28));
        g2d.drawString("↻", rect.x + 10, rect.y + 35);
    }
}