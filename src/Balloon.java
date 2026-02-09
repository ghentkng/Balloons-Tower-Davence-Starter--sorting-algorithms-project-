// -------------------- Balloon --------------------

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class Balloon {
    private final Map map;
    private final List<Point> path;

    // Movement state
    private int segmentIndex = 0; // moving from path[i] to path[i+1]
    private double x, y;
    private final double speedPxPerSec;

    // Visual / gameplay
    private final int radius = 10;
    private int health = 1; // placeholder (for later)
    private final Color color;

    Balloon(Map map, double speedPxPerSec) {
        this.map = map;
        this.path = map.getPath();
        this.speedPxPerSec = speedPxPerSec;

        Point start = path.get(0);
        this.x = start.x;
        this.y = start.y;

        // Random-ish color by speed
        if (speedPxPerSec < 90)
            color = new Color(230, 80, 80);
        else if (speedPxPerSec < 120)
            color = new Color(80, 130, 230);
        else
            color = new Color(80, 200, 120);
    }

    public void update(double dtSec) {
        if (isFinished())
            return;

        double remainingMove = speedPxPerSec * dtSec;

        while (remainingMove > 0 && !isFinished()) {
            Point a = path.get(segmentIndex);
            Point b = path.get(segmentIndex + 1);

            double dx = b.x - x;
            double dy = b.y - y;
            double distToNext = Math.hypot(dx, dy);

            if (distToNext < 0.0001) {
                // We are basically at the next waypoint
                segmentIndex++;
                continue;
            }

            if (remainingMove >= distToNext) {
                // Move to the next waypoint and continue with leftover movement
                x = b.x;
                y = b.y;
                remainingMove -= distToNext;

                segmentIndex++;
            } else {
                // Move partially along the segment
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

    /**
     * Useful for tower targeting later:
     * smaller value means closer to exit (more dangerous).
     */
    public double distanceRemaining() {
        if (isFinished())
            return 0.0;

        // Distance from current position to end of current segment
        Point next = path.get(segmentIndex + 1);
        double dist = Math.hypot(next.x - x, next.y - y);

        // Add full remaining segments
        for (int i = segmentIndex + 1; i < path.size() - 1; i++) {
            Point a = path.get(i);
            Point b = path.get(i + 1);
            dist += Math.hypot(b.x - a.x, b.y - a.y);
        }
        return dist;
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

    public void draw(Graphics2D g2) {
        int cx = (int) Math.round(x);
        int cy = (int) Math.round(y);

        g2.setColor(color);
        g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

        g2.setColor(Color.BLACK);
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
    }
}