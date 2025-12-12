package model.chef;

import model.map.Map;

public interface Moveable {
    public void moveUp(Map map);
    public void moveDown(Map map);
    public void moveLeft(Map map);
    public void moveRight(Map map);
}