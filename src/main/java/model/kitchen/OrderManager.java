package model.kitchen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import model.item.Ingredient;
import model.item.Preparable;
import model.item.ItemState;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OrderManager implements Runnable{
    private int failedOrdersCount;
    private int timeRemaining;
    private int spawnTimer;
    private int score;
    private int ordersSpawnedCount;
    private boolean isTimeFrozen = false;

    private int orderDuration;
    private int maxFailedOrders;
    private int targetScore;

    private List<Order> activeOrders;
    private Timer gameLoopTimer;

    private volatile boolean isGameOver;
    private volatile boolean isStageCleared;
    private volatile boolean isRunning;

    private static final int MAX_ACTIVE_ORDERS = 3;
    private static final int SPAWN_INTERVAL = 15;
    private static final int GAME_DURATION_SECONDS = 10;

    private Thread gameThread;
    private GameStatusListener statusListener;

    private OrderManager() {
        this.activeOrders = new CopyOnWriteArrayList<>();
    }

    private static class OrderManagerHolder {
        private static final OrderManager instance = new OrderManager();
    }

    public static OrderManager getInstance() {
        return OrderManagerHolder.instance;
    }

    public void setLevelDifficulty(int level) {
        switch (level) {
            case 1:
                this.orderDuration = 120;
                this.maxFailedOrders = 5;
                this.targetScore = 400;
                break;
            case 2:
                this.orderDuration = 100;
                this.maxFailedOrders = 4;
                this.targetScore = 700;
                break;
            case 3:
                this.orderDuration = 80;
                this.maxFailedOrders = 3;
                this.targetScore = 1000;
                break;
            case 4:
                this.orderDuration = 90;
                this.maxFailedOrders = 3;
                this.targetScore = 800;
                break;

        }
    }

    public void setStatusListener(GameStatusListener listener) {
        this.statusListener = listener;
    }

    public void startGame() {
        stopGameLoop();
        this.score = 0;
        this.failedOrdersCount = 0;
        this.timeRemaining = GAME_DURATION_SECONDS;
        this.spawnTimer = SPAWN_INTERVAL;
        this.isGameOver = false;
        this.isStageCleared = false;

        this.activeOrders.clear();

        addOrder();
        this.ordersSpawnedCount = 1;

        this.isRunning = true;
        this.gameThread = new Thread(this, "GameLogicThread");
        this.gameThread.start();
    }

    public synchronized void stopGameLoop() {
        isRunning = false;
        if (gameThread != null) {
            try {
                gameThread.join(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        while (isRunning) {
            try {
                tick();
                // Cek Kondisi Berhenti (Game Over / Menang)
                if (isGameOver || isStageCleared) {
                    isRunning = false; // Keluar loop
                }

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("Thread interrupted!");
                isRunning = false;
            }
        }
        System.out.println("GAME THREAD STOPPED.");
    }

    private void tick() {
        if (isGameOver || isStageCleared) return;
        if (isTimeFrozen) {
            return;
        }
        // Update Waktu Game
        if (timeRemaining > 0) {
            timeRemaining--;
        } else {
            checkWinCondition(); // Waktu habis, cek menang/kalah
            return;
        }

        // Update Spawn Timer (Muncul tiap 15 detik hanya untuk awalan saja)
        if (ordersSpawnedCount < 3) {
            if (activeOrders.size() < MAX_ACTIVE_ORDERS) {
                spawnTimer--;
                if (spawnTimer <= 0) {
                    addOrder();
                    ordersSpawnedCount++; // Nambah counter
                    spawnTimer = SPAWN_INTERVAL; // Reset timer
                    System.out.println("SPAWN FASE 1 (" + ordersSpawnedCount + "/3)");
                }
            }
        } else {
            // Jika slot kosong, langsung isi tanpa menunggu timer
            if (activeOrders.size() < MAX_ACTIVE_ORDERS) {
                addOrder();
                System.out.println("SPAWN FASE 2 (Instant Refill)");
            }
        }

        // Update Order Aktif (Durasi order berjalan)
        Iterator<Order> iterator = activeOrders.iterator();
        boolean needNewOrder = false;

        while (iterator.hasNext()) {
            Order order = iterator.next();
            order.tick();

            if (order.isExpired()) {
                System.out.println("ORDER EXPIRED: " + order.getRecipe().getName());
                addScore(-order.getRecipe().getPenalty());
                activeOrders.remove(order);

                incrementFailedOrder();
            }
        }
    }

    public void addOrder() {
        if (activeOrders.size() < MAX_ACTIVE_ORDERS) {
            Recipe r = RecipeBook.getRandomRecipe();
            Order newOrder = new Order(r, this.orderDuration);
            activeOrders.add(newOrder);
            System.out.println("NEW ORDER: " + r.getName());
        }
    }

    // Logic Validasi Serving
    public boolean deliverOrder(Set<Preparable> plateContents) {
        for (Preparable prep : plateContents) {
            if (prep instanceof Ingredient) {
                Ingredient ing = (Ingredient) prep;

                // Cek Gosong
                if (ing.getState() == ItemState.BURNED) {
                    System.out.println("SERVE GAGAL: Makanan GOSONG tidak boleh disajikan!");
                    return false;
                }

                // Cek Mentah
                if (ing.getState() != ItemState.COOKED) {
                    System.out.println("SERVE GAGAL: Makanan belum matang! (" + ing.getName() + " masih " + ing.getState() + ")");
                    return false;
                }
            }
        }

        Recipe cookedRecipe = RecipeBook.findRecipe(plateContents);

        if (cookedRecipe == null) {
            System.out.println("SERVE GAGAL: Masakan tidak valid.");
            return false;
        }

        // Cek Order Aktif
        for (int i = 0; i < activeOrders.size(); i++) {
            Order order = activeOrders.get(i);

            if (order.getRecipe().getName().equals(cookedRecipe.getName())) {
                int points = order.getRecipe().getReward();
                addScore(points);
                activeOrders.remove(i);
                this.failedOrdersCount = 0;
                System.out.println("SERVE SUKSES! " + order.getRecipe().getName());

                addOrder();
                return true;
            }
        }

        System.out.println("SERVE GAGAL: Tidak ada yang pesan " + cookedRecipe.getName());
        addScore(-cookedRecipe.getPenalty());
        return false;
    }

    private void addScore(int points) {
        this.score += points;
    }

    private void incrementFailedOrder() {
        failedOrdersCount++;
        if (failedOrdersCount >= maxFailedOrders) {
            isGameOver = true;
            stopGameLoop();
            if (statusListener != null) {
                statusListener.onGameOver(score);
            }
        }
    }

    private void checkWinCondition() {
        stopGameLoop();
        if (score >= targetScore) {
            isStageCleared = true;
            if (statusListener != null) {
                statusListener.onStageCleared(score);
            }
        } else {
            isGameOver = true;
            if (statusListener != null) {
                statusListener.onGameOver(score);
            }
        }
    }

    public void setTimeFrozen(boolean frozen) {
        this.isTimeFrozen = frozen;
        if (frozen) System.out.println("Waktu Beku!");
        else System.out.println("Waktu Jalan Lagi!");
    }

    public int getScore() { return score; }
    public int getTargetScore() { return targetScore; }
    public int getFailedOrdersCount() { return failedOrdersCount; }
    public int getMaxFailedOrders() { return maxFailedOrders; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isStageCleared() { return isStageCleared; }
    public int getTimeRemaining() { return timeRemaining; }
    public List<Order> getActiveOrders() { return activeOrders; }
}