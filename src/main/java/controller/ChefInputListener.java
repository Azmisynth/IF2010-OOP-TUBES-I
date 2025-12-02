package main.java.controller;

import main.java.model.chef.ChefPlayer;
import main.java.model.map.Map;
import main.java.view.MapViewPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChefInputListener implements KeyListener {
    private final ChefPlayer chefModel;
    private final Map gameMap;
    private final MapViewPanel gameView;

    public ChefInputListener(ChefPlayer chef, Map map, MapViewPanel view) {
        this.chefModel = chef;
        this.gameMap = map;
        this.gameView = view;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!chefModel.isActive()) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                chefModel.moveUp(gameMap);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                chefModel.moveDown(gameMap);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                chefModel.moveLeft(gameMap);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                chefModel.moveRight(gameMap);
                break;
        }

        gameView.refreshView();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}