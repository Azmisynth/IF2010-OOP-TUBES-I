package view;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private GameFrame gameFrame;

    public SettingsDialog(GameFrame gameFrame) {
        super(gameFrame, "Settings", true);
        this.gameFrame = gameFrame;

        setUndecorated(true);
        setLayout(new BorderLayout());

        SettingsPanel panel = new SettingsPanel(this);
        add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(gameFrame);
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

    public void adjustVolume() {
        gameFrame.adjustVolume();
    }
}