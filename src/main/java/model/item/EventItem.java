package model.item;


import model.kitchen.EventManager;

public class EventItem extends Item {
    private EventManager.EventType type;

    public EventItem(EventManager.EventType type) {
        super(getNameFromType(type));
        this.type = type;
    }

    public EventManager.EventType getType() {
        return type;
    }

    private static String getNameFromType(EventManager.EventType type) {
        return switch (type) {
            case TIME_FREEZE -> "Jam";
            case UNLIMITED_DASH -> "Jam";
            default -> "null";
        };
    }
}