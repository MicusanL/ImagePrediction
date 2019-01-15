import bitreaderwriter.BitReader;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Decodeing {

    private int predictionType;
    private int[][][] predictionMatrix;
    private int[][][] errorMatrix;

    public Decodeing(String encodedFileName) {

        errorMatrix = readEncodedFile(encodedFileName);
        predictionMatrix = new int[Constant.PIXEL_NUMBER_IMAGE][Constant.PIXEL_NUMBER_IMAGE][3];

    }

    public int[][][] readEncodedFile(String encodedFileName) {


        int errorMatrix[][][] = new int[Constant.PIXEL_NUMBER_IMAGE][Constant.PIXEL_NUMBER_IMAGE][3];


        BitReader bitReader = new BitReader(encodedFileName);
        predictionType = bitReader.ReadNBits(4);
        int twoPowN[] = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};

        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                for (int k = 0; k < 3; k++) {

                    int bitsNumber = 0;
                    while (bitReader.ReadNBits(1) == 1) {
                        bitsNumber++;
                    }
                    if (bitsNumber == 0) {
                        errorMatrix[i][j][k] = 0;
                    } else {
                        int readValue = bitReader.ReadNBits(bitsNumber);

                        if (readValue < twoPowN[bitsNumber - 1]) {
//                            System.out.println("--"+bitsNumber+"--");
                            readValue -= twoPowN[bitsNumber] - 1;
                        }

                        errorMatrix[i][j][k] = readValue;
                    }
                }
            }

        }

        return errorMatrix;
    /*
    *
    * BitWriter bitWriterInstance = new BitWriter(outputFile);

        bitWriterInstance.WriteNBits(predictionType, 4);
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                writeByteValue(errorMatrix[i][j][0], bitWriterInstance);
                writeByteValue(errorMatrix[i][j][1], bitWriterInstance);
                writeByteValue(errorMatrix[i][j][2], bitWriterInstance);
            }
        }*/
    }

    public BufferedImage getDecryptedPicture(int w, int h) {
        BufferedImage decryptedPicture = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = decryptedPicture.getRaster();

        if (predictionType == 0) {
            for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
                for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {
                    for (int k = 0; k < 3; k++) {
                        predictionMatrix[i][j][k] = 128;
                    }
                }
            }
        }
        for (int i = 0; i < Constant.PIXEL_NUMBER_IMAGE; i++) {
            for (int j = 0; j < Constant.PIXEL_NUMBER_IMAGE; j++) {

                int px[] = new int[3];
                if (predictionType != 0) {
                    for (int k = 0; k < 3; k++) {

                        if (i == 0 && j == 0) {
                            predictionMatrix[i][j][k] = 128;
                        } else if (i == 0) {
                            predictionMatrix[i][j][k] = raster.getPixel(i, j - 1, (int[]) null)[k];
                        } else if (j == 0) {
                            predictionMatrix[i][j][k] = raster.getPixel(i - 1, j, (int[]) null)[k];
                        } else {
                            predictionMatrix[i][j][k] = new Coding(predictionType).predict((raster.getPixel(i - 1, j, (int[]) null))[k],
                                    (raster.getPixel(i, j - 1, (int[]) null)[k]),
                                    (raster.getPixel(i - 1, j - 1, (int[]) null))[k]);
                            if (predictionMatrix[i][j][k] < 0) {
                                predictionMatrix[i][j][k] = 0;
                            } else if (predictionMatrix[i][j][k] > 255) {
                                predictionMatrix[i][j][k] = 255;
                            }
                        }


                    }
                }
                for (int k = 0; k < 3; k++) {
                    px[k] = errorMatrix[i][j][k] + predictionMatrix[i][j][k];
                }
                raster.setPixel(i, j, px);

            }
        }

        return decryptedPicture;
    }
}
