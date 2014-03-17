import javax.swing.*;
import java.awt.*;


public class InfoPanel extends JPanel {

  JList personList;
  DefaultListModel personListModel;
  public InfoPanel() {


    personListModel = new DefaultListModel();

    personList = new JList(personListModel);

    personList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    personList.setLayoutOrientation(JList.VERTICAL);
    personList.setVisibleRowCount(-1);

    JScrollPane listScroller = new JScrollPane(personList);

    listScroller.setPreferredSize(new Dimension(270, 200));


    add(listScroller, BorderLayout.CENTER);
  }

  public void updatePeople() {
    personListModel.setSize(20);
    for(int i = 0; i< 20; i++) {
       try { personListModel.set(i, Main.simulation.getPerson(i)); }
       catch (ArrayIndexOutOfBoundsException e) { personListModel.remove(i); }
    }
  }

  public Person getSelectedPerson() {
    try{ return ((Person) personListModel.getElementAt(personList.getSelectedIndex())); }
    catch (ArrayIndexOutOfBoundsException e) { return null; }
  }


}
