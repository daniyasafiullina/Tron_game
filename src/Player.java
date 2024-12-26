import java.awt.*;
import java.util.LinkedList;

public class Player {
    private String name;
    private Color color;
    private int x, y;
    private String direction;
    private LinkedList<Point> lightTrace;

    public Player(String name, Color color, String direction, int startX, int startY) {
        this.name = name;
        this.color = color;
        this.direction = direction;
        this.x = startX;
        this.y = startY;
        this.lightTrace = new LinkedList<>();
    }

    public void move() {
        lightTrace.add(new Point(x, y));
        switch (direction) {
            case "UP" -> y--;
            case "DOWN" -> y++;
            case "LEFT" -> x--;
            case "RIGHT" -> x++;
        }
    }

    public void draw(Graphics g, int gridSize) {
        g.setColor(color);
        for (Point point : lightTrace) {
            g.fillRect(point.x * gridSize, point.y * gridSize, gridSize, gridSize);
        }
        g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public LinkedList<Point> getLightTrace() {
        return lightTrace;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
