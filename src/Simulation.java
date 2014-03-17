import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Simulation extends JPanel {
//    private CanvasPane canvas;

  private Graphics2D graphic;
  private Image canvasImage;
  private Image background;
  private ArrayList<MovingMapItem> items;
  int passengerCounter;

  public Simulation(BufferedImage image) {
    passengerCounter = 0;

    items = new ArrayList<MovingMapItem>();

    Dimension size = new Dimension(image.getWidth(), image.getHeight());

    setPreferredSize(size);

    /*
    canvas = new CanvasPane();
    canvas.setPreferredSize(size);
     */


    background = image;
  }

  public void initialise() {
    Dimension size = getPreferredSize();

    canvasImage = createImage(size.width, size.height);

    graphic = (Graphics2D) canvasImage.getGraphics();
    graphic.setColor(Color.WHITE);
    graphic.fillRect(0, 0, size.width, size.height);
    graphic.setColor(Color.black);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);    // paints background

    g.drawImage(canvasImage, 0, 0, null);

  }



  public void removeMapItem(MapItem item) {
    items.remove(item);
  }

  public void drawBackground() {
    //graphic.drawImage(background, 0, 0, Color.BLACK, null);

    graphic.drawImage(background, 0, 0, Color.BLACK, null); // :)

    try {
      Thread.sleep(10);
    } catch (Exception e) {
    }
  }

  public void act() {
    drawBackground();



    for (int i = 0; i < items.size(); i++) {
      items.get(i).draw(graphic);

    }

    Main.gui.update();

    for (int i = 0; i < items.size(); i++) {
      ((MovingMapItem) items.get(i)).act();

    }
    repaint();

  //
  }

  public void addTaxi(Taxi tempTaxi) {
    items.add(tempTaxi);
  }

  public int getPeople() {
    int persons = 0;
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) instanceof Person) {
        persons++;
      }
    }
    return persons;
  }

  public int getHealthyPeople() {
    int persons = 0;
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) instanceof Person) {
        if(! items.get(i).dying() ) persons++;
      }
    }
    return persons;
  }

  public Person getPerson(int i) {
    int count = 0;
    for (int j = 0; j < items.size(); j++) {
      if (items.get(j) instanceof Person) {
        if (count == i) {
          return (Person) items.get(j);
        }
        count++;
      }
    }
    return null;
  }

    public void addPersons(int persons) {
    for (int i = 1; i <= persons; i++) {
      items.add(new Person("Person " + (++passengerCounter)));
    }
  }

  public void removePersons(int persons) {
    int i = 0;
    try {
      int loops = 0;
      while (i < persons) {
        loops++;
        if (items.get(loops) instanceof Person) {
          if (!((MovingMapItem) items.get(loops)).dying()) {
            ((MovingMapItem) items.get(loops)).kill();
            i++;
          }
        }
      }
    } catch  (ArrayIndexOutOfBoundsException e) {
    return; /* Out of bounds */ }
  }


  public int getTaxis() {
    int taxis = 0;
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) instanceof Taxi) {
        taxis++;
      }
    }
    return taxis;
  }


  public int getHealthyTaxis() {
    int taxis = 0;
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i) instanceof Taxi) {
        if(! items.get(i).dying() ) taxis++;
      }
    }
    return taxis;
  }


  public void removeTaxis(int taxis) {
    int i = 0;
    try {
      int loops = 0;
      while (i < taxis) {
        loops++;
        if (items.get(loops) instanceof Taxi) {
          if (!((MovingMapItem) items.get(loops)).dying()) {
            ((MovingMapItem) items.get(loops)).kill();
            i++;
          }
        }
      }
    } catch  (ArrayIndexOutOfBoundsException e) {
    return; /* Out of bounds */ }
  }


  public Graphics2D getGraphic() {
    return graphic;
  }
}