
//Imports for graphics
import java.awt.Color;
import java.awt.Graphics2D;
//Import for collection of balloons
import java.util.List;

public class Tower {

    // Tower data
    private final int x, y;
    private final int rangePx;
    private final int damage = 1;
    private final double attacksPerSecond = 2.0;
    private double cooldownSec = 0.0;
    private final TargetingStrategy strategy;

    public Tower(int x, int y, int rangePx, TargetingStrategy strategy) {
        this.x = x;
        this.y = y;
        this.rangePx = rangePx;
        this.strategy = strategy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRangePx() {
        return rangePx;
    }

    public boolean inRange(Balloon b) {
        double dx = b.getX() - x;
        double dy = b.getY() - y;
        return (dx * dx + dy * dy) <= (double) rangePx * rangePx;
    }

    // Called regularly. Selects a target using the strategy and fires. Triggers
    // laser animation.
    public Laser updateAndMaybeFire(List<Balloon> balloons, double dtSec) {
        cooldownSec -= dtSec;
        if (cooldownSec > 0)
            return null;

        Balloon target = strategy.chooseTarget(this, balloons);
        if (target == null)
            return null;

        attack(target);

        cooldownSec = 1.0 / attacksPerSecond;

        return new Laser(x, y, target.getX(), target.getY(), 0.12);
    }

    public void attack(Balloon b) {
        b.takeDamage(damage);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 35));
        g2.drawOval(x - rangePx, y - rangePx, rangePx * 2, rangePx * 2);

        g2.setColor(new Color(90, 90, 90));
        g2.fillRoundRect(x - 12, y - 12, 24, 24, 8, 8);

        g2.setColor(new Color(60, 60, 60));
        g2.fillRoundRect(x + 8, y - 4, 16, 8, 6, 6);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(x - 12, y - 12, 24, 24, 8, 8);
    }
}
