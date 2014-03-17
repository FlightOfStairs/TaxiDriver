import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

public class Person extends MovingMapItem {

  private Taxi taxi;
  private Point destination;
  private Point pointOnRoadStart;
  private Point pointOnRoadEnd;

  public Person(String name) {
    super(name, 1, 1, 1);

    Random rn = new Random();
    if (rn.nextInt(50) == 0) {
      setName("The Murray");
    }
    Point location = new Point(rn.nextInt(Main.loader.getWidth()) + 1, rn.nextInt(Main.loader.getHeight()) + 1);
    destination = new Point(rn.nextInt(Main.loader.getWidth()) + 1, rn.nextInt(Main.loader.getHeight()) + 1);
    setLocation(location.x, location.y);

    state = new PersonState();
  }

  public void act() {
    if (state.getState() == "idle") {


      // If we're getting rid of this object...
      if(dying()) {
        Main.simulation.removeMapItem(this);
      }

      Random rn = new Random();
      if (rn.nextInt(100) == 0) {
        state.nextState();
        Point location = new Point(rn.nextInt(Main.loader.getWidth()) + 1, rn.nextInt(Main.loader.getHeight()) + 1);
        destination = new Point(rn.nextInt(Main.loader.getWidth()) + 1, rn.nextInt(Main.loader.getHeight()) + 1);
        setLocation(location.x, location.y);
      }
    }

    if (state.getState() == "Walking to Road") {
      walkToRoad();
    }

    if (state.getState() == "Calling Taxi") {

      if(dying()) {
        Main.simulation.removeMapItem(this);
      }

      Taxi tempTaxi = Main.taxiService.callTaxi(this);

      if (tempTaxi != null) {
        state.nextState();
        taxi = tempTaxi;
      }
    }

    if (state.getState() == "In Taxi") {
      setLocation(taxi.getLocation().x, taxi.getLocation().y);
    }

    if (state.getState() == "Walking to Destination") {
      if (getLocation().equals(destination)) {
        state.nextState();
        return;
      }
      moveTo(destination);
    }

    if (state.getState() == "done") {
    }

  }

  public void pickUp() {
    pointOnRoadStart = null;
    state.nextState();
  }

  void dropOff() {
    pointOnRoadEnd = null;
    state.nextState();
  }

  private void walkToRoad() {
    if (pointOnRoadStart == null) {
      RouteFinder rf = new RouteFinder(Main.loader.getMask());
      pointOnRoadStart = rf.getClosestRoadPoint(getLocation());
    }

    if (getLocation().equals(pointOnRoadStart)) {
      state.nextState();
      return;
    }

    moveTo(pointOnRoadStart);
  }

  public Point getEndPointOnRoad() {
    if (pointOnRoadEnd == null) {
      RouteFinder rf = new RouteFinder(Main.loader.getMask());
      pointOnRoadEnd = rf.getClosestRoadPoint(destination);
    }
    return pointOnRoadEnd;
  }

  /**
   * Make the person move towards the point by one pixel (orthogonally or diagonally). Persons move similar to the manhattan method with the diagonal shortcut.
   * @param point The Point object the person is to move towards.
   */
  private void moveTo(Point point) {
    Point nextLocation = getLocation();

    if (point.x > nextLocation.x) {
      nextLocation.x++;
    }
    if (point.x < nextLocation.x) {
      nextLocation.x--;
    }
    if (point.y > nextLocation.y) {
      nextLocation.y++;
    }
    if (point.y < nextLocation.y) {
      nextLocation.y--;
    }
    setLocation(nextLocation.x, nextLocation.y);
  }

  public boolean inTaxi() {
    if (state.getState().equals("In Taxi")) {
      return true;
    }
    return false;
  }

    public boolean idle() {
    if (state.getState().equals("idle")) {
      return true;
    }
    return false;
  }


  public void draw(Graphics2D graphic) {

    if (state.getState() == "idle") {
      return;
    }
    graphic.setColor(Color.RED);

    graphic.setFont(new Font("Arial", Font.PLAIN, 10));
    graphic.drawString(getName(), getLocation().x + 1, getLocation().y - 1);

    graphic.fillOval(getLocation().x - 2, getLocation().y - 2, 4, 4);

    graphic.setColor(Color.GREEN.darker());
    graphic.fillRect(destination.x, destination.y, 2, 2);
    graphic.drawString(getName(), destination.x + 1, destination.y - 1);

  }

  @Override
  public String toString() {
    return getName() + " - " + state.getState();


  }

}
