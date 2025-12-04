package main.java.view;

import main.java.controller.ChefInputListener;
import main.java.model.chef.ChefPlayer;
import main.java.model.chef.Position;
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
        MapType config = new PizzaMap();
        ChefPlayer chefA = new ChefPlayer("C1", "Kebin", new Position(8, 2));
        ChefPlayer chefB = new ChefPlayer("C2", "Stewart", new Position(5, 7));
        List<ChefPlayer> allChefs = new ArrayList<>(List.of(chefA, chefB));
        //main.java.model.map.Map gameMap = new main.java.model.map.Map(config, chefA);
        Map gameMap = new Map(config, allChefs);
        GameFrame gameController = new GameFrame(allChefs);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nimonscooked");

            MapViewPanel gameView = new MapViewPanel(gameMap, allChefs);
            //MapViewPanel gameView = new MapViewPanel(gameMap, chefA);
//            ChefInputListener inputListener = new ChefInputListener(chefA, gameMap, gameView);
            ChefInputListener inputListener = new ChefInputListener(allChefs, gameMap, gameView, gameController);
            frame.add(gameView);

            frame.pack();
            gameView.addKeyListener(inputListener);
            gameView.setFocusable(true);
            gameView.requestFocusInWindow();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        });
    }
}