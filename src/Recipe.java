import java.util.ArrayList;
import java.util.List;

class IngredientRequirement {
    String name;
    String requiredState;

    public IngredientRequirement(String name, String requiredState) {
        this.name = name;
        this.requiredState = requiredState;
    }

    public boolean isMatch(Ingredient ingredient) {
        if (ingredient == null) return false;

        boolean nameMatch = this.name.equalsIgnoreCase(ingredient.getName());
        boolean stateMatch = this.requiredState.equalsIgnoreCase(ingredient.getState());

        return nameMatch && stateMatch;
    }
}

public class Recipe {
    private String name;
    private int scoreValue;
    private List<IngredientRequirement> requirements;

    public Recipe(String name, int scoreValue) {
        this.name = name;
        this.scoreValue = scoreValue;
        this.requirements = new ArrayList<>();
    }

    public void addRequirement(String ingredientName, String stateValue) {
        requirements.add(new IngredientRequirement(ingredientName, stateValue));
    }

    public boolean matches(Dish dish) {
        if (dish == null) return false;
        return this.name.equalsIgnoreCase(dish.getName());
    }

    public boolean validateIngredients(List<Ingredient> ingredients) {
        if (ingredients.size() != requirements.size()) {
            return false;
        }

        int matchCount = 0;

        for (IngredientRequirement req : requirements) {
            boolean found = false;

            for (Ingredient ing : ingredients) {
                if (req.isMatch(ing)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                matchCount++;
            }
        }

        return matchCount == requirements.size();
    }

    public String getName() {
        return name;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}