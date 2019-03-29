package nl.saxion;

public class Vector implements java.io.Serializable{

    double x = 0;
    double y = 0;


    private static final long serialVersionUID = 1L;
    public Vector (double pX, double pY) {
        x = pX;
        y = pY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector clone() {
        return new Vector (x, y);
    }

    public Vector add (Vector pOther) {
        x += pOther.x;
        y += pOther.y;
        return this;
    }

    public Vector sub (Vector pOther) {
        x -= pOther.x;
        y -= pOther.y;
        return this;
    }

    public Vector scale(double pScale) {
        x *= pScale;
        y *= pScale;
        return this;
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector normalize() {
        double length = length();
        if (length != 0) {
            x /= length;
            y /= length;
        }
        return this;
    }

    public double angleInRadians() {
        return Math.atan2(x,y);
    }

    public double angleInDegrees() {
        return Math.toDegrees(angleInRadians());
    }

    public static double distance (Vector a, Vector b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

}