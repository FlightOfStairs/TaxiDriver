import javax.swing.*;
import java.awt.*;

public class Gui {

  private static final int RIGHTPANEL_WIDTH = 300;
  private JFrame frame;
  private JPanel infoPanel;
  private JPanel controlPanel;
  private JPanel pausePanel;
  private Simulation simulation;
  private Dimension size;

  public Gui(String string) {
    //try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) { e.printStackTrace(); }

    simulation = Main.simulation;

    frame = new JFrame(string);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Container contentPane = frame.getContentPane();


    controlPanel = new ControlPanel();
    infoPanel = new InfoPanel();
    pausePanel = new PausePanel();

    JPanel rightPanel = new JPanel();


    contentPane.setLayout(new FlowLayout());
    rightPanel.setLayout(new BorderLayout());

    rightPanel.setPreferredSize(new Dimension(RIGHTPANEL_WIDTH, Main.loader.getHeight()));


    simulation.setBorder(BorderFactory.createTitledBorder(""));
    controlPanel.setBorder(BorderFactory.createTitledBorder("Actors"));
    infoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
    pausePanel.setBorder(BorderFactory.createTitledBorder(" "));

    rightPanel.add(controlPanel, BorderLayout.NORTH);
    rightPanel.add(infoPanel, BorderLayout.CENTER);
    rightPanel.add(pausePanel, BorderLayout.SOUTH);


    contentPane.add(simulation);
    contentPane.add(rightPanel);


    frame.pack();

    contentPane.setMaximumSize(contentPane.getSize());

    size = frame.getSize();

    frame.setMaximumSize(size);
    frame.setMinimumSize(size);

    frame.setVisible(true);

    simulation.initialise();

  }

  public void setTitle(String title) {
    frame.setTitle(title);
  }

  public String getTitle() {
    return frame.getTitle();
  }

  public void update() {
    ((InfoPanel) infoPanel).updatePeople();
    ((ControlPanel) controlPanel).update();

    Person selected = ((InfoPanel) infoPanel).getSelectedPerson();

    if ((selected == null)||(selected.idle())) return;

    simulation.getGraphic().setColor(Color.GREEN);

    simulation.getGraphic().drawOval(selected.getLocation().x - 4, selected.getLocation().y - 4, 8, 8);
    simulation.getGraphic().drawOval(selected.getLocation().x - 5, selected.getLocation().y - 5, 10, 10);

    simulation.repaint();
  }



}
