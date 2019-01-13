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
ShowResult.frame = new JFrame("Image Prediction");
            ShowResult.frame.setExtendedState(ShowResult.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            ShowResult.frame.addWindowListener(new WindowAdapter() {
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
                    ShowResult.frame.add("Center", si);

                    ShowResult.frame.add("South", new ShowResult().panel1);
                    ShowResult.frame.pack();
                    ShowResult.frame.setVisible(true);
                }


        });
    }
}
