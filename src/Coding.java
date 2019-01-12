

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
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Coding extends Component {


    int opIndex;

    private int predictionType;
    private BufferedImage originalPicture, reconstructedPicture, errorColorPicture, biFiltered;

    int w, h;


    public Coding(File imgFile, int predictionType) {
        try {
            this.predictionType = predictionType;

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

    WritableRaster originalPictureWriter;

    private int getGrayScale(int[] rgb) {
        return (rgb[0] + rgb[1] + rgb[2]) / 3;
    }

    private int predict(int a, int b, int c) {
        switch (predictionType) {
            case 1:
                return a;

            case 2:
                return b;

            case 3:
                return c;

            case 4:
                return a + b - c;

            case 5:
                return a + (b - c) / 2;

            case 6:
                return b + (a - c) / 2;

            case 7:
                return (a + b) / 2;

            case 8:
                if (c >= Math.max(a, b)) {
                    return Math.min(a, b);
                }

                if (c <= Math.min(a, b)) {
                    return Math.max(a, b);
                }

                return a + b - c;
        }
        return 128;
    }

    private int[][] getErrorMatrix(int predictionMatrix[][]) {

        int errorMatrix[][] = new int[Constant.PIXEL_NUMBER_IMAGE][Constant.PIXEL_NUMBER_IMAGE];
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                errorMatrix[i][j] = getGrayScale(originalPictureWriter.getPixel(i, j, (int[]) null)) - predictionMatrix[i][j];
            }
        }

        return errorMatrix;
    }

    private int[][] getPredictionMatrix() {

        originalPictureWriter = originalPicture.getRaster();
        int[][] predictionMatrix = new int[Constant.PIXEL_NUMBER_IMAGE][Constant.PIXEL_NUMBER_IMAGE];


        if (predictionType == 0) {
            for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                    predictionMatrix[i][j] = 128;
                }
            }
        } else {

            predictionMatrix[0][0] = 128;

            for (int i = 1; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                predictionMatrix[i][0] = getGrayScale(originalPictureWriter.getPixel(i - 1, 0, (int[]) null));
                predictionMatrix[0][i] = getGrayScale(originalPictureWriter.getPixel(0, i - 1, (int[]) null));
            }

            for (int i = 1; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                for (int j = 1; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                    predictionMatrix[i][j] = predict(getGrayScale(originalPictureWriter.getPixel(i - 1, j, (int[]) null)),
                            getGrayScale(originalPictureWriter.getPixel(i, j - 1, (int[]) null)),
                            getGrayScale(originalPictureWriter.getPixel(i - 1, j - 1, (int[]) null)));
                }
            }
        }


        return predictionMatrix;
    }

    public void paint(Graphics g) {
//        filterImage();
        double mat[][] = new double[8][8];

        g.drawImage(originalPicture, Constant.ORIGINAL_IMAGE_X, Constant.ORIGINAL_IMAGE_Y, Constant.IMAGE_WIDTH
                , Constant.IMAGE_HEIGHT, null);
        // JPEG();
        int[][] predictionMatrix = getPredictionMatrix();
        /*for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                System.out.print(predictionMatrix[i][j] + " ");
            }
            System.out.println();
        }*/
        int[][] errorMatrix = getErrorMatrix(predictionMatrix);
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                System.out.print(errorMatrix[i][j] + " ");
            }
            System.out.println();
        }
        // g.drawImage(reconstructedPicture, ORIGINAL_IMAGE_X + IMAGE_WIDTH + 50, ORIGINAL_IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        g.drawImage(originalPicture, (Constant.ORIGINAL_IMAGE_X + Constant.IMAGE_WIDTH + 50) * 2,
                Constant.ORIGINAL_IMAGE_Y, Constant.IMAGE_WIDTH, Constant.IMAGE_HEIGHT, null);

    }


}
