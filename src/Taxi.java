import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

public class Taxi extends MovingMapItem {

  Person person; // Person associated with taxi

  PixelRoute route;


  public Taxi(String name, int x, int y) {
    super(name, 1, 1, 5);

    RouteFinder rf = new RouteFinder(Main.loader.getMask());
    setLocation(rf.getClosestRoadPoint(new Point(x, y)));

    state = new TaxiState();
  }

  public Taxi(String name) {
    this(name, 1, 1);

    Random rn = new Random();
    RouteFinder rf = new RouteFinder(Main.loader.getMask());

    int x = rn.nextInt(Main.loader.getWidth());
    int y = rn.nextInt(Main.loader.getHeight());

    setLocation(rf.getClosestRoadPoint(new Point(x, y)));

    state = new TaxiState();
  }

  public void act() {

    if (state.getState().equals("idle")) {
      if(dying()) {
        Main.simulation.removeMapItem(this);
        Main.taxiService.removeTaxi(this);
      }
    }


    if (state.getState().equals("Collecting Person")) {
      if(! person.inTaxi()) {
        setLocation(route.nextPoint(speed/2));// System.out.println("blah");
        if(getLocation().equals(person.getLocation())) {
          person.pickUp();
          RouteFinder rf = new RouteFinder(Main.loader.getMask());

          route = rf.findRoute(getLocation(), person.getEndPointOnRoad());
          try { route.initialise(); } catch (Exception e) {}
          state.nextState();
        }
      }
    }



    if (state.getState().equals("Moving Person")) {
      if(getLocation().equals(route.end)) {
        person.dropOff();
        state.nextState();

        person = null;
      } else setLocation(route.nextPoint(speed/2));
    }

  }

  public boolean isIdle() { return state.getState().equals("idle"); }

  public void collectPerson(Person p) {
    person = p;
    RouteFinder rf = new RouteFinder(Main.loader.getMask());

    route = rf.findRoute(getLocation(), person.getLocation());
    state.nextState();

    try { route.initialise(); } catch (Exception e) {}
  }

  public void draw(Graphics2D graphic) {
    graphic.setColor(Color.BLUE);

//        graphic.drawString(getName(), getLocation().x+4, getLocation().y-4);

    graphic.drawOval(getLocation().x - 3, getLocation().y - 3, 6, 6);
    graphic.drawOval(getLocation().x - 4, getLocation().y - 4, 8, 8);
  }

}
