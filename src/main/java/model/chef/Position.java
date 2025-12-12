package model.chef;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Position getAdjacent(Position current, Direction direction) {
        int newX = current.getX() + direction.getX();
        int newY = current.getY() + direction.getY();

        return new Position(newX, newY);
    }
}