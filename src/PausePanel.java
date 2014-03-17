import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PausePanel extends JPanel {

  JButton pause;

  public PausePanel() {
    setLayout(new FlowLayout());

    pause = new JButton("Start");

    //pause.setPreferredSize(new Dimension (getSize().width - 10, getSize().height - 10));

    pause.setPreferredSize(new Dimension (200, 50));

    pause.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if(Main.paused) {
          Main.paused = false;
          ((JButton) e.getSource()).setText("Pause");
        } else {
          Main.paused = true;
          ((JButton) e.getSource()).setText("Start");
        }
      }
    });



    add(pause);

  }

}
