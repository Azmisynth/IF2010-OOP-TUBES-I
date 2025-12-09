package main.java.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HowToPlayPanel extends JPanel {
    private BufferedImage howToPlayScreen;
    public HowToPlayPanel(GameFrame controller) {
        this.setPreferredSize(new Dimension(800, 500));
        this.setLayout(new BorderLayout());
        try {
            howToPlayScreen = ImageIO.read(getClass().getResource("/images/main/how_to_play_screen.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutlineLabel backLabel = new OutlineLabel("< Back to Homepage");

        backLabel.setFont(new Font("Red Hat Text", Font.BOLD, 16));
        backLabel.setForeground(Color.WHITE);
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                controller.showMainMenu();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(Color.WHITE);
            }
        });
//        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30));
//        linkPanel.setOpaque(false);
//        linkPanel.add(backToMenuLabel);
//        backToMenuLabel.setOpaque(false);
//        this.add(linkPanel, BorderLayout.SOUTH);
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (howToPlayScreen != null) {
            g2d.drawImage(howToPlayScreen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
