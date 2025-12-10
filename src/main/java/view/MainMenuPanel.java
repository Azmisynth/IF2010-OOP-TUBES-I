package main.java.view;

import main.java.controller.ChefInputListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private final GameFrame controller;
    private ChefInputListener inputHandler;
    BufferedImage backgroundImage;
    BufferedImage startGameButton;
    BufferedImage startGameButtonHover;
    BufferedImage howToPlayButton;
    BufferedImage howToPlayButtonHover;
    BufferedImage exitButton;
    BufferedImage exitButtonHover;

    public MainMenuPanel(GameFrame controller) {
        this.controller = controller;
        this.setPreferredSize(new Dimension(800, 500)); // Ukuran default
        this.setLayout(new GridBagLayout());
        try {
            this.backgroundImage = ImageIO.read(getClass().getResource("/images/main/background.png"));
            this.startGameButton = ImageIO.read(getClass().getResource("/images/main/start_game.png"));
            this.howToPlayButton = ImageIO.read(getClass().getResource("/images/main/how_to_play.png"));
            this.exitButton = ImageIO.read(getClass().getResource("/images/main/exit.png"));
            this.startGameButtonHover = ImageIO.read(getClass().getResource("/images/main/start_game_hover.png"));
            this.howToPlayButtonHover = ImageIO.read(getClass().getResource("/images/main/how_to_play_hover.png"));
            this.exitButtonHover = ImageIO.read(getClass().getResource("/images/main/exit_hover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton startButton;
        if (startGameButton != null) {
            ImageIcon defaultIcon = new ImageIcon(startGameButton);
            ImageIcon rolloverIcon = new ImageIcon(startGameButtonHover);
            startButton = new JButton(defaultIcon);
            startButton.setRolloverIcon(rolloverIcon);

            startButton.setBorderPainted(false);
            startButton.setContentAreaFilled(false);
            startButton.setFocusPainted(false);
        } else {
            startButton = new JButton("Start Game");
        }
        JButton howToButton;
        if(howToPlayButton != null) {
            ImageIcon defaultIcon = new ImageIcon(howToPlayButton);
            ImageIcon rolloverIcon = new ImageIcon(howToPlayButtonHover);
            howToButton = new JButton(defaultIcon);
            howToButton.setRolloverIcon(rolloverIcon);

            howToButton.setBorderPainted(false);
            howToButton.setContentAreaFilled(false);
            howToButton.setFocusPainted(false);
        } else {
            howToButton = new JButton("How To Play");
        }
        JButton exit;
        if (exitButton != null) {
            ImageIcon defaultIcon = new ImageIcon(exitButton);
            ImageIcon rolloverIcon = new ImageIcon(exitButtonHover);
            exit = new JButton(defaultIcon);
            exit.setRolloverIcon(rolloverIcon);

            exit.setBorderPainted(false);
            exit.setContentAreaFilled(false);
            exit.setFocusPainted(false);
        } else {
            exit = new JButton("Exit");
        }

        startButton.addActionListener(e -> controller.showStageSelect());
        howToButton.addActionListener(e -> controller.showHowToPlay());
        exit.addActionListener(e -> controller.handleExitRequest());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;

        gbc.gridy = 6; add(startButton, gbc);
        gbc.gridy = 8; add(howToButton, gbc);
        gbc.gridy = 10; add(exit, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
