/**
 * Class with functions for advanced image editing.
 * 
 * @author Max Wenk 
 * @version 6/13/2022
 * 
 * Funtions:
 * - standardBlur (originalImg)
 * - intentionalBlur (originalImg, int filterSize)
 * - automaticBlur (originalImg)
 * - gaussianBlur (originalImg, int filterSize)
 * - sharpen (originalImg)
 * - relief (originalImg)
 * - edgeDetectionHorizontally (originalImg)
 * - edgeDetectionVertically (originalImg)
 */

//Importing libraries
import imp.*;
import java.awt.Color;
import java.util.Random;

public class Folding {
    //Function for the application of the matrices
    private Picture folding (Picture originalImg, double[][] filter) {
        //Getting the width and the height of the original image
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        //Transforming the image to a two dimensional array and creating one for the new image
        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = originalImg.getPixelArray();

        //Exception for the left part of the picture
        //System.out.println("Left");
        for (int x = 1; x < filter.length/2; x++) {
            double[][] newFilter = createMatrixForBlur((int)Math.pow(1+2*x, 2));
            System.out.println(newFilter.length);
            for (int y = x; y < height - x; y++) {
                //Initialising and declaring default values for the red, green and blue color values
                double red, green, blue = green = red = 0.0;

                //Declaring variables for applying the matrices on the the color values

                int xx = x - newFilter.length/2;
                int yy = y - newFilter.length/2;

                //Applying the matrices on the color values while going through the matrix
                for (int i = 0; i < newFilter.length; i++) {
                    for (int j = 0; j < newFilter.length; j++) {
                        //Mulitplying the color values by the filter
                        red   += newFilter[i][j] * pixelOld[xx+i][yy+j].getRed();
                        green += newFilter[i][j] * pixelOld[xx+i][yy+j].getGreen();
                        blue  += newFilter[i][j] * pixelOld[xx+i][yy+j].getBlue();

                    }
                }

                //Setting a new color to the pixel and check if the new color values are inside the 8bit color boundaries
                pixelNew[x][y] = new Color(validColor(red), validColor(green), validColor(blue));

            }
        }
        //Exception for the right part of the picture
        //System.out.println("Right");
        for (int x = width - 1; x >= width - filter.length/2; x--) {
            double[][] newFilter = createMatrixForBlur((int)Math.pow(1+2*(width-x), 2));
            System.out.println(newFilter.length);
            for (int y = width - x; y < height -(width-x); y++) {
                //Initialising and declaring default values for the red, green and blue color values
                double red, green, blue = green = red = 0.0;

                //Declaring variables for applying the matrices on the the color values

                int xx = x - newFilter.length/2-1;
                int yy = y - newFilter.length/2;

                //Applying the matrices on the color values while going through the matrix
                for (int i = 0; i < newFilter.length; i++) {
                    for (int j = 0; j < newFilter.length; j++) {
                        //Mulitplying the color values by the filter
                        red   += newFilter[i][j] * pixelOld[xx+i][yy+j].getRed();
                        green += newFilter[i][j] * pixelOld[xx+i][yy+j].getGreen();
                        blue  += newFilter[i][j] * pixelOld[xx+i][yy+j].getBlue();

                    }
                }

                //Setting a new color to the pixel and check if the new color values are inside the 8bit color boundaries
                pixelNew[x][y] = new Color(validColor(red), validColor(green), validColor(blue));

            }
        }
        
        //Exception for the upper part of the picture
        //System.out.println("Upper");
        for (int y = 1; y < filter.length/2; y++) {
            double[][] newFilter = createMatrixForBlur((int)Math.pow(1+2*y, 2));
            System.out.println(newFilter.length);
            for (int x = y; x < width - y; x++) {
                //Initialising and declaring default values for the red, green and blue color values
                double red, green, blue = green = red = 0.0;

                //Declaring variables for applying the matrices on the the color values

                int xx = x - newFilter.length/2;
                int yy = y - newFilter.length/2;

                //Applying the matrices on the color values while going through the matrix
                for (int i = 0; i < newFilter.length; i++) {
                    for (int j = 0; j < newFilter.length; j++) {
                        //Mulitplying the color values by the filter
                        red   += newFilter[i][j] * pixelOld[xx+i][yy+j].getRed();
                        green += newFilter[i][j] * pixelOld[xx+i][yy+j].getGreen();
                        blue  += newFilter[i][j] * pixelOld[xx+i][yy+j].getBlue();

                    }
                }

                //Setting a new color to the pixel and check if the new color values are inside the 8bit color boundaries
                pixelNew[x][y] = new Color(validColor(red), validColor(green), validColor(blue));

            }
        }
        //Exception for the lower part of the picture
        //System.out.println("Lower");
        for (int y = height - 1; y >= height - filter.length/2; y--) {
            double[][] newFilter = createMatrixForBlur((int)Math.pow(1+2*(height-y), 2));
            System.out.println(newFilter.length);
            for (int x = height - y; x < width -(height-y); x++) {
                //Initialising and declaring default values for the red, green and blue color values
                double red, green, blue = green = red = 0.0;

                //Declaring variables for applying the matrices on the the color values

                int xx = x - newFilter.length/2;
                int yy = y - newFilter.length/2-1;

                //Applying the matrices on the color values while going through the matrix
                for (int i = 0; i < newFilter.length; i++) {
                    for (int j = 0; j < newFilter.length; j++) {
                        //Mulitplying the color values by the filter
                        red   += newFilter[i][j] * pixelOld[xx+i][yy+j].getRed();
                        green += newFilter[i][j] * pixelOld[xx+i][yy+j].getGreen();
                        blue  += newFilter[i][j] * pixelOld[xx+i][yy+j].getBlue();

                    }
                }

                //Setting a new color to the pixel and check if the new color values are inside the 8bit color boundaries
                pixelNew[x][y] = new Color(validColor(red), validColor(green), validColor(blue));

            }
        }
        

        //Going through the array with a gap of the half filter length to prevent unexpected image which could lead to unintended errors
        for(int x=filter.length/2; x < width - filter.length/2; x++) {
            for(int y=filter.length/2;y < height - filter.length/2; y++) {
                //Initialising and declaring default values for the red, green and blue color values
                double red, green, blue = green = red = 0.0;

                //Declaring variables for applying the matrices on the the color values
                int xx = x - filter.length/2;
                int yy = y - filter.length/2;

                //Applying the matrices on the color values while going through the matrix
                for (int i = 0; i < filter.length; i++) {
                    for (int j = 0; j < filter.length; j++) {
                        //Mulitplying the color values by the filter
                        red   += filter[i][j] * pixelOld[xx+i][yy+j].getRed();
                        green += filter[i][j] * pixelOld[xx+i][yy+j].getGreen();
                        blue  += filter[i][j] * pixelOld[xx+i][yy+j].getBlue();

                    }
                }

                //Setting a new color to the pixel and check if the new color values are inside the 8bit color boundaries
                pixelNew[x][y] = new Color(validColor(red), validColor(green), validColor(blue));
            }
        }

        //Transforming the new pixel arry into a new picture and return it
        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;                
    }

    //Function for a standard blur with a matrxi size of 3 by 3
    public Picture standardBlur (Picture originalImg) {
        double[][] matrix = createMatrixForBlur(9); //Creating a matrix with a total number of 9 values => 3x3
        return folding(originalImg, matrix);    //Returning a blurred image
    }

    //Function for a blur which can be customized by the user
    public Picture intentionalBlur (Picture originalImg, int size) {
        //For DAU ("dumbest imagineable user") -> so there are no input errors => not that important in this case because the program needs a lot of time for large blur sizes
        if (size < 0) {
            size = 0;
        }
        if (size > Math.pow(originalImg.getWidth(), 2) * 0.8) {
            size = (int) (Math.pow(originalImg.getWidth(), 2) * 0.8);
        }
        if (size > Math.pow(originalImg.getHeight(), 2) * 0.8) {
            size = (int) (Math.pow(originalImg.getHeight(), 2) * 0.8);
        }

        double[][] matrix = createMatrixForBlur(size);  //Creating a valid matrix with a given size
        return folding(originalImg, matrix);
    }

    //Function for an automatic blur -> it is adjusted to the image size
    public Picture automaticBlur (Picture originalImg) {
        //Calculating a value for an automatic blur -> matrix size
        int size = (int) (originalImg.getWidth() * originalImg.getHeight()) / 10000;

        double[][] matrix = createMatrixForBlur(size);  //Creating a valid matrix with a calculated size
        return folding(originalImg, matrix);
    }

    //Function for a blur with the distance taken into consideration -> normal distribution
    public Picture gaussianBlur (Picture originalImg, int size) {
        //For DAU ("dumbest imagineable user") -> so there are no input errors
        if (size < 0) {
            size = 0;
        }
        if (size > Math.pow(originalImg.getWidth(), 2) * 0.8) {
            size = (int) (Math.pow(originalImg.getWidth(), 2) * 0.8);
        }
        if (size > Math.pow(originalImg.getHeight(), 2) * 0.8) {
            size = (int) (Math.pow(originalImg.getHeight(), 2) * 0.8);
        }

        double[][] matrix = createMatrixForBlur(size);  //Creating a valid matrix with a given size and default values
        double sigma = matrix.length / 6; //Calculating sigma

        double midCoord = matrix.length / 2 - 0.5;  //Matrix length will always be uneven (function for creating the matrix). The middle point is 0.5 lower because arrays have a starting index of 0

        double sum = 0; //Creating a variable to calculate the sum of the matrix values

        //Calculating every value for the matrix with gaussian calculations
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                //Calculating the radius with the pythagorean theorem
                double radius = Math.sqrt(Math.pow(i - midCoord, 2) + Math.pow(i - midCoord, 2));
                //Calculating the matrix values and adding it to the sum
                sum += matrix[i][j] = Math.exp(-(double)(Math.pow(radius, 2)) / (2 * Math.pow(sigma, 2)));
            }
        }

        //Calculating k and applying it to the matrix
        double k = 1 / sum;
        for (int i = 0; i < matrix.length; i++) { 
            for (int j = 0; j < matrix.length; j++) { 
                matrix[i][j] *= k;
            }
        }

        return folding(originalImg, matrix);
    }

    //Creating a valid matrix for the blur functions
    private double[][] createMatrixForBlur(int size) {
        int root = 1;    //Just for default
        //Getting a value for i squared which is smaller or equal to the given size
        for (int i = 0; i < size; i++) {
            if (i*i == size) {
                root = i;
                break;
            }else if (i*i > size) {
                root = i-1;
                break;
            }
        }

        //Creating an uneven number as root -> the matrix has to have an uneven size
        if (root % 2 == 0) {
            root -= 1;
        } else {
            root = root;
        }
        //Creating a 2d matrix with root x root size
        double[][] matrix = new double[root][root];
        //Setting every matrix value to 1/root^2, to calculate the average (required for applying a blur)
        for (int i = 0; i < root; i++) {
            for (int j = 0; j < root; j++) {
                matrix[i][j] = 1.0/(root*root);
            }
        }
        return matrix;  //Returning the created matrix
    }

    //Function for sharpening a picture
    public  Picture sharpen(Picture originalImg) {
        //Creating a matrix which reinforces the middle pixel
        double[][] matrix ={{0,-1,0},
                {-1,5,-1},
                {0,-1,0}};
        return folding(originalImg, matrix);
    }

    //Function for drawing a relief -> only the relief is shown
    public  Picture relief(Picture originalImg) {
        double[][] matrix ={{-2,-1,0},
                {-1,1,-2},
                {0,1,2}};
        return folding(originalImg, matrix);
    }

    //Function for detecting and drawing the horizontal edges -> only edges are shown
    public  Picture edgeDetectionHorizontally(Picture originalImg) {
        double[][] matrix ={{-1,0,1},
                {-2,0,2},
                {-1,0,1}};
        return folding(originalImg, matrix);
    }

    //Function for detecting and drawing the horizontal edges -> only edges are shown
    public  Picture edgeDetectionVertically(Picture originalImg) {
        double[][] matrix={{-1,-2,-1},
                {0,0,0},
                {1,2,1}};
        return folding(originalImg, matrix);
    }

    //Private Funtion for returning a valid color value
    private int validColor(double pixelColor) {
        int validColor = (int)pixelColor;
        //Check if the new color is inside the 8bit color boundaries
        if (validColor > 255) {
            validColor = 255;
        } else if (validColor < 0) {
            validColor = 0;           
        } else {
            //The color remains the same
            validColor = validColor;
        }  
        //Returning the new color
        return validColor;
    }

    //This function is not working as intended
    private double squareRoot(int number) {
        double root = 0;
        if (number == 0)  {
            root = 0;
            return root;
        }
        for (int i = 0; i <= number; i++) {
            if (i*i == number) {
                root = i;
                break;
            }else if (i*i < number && i*(i+1) > number) {
                root = i;
                int difference10 = i*(i+1) - i*i;
                for (int j = 0; j < 10; j++) {
                    if (j*j == difference10) {
                        root = i + j/10;
                        break;
                    }else if (j*j < difference10 && j*(j+1) > difference10) {
                        root = i + j/10;
                        int difference100 = j*(j+1) - j*j;
                        for (int k = 0; k < 10; k++) {
                            if (k*k == difference100) {
                                root = i + j/10 + k/100;
                                break;
                            }else if (k*k < difference100 && k*(k+1) > difference100) {
                                root = i + j/10 + k/100;
                                break; //
                            }else if (k >= 10) {
                                break;
                            }
                        }
                        break;
                    }else if(j >= 10) {
                        break;
                    }
                }
                break;
            }else if (i >= number) {
                //Root is out of boundaries
                root = 1;
                break;
            }
        }

        return root;
    }
}
