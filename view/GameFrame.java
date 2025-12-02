import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameFrame {
    public static void main(String[] args) {
        MapType config = new PizzaMap();
//        Chef chefA = new Chef("C1", "Kebin", new Position(5, 5));
//        Chef chefB = new Chef("C2", "Stewart", new Position(10, 8));
//        List<Chef> allChefs = List.of(chefA, chefB);
        config.initialLayout();
        //Map gameMap = new Map(config, allChefs); // Map harus menerima List<Chef>
        Map gameMap = new Map(config);
        //chefA.activate(); // Aktifkan Chef utama

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(config.getMapName() + " - Nimonscooked");

            // MapViewPanel gameView = new MapViewPanel(gameMap, allChefs);
            MapViewPanel gameView = new MapViewPanel(gameMap);

            frame.add(gameView);

            frame.pack(); // Sesuaikan ukuran frame dengan panel
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        });
    }
}