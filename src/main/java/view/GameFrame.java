package main.java.view;

import main.java.controller.ChefInputListener;
import main.java.model.chef.ChefPlayer;
import main.java.model.chef.Position;
import main.java.model.map.Map;
import main.java.model.map.MapType;
import main.java.model.map.PizzaMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameFrame {
    public static void main(String[] args) {
        MapType config = new PizzaMap();
        ChefPlayer chefA = new ChefPlayer("C1", "Kebin", new Position(5, 5));
//        Chef chefB = new Chef("C2", "Stewart", new Position(10, 8));
//        List<Chef> allChefs = List.of(chefA, chefB);
        //main.java.model.map.Map gameMap = new main.java.model.map.Map(config, chefA);
        Map gameMap = new Map(config, chefA);
        chefA.activate();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nimonscooked");

            // MapViewPanel gameView = new MapViewPanel(gameMap, allChefs);
            MapViewPanel gameView = new MapViewPanel(gameMap, chefA);
            ChefInputListener inputListener = new ChefInputListener(chefA, gameMap, gameView);
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