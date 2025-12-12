package main.java.model.kitchen;

import java.util.concurrent.CopyOnWriteArrayList;
import main.java.model.item.Ingredient;
import main.java.model.item.Preparable;
import main.java.model.item.ItemState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OrderManager {
    private static OrderManager instance;
    private int spawnTimer;
    private int score;
    private List<Order> activeOrders;

    private static final int MAX_ACTIVE_ORDERS = 3;
    private static final int ORDER_DURATION = 120;
    private static final int SPAWN_INTERVAL = 15;

    public OrderManager() {
        this.score = 0;
        this.activeOrders = new ArrayList<>();
        this.spawnTimer = SPAWN_INTERVAL;
        addOrder();
    }

    public static OrderManager getInstance() {
        if (instance == null) instance = new OrderManager();
        return instance;
    }

    public void addOrder() {
        if (activeOrders.size() < MAX_ACTIVE_ORDERS) {
            Recipe r = RecipeBook.getRandomRecipe();
            Order newOrder = new Order(r, ORDER_DURATION);
            activeOrders.add(newOrder);
            System.out.println("NEW ORDER: " + r.getName());
        }
    }

    public void updateOrders() {
        Iterator<Order> iterator = activeOrders.iterator();
        boolean needNewOrder = false;

        while (iterator.hasNext()) {
            Order order = iterator.next();
            order.tick(); // Kurangi waktu

            if (order.isExpired()) {
                System.out.println("EXPIRED: " + order.getRecipe().getName());
                addScore(-order.getRecipe().getPenalty());
                iterator.remove();
            }
        }

        //isi sampai penuh kalau ada yang selesai.
        if (activeOrders.size() < MAX_ACTIVE_ORDERS) {
            spawnTimer--;
            if (spawnTimer <= 0) {
                addOrder();
                spawnTimer = SPAWN_INTERVAL;
            }

        }
    }

    // Logic Validasi Serving
    public boolean deliverOrder(Set<Preparable> plateContents) {
        for (Preparable prep : plateContents) {
            if (prep instanceof Ingredient) {
                Ingredient ing = (main.java.model.item.Ingredient) prep;

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

    public int getScore() { return score; }
    public List<Order> getActiveOrders() { return activeOrders; }
}