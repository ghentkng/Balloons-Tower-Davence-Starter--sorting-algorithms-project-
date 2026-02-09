
//Imports for graphics
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
//Import for collection of points
import java.util.List;

public class Balloon {

    // Each balloon is assigned an ID based on order of generation
    private static int NEXT_ID = 1;
    private final int id;

    // Map and path this balloon belongs to
    private final Map map;
    private final List<Point> path;

    // Speed and location info
    private int segmentIndex = 0;
    private double x, y;
    private final double speedPxPerSec;

    // Customize balloon
    private final int radius = 10;
    private int health;
    private final Color color;

    // Constructor
    public Balloon(Map map, double speedPxPerSec) {
        this.id = NEXT_ID++;
        this.map = map;
        this.path = map.getPath();
        this.speedPxPerSec = speedPxPerSec;
        this.health = 3;

        // Assign point where the balloon will start
        Point start = path.get(0);
        this.x = start.x;
        this.y = start.y;

        // Change the color based on the balloon speed
        if (speedPxPerSec < 90)
            color = new Color(230, 80, 80); // red
        else if (speedPxPerSec < 120)
            color = new Color(80, 130, 230); // blue
        else
            color = new Color(80, 200, 120); // green
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speedPxPerSec;
    }

    // Calculate damage
    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0)
            health = 0;
    }

    public boolean isDead() {
        return health <= 0;
    }

    // Move balloon along the path
    public void update(double dtSec) {
        if (isFinished())
            return;

        double remainingMove = speedPxPerSec * dtSec;

        while (remainingMove > 0 && !isFinished()) {
            Point b = path.get(segmentIndex + 1);

            double dx = b.x - x;
            double dy = b.y - y;
            double distToNext = Math.hypot(dx, dy);

            if (distToNext < 0.0001) {
                segmentIndex++;
                continue;
            }

            if (remainingMove >= distToNext) {
                x = b.x;
                y = b.y;
                remainingMove -= distToNext;
                segmentIndex++;
            } else {
                double ratio = remainingMove / distToNext;
                x += dx * ratio;
                y += dy * ratio;
                remainingMove = 0;
            }
        }
    }

    public boolean isFinished() {
        return segmentIndex >= path.size() - 1;
    }

    public double distanceRemaining() {
        if (isFinished())
            return 0.0;

        Point next = path.get(segmentIndex + 1);
        double dist = Math.hypot(next.x - x, next.y - y);

        for (int i = segmentIndex + 1; i < path.size() - 1; i++) {
            Point a = path.get(i);
            Point b = path.get(i + 1);
            dist += Math.hypot(b.x - a.x, b.y - a.y);
        }
        return dist;
    }

    public void draw(Graphics2D g2) {
        int cx = (int) Math.round(x);
        int cy = (int) Math.round(y);

        g2.setColor(color);
        g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

        g2.setColor(java.awt.Color.BLACK);
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

        g2.drawString(String.valueOf(health), cx - 3, cy + 4);
    }
}
