package main.java.model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelGenerator {
    private int width;
    private int height;
    private char[][] grid;
    private Random rand;

    private static final char WALL = 'X';
    private static final char FLOOR = '.';

    // 1. KIRI ATAS (Score & Orders)
    private static final int UI_TL_WIDTH = 3;
    private static final int UI_TL_HEIGHT = 8;

    // 2. KANAN BAWAH (Timer)
    private static final int UI_BR_WIDTH = 2;
    private static final int UI_BR_HEIGHT = 1;

    // 3. KIRI BAWAH (Fail Streak Count)
    private static final int UI_BL_WIDTH = 2;
    private static final int UI_BL_HEIGHT = 2;

    public LevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.rand = new Random();
    }

    public String generate() {
        // 1. Inisialisasi Tembok
        grid = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = WALL;
            }
        }

        // 2. Gali Lantai (Algoritma Digger)
        digFloor(width / 2, height / 2, (width * height) / 4);

        // 3. Smoothing: Hapus sudut-sudut tajam/tembok jomblo
        smoothMap();

        // 4. Tempatkan Station dengan Validasi Ketat
        placeStationsSmartly();

        // 5. Convert ke String
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(grid[y][x]);
            }
            if (y < height - 1) sb.append("\n");
        }
        return sb.toString();
    }

    private boolean isReserved(int x, int y) {
        if (x < UI_TL_WIDTH && y < UI_TL_HEIGHT) {
            return true;
        }

        if (x >= width - UI_BR_WIDTH && y >= height - UI_BR_HEIGHT) {
            return true;
        }

        if (x < UI_BL_WIDTH && y >= height - UI_BL_HEIGHT) {
            return true;
        }
        return false;
    }

    private void digFloor(int startX, int startY, int targetFloors) {
        int x = startX;
        int y = startY;
        int floorCount = 0;
        int attempts = 0;
        int maxAttempts = 10000;

        while (floorCount < targetFloors && attempts < maxAttempts) {
            attempts++;
            if (grid[y][x] == WALL) {
                grid[y][x] = FLOOR;
                floorCount++;
            }
            int dir = rand.nextInt(4);
            int dx = 0, dy = 0;
            switch (dir) {
                case 0 -> dy = -1; case 1 -> dy = 1;
                case 2 -> dx = -1; case 3 -> dx = 1;
            }
            int nx = x + dx;
            int ny = y + dy;
            if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && !isReserved(nx, ny)) {
                x = nx; y = ny;
            }
        }
    }

    private void smoothMap() {
        for (int k = 0; k < 2; k++) {
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    if (isReserved(x, y)) continue;
                    int floorNeighbors = countFloorNeighbors(x, y);

                    if (grid[y][x] == WALL) {
                        // Tembok dikepung lantai -> Hapus
                        if (floorNeighbors > 4) grid[y][x] = FLOOR;
                    }
                    if (grid[y][x] == FLOOR) {
                        // Lantai dikepung tembok -> Tutup
                        if (floorNeighbors < 1) grid[y][x] = WALL;
                    }
                }
            }
        }
    }

    private void placeStationsSmartly() {
        List<int[]> candidates = new ArrayList<>();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Hanya pilih tembok yang punya "Standing Spot" valid
                if (!isReserved(x, y) && grid[y][x] == WALL && isInteractable(x, y)) {
                    candidates.add(new int[]{x, y});
                }
            }
        }
        Collections.shuffle(candidates);

        char[] stations = {
                'D', 'A', 'W', 'P',
                'S', 'S', 'C', 'C', 'R',
                'T', 'K', 'B', 'Z', 'M'
        };

        for (char station : stations) {
            if (!candidates.isEmpty()) {
                int[] pos = candidates.remove(0);
                grid[pos[1]][pos[0]] = station;
            }
        }
    }

    private boolean isInteractable(int wallX, int wallY) {
        if (isValidStandingSpot(wallX, wallY - 1)) return true; // Atas
        if (isValidStandingSpot(wallX, wallY + 1)) return true; // Bawah
        if (isValidStandingSpot(wallX - 1, wallY)) return true; // Kiri
        if (isValidStandingSpot(wallX + 1, wallY)) return true; // Kanan
        return false;
    }

    private boolean isValidStandingSpot(int x, int y) {
        // Harus lantai dan punya jalan keluar (min 1 lantai tetangga lain)
        if (isReserved(x, y)) return false;
        if (grid[y][x] != FLOOR) return false;
        return countFloorNeighbors(x, y) >= 1;
    }

    private int countFloorNeighbors(int x, int y) {
        int count = 0;

        // Loop dari -1 (Kiri/Atas) sampai +1 (Kanan/Bawah)
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {

                // Skip titik tengah (diri sendiri)
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                // Pastikan koordinat valid (tidak keluar array)
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (grid[ny][nx] == FLOOR) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== TEST GENERATOR ===\n");

        for (int i = 1; i <= 3; i++) {
            System.out.println("MAP #" + i);
            LevelGenerator gen = new LevelGenerator(16, 10);
            System.out.println(gen.generate());
            System.out.println("--------------------------------");
        }
    }
}