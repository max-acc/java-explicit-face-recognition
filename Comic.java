/**
 * Class with functions for applying a comic-style to a picture.
 * 
 * @author Max Wenk 
 * @version 6/14/2022
 * 
 * Funtions:
 * - convertComicStyle(originalImg, int colorDepth [With power of two]
 */

//Importing libraries
import imp.*;
import java.awt.Color;
import java.util.Random;

public class Comic {
    //Function for creating a comic style picture
    public Picture convertComicStyle(Picture originalImg, int colorDepth /*With power of two*/) {
        Color[][] pixelOld = originalImg.getPixelArray();
        Color[][] pixelNew = new Color[originalImg.getWidth()][originalImg.getHeight()];
        Color colorNew = Color.GREEN;   //Just as a default value
        
        //Iterating through the image and convert the color values to specific (maybe given) color values
        for (int x = 0; x < originalImg.getWidth(); x++) {
            for (int y = 0; y < originalImg.getHeight(); y++) {
                //Converting the color and appplying it to the image
                colorNew = convertColor(pixelOld[x][y].getRed(), pixelOld[x][y].getGreen(), pixelOld[x][y].getBlue(), colorDepth);
                pixelNew[x][y] = colorNew;
            }
        }
        
        
        //Returning the comic-style image
        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        //return newImg;
        return blur(newImg);    //Applying a blur to the picture -> a blur decreases artefacts
    }
    
    //Private function for returning the converted color
    private Color convertColor(int red, int green, int blue, int colorDepth) {
        //Changing the specific color values
        red   = color(red,   colorDepth);
        green = color(green, colorDepth);
        blue  = color(blue,  colorDepth);
        
        //Creating and returning a new color with the adjusted color values
        Color changedColor = new Color(red, green, blue);
        return changedColor;
    }
    
    //Private funtion for converting the color values to another color depth to achieve a comic effect
    private int color(int color, int colorDepth) {
        //Converting tthe colors to color ranges
        if (color > 200) {
            color = 255;
        }else if (color > 150) {
            color = 175;
        }else if (color > 100) {
            color = 125;
        }else if (color > 50) {
            color = 75;
        }else {
            color = 0;
        }
        
        //Returning the adjusted color
        return color;
    }
    
    /*
     * The funtions below are from the Folding the class.
     * For better documentation look at that class.
     */
    
    //Private function from the Folding class for applying a blur on the image
    private Picture folding(Picture originalImg, double[][] filter) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = originalImg.getPixelArray();

        for(int x=filter.length/2; x < width - filter.length/2; x++) {
            for(int y=filter.length/2;y < height - filter.length/2; y++) {
                double red, green, blue = green = red = 0.0;

                int xx = x - filter.length/2;
                int yy = y - filter.length/2;

                for (int i = 0; i < filter.length; i++) {
                    for (int j = 0; j < filter.length; j++) {
                        red   += filter[i][j] * pixelOld[xx+i][yy+j].getRed();
                        green += filter[i][j] * pixelOld[xx+i][yy+j].getGreen();
                        blue  += filter[i][j] * pixelOld[xx+i][yy+j].getBlue();

                    }
                }
                pixelNew[x][y] = new Color(validColor(red), validColor(green), validColor(blue));
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;                
    }
    
    //Check if colors are valid for the 8bit color range
    private int validColor(double pixelColor) {
        int validColor = (int)pixelColor;
        //Check if the new color is inside the 8bit color boundaries
        if (validColor > 255) {
            validColor = 255;
        } else if (validColor < 0) {
            validColor = 0;           
        } else {
            validColor = validColor;    //It literally does nothing
        }  
        //Returning the new color
        return validColor;
    }
    
    //Private function for applying a blur
    private Picture blur(Picture originalImg) {
        double[][] matrix = createMatrixForBlur(15); //Maybe the automatic blur should be used
        return folding(originalImg, matrix);
    }
    //Private function for creating a blur matrix with a given size
    private double[][] createMatrixForBlur(int size) {
        int root = 1;    //Just for default
        for (int i = 0; i < size; i++) {
            if (i*i == size) {
                root = i;
                break;
            }else if (i*i > size) {
                root = i-1;
                break;
            }
        }
        
        if (root % 2 == 0) {
            root -= 1;
        } else {
            root = root;
        }
        double[][] matrix = new double[root][root];
        for (int i = 0; i < root; i++) {
            for (int j = 0; j < root; j++) {
                matrix[i][j] = 1.0/(root*root);
            }
        }
        return matrix;
    }
}
