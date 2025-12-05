package main.java.model.item;

import java.util.LinkedList;

public class PlateStorage extends Station {
    private LinkedList<Plate> plateStack;

    public PlateStorage() {
        this.plateStack = new LinkedList<>();
    }

    public Plate pickUpPlate() {
        if (!plateStack.isEmpty() && plateStack.peek().isClean()) {
            return plateStack.pop();
        }
        return null;
    }

    public void returnDirtyPlate(Plate plate) {
        if (!plate.isClean()) {
            plateStack.push(plate);
        }
    }

    public void addPlate(Plate plate) {
        plateStack.push(plate);
    }
}