package model.kitchen;

import model.item.Ingredient;
import model.item.Preparable;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.Random;

public class RecipeBook {
    private static List<Recipe> recipes = new ArrayList<>();
    private static Random random = new Random();

    static {
        recipes.add(new Recipe("Pizza Margherita", Arrays.asList("Dough", "Tomato", "Cheese"), 100, 50));

        recipes.add(new Recipe("Pizza Sosis", Arrays.asList("Dough", "Tomato", "Cheese", "Sausage"), 150, 75));

        recipes.add(new Recipe("Pizza Ayam", Arrays.asList("Dough", "Tomato", "Cheese", "Chicken"),150, 75));
    }

    public static Recipe findRecipe(Set<Preparable> contents) {
        for (Recipe recipe : recipes) {
            if (recipe.matches(contents)) {
                return recipe;
            }
        }
        return null;
    }

    public static Recipe getRandomRecipe() {
        return recipes.get(random.nextInt(recipes.size()));
    }
}