package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DetailStagePanel extends JPanel {
    private BufferedImage mapPreview;

    public DetailStagePanel(String stageId) {
        this.setPreferredSize(new Dimension(800, 500));
        this.setLayout(new BorderLayout());
        try {
            this.mapPreview = ImageIO.read(getClass().getResource("/images/stage/preview_map.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load image preview");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (mapPreview != null) {
            g2d.drawImage(mapPreview, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
