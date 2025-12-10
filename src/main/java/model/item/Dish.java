package main.java.model.item;

import main.java.model.item.Ingredient;
import java.util.List;
import java.util.ArrayList;

public class Dish extends Item {
    private List<Ingredient> ingredients;

    public Dish(String name, List<Ingredient> ingredients) {
        super(name);
        this.ingredients = new ArrayList<>(ingredients);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public boolean isBurned() {
        for (Ingredient ing : ingredients) {
            if (ing.getState() == ItemState.BURNED) return true;
        }
        return false;
    }
}