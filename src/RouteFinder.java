import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Class to find routes on map between two points
 *
 */
public class RouteFinder {

  BufferedImage mask;

  /**
   * Create new instance of RouteFinder
   *
   */
  public RouteFinder(BufferedImage mask) {

    this.mask = mask;
  }

  /**
   * Get a resonably close point on a road. Not really the closest, as it finds diagonal points before ones along the same line. Works by looking at the border of an expanding square centered on the start point until it finds a white pixel.
   * @param start Point from which to start the search
   * @return A reasonably close point which on the mask image is white.
   */
  public Point getClosestRoadPoint(Point start) {
    if ((onScreen(start.x, start.y)) && mask.getRGB(start.x, start.y) == Color.WHITE.getRGB()) {
      return start;
    }

    int halfLength = 0;


    while(true) {
      halfLength++;


      for(int x = -halfLength; x <= halfLength; x++) {
        //Check top side
        if(onScreen(start.x + x, start.y - halfLength)) {
          if(mask.getRGB(start.x + x, start.y - halfLength) == Color.WHITE.getRGB()) return new Point(start.x + x, start.y - halfLength);
        }
        // Check bottom
        if(onScreen(start.x + x, start.y + halfLength)) {
          if(mask.getRGB(start.x + x, start.y + halfLength) == Color.WHITE.getRGB()) return new Point(start.x + x, start.y + halfLength);
        }
      }

      for(int y = -halfLength; y <= halfLength; y++) {
        //Check left side
        if(onScreen(start.x - halfLength, start.y + y)) {
          if(mask.getRGB(start.x - halfLength, start.y + y) == Color.WHITE.getRGB()) return new Point(start.x - halfLength, start.y + y);
        }
        // Check right
        if(onScreen(start.x + halfLength, start.y + y)) {
          if(mask.getRGB(start.x + halfLength, start.y + y) == Color.WHITE.getRGB()) return new Point(start.x + halfLength, start.y + y);
        }
      }

    }



  }

  /**
   * Returns route between two points. Implementation of the A* algorithm.
   * @param start
   * @param end
   * @return
   */
  /*
   * Complex. An 'easy' to follow description can be found at http://www.policyalmanac.org/games/aStarTutorial.htm (Alistair Smith is not responsible for external sites).
   * The plan is that this code is equivelent to that description. We'll see how that goes... *-)
   */
  public PixelRoute findRoute(Point start, Point end) {
    //ArrayList<PixelRoute> routes = new ArrayList<PixelRoute>();

    Main.gui.setTitle(Main.gui.getTitle() + " - Thinking...");

    Main.simulation.getGraphic().setColor(Color.RED);
    Main.simulation.getGraphic().setFont(new Font("Arial", Font.BOLD, 20));
    Main.simulation.getGraphic().drawString("Thinking...", 10, 30);

    Main.simulation.repaint();

    boolean atEnd = false;

    ArrayList<Node> openList = new ArrayList<Node>();
    ArrayList<Node> closedList = new ArrayList<Node>();


    start = getClosestRoadPoint(start);
    end = getClosestRoadPoint(end);

    Node startNode = new Node(start, null);
    Node endNode = new Node(end, null);


    // Add first Point to openList.
    openList.add(startNode);


    // Add all possible points surrounding start to openList
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        if (!((x == 0) && (y == 0))) {
          if (mask.getRGB(openList.get(openList.size() - 1).x + x, openList.get(openList.size() - 1).y + y) == Color.WHITE.getRGB()) {
            openList.add(new Node(new Point(openList.get(openList.size() - 1).x + x, openList.get(openList.size() - 1).y + y), openList.get(openList.size() - 1)));
          }
        }
      }
    }

    // Switch start from open list to closed list
    openList.remove(startNode);
    closedList.add(startNode);

    do {

      // Look for the node with the lowest cost. This is going to be quite expensive, so optimise as much as sensible. The less calls to cost() the better.
      int tempCost;

      Node lowestCostNode = openList.get(0);
      int lowestCost = cost(openList.get(0), startNode, endNode); // Just so we don't have to go through the expensive cost() method each time.

      for (int i = 1; i < openList.size(); i++) {
        tempCost = cost(openList.get(i), startNode, endNode);
        if (tempCost <= lowestCost) { // Use the one closest to the end if possible. "For the purposes of speed, it can be faster to choose the last one you added to the open list." http://www.policyalmanac.org/games/aStarTutorial.htm

          lowestCost = tempCost;
          lowestCostNode = openList.get(i);
        }
      }

      // Switch from open list to closed list
      openList.remove(lowestCostNode);
      closedList.add(lowestCostNode);

      /*
      // Add new nodes to openlist
      for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
      if (pointOnList(new Point(lowestCostNode.x + x, lowestCostNode.y + y), closedList) == null) { // if they are not on the closed list...

      if (mask.getRGB(lowestCostNode.x + x, lowestCostNode.y + y) == Color.WHITE.getRGB()) // If the point color is white...
      {
      if (pointOnList(new Point(lowestCostNode.x + x, lowestCostNode.y + y), openList) == null) { // If they're not on the open list

      openList.add(new Node(new Point(lowestCostNode.x + x, lowestCostNode.y + y), lowestCostNode));
      if (endNode.equals(new Node(new Point(lowestCostNode.x + x, lowestCostNode.y + y), lowestCostNode))) {
      atEnd = true;
      }
      } else { // if they are on the open list...

      Node tempPointOnList = pointOnList(new Node(new Point(lowestCostNode.x + x, lowestCostNode.y + y), lowestCostNode), openList); // get the point with the same location on the open list

      int tempG = g(tempPointOnList, startNode);
      Node comareRouteNode = new Node(new Point(lowestCostNode.x + x, lowestCostNode.y + y), lowestCostNode);
      if (g(comareRouteNode, startNode) < tempG) // if the cost to the start is lower using new route
      {
      tempPointOnList.setParent(lowestCostNode);
      }
      }
      }
      }
      }
      }
       */

      ArrayList<Node> nextNodes = getValidSurroundingNodes(lowestCostNode);

      for (int i = 0; i < nextNodes.size(); i++) {
        Node n = new Node(nextNodes.get(i), lowestCostNode);
        if (pointOnList(n, closedList) == null) {  // if it's not on the closed list...

          if (pointOnList(n, openList) == null) {   // if it's not on the open list...

            openList.add(n); // add it to the open list

            if (endNode.getLocation().equals(n.getLocation())) {
              atEnd = true;
              endNode.setParent(lowestCostNode);
            }
          } else {  // it is on the open list...

            Node tpol = pointOnList(n, openList);
            int tempG = g(tpol, startNode);
            if (g(n, startNode) < tempG) {
              tpol.setParent(lowestCostNode);
            }
          }
        }
      }
    } while (!atEnd);



    ArrayList<Point> routePoints = new ArrayList<Point>();

    //endNode = pointOnList(endNode, openList);
    Node n = endNode.parent();

    routePoints.add(endNode.getLocation());


    while (n != null) {
      routePoints.add(n.getLocation());
      n = n.parent();

    }

    // routePoints is in the wrong order...

    PixelRoute pr = new PixelRoute(startNode.getLocation(), endNode.getLocation());

    for (int i = routePoints.size() - 1; i >= 0; i--) {
      try {
        pr.addPoint(routePoints.get(i).getLocation());
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }


    Main.gui.setTitle(Main.loader.getTitle());

    return pr;

  }

  private boolean onScreen(int x, int y) {
    try{
      mask.getRGB(x, y);
    } catch(Exception e) {
      return false;
    }
    return true;
  }

  private Node pointOnList(Point point, ArrayList<Node> list) {
    Node listItem;
    for (int i = 0; i < list.size(); i++) {
      listItem = list.get(i);
      if (point.equals(listItem)) {
        return listItem;
      }
    }
    return null;
  }

  private ArrayList<Node> getValidSurroundingNodes(Node n) {
      ArrayList<Point> tempArrayList = new ArrayList<Point>();

    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        if (!((x == 0) && (y == 0))) {
          tempArrayList.add(new Point(n.x + x, n.y + y));
        }
      }
    }

    // now we check to see if any of these isn't white on the mask.

//    System.out.println(tempArrayList.size());

      ArrayList<Node> newList = new ArrayList<Node>();

    Node nextItem;


    for (int i = 0; i < tempArrayList.size(); i++) {
      nextItem = new Node(tempArrayList.get(i), n);
      //System.out.println(nextItem);
      if ((onScreen(nextItem.x, nextItem.y)) && mask.getRGB(nextItem.x, nextItem.y) == Color.WHITE.getRGB()) {
        newList.add(nextItem);
      }
    }
    //System.out.println(newList.size());

    return newList;
  }

  private int cost(Node n, Node startNode, Node endNode) {
    // h = heuristic. Using manhattan method with diagonal shortcut.
    int h = 0;

    int xDistance = Math.abs(n.x - endNode.x);
    int yDistance = Math.abs(n.y - endNode.y);
    if (xDistance > yDistance) {
      h = 14 * yDistance + 10 * (xDistance - yDistance);
    } else {
      h = 14 * xDistance + 10 * (yDistance - xDistance);
    // the total cost is movement cost to start + heuristic
    }
    return g(n, startNode) + h;
  }

  private int g(Node n, Node startNode) {
    // g = the movement cost to move from the starting point to a n, following the path generated to get there.
    int g = 0;

    Node parent;
    do {
      // Add 10 if next point is orthogonal, 14 for diagonal. see comment above.
      parent = n.parent;
      if ((n.x == parent.x) || (n.y == parent.y)) {
        g += 10; // we're now only considering every third pixel

      } else {
        g += 14;
      // n becomes equal to the the next point (it's parent)
      }
      n = parent;
    } while (n != startNode);
    return g;
  }

  /**
   * Very simple extension to Point, which remembers what the last point was. Important for routefinding. Basicly just a linked list
   */
  private class Node extends Point {

    private Node parent;

    public Node(Point pt, Node parent) {
      super(pt);
      this.parent = parent;
    }

    public Node parent() {
      return parent;
    }

    public void setParent(Node n) {
      parent = n;
    }
  }
}
