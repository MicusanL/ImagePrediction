

/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Prediction extends Component {

    String descs[] = {"Original", "Convolve : LowPass", "Convolve : Sharpen",
            "LookupOp", "Grayscale"};

    int opIndex;

    private BufferedImage originalPicture, reconstructedPicture, errorColorPicture, biFiltered;

    int w, h;


    private static final int IMAGE_WIDTH = 200;
    private static final int IMAGE_HEIGHT = 200;

    private static final int ORIGINAL_IMAGE_X = 50;
    private static final int ORIGINAL_IMAGE_Y = 50;


    public Prediction(File imgFile) {
        try {
            originalPicture = ImageIO.read(imgFile);
            w = originalPicture.getWidth(null);
            h = originalPicture.getHeight(null);

            if (originalPicture.getType() != BufferedImage.TYPE_INT_RGB) {
                BufferedImage bi2 = new BufferedImage(w, h,
                        BufferedImage.TYPE_INT_RGB);
                Graphics big = bi2.getGraphics();

                big.drawImage(originalPicture, 0, 0, null);
                biFiltered = originalPicture = bi2;

            }
        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.exit(1);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }

    String[] getDescriptions() {
        return descs;
    }

    void setOpIndex(int i) {
        opIndex = i;
    }

    public void paint(Graphics g) {
//        filterImage();
        double mat[][] = new double[8][8];

        g.drawImage(originalPicture, ORIGINAL_IMAGE_X, ORIGINAL_IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        // JPEG();
        // g.drawImage(reconstructedPicture, ORIGINAL_IMAGE_X + IMAGE_WIDTH + 50, ORIGINAL_IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        g.drawImage(originalPicture, (ORIGINAL_IMAGE_X + IMAGE_WIDTH + 50) * 2, ORIGINAL_IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT, null);

    }

    int lastOp;





  /*  public static void main(String s[]) {
    //ShowConfiguration showConfiguration = new ShowConfiguration();
        JFrame f = new JFrame("Save Image Sample");
        f.setExtendedState( f.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        //f.add("Center", si);
        f.pack();
        f.setVisible(true);
        //Prediction si = new Prediction(jfc.getSelectedFile());
        String[] petStrings = {"128", "A", "B", "C", "A + B - C", "A + (B - C) / 2", "B + (A - C) / 2", "(A + B) / 2", "jpegLS"};
      //  f.setSize(500, 500);
        JComboBox predictionType = new JComboBox(petStrings);
        predictionType.setSelectedIndex(4);
        //f.setSize(300,300);
//        predictionType.addActionListener(this);
        f.add(predictionType);
        predictionType.setVisible(true);
        predictionType.setLocation(500, 500);
        predictionType.setSize(150, 20);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JFileChooser jfc = new JFileChooser();
        int r = jfc.showOpenDialog(f);
        if (r == JFileChooser.APPROVE_OPTION) {
            Prediction si = new Prediction(jfc.getSelectedFile());

            f.add("Center", si);
        }

    }*/
}
