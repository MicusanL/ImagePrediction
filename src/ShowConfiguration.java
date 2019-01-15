import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ShowConfiguration {
    private JComboBox comboBoxPredictionType;
    private JTextField textFieldScale;
    private JComboBox PredictionTypeShowedDiagram;
    private JButton buttonLoad;
    private JPanel panel1;
    private static JFrame configurationFrame;
public static String[] argsCopy;


    public static void main(String[] args) {
        argsCopy = args;
        configurationFrame = new JFrame("UserInterface");
        configurationFrame.setContentPane(new ShowConfiguration().panel1);
        configurationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configurationFrame.pack();
        configurationFrame.setVisible(true);


    }


    private ShowConfiguration() {

        buttonLoad.addActionListener(e -> {
            ShowResult.resultFrame = new JFrame("Image Prediction");
            ShowResult.resultFrame.setExtendedState(ShowResult.resultFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            ShowResult.resultFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });


            JFileChooser jfc = new JFileChooser();
            int r = jfc.showOpenDialog(buttonLoad);
            if (r == JFileChooser.APPROVE_OPTION) {
                Coding si = new Coding(jfc.getSelectedFile(), comboBoxPredictionType.getSelectedIndex());

                configurationFrame.setVisible(false);
                ShowResult.resultFrame.add("Center", si);
                ShowResult.resultFrame.add("South", new ShowResult().panel1);
                ShowResult.resultFrame.pack();
                ShowResult.resultFrame.setVisible(true);
            }


        });
    }
}
