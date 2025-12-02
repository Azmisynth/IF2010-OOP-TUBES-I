import java.util.List;

public class GameManager {
    private OrderManager orderManager;
    private ScoreManager scoreManager;

    private int stageTimeLeft;
    private boolean isRunning;
    private boolean isGameOver;
    private String gameOverReason;

    private Chef chefA;
    private Chef chefB;
    private Chef activeChef;

    public GameManager(int durationSeconds) {
        this.scoreManager = new ScoreManager(5);
        this.orderManager = new OrderManager(scoreManager);

        this.stageTimeLeft = durationSeconds;
        this.isRunning = false;
        this.isGameOver = false;

        Position startPosA = new Position(1, 1);
        Position startPosB = new Position(1, 2);

        this.chefA = new Chef("C001", "Chef Kebin", startPosA);
        this.chefB = new Chef("C002", "Chef Stewart", startPosB);
        this.activeChef = chefA;

        initPizzaLevel();
    }

    public void tick() {
        if (!isRunning || isGameOver) return;

        stageTimeLeft--;

        orderManager.tickOrders();

        checkGameOverConditions();
    }

    public void handleSwitchChef() {
        if (!isRunning || isGameOver) return;

        if (activeChef == chefA) {
            activeChef = chefB;
        } else {
            activeChef = chefA;
        }
    }

    private void initPizzaLevel() {
        Recipe r1 = new Recipe("Pizza Margherita", 120);
        r1.addRequirement("Adonan", "chopped");
        r1.addRequirement("Tomat", "chopped");
        r1.addRequirement("Keju", "chopped");

        Recipe r2 = new Recipe("Pizza Sosis", 150);
        r2.addRequirement("Adonan", "chopped");
        r2.addRequirement("Tomat", "chopped");
        r2.addRequirement("Keju", "chopped");
        r2.addRequirement("Sosis", "chopped");

        Recipe r3 = new Recipe("Pizza Ayam", 150);
        r3.addRequirement("Adonan", "chopped");
        r3.addRequirement("Tomat", "chopped");
        r3.addRequirement("Keju", "chopped");
        r3.addRequirement("Ayam", "chopped");

        for (int i = 0; i < 5; i++) {
            orderManager.addToQueue(r1);
            orderManager.addToQueue(r2);
            orderManager.addToQueue(r3);
        }

        orderManager.spawnOrder();
        orderManager.spawnOrder();
    }

    private void checkGameOverConditions() {
        if (stageTimeLeft <= 0) {
            isGameOver = true;
            gameOverReason = "Time's Up!";
            stopGame();
        }

        if (orderManager.getFailedCount() >= 5) {
            isGameOver = true;
            gameOverReason = "Too Many Failed Orders!";
            stopGame();
        }
    }

    public void startGame() {
        this.isRunning = true;
        this.isGameOver = false;
    }

    public void stopGame() {
        this.isRunning = false;
    }

    public int getTimeLeft() {
        return stageTimeLeft;
    }

    public int getScore() {
        return scoreManager.getCurrentScore();
    }

    public Chef getActiveChef() {
        return activeChef;
    }

    public Chef getChefA() {
        return chefA;
    }

    public Chef getChefB() {
        return chefB;
    }

    public List<Order> getActiveOrders() {
        return orderManager.getActiveOrders();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public String getGameOverReason() {
        return gameOverReason;
    }
}