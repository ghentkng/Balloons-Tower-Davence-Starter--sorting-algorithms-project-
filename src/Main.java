import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starter Tower Defense (BTD-lite) Engine
 * - Simple map: a fixed path made of waypoints
 * - Balloon class: moves along the waypoint path
 * - Tower class: drawn only (no targeting yet)
 * - Game loop: Swing Timer updates + repaints
 *
 * Compile: javac TowerDefenseStarter.java
 * Run: java TowerDefenseStarter
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tower Defense Starter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GamePanel panel = new GamePanel();
            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}