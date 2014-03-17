import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ControlPanel extends JPanel {

  JLabel peopleStatusLabel;
  JLabel taxiStatusLabel;

  public ControlPanel() {

    JPanel peoplePanel = new JPanel();
    JLabel peopleLabel = new JLabel("People");
    JSpinner peopleSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    peopleStatusLabel = new JLabel("   People still to remove");

    JPanel taxiPanel = new JPanel();
    JLabel taxiLabel = new JLabel("Taxis");
    JSpinner taxiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    taxiStatusLabel = new JLabel("   Taxi(s) still to remove");


    peopleSpinner.setValue(Main.loader.getPersons());
    taxiSpinner.setValue(Main.loader.getTaxis());

    peoplePanel.setLayout(new GridLayout(2, 2));
    taxiPanel.setLayout(new GridLayout(2, 2));

    peoplePanel.add(peopleLabel);
    peoplePanel.add(peopleSpinner);
    peoplePanel.add(peopleStatusLabel);

    taxiPanel.add(taxiLabel);
    taxiPanel.add(taxiSpinner);
    taxiPanel.add(taxiStatusLabel);


    setLayout(new GridLayout(0, 1));




    add(peoplePanel);
    add(taxiPanel);

    peopleStatusLabel.setVisible(false);
    taxiStatusLabel.setVisible(false);


    peopleSpinner.addChangeListener(new ChangeListener() {

      public void stateChanged(ChangeEvent e) {
        //SpinnerModel sm = ((JSpinner) e.getSource()).getModel();

        int value = ((Integer) ((JSpinner) e.getSource()).getValue());

        if (value > Main.simulation.getHealthyPeople()) {
          Main.simulation.addPersons(value - Main.simulation.getHealthyPeople());
        }
        if (value < Main.simulation.getHealthyPeople()) {
          Main.simulation.removePersons(Main.simulation.getHealthyPeople() - value);
        }
      }
    });


    taxiSpinner.addChangeListener(new ChangeListener() {

      public void stateChanged(ChangeEvent e) {
        //SpinnerModel sm = ((JSpinner) e.getSource()).getModel();

        int value = ((Integer) ((JSpinner) e.getSource()).getValue());

        if (value > Main.simulation.getHealthyTaxis()) {
          Main.taxiService.addTaxis(value - Main.simulation.getHealthyTaxis());
        }
        if (value < Main.simulation.getHealthyTaxis()) {
          Main.simulation.removeTaxis(Main.simulation.getHealthyTaxis() - value);
        }
      }
    });


  }

  public void update() {
    if (Main.simulation.getPeople() != Main.simulation.getHealthyPeople()) {
      peopleStatusLabel.setVisible(true);
    } else {
      peopleStatusLabel.setVisible(false);
    }

    if (Main.simulation.getTaxis() != Main.simulation.getHealthyTaxis()) {
      taxiStatusLabel.setVisible(true);
    } else {
      taxiStatusLabel.setVisible(false);
    }
  }
}
