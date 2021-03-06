

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

import bitreaderwriter.BitWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Coding extends Component {
    private WritableRaster originalPictureWriter;

    private int predictionType;
    private BufferedImage originalPicture, reconstructedPicture, errorColorPicture, biFiltered;

    private int w, h;
    private String outputFileName;

    public Coding(File imgFile, int predictionType) {
        try {
            this.predictionType = predictionType;
            outputFileName = getOutputFileName(imgFile.getName());
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

    public Coding(int predictionType) {
        this.predictionType = predictionType;
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }


    public int predict(int a, int b, int c) {
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

    private int[][][] getErrorMatrix(int predictionMatrix[][][]) {

        int errorMatrix[][][] = new int[Constant.PIXEL_NUMBER_IMAGE][Constant.PIXEL_NUMBER_IMAGE][3];
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                errorMatrix[i][j][0] = originalPictureWriter.getPixel(i, j, (int[]) null)[0] - predictionMatrix[i][j][0];
                errorMatrix[i][j][1] = originalPictureWriter.getPixel(i, j, (int[]) null)[1] - predictionMatrix[i][j][1];
                errorMatrix[i][j][2] = originalPictureWriter.getPixel(i, j, (int[]) null)[2] - predictionMatrix[i][j][2];
            }
        }

        return errorMatrix;
    }

    private void writeByteValue(int value, BitWriter bitWriterInstance) {

        int byteValue;
        int absValue = Math.abs(value);

        for (int i = 0; i <= 8; i++) {

            int twoPowI = (int) Math.pow(2, i);

            if (absValue <= Math.pow(2, i) - 1) {
                if (value < 0) {
                    byteValue = ((twoPowI - 1) << i + 1) | ((~absValue) & (twoPowI - 1) % twoPowI);

                } else if (value > 0) {
                    byteValue = ((twoPowI - 1) << i + 1) + value % twoPowI;
                } else {
                    byteValue = 0;
                }
                if (i * 2 + 1 > 17) {
                    System.out.println("BV!!!!!!!!!!!!!!!!!! " + byteValue + " " + (i * 2 + 1));
                } else  if (value < -255 || value > 255){
                    System.out.println("VALUE!!!!!!!!!!!!!!!!!! " + byteValue + " " + (i * 2 + 1));
                } else if(byteValue>130815){
                    System.out.println("ByteVAl !!!!!!!!!!!!!!!!!! " + byteValue + " " + (i * 2 + 1));
                }
                bitWriterInstance.WriteNBits(byteValue, i * 2 + 1);
                return;
            }
        }
    }

    private int[][][] getPredictionMatrix() {

        originalPictureWriter = originalPicture.getRaster();
        int[][][] predictionMatrix = new int[Constant.PIXEL_NUMBER_IMAGE][Constant.PIXEL_NUMBER_IMAGE][3];


        if (predictionType == 0) {
            for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                    for (int k = 0; k < 3; k++) {
                        predictionMatrix[i][j][k] = 128;
                    }
                }
            }
        } else {
            for (int k = 0; k < 3; k++) {
                predictionMatrix[0][0][k] = 128;
            }

            for (int i = 1; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                for (int k = 0; k < 3; k++) {
                    predictionMatrix[i][0][k] = originalPictureWriter.getPixel(i - 1, 0, (int[]) null)[k];
                    predictionMatrix[0][i][k] = originalPictureWriter.getPixel(0, i - 1, (int[]) null)[k];
                }
            }

            for (int i = 1; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                for (int j = 1; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                    for (int k = 0; k < 3; k++) {
                        predictionMatrix[i][j][k] = predict(
                                (originalPictureWriter.getPixel(i - 1, j, (int[]) null)[k]),
                                (originalPictureWriter.getPixel(i, j - 1, (int[]) null))[k],
                                (originalPictureWriter.getPixel(i - 1, j - 1, (int[]) null))[k]);
                        if (predictionMatrix[i][j][k] < 0) {
                            predictionMatrix[i][j][k] = 0;
                        } else if (predictionMatrix[i][j][k] > 255) {
                            predictionMatrix[i][j][k] = 255;
                        }


                    }
                }
            }
        }


        return predictionMatrix;
    }

    private void writeCodedFile(String outputFile, int errorMatrix[][][]) {
        BitWriter bitWriterInstance = new BitWriter(outputFile);

        bitWriterInstance.WriteNBits(predictionType, 4);
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                writeByteValue(errorMatrix[i][j][0], bitWriterInstance);
                writeByteValue(errorMatrix[i][j][1], bitWriterInstance);
                writeByteValue(errorMatrix[i][j][2], bitWriterInstance);
            }
        }
        bitWriterInstance.CloseFile();
    }

    private void printMatrix(int[][][] matrix) {
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(matrix[i][j][k] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void paint(Graphics g) {

        g.drawImage(originalPicture, Constant.ORIGINAL_IMAGE_X, Constant.ORIGINAL_IMAGE_Y, Constant.IMAGE_WIDTH
                , Constant.IMAGE_HEIGHT, null);

        int[][][] predictionMatrix = getPredictionMatrix();
        int[][][] errorMatrix = getErrorMatrix(predictionMatrix);

        // printMatrix(errorMatrix);

        writeCodedFile(outputFileName, errorMatrix);

        Decodeing decodeing = new Decodeing(outputFileName);


        // printMatrix(errorMatrixOut);
        reconstructedPicture = decodeing.getDecryptedPicture(w, h);

        g.drawImage(reconstructedPicture, (Constant.ORIGINAL_IMAGE_X + Constant.IMAGE_WIDTH + 50),
                Constant.ORIGINAL_IMAGE_Y, Constant.IMAGE_WIDTH, Constant.IMAGE_HEIGHT, null);

    }

    private String getOutputFileName(String inputFile) {

        /* filename.bmp[predictionNumber].pre  */
        String[] parts = inputFile.split("\\\\");
        String outputFile = parts[parts.length - 1];
        parts = outputFile.split("\\.");
        outputFile = "C://Users//lidia//Desktop//criptare//" + parts[0] + "." + parts[1] + "[" + predictionType + "].pre";

        return outputFile;
    }
}
