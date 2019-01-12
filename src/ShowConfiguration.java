import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.TreeSet;

public class ShowConfiguration {
    private JComboBox comboBoxPredictionType;
    private JTextField textFieldScale;
    private JComboBox PredictionTypeShowedDiagram;
    private JButton buttonLoad;
    private JPanel panel1;
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("UserInterface");
        frame.setContentPane(new ShowConfiguration().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }


    private ShowConfiguration() {

        buttonLoad.addActionListener(e-> {

                JFrame f = new JFrame("Image Prediction");
                f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                f.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
/*
button.addActionListener(e -> {
    System.out.println("Handled Lambda listener");
    System.out.println("Have fun!");
});
 */

                JFileChooser jfc = new JFileChooser();
                int r = jfc.showOpenDialog(buttonLoad);
                if (r == JFileChooser.APPROVE_OPTION) {
                    Coding si = new Coding(jfc.getSelectedFile(), comboBoxPredictionType.getSelectedIndex());

                    frame.setVisible(false);
                    f.add("Center", si);
                    JPanel panel = new JPanel();

                    panel.add(new JLabel("Save As"));

                    f.add("South", panel);
                    f.pack();
                    f.setVisible(true);
                }


        });
    }
}
