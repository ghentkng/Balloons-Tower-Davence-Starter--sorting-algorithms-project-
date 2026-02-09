
//Imports for graphics
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
//Imports for collection of balloons and towers
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, MouseListener {

    // Screen size
    private static final int W = 900;
    private static final int H = 550;

    // Related to timing
    private static final int TICKS_PER_SEC = 30;
    private static final int TIMER_DELAY_MS = 1000 / TICKS_PER_SEC;
    private int tickCount = 0;
    private final Timer timer;

    // Items to display
    private final Map map;
    private final List<Balloon> balloons;
    private final List<Tower> towers;
    private final List<Laser> lasers;

    // Example lambda expression, that returns the oldest balloon
    TargetingStrategy oldestBalloon = (tower, allBalloons) -> {
        Balloon best = null;
        for (Balloon b : allBalloons) {
            if (b.isDead() || b.isFinished())
                continue;
            if (!tower.inRange(b))
                continue;

            if (best == null || b.getId() < best.getId()) {
                best = b;
            }
        }
        return best;
    };

    // TODO your lambdas here. Use sorting strategies to target balloons
    // based on health remaining, proximity, or some other metric
    // that you implement. Then create towers using your lambdas!

    // Graphics
    public GamePanel() {
        setPreferredSize(new Dimension(W, H));
        setBackground(Color.WHITE);

        map = new Map(makeDefaultPath());
        balloons = new ArrayList<>();
        towers = new ArrayList<>();

        lasers = new ArrayList<>();

        towers.add(new Tower(250, 180, 110, oldestBalloon));
        towers.add(new Tower(560, 350, 130, oldestBalloon));

        addMouseListener(this);

        timer = new Timer(TIMER_DELAY_MS, this);
        timer.start();
    }

    // Called on every tick. Advances the game
    @Override
    public void actionPerformed(ActionEvent e) {
        tickCount++;

        if (tickCount % (TICKS_PER_SEC) == 0) {
            balloons.add(new Balloon(map, 80 + (tickCount % 60)));
        }

        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon b = balloons.get(i);
            b.update(1.0 / TICKS_PER_SEC);
            if (b.isFinished()) {
                balloons.remove(i);
            }
        }
        double dt = 1.0 / TICKS_PER_SEC;

        for (Tower t : towers) {
            Laser shot = t.updateAndMaybeFire(balloons, dt);
            if (shot != null)
                lasers.add(shot);
        }

        for (int i = balloons.size() - 1; i >= 0; i--) {
            if (balloons.get(i).isDead() || balloons.get(i).isFinished()) {
                balloons.remove(i);
            }
        }

        for (int i = lasers.size() - 1; i >= 0; i--) {
            lasers.get(i).update(dt);
            if (lasers.get(i).isDead())
                lasers.remove(i);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        map.draw(g2);

        for (Tower t : towers) {
            t.draw(g2);
        }
        for (Laser l : lasers) {
            l.draw(g2);
        }

        for (Balloon b : balloons) {
            b.draw(g2);
        }

        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Balloons: " + balloons.size(), 12, 18);
        g2.drawString("Click to place a tower.", 12, 36);

        g2.dispose();
    }

    private static List<Point> makeDefaultPath() {
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
        towers.add(new Tower(e.getX(), e.getY(), 120, oldestBalloon));
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