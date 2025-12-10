package main.java.view;

import javax.swing.*;
import java.awt.*;

public class StageInfoDialog extends JDialog {
    private final GameFrame gameFrame;
    private boolean playConfirmed = false;

    public StageInfoDialog(GameFrame frame, String stageId) {
        super(frame, "Stage Info", true);
        this.gameFrame = frame;

        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0));

        StageInfoPanel panel = new StageInfoPanel(stageId, this);
        this.setContentPane(panel);

        this.pack();
        this.setLocationRelativeTo(frame);
    }

    public void confirmPlay() {
        this.playConfirmed = true;
        this.dispose();
    }

    public void cancelPlay() {
        this.dispose();
    }

    public boolean isPlayConfirmed() {
        return playConfirmed;
    }
}
