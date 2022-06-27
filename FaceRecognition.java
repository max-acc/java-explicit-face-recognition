
/**
 * Class for very simple face recognition.
 * This is not about classifying humans by their skin color (there are only 6 skincolors for simplyfying reasons).
 * It is not about racism or colorism in any thinkable way.
 * 
 * This website has been used color palette:
 * https://colorcodes.io/fair-skin-color-codes/
 * 
 * Required classes:
 * - Foling.java
 * - ColorEdit.java
 * - Gemetry.java
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import imp.*;
import java.awt.Color;
import java.util.Random;

public class FaceRecognition
{
    private Folding externClassFolding;
    private ColorEdit externClassColorEdit;
    private Geometry externClassGeometry;

    public Picture newExtractFace (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        if (width < 100 || height < 100) {
            System.out.println("Image is too small");
            return originalImg;
        }else if (width > 2000 || height > 2000) {
            System.out.println("Image is too big");
            return originalImg;
        }

        boolean orientationHorizontal;
        int smallestSide;
        if (height > width) {
            orientationHorizontal = false;
            smallestSide = width;
        }else if (height == width) {
            orientationHorizontal = true;
            smallestSide = height;
        }else {
            orientationHorizontal = true;
            smallestSide = height;
        }

        externClassFolding = new Folding();
        externClassColorEdit = new ColorEdit();
        Picture imgEdgeDetectionHorizontal = externClassFolding.edgeDetectionHorizontally(externClassColorEdit.greyScaleAvg(originalImg));
        Picture imgEdgeDetectionVertical   = externClassFolding.edgeDetectionVertically(externClassColorEdit.greyScaleAvg(originalImg));

        Color [][] pixelEdgeDetectionHorizontal = imgEdgeDetectionHorizontal.getPixelArray();
        Color [][] pixelEdgeDetectionVertical   = imgEdgeDetectionVertical.getPixelArray();
        Color [][] pixelEdgeDetectionComplete   = new Color[width][height];

        //Deleting unused variables => only "pixelEdgeDetectionComplete" remains
        imgEdgeDetectionHorizontal  = null;
        imgEdgeDetectionVertical    = null;
        System.gc();

        //Face outline detection => chin is difficult ---------------------------------------------!!!
        //15px white line
        int detectionLine = (int)(smallestSide * 0.1);
        //System.out.println(detectionLine);

        pixelEdgeDetectionComplete = addEdgeDetectionAndAmplify(width, height, pixelEdgeDetectionHorizontal, pixelEdgeDetectionVertical);
        Color[][] pixelLongEdgesHorizontal = pixelEdgeDetectionComplete;        

        for (int x = 0; x < originalImg.getWidth()-detectionLine; x++) {
            for (int y = 0; y < originalImg.getHeight(); y++) {

                if ((pixelEdgeDetectionComplete[x][y].getRed() == 255 && pixelEdgeDetectionComplete[x][y].getGreen() == 255) && pixelEdgeDetectionComplete[x][y].getBlue() == 255) {
                    for (int i = 0; i < detectionLine; i++) {
                        //System.out.println(i);
                        if ((pixelEdgeDetectionComplete[x+i][y].getRed() != 255 && pixelEdgeDetectionComplete[x+i][y].getGreen() != 255) && pixelEdgeDetectionComplete[x+i][y].getBlue() != 255) {
                            break;
                        }if ((pixelEdgeDetectionComplete[x+i][y].getRed() == 255 && pixelEdgeDetectionComplete[x+i][y].getGreen() == 255) && (pixelEdgeDetectionComplete[x+i][y].getBlue() == 255 && i == 14)) {
                            for (int j = 0; j < detectionLine; j++) {
                                pixelLongEdgesHorizontal[x+j][y] = Color.GREEN;
                            }
                            break;
                        }if ((pixelEdgeDetectionComplete[x+i][y].getRed() == 255 && pixelEdgeDetectionComplete[x+i][y].getGreen() == 255) && pixelEdgeDetectionComplete[x+i][y].getBlue() == 255) {
                            continue;
                        }
                    }

                }
            }
        }
        pixelEdgeDetectionComplete    = null;
        System.gc();

        pixelEdgeDetectionComplete = addEdgeDetectionAndAmplify(width, height, pixelEdgeDetectionHorizontal, pixelEdgeDetectionVertical);
        Color [][] pixelLongEdgesVertical = pixelEdgeDetectionComplete;

        for (int x = 0; x < originalImg.getWidth(); x++) {
            for (int y = 0; y < originalImg.getHeight()-detectionLine; y++) {
                //Color [][] pixelEdgeDetectionCompleteNew = pixelEdgeDetectionComplete;
                if (pixelEdgeDetectionComplete[x][y].getRed() == 255 && pixelEdgeDetectionComplete[x][y].getGreen() == 255 && pixelEdgeDetectionComplete[x][y].getBlue() == 255) {
                    for (int i = 0; i < detectionLine; i++) {
                        //System.out.println(i);
                        if (pixelEdgeDetectionComplete[x][y+i].getRed() != 255 && pixelEdgeDetectionComplete[x][y+i].getGreen() != 255 && pixelEdgeDetectionComplete[x][y+i].getBlue() != 255) {
                            break;
                        }else if (pixelEdgeDetectionComplete[x][y+i].getRed() == 255 && pixelEdgeDetectionComplete[x][y+i].getGreen() == 255 && pixelEdgeDetectionComplete[x][y+i].getBlue() == 255 && i == 14) {
                            for (int j = 0; j < detectionLine; j++) {
                                pixelLongEdgesVertical[x][y+j] = Color.GREEN;
                            }
                            break;
                        }else if (pixelEdgeDetectionComplete[x][y+i].getRed() == 255 && pixelEdgeDetectionComplete[x][y+i].getGreen() == 255 && pixelEdgeDetectionComplete[x][y+i].getBlue() == 255) {
                            continue;
                        }
                    }
                }
            }
        }
        pixelEdgeDetectionComplete    = null;
        System.gc();
        pixelEdgeDetectionComplete = addEdgeDetectionAndAmplify(width, height, pixelEdgeDetectionHorizontal, pixelEdgeDetectionVertical);

        //Add both detected long edges
        /*
        for (int x = 0; x < originalImg.getWidth(); x++) {
        for (int y = 0; y < originalImg.getHeight(); y++) {
        if (pixelLongEdgesHorizontal[x][y] == Color.GREEN && pixelLongEdgesVertical[x][y] == Color.GREEN) {
        pixelEdgeDetectionComplete[x][y] = Color.BLUE;
        }else if (pixelLongEdgesHorizontal[x][y] == Color.GREEN || pixelLongEdgesVertical[x][y] == Color.GREEN) {
        pixelEdgeDetectionComplete[x][y] = Color.GREEN;
        }else {
        pixelEdgeDetectionComplete[x][y] = Color.BLACK;
        }
        }
        }*/

        //Vertical Detection
        int var;    //Variable for remembering things
        int largestValue;
        int[][] largestFrequenceVertical = new int[width][height];
        int[][] largestFrequenceHorizontal = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                largestFrequenceVertical[x][y] = 0;
                largestFrequenceHorizontal[x][y] = 0;
            }
        }
        largestValue = 0;
        for (int x = 0; x < originalImg.getWidth()- (int)(smallestSide*0.1); x += (int)(smallestSide*0.05)) {
            for (int y = 0; y < originalImg.getHeight()-(int)(smallestSide*0.3); y += (int)(smallestSide*0.05)) {
                var = 0;
                for (int i = 0; i < (int)(smallestSide*0.1); i++) {
                    for (int j = 0; j < (int)(smallestSide*0.2); j++) {
                        if (pixelLongEdgesVertical[x+i][y+j] == Color.GREEN) {
                            var++;
                        }
                    }
                }
                //System.out.println(var);
                largestFrequenceVertical[x][y] = var;

            }
        }

        largestValue = 0;
        for (int x = 0; x < originalImg.getWidth()- (int)(smallestSide*0.3); x += (int)(smallestSide*0.05)) {
            for (int y = 0; y < originalImg.getHeight()-(int)(smallestSide*0.1); y += (int)(smallestSide*0.05)) {
                var = 0;
                for (int i = 0; i < (int)(smallestSide*0.2); i++) {
                    for (int j = 0; j < (int)(smallestSide*0.1); j++) {
                        if (pixelLongEdgesHorizontal[x+i][y+j] == Color.GREEN) {
                            var++;
                        }
                    }
                }
                //System.out.println(var);
                largestFrequenceHorizontal[x][y] = var;

            }
        }

        var = 0;
        int var2 = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (largestFrequenceVertical[x][y] != 0) var++;
                if (largestFrequenceHorizontal[x][y] != 0) var2++;
            }
        }
        int[]edgeFrequenceVertical = new int[var];
        int[]edgeFrequenceHorizontal = new int[var2];
        int var3 = 0;
        int var4 = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (largestFrequenceVertical[x][y] != 0) {
                    while (edgeFrequenceVertical[var3] != 0) {
                        var3++;
                    }
                    edgeFrequenceVertical[var3] = largestFrequenceVertical[x][y];
                }
                if (largestFrequenceHorizontal[x][y] != 0) {
                    while (edgeFrequenceHorizontal[var4] != 0) {
                        var4++;
                    }
                    edgeFrequenceHorizontal[var4] = largestFrequenceHorizontal[x][y];
                }
            }
        }
        for (int i = 0; i < edgeFrequenceVertical.length; i++)   {  
            for (int j = i + 1; j < edgeFrequenceVertical.length; j++)   {  
                int tmp = 0;  
                if (edgeFrequenceVertical[i] < edgeFrequenceVertical[j])   {  
                    tmp = edgeFrequenceVertical[i];  
                    edgeFrequenceVertical[i] = edgeFrequenceVertical[j];  
                    edgeFrequenceVertical[j] = tmp;  
                }  
            }  
            //prints the sorted element of the array  
            //System.out.println(edgeFrequenceVertical[i]);  
        } 
        for (int i = 0; i < edgeFrequenceHorizontal.length; i++)   {  
            for (int j = i + 1; j < edgeFrequenceHorizontal.length; j++)   {  
                int tmp = 0;  
                if (edgeFrequenceHorizontal[i] < edgeFrequenceHorizontal[j])   {  
                    tmp = edgeFrequenceHorizontal[i];  
                    edgeFrequenceHorizontal[i] = edgeFrequenceHorizontal[j];  
                    edgeFrequenceHorizontal[j] = tmp;  
                }  
            }  
            //prints the sorted element of the array  
            //System.out.println(edgeFrequenceHorizontal[i]);  
        } 

        int[] startingPointX = new int[1000];
        int[] ellipseWidth = new int[1000];
        int[][] changedLargestFrequenceVertical = largestFrequenceVertical;
        int[][] changedLargestFrequenceVertical2 = largestFrequenceVertical;
        complete:
        for (int i = 0; i < (int)(edgeFrequenceVertical.length*0.2)-1; i++) {
            int x1, y1 = x1 = 0;
            //System.out.println(i);
            firstNumber:
            for(int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    //System.out.println(edgeFrequenceVertical[i]);
                    if (changedLargestFrequenceVertical[x][y] == edgeFrequenceVertical[i]) {
                        changedLargestFrequenceVertical[x][y] = 0;
                        System.out.println(x);
                        x1 = x;
                        y1 = y;
                        break firstNumber;
                    }
                }
            }
            for (int j = i; j < (int)(edgeFrequenceVertical.length*0.2); j++) {
                int x2, y2 = x2 = 0;
                search:
                for(int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        if (changedLargestFrequenceVertical2[x][y] == edgeFrequenceVertical[j+1]) {
                            changedLargestFrequenceVertical2[x][y] = 0;
                            x2 = x;
                            y2 = y;
                            //System.out.println(x2);
                            break search;
                        }

                    }
                }

                if ((x2-x1) > smallestSide*0.2/* || (largestFrequenceVertical[x2][y2] - largestFrequenceVertical[x1][y1]) > smallestSide*0.2*/)  {
                    //System.out.println(x1);
                    //System.out.println(x2);
                    //System.out.print("Difference of values : ");
                    //System.out.println(largestFrequenceVertical[x1][y1] - largestFrequenceVertical[x2][y2]);
                    startingPointX[i*j+j] = x1;
                    ellipseWidth[i*j+j] = (x2 - x1)+ detectionLine-1;
                    //System.out.println(ellipseWidth[i]);
                }
            }
        }

        int[] startingPointY = new int[1000];
        int[] ellipseHeight = new int[1000];
        int[][] changedLargestFrequenceHorizontal = largestFrequenceHorizontal;
        int[][] changedLargestFrequenceHorizontal2 = largestFrequenceHorizontal;
        complete:
        for (int i = 0; i < (int)(edgeFrequenceVertical.length*0.2)-1; i++) {
            int x1, y1 = x1 = 0;
            //System.out.println(i);
            firstNumber:
            for(int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    //System.out.println(edgeFrequenceVertical[i]);
                    if (changedLargestFrequenceHorizontal[x][y] == edgeFrequenceHorizontal[i]) {
                        changedLargestFrequenceHorizontal[x][y] = 0;
                        System.out.println(x);
                        x1 = x;
                        y1 = y;
                        break firstNumber;
                    }
                }
            }
            for (int j = i; j < (int)(edgeFrequenceHorizontal.length*0.2); j++) {
                int x2, y2 = x2 = 0;
                search:
                for(int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        if (changedLargestFrequenceHorizontal2[x][y] == edgeFrequenceHorizontal[j+1]) {
                            changedLargestFrequenceHorizontal2[x][y] = 0;
                            x2 = x;
                            y2 = y;
                            //System.out.println(x2);
                            break search;
                        }

                    }
                }

                if ((y2-y1) > smallestSide*0.2/* || (largestFrequenceVertical[x2][y2] - largestFrequenceVertical[x1][y1]) > smallestSide*0.2*/)  {
                    //System.out.println(x1);
                    //System.out.println(x2);
                    //System.out.print("Difference of values : ");
                    //System.out.println(largestFrequenceVertical[x1][y1] - largestFrequenceVertical[x2][y2]);
                    startingPointY[i*j+j] = y1;
                    ellipseHeight[i*j+j] = (y2 - y1)+ detectionLine-1;
                    //System.out.println(ellipseWidth[i]);
                }
            }
        }
        
        Color [][] output = originalImg.getPixelArray();
        
        //Eye detection
        //Mouth/Nose detection
        //Skin color detection
        //Draw an ellipse
        externClassGeometry = new Geometry();
        Color green = Color.GREEN;
        //int startingPointX = 30;
        //int startingPointY = 0;
        for (int i = 0; i < ellipseWidth.length; i++) {
            if (ellipseWidth[i] == 0) {
                    continue;
                }
            for (int j = 0; j < ellipseHeight.length; j++) {
                if (ellipseHeight[j] == 0) {
                    continue;
                }
                int[][] matrix = externClassGeometry.ellipse(ellipseWidth[i], ellipseHeight[j]);
                System.out.print("StartingpointX: ");
                System.out.println(startingPointX[i]);
                System.out.print("StartingpointY: ");
                System.out.println(startingPointY[i]);
                System.out.print("width         : ");
                System.out.println(ellipseWidth[i]);
                System.out.print("Height        : ");
                System.out.println(ellipseHeight[i]);
                drawEllipse:
                for (int x = 0; x < ellipseWidth[i]; x++) {
                    //System.out.println("First loop");
                    if (startingPointX[i] == 0) {
                            break drawEllipse;
                        }
                    for (int y = 0; y < ellipseHeight[j]; y++) {
                        //System.out.println("Second loop");
                        if (startingPointY[j] == 0) {
                            break drawEllipse;
                        }
                        if (matrix[x][y] == 1) {
                            output[startingPointX[i] + x][startingPointY[j] + y] = green;
                        }

                    }
                }  
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(output); 

        //externClassColorEdit = new ColorEdit();
        //Picture edited = externClassColorEdit.rgbAmplifier(newImg);
        return newImg;
    }

    private Color[][] addEdgeDetectionAndAmplify(int width, int height, Color[][] inputHorizontal, Color[][] inputVertical) {
        Color[][] output = new Color[width][height];
        Color colorHorizontal;
        Color colorVertical;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colorHorizontal = inputHorizontal[x][y];
                colorVertical   = inputVertical[x][y];
                output[x][y] = new Color((colorHorizontal.getRed() + colorVertical.getRed())/2, (colorHorizontal.getGreen() + colorVertical.getGreen())/2, (colorHorizontal.getBlue() + colorVertical.getBlue())/2);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red, green, blue = green = red = 0;
                if (output[x][y].getRed() > 25) {
                    red = 255;
                }else {
                    red = 0;
                }
                if (output[x][y].getGreen() > 25) {
                    green = 255;
                }else {
                    green = 0;
                }
                if (output[x][y].getBlue() > 25) {
                    blue = 255;
                }else {
                    blue = 0;
                }

                output[x][y] = new Color(red, green, blue);
            }
        }

        return output;
    }

    private Picture extractFace (Picture originalImg) {
        Color[] skinColors = {new Color(255, 204, 153), /*FFCC99*/
                new Color(191, 153, 115),       /*BF9973*/ 
                new Color(128, 102,  77),       /*80664D*/
                new Color( 64,  51,  38),       /*403326*/
                new Color(230, 184, 138),       /*E6B88A*/
                new Color(179, 134,  89),       /*B38659*/
                new Color(255, 217, 179)};      /*FFD9B3*/

        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld = originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];

        if (width < 50 || height < 50) {
            System.out.println("Image is too small");
            return originalImg;
        }

        //Boolean for image orientation and integer for smallest side
        boolean orientationHorizontal;
        int smallestSide;
        if (height > width) {
            orientationHorizontal = false;
            smallestSide = width;
        }else if (height == width) {
            orientationHorizontal = true;
            smallestSide = height;
        }else {
            orientationHorizontal = true;
            smallestSide = height;
        }

        //Decrementing through different possible image sizes
        //for (int recoSize = smallestSide; recoSize >= 50; recoSize -= 10) {
        // System.out.println(recoSize);
        int recoSize = 600;
        if (true) {  //For later direction adaption
            for (int x = 0; x < width - recoSize; x+= (int)(recoSize*0.25)) {
                for (int y = 0; y < height - recoSize; y+= (int)(recoSize*0.25)) {
                    double avgRed   = 0;
                    double avgGreen = 0;
                    double avgBlue  = 0;
                    int[] skinColorsFrequency = new int[skinColors.length];
                    for (int i = 0; i < skinColorsFrequency.length; i++) {
                        skinColorsFrequency[i] = 0;
                    }

                    for (int i = 0; i < recoSize; i++) {
                        for (int j = 0; j < recoSize; j++) {
                            for (int k = 0; k < skinColors.length; k++) {
                                if (pixelOld[x+i][y+j].getRed() == skinColors[0].getRed() && pixelOld[x+i][y+j].getGreen() == skinColors[0].getGreen() && pixelOld[x+i][y+j].getBlue() == skinColors[0].getBlue()) {
                                    skinColorsFrequency[0] += 1;
                                }
                            }
                            avgRed   += pixelOld[x+i][y+j].getRed();
                            avgGreen += pixelOld[x+i][y+j].getGreen();
                            avgBlue  += pixelOld[x+i][y+j].getBlue();
                        }
                    }
                    avgRed = avgRed / Math.pow(recoSize, 2);
                    avgGreen = avgGreen / Math.pow(recoSize, 2);
                    avgBlue = avgBlue / Math.pow(recoSize, 2);
                    System.out.println(skinColorsFrequency[0]);
                    //System.out.println(avgRed);
                    //System.out.println(avgGreen);
                    //System.out.println(avgBlue);
                }
            }
        }

        //}

        return originalImg;
    }

    private Picture greyScaleAvg (Picture originalImg) {
        //Getting the width and the height of the original image
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        //Transforming the image to a two dimensional array and creating one for the new image
        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorAvg = Color.GREEN; //Setting up a new color with a default value (just for debugging -> if there is something green in the picture, something went wrong)

        //Changing every pixel of the old pixel array to the new one
        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Calculating the average of all color brightnesses
                int avg = (pixelOld[x][y].getRed() + pixelOld[x][y].getGreen() + pixelOld[x][y].getBlue()) / 3;
                //Creating a new color and set it with the average saturation
                colorAvg = new Color(avg, avg, avg);
                pixelNew[x][y] = colorAvg;
            }
        }

        //Transforming the new pixel arry into a new picture and return it
        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

}
