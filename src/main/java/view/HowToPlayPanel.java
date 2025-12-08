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
//        JButton backButton = new JButton("Back to Main Menu");
//        backButton.addActionListener(e -> controller.showMainMenu());

        String linkText = "<html><u>&lt; Back to Homepage</u></html>";
        JLabel backToMenuLabel = new JLabel(linkText);

        backToMenuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        backToMenuLabel.setForeground(Color.WHITE);
        backToMenuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(backButton);

        backToMenuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.showMainMenu();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backToMenuLabel.setForeground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backToMenuLabel.setForeground(Color.WHITE);
            }
        });
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30));
//        linkPanel.setBackground(Color.WHITE);
        linkPanel.setOpaque(false);
        linkPanel.add(backToMenuLabel);
        backToMenuLabel.setOpaque(false);
        this.add(linkPanel, BorderLayout.SOUTH);
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
