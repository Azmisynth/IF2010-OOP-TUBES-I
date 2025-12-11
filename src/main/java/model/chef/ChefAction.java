package main.java.model.chef;

public enum ChefAction {
    IDLE,
    MOVING,
    PICKING_UP,
    DROPPING,
    THROWING, // Untuk aksi lempar
    INTERACTING, // Untuk aksi di stasiun (non-durasi)
    BUSY_WORKING, // Untuk aksi berdurasi (Washing, Cooking)
    DASHING
}