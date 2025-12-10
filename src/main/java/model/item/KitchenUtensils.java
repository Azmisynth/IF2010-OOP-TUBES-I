package main.java.model.item;

import java.util.HashSet;
import java.util.Set;

public abstract class KitchenUtensils<T extends Preparable> extends Item {

    protected Set<T> contents;

    public KitchenUtensils(String name) {
        super(name);
        this.contents = new HashSet<>();
    }

    public Set<T> getContents() {
        return contents;
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public void clearContents() {}
}