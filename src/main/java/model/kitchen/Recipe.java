package main.java.model.kitchen;

import main.java.model.item.Dish;
import main.java.model.item.Ingredient;
import main.java.model.item.ItemState;
import main.java.model.item.Preparable;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Recipe {
    private String name;
    private List<String> requiredIngredients;
    private int reward;
    private int penalty;

    public Recipe(String name, List<String> ingredients, int reward, int penalty) {
        this.name = name;
        this.requiredIngredients = ingredients;
        this.reward = reward;
        this.penalty = penalty;
    }

    public String getName() {
        return name;
    }

    public int getReward() {
        return reward;
    }

    public int getPenalty() {
        return penalty;
    }

    public List<String> getRequiredIngredients() {
        return requiredIngredients;
    }

    public boolean matches(Set<Preparable> plateContents) {
        if (plateContents.size() != requiredIngredients.size()) return false;
        int matchCount = 0;
        List<String> tempReqs = new ArrayList<>(requiredIngredients);
        for (Preparable prep : plateContents) {
            if (prep instanceof Ingredient) {
                Ingredient ing = (Ingredient) prep;

                if (ing.getState() != ItemState.COOKED && ing.getState() != ItemState.CHOPPED) return false;

                if (tempReqs.contains(ing.getName())) {
                    tempReqs.remove(ing.getName());
                    matchCount++;
                }
            }
        }
        return matchCount == requiredIngredients.size();
    }
}