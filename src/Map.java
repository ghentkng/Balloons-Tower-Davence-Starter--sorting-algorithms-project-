// -------------------- Map --------------------

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.List;

public class Map {
    private final List<Point> path;
    private final int pathWidth = 26;

    Map(java.util.List<Point> path) {
        if (path == null || path.size() < 2) {
            throw new IllegalArgumentException("Path must have at least 2 points.");
        }
        this.path = path;
    }

    public List<Point> getPath() {
        return path;
    }

    public void draw(Graphics2D g2) {
        // Draw a wide "road" for the path
        Stroke old = g2.getStroke();

        // Road base
        g2.setStroke(new BasicStroke(pathWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(220, 220, 220));
        drawPolyline(g2, path);

        // Road outline
        g2.setStroke(new BasicStroke(pathWidth + 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(180, 180, 180));
        drawPolyline(g2, path);

        // Draw direction dots (optional)
        g2.setStroke(old);
        g2.setColor(new Color(120, 120, 120));
        for (Point p : path) {
            g2.fillOval(p.x - 3, p.y - 3, 6, 6);
        }
    }

    private static void drawPolyline(Graphics2D g2, List<Point> pts) {
        for (int i = 0; i < pts.size() - 1; i++) {
            Point a = pts.get(i);
            Point b = pts.get(i + 1);
            g2.drawLine(a.x, a.y, b.x, b.y);
        }
    }
}