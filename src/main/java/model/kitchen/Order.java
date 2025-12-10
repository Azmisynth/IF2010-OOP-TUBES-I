package main.java.model.kitchen;

public class Order {
    private Recipe recipe;
    private int maxTime; // Durasi total
    private int timeLeft; // Sisa waktu

    public Order(Recipe recipe, int durationSeconds) {
        this.recipe = recipe;
        this.maxTime = durationSeconds;
        this.timeLeft = durationSeconds;
    }

    public void tick() {
        if (timeLeft > 0) {
            timeLeft--;
        }
    }

    public boolean isExpired() {
        return timeLeft <= 0;
    }

    public Recipe getRecipe() { return recipe; }
    public int getTimeLeft() { return timeLeft; }
    public int getMaxTime() { return maxTime; }

    public float getProgress() {
        return (float) timeLeft / maxTime;
    }
}