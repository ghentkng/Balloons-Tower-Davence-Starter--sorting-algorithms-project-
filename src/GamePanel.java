// -------------------- Game Panel / Loop --------------------

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    // Fixed "logical" size
    private static final int W = 900;
    private static final int H = 550;

    // Ticks per second (update rate)
    private static final int TICKS_PER_SEC = 30;
    private static final int TIMER_DELAY_MS = 1000 / TICKS_PER_SEC;

    private final Timer timer;

    private final Map map;
    private final List<Balloon> balloons;
    private final List<Tower> towers;

    private int tickCount = 0;

    GamePanel() {
        setPreferredSize(new Dimension(W, H));
        setBackground(Color.WHITE);

        map = new Map(makeDefaultPath());
        balloons = new ArrayList<>();
        towers = new ArrayList<>();

        // Add a couple towers (placeholder)
        towers.add(new Tower(250, 180, 110));
        towers.add(new Tower(560, 350, 130));

        addMouseListener(this);

        timer = new Timer(TIMER_DELAY_MS, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tickCount++;

        // Spawn balloons periodically
        if (tickCount % (TICKS_PER_SEC) == 0) { // every ~1 second
            // speed is in pixels per second
            balloons.add(new Balloon(map, 80 + (tickCount % 60))); // vary speed a little
        }

        // Update balloons
        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon b = balloons.get(i);
            b.update(1.0 / TICKS_PER_SEC);
            if (b.isFinished()) {
                balloons.remove(i);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Nice rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw map path
        map.draw(g2);

        // Draw towers
        for (Tower t : towers) {
            t.draw(g2);
        }

        // Draw balloons
        for (Balloon b : balloons) {
            b.draw(g2);
        }

        // HUD
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Balloons: " + balloons.size(), 12, 18);
        g2.drawString("Click to place a tower (visual only).", 12, 36);

        g2.dispose();
    }

    private static List<Point> makeDefaultPath() {
        // A simple zig-zag path across the screen
        List<Point> pts = new ArrayList<>();
        pts.add(new Point(60, 80));
        pts.add(new Point(220, 80));
        pts.add(new Point(220, 220));
        pts.add(new Point(420, 220));
        pts.add(new Point(420, 120));
        pts.add(new Point(650, 120));
        pts.add(new Point(650, 380));
        pts.add(new Point(840, 380));
        return pts;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Add a new tower at click
        towers.add(new Tower(e.getX(), e.getY(), 120));
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}