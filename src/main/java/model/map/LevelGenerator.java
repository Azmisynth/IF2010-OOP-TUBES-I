package model.map;

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

    // Score & Orders)
    private static final int UI_TL_WIDTH = 3;
    private static final int UI_TL_HEIGHT = 8;

    //  (Timer)
    private static final int UI_BR_WIDTH = 2;
    private static final int UI_BR_HEIGHT = 1;

    //  (Fail Streak Count)
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

        // 3. Smoothing
        smoothMap();

        // 4. Tempatkan Station dengan Validasi
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
        int maxAttempts = 5000;

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
                'O', 'D', 'K', 'U', 'V',
                'Y', 'S', 'C', 'C', 'S', 'R', 'R',
                'W', 'H', 'P', 'T', 'A', 'A', 'A',
                'A', 'A', 'A', 'A', 'A'

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

        if (isFloor(x, y - 1)) count++; // up
        if (isFloor(x, y + 1)) count++; // down
        if (isFloor(x - 1, y)) count++; // left
        if (isFloor(x + 1, y)) count++; // right

        return count;
    }
    private boolean isFloor(int x, int y) {
        return isInside(x, y) && grid[y][x] == FLOOR;
    }


    private boolean isInside(int x, int y) {
        return x > 0 && x < width - 1 && y > 0 && y < height - 1;
    }
}