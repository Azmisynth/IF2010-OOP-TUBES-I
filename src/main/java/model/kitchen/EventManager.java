package model.kitchen;


import model.item.EventItem;
import model.map.Map;
import model.map.Tile;

import java.util.Random;

public class EventManager {
    private static EventManager instance;
    private Random rand = new Random();
    public enum EventType {
        NONE,
        BLACKOUT,
        TIME_FREEZE,
        UNLIMITED_DASH
    }

    private EventType activeEvent = EventType.NONE;
    private int eventTimer = 0;
    private int currentLevel = 1;
    private int matchTick = 0;
    private static final int SEC = 60;
    private EventManager() {}

    public static EventManager getInstance() {
        if (instance == null) instance = new EventManager();
        return instance;
    }

    public void setLevel(int level) {
        this.currentLevel = level;
        this.matchTick = 0;
        stopEvent();
    }

    public void update(Map map) {
        if (activeEvent != EventType.NONE) {
            eventTimer--;
            // Jika waktu habis, hentikan event
            if (eventTimer <= 0) {
                stopEvent();
            }
        }
        runLevelScenario(map);
        matchTick++;
    }

    private void runLevelScenario(Map map) {
        switch (currentLevel) {
            case 1:
                break;
            case 2:
                if (matchTick == 5 * SEC) triggerEvent(EventType.BLACKOUT, 8);
                if (matchTick == 120 * SEC) triggerEvent(EventType.BLACKOUT, 8);
                break;

            case 3:
                if (matchTick == 30 * SEC) spawnPowerUp(map, EventType.UNLIMITED_DASH);
                if (matchTick == 30 * SEC) triggerEvent(EventType.BLACKOUT, 10);
                if (matchTick == 4 * SEC) spawnPowerUp(map, EventType.TIME_FREEZE);

            case 4:
                if (matchTick > 0 && matchTick % (30 * SEC) == 0) {
                    if (rand.nextDouble() < 0.4) {
                        double gacha = rand.nextDouble();
                        if (gacha < 0.15) triggerEvent(EventType.BLACKOUT, 8);
                        else if (gacha < 0.575) spawnPowerUp(map, EventType.UNLIMITED_DASH);
                        else spawnPowerUp(map, EventType.TIME_FREEZE);
                    }
                }
                break;
        }
    }

    private void spawnPowerUp(Map map, EventType type) {
        for (int i = 0; i < 50; i++) { // Coba cari tempat kosong 50x
            int rx = rand.nextInt(map.getWidth());
            int ry = rand.nextInt(map.getHeight());
            Tile t = map.getTile(rx, ry);

            // Syarat: Lantai bisa jalan, gak ada item, gak ada meja
            if (t.isWalkable(rx, ry) && t.getItem() == null && t.getStation() == null) {
                t.setItem(new EventItem(type));
                System.out.println("SPAWN ITEM: " + type + " di (" + rx + "," + ry + ")");
                return;
            }
        }
    }

    public void triggerEvent(EventType type, int durationSeconds) {
        // Hentikan event lama jika ada
        if (activeEvent != EventType.NONE) stopEvent();

        this.activeEvent = type;
        this.eventTimer = durationSeconds * SEC;
        System.out.println("EVENT STARTED: " + type);

        if (type == EventType.TIME_FREEZE) {
            OrderManager.getInstance().setTimeFrozen(true);
        }
    }

    public void stopEvent() {
        System.out.println("EVENT ENDED: " + activeEvent);
        if (activeEvent == EventType.TIME_FREEZE) {
            OrderManager.getInstance().setTimeFrozen(false);
        }

        this.activeEvent = EventType.NONE;
        this.eventTimer = 0;
    }

    public EventType getActiveEvent() {
        return activeEvent;
    }
}