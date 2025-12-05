package main.java.model.chef;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static Position getAdjacent(Position current, Direction direction) {
        double newX = current.getX() + direction.getX();
        double newY = current.getY() + direction.getY();

        return new Position(newX, newY);
    }
}