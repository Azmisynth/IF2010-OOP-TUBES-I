package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SettingsPanel extends JPanel {
    private final SettingsDialog dialog;

    private BufferedImage settingsPopUpImage;

    private Rectangle howToPlayRect, restartRect, backToHomepageRect, okRect, quitRect, soundRect;
    boolean howToPlayButton, restartButton, backToHomepageButton, okButton, quitButton, soundButton;
    private boolean soundHover;
    private BufferedImage soundImage;
    private BufferedImage soundHoverImage;

    public SettingsPanel(SettingsDialog dialog) {
        this.dialog = dialog;
        this.setPreferredSize(new Dimension(231, 246));
        setLayout(null);
        setOpaque(false);

        try {
            this.settingsPopUpImage = ImageIO.read(getClass().getResource("/images/map/settings_popup.png"));
            this.soundImage = ImageIO.read(getClass().getResource("/images/map/sound.png"));
            this.soundHoverImage = ImageIO.read(getClass().getResource("/images/map/sound_hover.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        howToPlayRect       = new Rectangle(372-288, 172-122, 137, 35);
        restartRect         = new Rectangle(372-288, 216-122, 137, 35);
        backToHomepageRect  = new Rectangle(299-288, 262-122, 210, 35);
        okRect              = new Rectangle(299-288, 312-122, 103, 35);
        quitRect            = new Rectangle(405-288, 312-122, 103, 35);
        soundRect           = new Rectangle(307-288, 182-122, 54, 54);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();

                boolean newHowToPlayButton = howToPlayRect.contains(p);
                boolean newRestartButton = restartRect.contains(p);
                boolean newBackToHomepageButton = backToHomepageRect.contains(p);
                boolean newOkButton = okRect.contains(p);
                boolean newQuitButton = quitRect.contains(p);
                boolean newSoundHover = soundRect.contains(p);

                if (howToPlayButton != newHowToPlayButton ||
                        restartButton != newRestartButton ||
                        backToHomepageButton != newBackToHomepageButton ||
                        okButton != newOkButton ||
                        quitButton != newQuitButton ||
                        soundButton != newSoundHover) {

                    howToPlayButton = newHowToPlayButton;
                    restartButton = newRestartButton;
                    backToHomepageButton = newBackToHomepageButton;
                    okButton = newOkButton;
                    quitButton = newQuitButton;
                    soundHover = newSoundHover;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                if (restartRect.contains(p)) {
                    dialog.restartGame();
                    return;
                }

                if (backToHomepageRect.contains(p)) {
                    dialog.dispose();
                    return;
                }

                if (okRect.contains(p)) {
                    dialog.resumeGame();
                    return;
                }

                if (quitRect.contains(p)) {
                    dialog.quitGame();
                    return;
                }

                if (soundRect.contains(p)) {
                    dialog.adjustVolume();
                    return;
                }
            }
        });
    }

    private void drawButton(Graphics2D g2, Rectangle rect, String text,
                            boolean hover, Color baseColor, Color hoverColor) {

        g2.setColor(hover ? hoverColor : baseColor);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        g2.setColor(hover ? new Color(216, 48, 33) : new Color(255, 251, 202));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);

        g2.setFont(new Font("Red Had Text", Font.BOLD, 15));
        FontMetrics fm = g2.getFontMetrics();

        int tx = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int ty = rect.y + (rect.height - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(hover ? new Color(216, 48, 33) :  new Color(255, 251, 202));
        g2.drawString(text, tx, ty);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (settingsPopUpImage != null) {
            g2.drawImage(settingsPopUpImage, 0, 0, getWidth(), getHeight(), this);
        }

        Color baseColor = new Color(216, 48, 33);
        Color hoverColor = new Color(255, 251, 202);

        drawButton(g2, howToPlayRect, "How to Play", howToPlayButton, baseColor, hoverColor);
        drawButton(g2, restartRect, "Restart", restartButton, baseColor, hoverColor);
        drawButton(g2, backToHomepageRect, "Back to Homepage", backToHomepageButton, baseColor, hoverColor);
        drawButton(g2, okRect, "Ok", okButton, baseColor, hoverColor);
        drawButton(g2, quitRect, "Quit", quitButton, baseColor, hoverColor);

        if (soundHover) {
            g2.drawImage(soundHoverImage, 307-288, 182-122, 54, 54, null);
        } else {
            g2.drawImage(soundImage, 307-288, 182-122, 54, 54, null);
        }
        g2.dispose();
    }
}
