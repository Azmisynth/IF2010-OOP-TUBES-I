package main.java.view;

import javax.swing.*;
import java.awt.*;

public class ResultScreenPanel extends JPanel {
    public ResultScreenPanel(GameFrame controller, int score, int successfulOrders, int failedOrders, boolean passed) {
        this.setPreferredSize(new Dimension(800, 500));
        this.setLayout(new GridLayout(7, 1));

        String status = passed ? "🎉 STAGE CLEARED! (PASS)" : "❌ GAME OVER (FAIL)";

        add(new JLabel("--- RESULT SCREEN ---", SwingConstants.CENTER));
        add(new JLabel("Status: " + status, SwingConstants.CENTER));
        add(new JLabel("--------------------------", SwingConstants.CENTER));
        add(new JLabel("Total Score: " + score, SwingConstants.CENTER));
        add(new JLabel("Successful Orders: " + successfulOrders, SwingConstants.CENTER));
        add(new JLabel("Failed Orders: " + failedOrders, SwingConstants.CENTER));

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> controller.showMainMenu());
        add(backButton);
    }
}