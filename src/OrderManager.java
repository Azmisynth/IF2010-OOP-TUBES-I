import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;

public class OrderManager {
    private List<Order> activeOrders;
    private Queue<Recipe> orderQueue;
    private ScoreManager scoreManager;

    private final int MAX_ACTIVE_ORDERS = 4;
    private int failedCount;

    public OrderManager(ScoreManager scoreManager) {
        this.activeOrders = new LinkedList<>();
        this.orderQueue = new LinkedList<>();
        this.scoreManager = scoreManager;
        this.failedCount = 0;
    }

    public void addToQueue(Recipe recipe) {
        if (recipe != null) {
            orderQueue.add(recipe);
        }
    }

    public void spawnOrder() {
        if (activeOrders.size() < MAX_ACTIVE_ORDERS && !orderQueue.isEmpty()) {
            Recipe nextRecipe = orderQueue.poll();

            int reward = nextRecipe.getScoreValue();
            int penalty = 50;
            int timeLimit = 60;

            Order newOrder = new Order(nextRecipe, reward, penalty, timeLimit);
            activeOrders.add(newOrder);

            System.out.println("[SPAWN] Order Baru Muncul: " + newOrder.getRecipe().getName());
        }
    }

    private void completeOrder(Order order) {
        if (order.getState() != OrderState.ACTIVE) {
            return;
        }

        order.markAsCompleted();

        scoreManager.addScore(order.getReward());
        System.out.println("[SUCCESS] Order " + order.getRecipe().getName() + " Selesai! (+" + order.getReward() + " pts)");

        activeOrders.remove(order);

        failedCount = 0;

        spawnOrder();
    }

    public boolean processDelivery(Dish servedDish) {
        if (servedDish == null) return false;

        for (Order order : activeOrders) {
            if (order.getRecipe().matches(servedDish) && order.getState() == OrderState.ACTIVE) {
                completeOrder(order);
                return true;
            }
        }
        return false;
    }

    public void tickOrders() {
        Iterator<Order> iterator = activeOrders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();

            boolean isJustExpired = order.tick();

            if (isJustExpired) {
                System.out.println("[EXPIRED] Waktu habis untuk: " + order.getRecipe().getName());

                scoreManager.applyPenalty(order.getPenalty());

                failedCount++;
                System.out.println(">> Failed Streak: " + failedCount);

                iterator.remove();
                spawnOrder();
            }
        }
    }

    public void resetOrder() {
        activeOrders.clear();
        orderQueue.clear();
        failedCount = 0;
        System.out.println("[INFO] OrderManager has been reset.");
    }

    public int getFailedCount() {
        return failedCount;
    }

    public List<Order> getActiveOrders() {
        return activeOrders;
    }
}