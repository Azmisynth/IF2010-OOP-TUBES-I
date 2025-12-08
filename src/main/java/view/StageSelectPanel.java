package main.java.view;

import javax.swing.*;
import java.awt.*;

public class StageSelectPanel extends JPanel {
    private final GameFrame controller;

    public StageSelectPanel(GameFrame controller) {
        this.controller = controller;
        this.setPreferredSize(new Dimension(800, 500));
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        add(new JLabel("CHOOSE STAGE:"));

        // Contoh Stage 1
        JButton stage1Button = new JButton("Stage 1 (Target Time: 90 s)");
        stage1Button.addActionListener(e -> controller.startGame("Stage1"));
        add(stage1Button);

        // Tombol kembali
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> controller.showMainMenu());
        add(backButton);
    }
}