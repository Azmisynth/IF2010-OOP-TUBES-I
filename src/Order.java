enum OrderState {
    ACTIVE,
    COMPLETED,
    EXPIRED
}

public class Order {
    private static int idCounter = 1;
    private int id;
    private Recipe recipe;
    private int reward;
    private int penalty;
    private int timeLeft;
    private OrderState state;

    public Order(Recipe recipe, int reward, int penalty, int timeLimit) {
        this.id = idCounter++;
        this.recipe = recipe;
        this.reward = reward;
        this.penalty = penalty;
        this.timeLeft = timeLimit;
        this.state = OrderState.ACTIVE;
    }

    public boolean tick() {
        if (state != OrderState.ACTIVE) {
            return false;
        }

        if (timeLeft > 0) {
            timeLeft--;
        }

        if (timeLeft <= 0) {
            state = OrderState.EXPIRED;
            return true;
        }

        return false;
    }

    public void markAsCompleted() {
        this.state = OrderState.COMPLETED;
    }

    public int getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public int getReward() {
        return reward;
    }

    public int getPenalty() {
        return penalty;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public OrderState getState() {
        return state;
    }

}