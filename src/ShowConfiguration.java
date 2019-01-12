import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.TreeSet;

public class ShowConfiguration {
    private JComboBox comboBoxPredictionType;
    private JTextField textFieldScale;
    private JComboBox PredictionTypeShowedDiagram;
    private JButton buttonLoad;
    private JPanel panel1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane(new ShowConfiguration().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

    public String[] getFormats() {
        String[] formats = ImageIO.getWriterFormatNames();
        TreeSet formatSet = new TreeSet<>();
        for (int i=0; i<formats.length; i++) {
            formatSet.add(formats[i].toLowerCase());
        }
        //return formatSet.toArray(new String[0]);
        Object[] objSet = formatSet.toArray();
        String[] sortedFormats = new String[objSet.length];
        for (int i=0; i<objSet.length; i++)
            sortedFormats[i] = objSet[i].toString();
        return sortedFormats;
    }

      public ShowConfiguration(){

        buttonLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame f = new JFrame("Save Image Sample");
                f.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });


                JFileChooser jfc = new JFileChooser();
                int r = jfc.showOpenDialog(buttonLoad);
                if (r == JFileChooser.APPROVE_OPTION) {
                    Prediction si = new Prediction(jfc.getSelectedFile());


                    f.add("Center", si);
                    JComboBox choices = new JComboBox(si.getDescriptions());
//                    choices.setActionCommand("SetFilter");
//                    choices.addActionListener(si);
//                    JComboBox formats = new JComboBox(si.getFormats());
//                    formats.setActionCommand("Formats");
//                    formats.addActionListener(si);
                    JPanel panel = new JPanel();
                    panel.add(choices);
                    panel.add(new JLabel("Save As"));
//                    panel.add(formats);
                    f.add("South", panel);
                    f.pack();
                    f.setVisible(true);
                }


                /*
                JFrame f = new JFrame("Save Image Sample");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		JFileChooser jfc = new JFileChooser();
		int r = jfc.showOpenDialog(f);
		if (r == JFileChooser.APPROVE_OPTION) {
			SaveImage si = new SaveImage(jfc.getSelectedFile());
			f.add("Center", si);
			JComboBox choices = new JComboBox(si.getDescriptions());
			choices.setActionCommand("SetFilter");
			choices.addActionListener(si);
			JComboBox formats = new JComboBox(si.getFormats());
			formats.setActionCommand("Formats");
			formats.addActionListener(si);
			JPanel panel = new JPanel();
			panel.add(choices);
			panel.add(new JLabel("Save As"));
			panel.add(formats);
			f.add("South", panel);
			f.pack();
			f.setVisible(true);
		}
                 */
                //Prediction lzwCode = new Prediction();
               // lzwCode.codeUsingLZW(index, fullDictionaryAction);


            }
        });
    }
}
