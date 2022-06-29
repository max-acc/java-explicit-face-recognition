/**
 * Class for very simple face recognition.
 * This is not about classifying humans by their skin color (there are only 6 skincolors for simplyfying reasons).
 * It is not about racism or colorism in any thinkable way.
 * 
 * This website's color palette has been used:
 * https://colorcodes.io/fair-skin-color-codes/
 * 
 * Required classes:
 * - Foling.java
 * - ColorEdit.java
 * - Geometry.java
 *
 * @author Max Wenk
 * @version 6/28/2022
 */

import imp.*;
import java.awt.Color;
import java.util.Random;

public class FaceRecognition
{
    private Folding externClassFolding;
    private ColorEdit externClassColorEdit;
    private Geometry externClassGeometry;

    public Picture bokehEffect (Picture originalImg) {
        //Eye detection
        //Mouth/Nose detection
        //Skin color detection

        //Face-boundary detection
        return originalImg;
    }

    public Picture faceRecognition (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        //Check, if the image has the correct size (not too large or too small)
        if (checkImgSize(width, height) == false) return originalImg;

        externClassFolding = new Folding();
        externClassColorEdit = new ColorEdit();
        Picture imgEdgeDetectionHorizontal = externClassFolding.edgeDetectionHorizontally(externClassColorEdit.greyScaleAvg(originalImg));
        Picture imgEdgeDetectionVertical   = externClassFolding.edgeDetectionVertically(externClassColorEdit.greyScaleAvg(originalImg));
        Color [][] pixelEdgeDetectionHorizontal = imgEdgeDetectionHorizontal.getPixelArray();
        Color [][] pixelEdgeDetectionVertical   = imgEdgeDetectionVertical.getPixelArray();
        Color[][] pixelEdgeDetectionComplete = addEdgeDetectionAndAmplify(width, height, pixelEdgeDetectionHorizontal, pixelEdgeDetectionVertical);

        //Deleting unused variables => only "pixelEdgeDetectionComplete" remains
        imgEdgeDetectionHorizontal  = null;
        imgEdgeDetectionVertical    = null;
        pixelEdgeDetectionHorizontal= null;
        pixelEdgeDetectionVertical  = null;
        System.gc();

        //Eye detection
        //Mouth/Nose detection

        //Face-boundary detection
        externClassGeometry = new Geometry();
        int[][] facePositionInfoEdge = facePosition(originalImg);

        //Skin color detection
        int[][] facePositionInfoColor = skinColor(originalImg, facePositionInfoEdge);

        int[][] facePositionInfo = facePositionInfoColor;
        Color[][] output = originalImg.getPixelArray();
        printPositionArray(facePositionInfo);

        if (facePositionInfo.length == 1 && facePositionInfo[0][0] == 0 && facePositionInfo[0][1] == 0 && facePositionInfo[0][2] == 0 && facePositionInfo[0][3] == 0) {
            System.out.println("Some required face matching criteria isn't met!");
            Picture img = new Picture();
            img.setPixelArray(output);
            return img;
        }

        for (int i = 0; i < facePositionInfo.length; i++) {
            if (facePositionInfo[i][0] == 0 || facePositionInfo[i][1] == 0) {
                continue;
            }
            if (facePositionInfo[i][2] == 0 || facePositionInfo[i][3] == 0) {
                continue;
            }

            int[][] matrix = externClassGeometry.ellipse(facePositionInfo[i][2], facePositionInfo[i][3]);
            drawEllipse:
            for (int x = 0; x < facePositionInfo[i][2]; x++) {
                //System.out.println("First loop");
                for (int y = 0; y < facePositionInfo[i][3]; y++) {
                    //System.out.println("Second loop");

                    if (matrix[x][y] == 1) {
                        output[facePositionInfo[i][0] + x][facePositionInfo[i][1] + y] = Color.GREEN;
                    } 
                }
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(output);
        return newImg;
    }

    private boolean checkImgSize(int width, int height) {
        boolean correctSize = true;

        if (width < 100 || height < 100) {
            System.out.println("Image is too small");
            correctSize = false;
            return correctSize;
        }else if (width > 2000 || height > 2000) {
            System.out.println("Image is too large");
            correctSize = false;
            return correctSize;
        }
        return correctSize;
    }

    //Function for skin color detection
    private int[][] skinColor (Picture originalImg, int[][] possibleFacePosition) {
        Color[][] inputImg = originalImg.getPixelArray();
        Color[] skinColors = {new Color(255, 204, 153), /*FFCC99*/
                new Color(191, 153, 115),       /*BF9973*/ 
                new Color(128, 102,  77),       /*80664D*/
                new Color( 64,  51,  38),       /*403326*/
                new Color(230, 184, 138),       /*E6B88A*/
                new Color(179, 134,  89),       /*B38659*/
                new Color(255, 217, 179)};      /*FFD9B3*/
        int[] tempVar = new int[skinColors.length];
        int end = possibleFacePosition.length;
        int colorTolerance = 15;
        
        for (int i = 0; i < possibleFacePosition.length; i++) {
            for (int x = possibleFacePosition[i][0]; x < possibleFacePosition[i][0]+possibleFacePosition[i][2]; x++) {
                for (int y = possibleFacePosition[i][1]; y < possibleFacePosition[i][1]+possibleFacePosition[i][3]; y++) {
                    for (int j = 0; j < skinColors.length; j++) {
                        if((inputImg[x][y].getRed() >= skinColors[j].getRed()-colorTolerance && inputImg[x][y].getRed() <= skinColors[j].getRed()+colorTolerance) && (inputImg[x][y].getGreen() >= skinColors[j].getGreen()-colorTolerance && inputImg[x][y].getGreen() <= skinColors[j].getGreen()+colorTolerance) && (inputImg[x][y].getBlue() >= skinColors[j].getBlue()-colorTolerance && inputImg[x][y].getBlue() <= skinColors[j].getBlue()+colorTolerance)) {
                            tempVar[j] += 1;
                        }
                    }
                }
            }
            for (int j = 0; j < skinColors.length; j++) {
                if ((possibleFacePosition[i][2]*possibleFacePosition[i][3])*0.1 < tempVar[j]) break;
                if ((possibleFacePosition[i][2]*possibleFacePosition[i][3])*0.1 > tempVar[j] && j == skinColors.length - 1) {
                    possibleFacePosition[i][0] = 0;
                    possibleFacePosition[i][1] = 0;
                    possibleFacePosition[i][2] = 0;
                    possibleFacePosition[i][3] = 0;
                }
            }
            
        }
        

        for (int i = 0; i < possibleFacePosition.length-1; i++) {
            if (possibleFacePosition[i][0] == 0 && possibleFacePosition[i][1] == 0 && possibleFacePosition[i][2] == 0 && possibleFacePosition[i][3] == 0) {
                end--;
                for (int j = i; j < possibleFacePosition.length-1; j++) {
                    possibleFacePosition[j][0] = possibleFacePosition[j+1][0];
                    possibleFacePosition[j][1] = possibleFacePosition[j+1][1];
                    possibleFacePosition[j][2] = possibleFacePosition[j+1][2];
                    possibleFacePosition[j][3] = possibleFacePosition[j+1][3];
                }
            }
        }

        if (false) {
            System.out.println("Face color isn't matching!");
            int[][] exeptionArray = new int[1][4];
            return exeptionArray;
        }

        int[][] newFacePosition = new int[end][4];
        for (int i = 0; i < end; i++) {
            for (int j = 0; j < 4; j++) {
                newFacePosition[i][j] = possibleFacePosition[i][j];
            }
        }
        return newFacePosition;
    }

    //Function for face-boundary detection
    private int[][] facePosition (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

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

        //Getting the number of longer edges in a 0.1x0.3 area and write it to an array => shows where the most vertical strokes are
        largestValue = 0;
        for (int x = 0; x < width -(int)(smallestSide*0.1); x += (int)(smallestSide*0.05)) {
            for (int y = 0; y < height -(int)(smallestSide*0.3); y += (int)(smallestSide*0.05)) {
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

        //Getting the number of longer edges in a 0.3x0.1 area and write it to an array => shows where the most horizontal strokes are
        largestValue = 0;
        for (int x = 0; x < width - (int)(smallestSide*0.3); x += (int)(smallestSide*0.05)) {
            for (int y = 0; y < height - (int)(smallestSide*0.1); y += (int)(smallestSide*0.05)) {
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

        //Setting up arrays with the largest numbers of the horizontal and vertical areas
        var = 0;
        int var2 = 0;
        //Calculating the size for these arrays
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (largestFrequenceVertical[x][y] != 0) var++;
                if (largestFrequenceHorizontal[x][y] != 0) var2++;
            }
        }
        //Initialising the arrays with a specific size
        int[]edgeFrequenceVertical = new int[var];
        int[]edgeFrequenceHorizontal = new int[var2];

        int var3 = 0;
        int var4 = 0;
        //Writing the values from the areas to the number arrays (unsorted)
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
        //Sorting the number arrays descending
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

        //Finding vertical boundaries
        int[] startingPointX = new int[1000];
        int[] ellipseWidth = new int[1000];
        int[][] changedLargestFrequenceVertical = largestFrequenceVertical;
        int[][] changedLargestFrequenceVertical2 = largestFrequenceVertical;
        complete:
        for (int i = 0; i < (int)(edgeFrequenceVertical.length*0.2)-1; i++) {
            int x1, y1 = x1 = 0;
            firstNumber:
            for(int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
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
                            break search;
                        }

                    }
                }

                if ((x2-x1) > smallestSide*0.2/* || (largestFrequenceVertical[x2][y2] - largestFrequenceVertical[x1][y1]) > smallestSide*0.2*/)  {
                    startingPointX[i*j+j] = x1;
                    ellipseWidth[i*j+j] = (x2 - x1)+ detectionLine-1;
                }
            }
        }

        //Finding horizontal boundaries
        int[] startingPointY = new int[1000];
        int[] ellipseHeight = new int[1000];
        int[][] changedLargestFrequenceHorizontal  = largestFrequenceHorizontal;
        int[][] changedLargestFrequenceHorizontal2 = largestFrequenceHorizontal;
        complete:
        for (int i = 0; i < (int)(edgeFrequenceVertical.length*0.2)-1; i++) {
            int x1, y1 = x1 = 0;
            firstNumber:
            for(int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
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
                            break search;
                        }
                    }
                }
                if ((y2-y1) > smallestSide*0.2/* || (largestFrequenceVertical[x2][y2] - largestFrequenceVertical[x1][y1]) > smallestSide*0.2*/)  {
                    startingPointY[i*j+j] = y1;
                    ellipseHeight[i*j+j] = (y2 - y1)+ detectionLine-1;
                }
            }
        }

        Color [][] output = originalImg.getPixelArray();

        int[][] ellipsePositionInfo = new int[startingPointX.length * startingPointY.length][4];

        //Draw an ellipse
        Color green = Color.GREEN;
        var = 0;
        for (int i = 0; i < ellipseWidth.length; i++) {
            if (ellipseWidth[i] == 0) {
                continue;
            }
            for (int j = 0; j < ellipseHeight.length; j++) {
                if (ellipseHeight[j] == 0) {
                    continue;
                }else {
                    ellipsePositionInfo[var][0] = startingPointX[i];
                    ellipsePositionInfo[var][1] = startingPointY[j];
                    ellipsePositionInfo[var][2] = ellipseWidth[i];
                    ellipsePositionInfo[var][3] = ellipseHeight[j];
                    if (var >= 1) {
                        if (ellipsePositionInfo[var-1][0] != ellipsePositionInfo[var][0] || ellipsePositionInfo[var-1][1] != ellipsePositionInfo[var][1] || ellipsePositionInfo[var-1][2] != ellipsePositionInfo[var][2] || ellipsePositionInfo[var-1][3] != ellipsePositionInfo[var][3]) {
                            var++; 
                        } 
                    }
                    if (var == 0) {
                        var++;
                    }

                }

            }
        }

        ellipsePositionInfo = removeDuplicates(ellipsePositionInfo);
        //printPositionArray(ellipsePositionInfo);
        return ellipsePositionInfo;       
    }

    //Debugging function for printing the postion array
    private void printPositionArray (int[][] facePositions) {
        if(true) {
            if (facePositions.length == 0) {System.out.print("Array is empty"); return;}
            for (int i = 0; i < facePositions.length; i++) {
                System.out.print(facePositions[i][0]);
                System.out.print(" ");
                System.out.print(facePositions[i][1]);
                System.out.print(" ");
                System.out.print(facePositions[i][2]);
                System.out.print(" ");
                System.out.print(facePositions[i][3]);
                System.out.println(" ");
            } 
        }
    }

    private int[][] removeDuplicates(int[][/*static length of 4*/] input) {
        int end = input.length;
        for (int i = 0; i < end; i++) {
            for(int j = i+1; j < end; j++) {
                if (input[i][0] == input[j][0] && input[i][1] == input[j][1] && input[i][2] == input[j][2] && input[i][3] == input[j][3]) {
                    input[j] = input[end-1];
                    end--;
                    j--;
                }
            }
        }
        int[][] output = new int[end][4];
        System.arraycopy(input, 0, output, 0, end);
        return output;
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

    //---------------------- not in use -------------------------------------------------------------
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
}
