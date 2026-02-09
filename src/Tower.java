
// -------------------- Tower (visual placeholder) --------------------
import java.awt.Color;
import java.awt.Graphics2D;

public class Tower {
    private final int x, y; // center
    private final int rangePx; // targeting range (for later)

    Tower(int x, int y, int rangePx) {
        this.x = x;
        this.y = y;
        this.rangePx = rangePx;
    }

    public boolean inRange(Balloon b) {
        double dx = b.getX() - x;
        double dy = b.getY() - y;
        return (dx * dx + dy * dy) <= (double) rangePx * rangePx;
    }

    public void draw(Graphics2D g2) {
        // Range circle (light)
        g2.setColor(new Color(0, 0, 0, 35));
        g2.drawOval(x - rangePx, y - rangePx, rangePx * 2, rangePx * 2);

        // Tower body
        g2.setColor(new Color(90, 90, 90));
        g2.fillRoundRect(x - 12, y - 12, 24, 24, 8, 8);

        // Tower "barrel"
        g2.setColor(new Color(60, 60, 60));
        g2.fillRoundRect(x + 8, y - 4, 16, 8, 6, 6);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(x - 12, y - 12, 24, 24, 8, 8);
    }
}
