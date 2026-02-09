
//Fairly self explanatory class
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Laser {
    private final double x1, y1, x2, y2;
    private double ttlSec;

    public Laser(double x1, double y1, double x2, double y2, double ttlSec) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.ttlSec = ttlSec;
    }

    public void update(double dtSec) {
        ttlSec -= dtSec;
    }

    public boolean isDead() {
        return ttlSec <= 0;
    }

    public void draw(Graphics2D g2) {
        var oldStroke = g2.getStroke();
        var oldColor = g2.getColor();

        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 60, 60, 200));
        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

        g2.setColor(new Color(255, 220, 220, 200));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

        g2.setStroke(oldStroke);
        g2.setColor(oldColor);
    }
}
