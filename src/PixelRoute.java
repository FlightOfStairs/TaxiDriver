import java.awt.*;
import java.util.ArrayList;

/**
 * A 'route' which contains a list of all the pixels along the route.
 */
public class PixelRoute {

    boolean isComplete;
    ArrayList<Point> points;
    Point end;

    // For when we're following the route, so we know what point we're on
    int index;

    /**
     * Create an instance of PixelRoute
     * @param start The point the route is to start at
     * @param end The ultimate destination of the route
     */
    public PixelRoute(Point start, Point end) {
        points = new ArrayList<Point>();
        points.add(start);
        this.end = end;
        isComplete = false;

        index = -1;
    }

    /**
     * Get the last point in the route
     * @return
     */
    private Point getLastPoint() { return points.get(points.size() - 1); }

    /**
     * Add next point in route.
     * If the route is complete it will throw an exception
     * @param pt
     * @throws Exception
     */
    public void addPoint(Point pt) throws Exception {
        if(isComplete) throw new Exception("Adding point to complete route");
        points.add(pt);
        if(pt.equals(end)) isComplete = true;
    }

    /**
     * Gets the route ready for following.
     * Throws exception if isn't complete
     * @throws Exception
     */
    public void initialise() throws Exception {
        if(!isComplete) throw new Exception("Route isn't complete. Can't initialise.");
        index = 0;
    }

    /**
     * Return the next point 'speed' in the route and increment index by 'speed'
     * @return
     */
    public Point nextPoint(int speed) {
        if(index == -1) throw new NullPointerException("Route hasn't been initialised for following");
        if(index > points.size()) throw new ArrayIndexOutOfBoundsException("Reading past end of route");
        if(index + speed > points.size()) return getLastPoint();

        index += speed;

        return points.get(index - 1);
    }

}
