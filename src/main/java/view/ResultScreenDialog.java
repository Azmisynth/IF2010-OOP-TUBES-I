package view;

import javax.swing.*;
import java.awt.*;

public class ResultScreenDialog extends JDialog {
    private final GameFrame gameFrame;

    public ResultScreenDialog(GameFrame gameFrame, int score, boolean passed) {
        // Modal: true - Memblokir interaksi di GameFrame
        super(gameFrame, "Stage Result", true);
        this.gameFrame = gameFrame;

        this.setUndecorated(true);
        this.setLayout(new BorderLayout());

        ResultScreenPanel panel = new ResultScreenPanel(this, score, passed);
        this.add(panel, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(gameFrame);
    }

    public void showStageSelect() {
        this.dispose();
        gameFrame.showStageSelect();
    }

    public void restartGame() {
        this.dispose();
        gameFrame.startGame("Stage1");
    }

    // Dipanggil saat tombol 'Try Again' diklik
//    public void tryAgain() {
//        this.dispose();
//        gameFrame.restart();
//    }
}