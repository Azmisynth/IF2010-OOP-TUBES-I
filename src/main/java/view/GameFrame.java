package main.java.view;

import main.java.controller.ChefInputListener;
import main.java.model.chef.ChefPlayer;
import main.java.model.chef.Position;
import main.java.model.kitchen.OrderManager;
import main.java.model.map.Map;
import main.java.model.map.MapType;
import main.java.model.map.PizzaMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.ArrayList;

public class GameFrame {
    private final List<ChefPlayer> allChefs;
    private int activeChefIndex;
    private ChefPlayer activeChef;
    private MapViewPanel gameView;

    public GameFrame(List<ChefPlayer> allChefs) {
        this.allChefs = allChefs;
        this.activeChefIndex = 0;
        if (!allChefs.isEmpty()) {
            allChefs.get(activeChefIndex).activate();
            for (int i = 1; i < allChefs.size(); i++) {
                allChefs.get(i).deactivate();
            }
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
//        MapType config = new PizzaMap();
        List<Position> chefPositions = config.getChefPositions();
        if (chefPositions.size() < 2) {
            System.err.println("FATAL ERROR: Only " + chefPositions.size() + " spawn points found. Minimum 2 required.");
            return;
        }
        ChefPlayer chef1 = new ChefPlayer("C1", "Kebin", chefPositions.get(0));
        ChefPlayer chef2 = new ChefPlayer("C2", "Stewart", chefPositions.get(1));
        List<ChefPlayer> allChefs = new ArrayList<>(List.of(chef1, chef2));
        //main.java.model.map.Map gameMap = new main.java.model.map.Map(config, chef1);
        Map gameMap = new Map(config, allChefs);
        GameFrame gameController = new GameFrame(allChefs);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nimonscooked");

            MapViewPanel gameView = new MapViewPanel(gameMap, allChefs); // untuk sekarang baru bisa sampe kaya gini, belum bisa switch chef soalnya
            //MapViewPanel gameView = new MapViewPanel(gameMap, chef1);
//            ChefInputListener inputListener = new ChefInputListener(chef1, gameMap, gameView);
            ChefInputListener inputListener = new ChefInputListener(allChefs, gameMap, gameView, gameController);
            frame.add(gameView);

            frame.pack();
            gameView.addKeyListener(inputListener);
            gameView.setFocusable(true);
            gameView.requestFocusInWindow();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            javax.swing.Timer timer = new javax.swing.Timer(16, e -> {
                gameView.refreshView();
            });
            timer.start();

            javax.swing.Timer logicTimer = new javax.swing.Timer(1000, e -> {
                OrderManager.getInstance().updateOrders();
            });
            logicTimer.start();
        });
    }
}