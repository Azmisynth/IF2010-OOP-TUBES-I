package main.java.controller;

import main.java.model.chef.ChefPlayer;
import main.java.model.map.Map;
import main.java.view.MapViewPanel;
import java.awt.event.KeyEvent;
import main.java.view.GameFrame;
import java.awt.event.KeyListener;
import java.util.List;

public class ChefInputListener implements KeyListener {
    private List <ChefPlayer> allChefs;
    private final Map gameMap;
    private final MapViewPanel gameView;
    private final GameFrame gameController;

    public ChefInputListener(List<ChefPlayer> allChefs, Map map, MapViewPanel view, GameFrame gameController) {
        this.allChefs = allChefs;
        this.gameMap = map;
        this.gameView = view;
        this.gameController = gameController;
    }

    private ChefPlayer getActiveChef() {
        for (ChefPlayer chef : allChefs) {
            if (chef.isActive()) {
                return chef;
            }
        }
        return null; // tidak ada chef yang aktif
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ChefPlayer activeChef = getActiveChef();
        if (activeChef == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_B:
                gameController.changeActiveChef();
                gameView.refreshView();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                activeChef.moveUp(gameMap);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                activeChef.moveDown(gameMap);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                activeChef.moveLeft(gameMap);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                activeChef.moveRight(gameMap);
                break;
        }

        gameView.refreshView();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}