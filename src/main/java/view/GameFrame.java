package view;

import controller.ChefInputListener;
import helper.SoundPlayer;
import model.chef.ChefPlayer;
import model.chef.Position;
import model.kitchen.OrderManager;
import model.map.Map;
import model.map.PizzaMap;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    private final List<ChefPlayer> allChefs;
    private int activeChefIndex;
    private ChefPlayer activeChef;
    private Map gameMap;
    private MapViewPanel gameView;
    private SoundPlayer bgmPlayer;
    private SettingsDialog settingsDialog;
    private javax.swing.Timer gameTimer;

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
        bgmPlayer = new SoundPlayer("/sound/bgm.wav");
        bgmPlayer.setVolume(0.3);
        bgmPlayer.loop();
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

    public void startGame(String stageId) {
        DetailStagePanel screen = new DetailStagePanel(stageId);
        switchPanel(screen);

        StageInfoDialog dialog = new StageInfoDialog(this, stageId);
        dialog.setVisible(true);

        if (!dialog.isPlayConfirmed()) {
            showStageSelect();
            return;
        }

        int level = 1; // Default
        switch (stageId) {
            case "Stage1": level = 1; break;
            case "Stage2": level = 2; break;
            case "Stage3": level = 3; break;
            case "Stage4": level = 4; break;
        }

        OrderManager.getInstance().setLevelDifficulty(level);
        OrderManager.getInstance().startGame();

        MapViewPanel gameView = new MapViewPanel(gameMap, allChefs, this);
        ChefInputListener inputHandler =
                new ChefInputListener(allChefs, gameMap, gameView, this);
        SwingUtilities.invokeLater(() -> {
            gameView.addKeyListener(inputHandler);
            switchPanel(gameView);
            gameView.setFocusable(true);
            gameView.requestFocusInWindow();
            gameTimer = new javax.swing.Timer(16, e -> {
                gameView.refreshView();

                if (OrderManager.getInstance().isGameOver() ||
                        OrderManager.getInstance().isStageCleared()) {
                    gameTimer.stop();
                    showStageSelect();
                }
            });
            gameTimer.start();

        });

    }

    public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public void pauseGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    public void resumeGame() {
        if (gameTimer != null && !gameTimer.isRunning()) {
            gameTimer.start();
        }
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

    public void showSettingsMenu() {
        // 1. Pause game loop
        this.pauseGame();

        SettingsDialog dialog = new SettingsDialog(this);
        dialog.setVisible(true);
    }


    public void adjustVolume() {
        if(bgmPlayer.getVolume() == 0) {
            bgmPlayer.setVolume(0.3);
        } else bgmPlayer.setVolume(0);
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