
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

import javax.swing.Box;
// Imports for UI controls (radio buttons)
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

    // -------------------- Tower Type Selection (Radio Buttons)
    // --------------------
    // Students will eventually implement different lambdas (tower types). For now,
    // Tower A targets the oldest balloon, and Tower B/C are placeholders.
    private final JRadioButton towerAButton = new JRadioButton("Tower A (Oldest)", true);
    private final JRadioButton towerBButton = new JRadioButton("Tower B (TODO)");
    private final JRadioButton towerCButton = new JRadioButton("Tower C (TODO)");

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

    // Placeholder lambdas so radio buttons compile now.
    // Replace these later with student-implemented strategies.
    TargetingStrategy towerBStrategy = (tower, allBalloons) -> null;
    TargetingStrategy towerCStrategy = (tower, allBalloons) -> null;

    // Graphics
    public GamePanel() {
        // IMPORTANT: Use a top strip for radio buttons and keep the playfield separate
        setLayout(new java.awt.BorderLayout());

        // --- Control panel (radio buttons) ---
        JPanel controls = new JPanel();
        controls.add(new JLabel("Select tower type: "));

        ButtonGroup group = new ButtonGroup();
        group.add(towerAButton);
        group.add(towerBButton);
        group.add(towerCButton);

        controls.add(towerAButton);
        controls.add(towerBButton);
        controls.add(towerCButton);

        controls.add(Box.createHorizontalStrut(12));
        controls.add(new JLabel("(Click on the map to place a tower.)"));

        add(controls, java.awt.BorderLayout.NORTH);

        // --- Playfield panel (where the game draws and where clicks place towers) ---
        PlayfieldPanel playfield = new PlayfieldPanel();
        add(playfield, java.awt.BorderLayout.CENTER);

        // Items to display
        map = new Map(makeDefaultPath());
        balloons = new ArrayList<>();
        towers = new ArrayList<>();
        lasers = new ArrayList<>();

        // Starter towers (use Tower A strategy by default)
        towers.add(new Tower(250, 180, 110, oldestBalloon));
        towers.add(new Tower(560, 350, 130, oldestBalloon));

        // Timing
        timer = new Timer(TIMER_DELAY_MS, this);
        timer.start();
    }

    /**
     * Choose which targeting lambda to assign to a new tower based on the radio
     * buttons.
     * (Later, students can make Tower B/C strategies real and sorting-based.)
     */
    private TargetingStrategy selectedStrategy() {
        if (towerAButton.isSelected())
            return oldestBalloon;
        if (towerBButton.isSelected())
            return towerBStrategy;
        return towerCStrategy;
    }

    // Called on every tick. Advances the game
    @Override
    public void actionPerformed(ActionEvent e) {
        tickCount++;

        // Spawn a balloon once per second
        if (tickCount % (TICKS_PER_SEC) == 0) {
            balloons.add(new Balloon(map, 80 + (tickCount % 60)));
        }

        // Move balloons and remove any that reach the end
        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon b = balloons.get(i);
            b.update(1.0 / TICKS_PER_SEC);
            if (b.isFinished()) {
                balloons.remove(i);
            }
        }

        double dt = 1.0 / TICKS_PER_SEC;

        // Let towers fire (creates lasers to show shots)
        for (Tower t : towers) {
            Laser shot = t.updateAndMaybeFire(balloons, dt);
            if (shot != null)
                lasers.add(shot);
        }

        // Remove dead balloons (or ones that finished)
        for (int i = balloons.size() - 1; i >= 0; i--) {
            if (balloons.get(i).isDead() || balloons.get(i).isFinished()) {
                balloons.remove(i);
            }
        }

        // Update lasers and remove expired ones
        for (int i = lasers.size() - 1; i >= 0; i--) {
            lasers.get(i).update(dt);
            if (lasers.get(i).isDead())
                lasers.remove(i);
        }

        repaint();
    }

    /**
     * Paints the game scene.
     * NOTE: The actual drawing happens in the PlayfieldPanel below, so this class
     * mainly handles the game state + timer updates.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // We keep this override empty-ish because the PlayfieldPanel is doing the
        // drawing.
        // Still call super for good practice.
        super.paintComponent(g);
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
        // Place a tower using whichever radio button is selected
        TargetingStrategy strat = selectedStrategy();
        towers.add(new Tower(e.getX(), e.getY(), 120, strat));
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

    /**
     * This inner panel is the actual playfield (the drawable game area).
     * It receives mouse clicks to place towers and renders
     * map/towers/lasers/balloons.
     */
    private class PlayfieldPanel extends JPanel {
        PlayfieldPanel() {
            setPreferredSize(new Dimension(W, H));
            setBackground(Color.WHITE);
            addMouseListener(GamePanel.this); // clicks place towers
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
    }
}
