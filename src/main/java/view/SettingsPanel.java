package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class SettingsPanel extends JPanel {
    private final SettingsDialog dialog;

    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;
    private static final int PANEL_WIDTH = 231;
    private static final int PANEL_HEIGHT = 246;

    private BufferedImage settingsPopUpImage, howToPlayImage, restartImage, backToHomepageImage, okImage, quitImage, soundImage;
    private BufferedImage settingsPopUpHover, howToPlayHover, restartHover, backToHomepageHover, okHover, quitHover, soundHover;
    private Rectangle settingsPopUpRect, howToPlayRect, restartRect, backToHomepageRect, okRect, quitRect, soundRect;

    public SettingsPanel(SettingsDialog dialog) {
        this.dialog = dialog;
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setLayout(null);
        this.setOpaque(false);

        settingsPopUpRect = new Rectangle(288, 122, PANEL_WIDTH, PANEL_HEIGHT);

        try {
            this.settingsPopUpImage = ImageIO.read(getClass().getResource("/images/map/settings_popup.png"));
            this.howToPlayImage = ImageIO.read(getClass().getResource("/images/map/how_to_play.png"));
            this.restartImage = ImageIO.read(getClass().getResource("/images/map/restart.png"));
            this.backToHomepageImage = ImageIO.read(getClass().getResource("/images/map/back_to_homepage.png"));
            this.okImage = ImageIO.read(getClass().getResource("/images/map/ok.png"));
            this.quitImage = ImageIO.read(getClass().getResource("/images/map/quit.png"));
            this.soundImage = ImageIO.read(getClass().getResource("/images/map/sound.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int buttonY = 350;
        okRect = new Rectangle(50, buttonY, 120, BUTTON_HEIGHT);
        quitRect = new Rectangle(180, buttonY, 120, BUTTON_HEIGHT);


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (okRect.contains(p)) {
                    dialog.resumeGame();
                } else if (quitRect.contains(p)) {
                    dialog.quitGame(); // Keluar
                }
                // ... Tambahkan kondisi untuk tombol Restart, Volume, dll. ...
            }
        });
    }

    // Method drawButton (seperti yang Anda gunakan di ExitConfirmPanel)
    private void drawButton(Graphics2D g2, Rectangle rect, String text, Color bgColor, Color textColor) {
        // Implementasi logika gambar tombol (fillRoundRect, drawString)
        g2.setColor(bgColor);
        g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        g2.setColor(textColor);
        g2.setFont(new Font("Poppins", Font.BOLD, 18));

        // Rata Tengah Teks
        FontMetrics fm = g2.getFontMetrics();
        int tx = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int ty = rect.y + (rect.height + fm.getAscent()) / 2 - (fm.getDescent() / 2);
        g2.drawString(text, tx, ty);
    }

    private void addSettingsButtonListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean hover = settingsButtonRect.contains(e.getPoint());
                if (hover != hoveringSettings) {
                    hoveringSettings = hover;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (settingsButtonRect.contains(e.getPoint())) {
                    if (gameFrame != null) {
                        gameFrame.showSettingsMenu();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Background Panel Utama (Krem)
        g2d.setColor(new Color(250, 240, 210, 240));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // 2. Header Merah (Settings)
        g2d.setColor(new Color(216, 48, 33));
        g2d.fillRoundRect(20, 0, getWidth() - 40, 70, 30, 30);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Poppins", Font.BOLD, 24));
        g2d.drawString("Settings", (getWidth() - g2d.getFontMetrics().stringWidth("Settings")) / 2, 45);

        // 3. Gambar Tombol OK dan QUIT
        drawButton(g2d, okRect, "Ok", new Color(216, 48, 33), Color.WHITE);
        drawButton(g2d, quitRect, "Quit", new Color(255, 251, 202), Color.RED);

        // ... Gambar Tombol Volume, Restart, dll. ...

        g2d.dispose();
    }
}