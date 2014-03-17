import javax.swing.*;

public class Main {

  public static TaxiService taxiService;
  public static Gui gui;
  public static Simulation simulation;
  public static Loader loader;
  public static boolean paused;

  /**
   * @param args
   */
  public static void main(String[] args) {

    paused = true;



    // See if anything is on the command line...

    String map = "glasgow.map";

    if(args.length > 0) map = args[0];




    // Lets see what Look and feels we have... :)
    int lookAndFeel = 0;

    UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
    for (int i = 1; i < info.length; i++) {
      if (info[i].getClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
        lookAndFeel = i;
      }
      if ((info[i].getClassName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) && // If we have the windows one but not gtk...
              !(info[lookAndFeel].getClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))) {
        lookAndFeel = i;
      }
    }

    // System.out.println(info[lookAndFeel]);
    try {
      UIManager.setLookAndFeel(info[lookAndFeel].getClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    loader = new Loader();
    loader.loadFile(map);

    simulation = new Simulation(loader.getBackground());
    gui = new Gui(loader.getTitle());

//    taxiService = new TaxiService(loader.getTaxis());
//    simulation.addPersons(loader.getPersons());

    try {
      Thread.sleep(500);
    } catch (Exception e) {
    }


    simulation.drawBackground();

    simulation.repaint();

    taxiService = new TaxiService(loader.getTaxis());
    simulation.addPersons(loader.getPersons());


    simulation.act();



    while (true) {
      try { Thread.sleep(40); } catch (Exception e) { }

      if(paused) {
        continue;
      }

      simulation.act();
    }
  //gui.error("Success!");
  // System.exit(0);
  }
}
