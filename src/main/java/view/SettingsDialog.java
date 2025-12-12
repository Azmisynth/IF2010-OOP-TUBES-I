package view;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private GameFrame gameFrame;

    public SettingsDialog(GameFrame gameFrame) {
        super(gameFrame, "Settings", true);
        this.gameFrame = gameFrame;

        // Setup Dialog
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());

        SettingsPanel panel = new SettingsPanel(this);
        this.add(panel, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(gameFrame);
    }

    public void resumeGame() {
        this.setVisible(false);
        this.dispose();
        gameFrame.resumeGame();
    }

    public void restartGame() {
        this.dispose();
        gameFrame.startGame("Stage1");
    }

    public void quitGame() {
        System.exit(0);
    }
}