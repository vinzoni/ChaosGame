import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ChaosGame
{
    Point[] points = new Triangle().points();
    private final MyCanvas canvas = new MyCanvas(points);

    public static void main(String[] args)
    {
        new ChaosGame();
    }

    public ChaosGame()
    {
        Frame f = new Frame("Canvas Example");
        f.setLayout(new BoxLayout(f, BoxLayout.PAGE_AXIS));
        JPanel buttonPanel = new JPanel();
        JLabel totalDotslabel1 = new JLabel("Plotting : ");
        JLabel totalDotslabel2 = new JLabel("" + 0 + " / " + Plotter.TOTAL_DOTS);
        JButton run = new JButton("Run");
        run.addActionListener(
                e -> {
                        Plotter plotter = new Plotter(points, canvas, totalDotslabel2);
                        plotter.start();
                });
        JButton quit = new JButton("Quit");
        quit.addActionListener(
                e -> System.exit(0));

        buttonPanel.add(run);
        buttonPanel.add(quit);
        buttonPanel.add(totalDotslabel1);
        buttonPanel.add(totalDotslabel2);

        f.add(buttonPanel);
        f.add(this.canvas);
        f.setSize(900, 900);
        f.setVisible(true);
    }

}
class MyCanvas extends Canvas
{
    private final Point[] points;
    private Point firstPoint;

    public MyCanvas(Point[] points) {
        this.points = points;
        setBackground (Color.WHITE);
        setSize(900, 900);
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.red);
        for (Point p: points)
            g.fillOval(p.x, p.y, 10, 10);

        g.setColor(Color.black);
        Point p0 = pickRandomFirstPoint(900, 900);
        g.fillOval(p0.x, p0.y, 4, 4);
    }

    private Point pickRandomFirstPoint(int w, int h) {
        Random r = new Random();
        this.firstPoint = new Point("first point", r.nextInt(w), r.nextInt(h));
        return this.firstPoint;
    }

    public Point firstPoint() {
        return firstPoint;
    }

    public void drawNextPoint(Point point) {
        this.getGraphics().fillOval(point.x, point.y, 4, 4);
    }
}

class Plotter extends Thread {

//    public static int TOTAL_DOTS = 12800;
    public static int TOTAL_DOTS = 25600;
    private static int DELAY_MILLIS = 2;
//    public static int TOTAL_DOTS = 2;
//    private static int DELAY_MILLIS = 6000;

    private final Point[] points;
    private Point lastPoint;
    private final JLabel labelToUpdate;
    private final MyCanvas canvas;

    public Plotter (Point[] points, MyCanvas canvas, JLabel label) {
        this.points = points;
        this.labelToUpdate = label;
        this.canvas = canvas;
    }

    public void run() {

        this.lastPoint = canvas.firstPoint();

        for (int dot=0; dot<TOTAL_DOTS; ++dot) {

            Point selectedRandomPoint = this.points[new Random().nextInt(points.length)];
//            System.out.print("" + lastPoint.x + "-" + lastPoint.y);
//            System.out.print("  " + selectedRandomPoint.label);
//            System.out.println("   " + selectedRandomPoint.x + "-" + selectedRandomPoint.y);

            int numerator = this.points.length - 2;
            int denumerator = this.points.length - 1;
//            Point nextPoint = new Point("halfway",
//                    (this.lastPoint.x + selectedRandomPoint.x) * numerator / denumerator,
//                    (this.lastPoint.y + selectedRandomPoint.y) * numerator / denumerator);
            int minX = Math.min(this.lastPoint.x, selectedRandomPoint.x);
            int maxX = Math.max(this.lastPoint.x, selectedRandomPoint.x);
            int minY = Math.min(this.lastPoint.y, selectedRandomPoint.y);
            int maxY = Math.max(this.lastPoint.y, selectedRandomPoint.y);

//            Point nextPoint = new Point("halfway",
//                    Math.min(this.lastPoint.x, selectedRandomPoint.x) + (Math.abs(this.lastPoint.x - selectedRandomPoint.x) * numerator / denumerator),
//                    Math.min(this.lastPoint.y, selectedRandomPoint.y) + (Math.abs(this.lastPoint.y - selectedRandomPoint.y) * numerator / denumerator));

            int nextX;
            if (this.lastPoint.x == minX)
                nextX = minX + (Math.abs(this.lastPoint.x - selectedRandomPoint.x) * numerator / denumerator);
            else
                nextX = maxX - (Math.abs(this.lastPoint.x - selectedRandomPoint.x) * numerator / denumerator);
            int nextY;
            if (this.lastPoint.y == minY)
                nextY = minY + (Math.abs(this.lastPoint.y - selectedRandomPoint.y) * numerator / denumerator);
            else
                nextY = maxY - (Math.abs(this.lastPoint.y - selectedRandomPoint.y) * numerator / denumerator);

            Point nextPoint = new Point("halfway", nextX, nextY);

            canvas.drawNextPoint(nextPoint);
            this.lastPoint = nextPoint;


            labelToUpdate.setText("" + (dot+1) + " / " + TOTAL_DOTS);
            try {
                Thread.sleep(DELAY_MILLIS);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }
    }
}

class Point  {
    public int x;
    public int y;
    public String label;

    public Point(String label, int x, int y) {
        this.x = x;
        this.y = y;
        this.label = label;
    }
}

interface Figure {
    public Point[] points();
}

class Triangle implements Figure {
    public Point[] points() {
        return new Point[] {
                new Point("P1", 75, 75),
                new Point("P2",820,  160),
                new Point("P3",320, 750),
        };
    }
}

class Square implements Figure {
    public Point[] points() {
        return new Point[] {
                new Point("P1",100, 50),
                new Point("P2",800,  50),
                new Point("P3",100, 750),
                new Point("P4",800, 750),
        };
    }
}

class Pentagon implements Figure {
    public Point[] points() {
        return new Point[]{
                new Point("P1", 450, 50),
                new Point("P2", 100, 400),
                new Point("P3", 800, 400),
                new Point("P4", 250, 750),
                new Point("P5", 650, 750),
        };
    }
}

class Exagon implements Figure {
    public Point[] points() {
        return new Point[] {
                new Point("P1",300, 50),
                new Point("P2",600,  50),
                new Point("P3",100, 400),
                new Point("P4",800, 400),
                new Point("P5",300, 750),
                new Point("P6",600, 750),
        };
    }
}