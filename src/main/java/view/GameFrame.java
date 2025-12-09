package main.java.view;

import main.java.controller.ChefInputListener;
import main.java.model.chef.ChefPlayer;
import main.java.model.chef.Position;
import main.java.model.map.Map;
import main.java.model.map.PizzaMap;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    private final List<ChefPlayer> allChefs;
    private int activeChefIndex;
    private ChefPlayer activeChef;
    private Map gameMap;

    public GameFrame(List<ChefPlayer> allChefs, Map gameMap) {
        this.allChefs = allChefs;
        this.activeChefIndex = 0;
        if (!allChefs.isEmpty()) {
            allChefs.get(activeChefIndex).activate();
            for (int i = 1; i < allChefs.size(); i++) {
                allChefs.get(i).deactivate();
            }
        }
        this.gameMap = gameMap;
        setTitle("KrustyCooked");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        showMainMenu();

        setVisible(true);
    }

    public void showMainMenu() {
        MainMenuPanel mainMenu = new MainMenuPanel(this);
        switchPanel((JPanel) mainMenu);
    }

    public void showStageSelect() {
        StageSelectPanel stageSelect = new StageSelectPanel(this);
        switchPanel(stageSelect);
    }

    private void showStageInfoPopup(String stageId) {
        if(stageId.equals("Stage1")) {
            String targetTime = "90 detik";
//            String targetScore = "500 poin";

            String message = String.format("Target Waktu: %s\n", targetTime);

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Stage Loaded: " + stageId,
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void startGame(String stageId) {
        if(stageId.equals("Stage1")) {
            DetailStagePanel screen = new DetailStagePanel("Stage1");
            switchPanel(screen);
            StageInfoDialog dialog = new StageInfoDialog(this, stageId);
            dialog.setVisible(true);
            if (dialog.isPlayConfirmed()) {
                MapViewPanel gameView = new MapViewPanel(gameMap, allChefs);
                ChefInputListener inputHandler = new ChefInputListener(allChefs, gameMap, gameView, this);
                gameView.addKeyListener(inputHandler);

                switchPanel(gameView);

                gameView.setFocusable(true);
                gameView.requestFocusInWindow();
            } else {
                showStageSelect();
            }
        }
    }

    public void showHowToPlay() {
        HowToPlayPanel howToPlayView = new HowToPlayPanel(this);
        switchPanel(howToPlayView);
    }

    public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public void handleExitRequest() {
        ExitConfirmDialog exitDialog = new ExitConfirmDialog(this);
        exitDialog.setVisible(true);

        if (exitDialog.isExitConfirmed()) {
            System.exit(0);
        }
    }

    public void changeActiveChef() {
        if (allChefs.isEmpty()) return;
        ChefPlayer oldChef = allChefs.get(activeChefIndex);
        oldChef.deactivate();

        activeChefIndex = (activeChefIndex + 1) % allChefs.size();

        ChefPlayer newChef = allChefs.get(activeChefIndex);
        newChef.activate();

        System.out.println("Switched control to: " + newChef.getName());
    }

    public static void main(String[] args) {
        PizzaMap config = new PizzaMap();
        List<Position> chefPositions = config.getChefPositions();
        if (chefPositions.size() < 2) {
            System.err.println("FATAL ERROR: Only " + chefPositions.size() + " spawn points found. Minimum 2 required.");
            return;
        }
        ChefPlayer chef1 = new ChefPlayer("C1", "Kebin", chefPositions.get(0));
        ChefPlayer chef2 = new ChefPlayer("C2", "Stewart", chefPositions.get(1));
        List<ChefPlayer> allChefs = new ArrayList<>(List.of(chef1, chef2));
        Map gameMap = new Map(config, allChefs);
        GameFrame gameController = new GameFrame(allChefs, gameMap);
    }
}