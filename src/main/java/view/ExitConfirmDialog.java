package main.java.view;

import javax.swing.*;
import java.awt.*;

public class ExitConfirmDialog extends JDialog {
    private boolean confirmedExit = false;

    public ExitConfirmDialog(Frame owner) {
        super(owner, "Exit Confirmation", true);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        setContentPane(new ExitConfirmPanel(this));

        pack();
        setLocationRelativeTo(owner);
    }

    public void confirmExit() {
        this.confirmedExit = true;
        dispose();
    }

    public void cancelExit() {
        dispose();
    }

    public boolean isExitConfirmed() {
        return confirmedExit;
    }
}
