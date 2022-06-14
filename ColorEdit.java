/**
 * Class with functions for basic image color editing.
 * 
 * @author Max Wenk 
 * @version 5/8/2022
 * 
 * Funtions:
 * - greyScaleAvg (originalImg)
 * - greyScaleMax (originalImg)
 * - greyScaleMin (originalImg)
 * - greyScaleNatural (originalImg)
 * - colorInvert (originalImg)
 * - changeRedGreen (originalImg)
 * - changeRedBlue (originalImg)
 * - changeGreenBlue (originalImg)
 * - changeSaturation (originalImg, factorR, factorG, factorB)
 */

import imp.*;
import java.awt.Color;
import java.util.Random;

public class ColorEdit {
    //Changing the image to a greyscaled image by the average pixel brightness -> Average Greyscale Image
    public Picture greyScaleAvg (Picture originalImg) {
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

    //Changing the image to a greyscaled image by the maximum pixel saturation -> Light Greyscale Image
    public Picture greyScaleMax (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorAvg = Color.GREEN;

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Calculating the largest brightness and set it as saturation
                int max;
                if ((pixelOld[x][y].getRed() >= pixelOld[x][y].getGreen()) & (pixelOld[x][y].getRed() >= pixelOld[x][y].getBlue())) {
                    max = pixelOld[x][y].getRed();
                } else if ((pixelOld[x][y].getGreen() >= pixelOld[x][y].getRed()) && (pixelOld[x][y].getGreen() >= pixelOld[x][y].getBlue())) {
                    max = pixelOld[x][y].getGreen();
                } else if ((pixelOld[x][y].getBlue() >= pixelOld[x][y].getRed()) & (pixelOld[x][y].getBlue() >= pixelOld[x][y].getGreen())) {
                    max = pixelOld[x][y].getBlue();
                } else {
                    //Error ig 
                    max = 0;
                }

                colorAvg = new Color(max, max, max); 
                pixelNew[x][y] = colorAvg;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Changing the image to a greyscaled image by the minimum pixel saturation -> Dark Greyscale Image
    public Picture greyScaleMin (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorAvg;
        colorAvg = Color.GREEN;

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Calculating the lowest brightness and set it as saturation
                int min;
                if ((pixelOld[x][y].getRed() <= pixelOld[x][y].getGreen()) & (pixelOld[x][y].getRed() <= pixelOld[x][y].getBlue())) {
                    min = pixelOld[x][y].getRed();
                } else if ((pixelOld[x][y].getGreen() <= pixelOld[x][y].getRed()) & (pixelOld[x][y].getGreen() <= pixelOld[x][y].getBlue())) {
                    min = pixelOld[x][y].getGreen();
                } else if ((pixelOld[x][y].getBlue() <= pixelOld[x][y].getRed()) & (pixelOld[x][y].getBlue() <= pixelOld[x][y].getGreen())) {
                    min = pixelOld[x][y].getBlue();
                } else {
                    //Error ig
                    min = 0;
                }

                colorAvg = new Color(min, min, min);
                pixelNew[x][y] = colorAvg;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Changing the image to a greyscaled image by the natural pixel saturation -> Natural Greyscale Image
    public Picture greyScaleNatural (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorAvg = Color.GREEN;

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Calculating the a new saturation with natural light values
                int natural = (int)(pixelOld[x][y].getRed() * 0.299 + pixelOld[x][y].getGreen() * 0.587 + pixelOld[x][y].getBlue() * 0.114) / 3;
                colorAvg = new Color(natural, natural, natural);
                pixelNew[x][y] = colorAvg;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Inverting the colors of the image -> Inverted Colorful Image
    public Picture colorInvert (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorInvert = Color.GREEN;

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Invert an 8bit (0-255) color
                colorInvert = new Color(255 - pixelOld[x][y].getRed(), 255 - pixelOld[x][y].getGreen(), 255 - pixelOld[x][y].getBlue());
                pixelNew[x][y] = colorInvert;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Exchanging the red and the green color saturations -> Weird Colorful Picture
    public Picture changeRedGreen (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorChange = Color.GREEN;    //Default color

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Setting the old red color values to the new green color values, and the old green color values to the new red color values
                colorChange = new Color(pixelOld[x][y].getGreen(), pixelOld[x][y].getRed(), pixelOld[x][y].getBlue());
                pixelNew[x][y] = colorChange;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Exchanging the red and the blue color saturations -> Weird Colorful Picture
    public Picture changeRedBlue (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorChange = Color.GREEN;    //Default color

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Setting the old red color values to the new green color values, and the old green color values to the new red color values
                colorChange = new Color(pixelOld[x][y].getBlue(), pixelOld[x][y].getGreen(), pixelOld[x][y].getRed());
                pixelNew[x][y] = colorChange;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Exchanging the green and the blue color saturations -> Weird Colorful Picture
    public Picture changeGreenBlue (Picture originalImg) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorChange = Color.GREEN;    //Default color

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Setting the old red color values to the new green color values, and the old green color values to the new red color values
                colorChange = new Color(pixelOld[x][y].getRed(), pixelOld[x][y].getBlue(), pixelOld[x][y].getGreen());
                pixelNew[x][y] = colorChange;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Changing the saturation of the different color sections
    public Picture changeSaturation (Picture originalImg, double factor_r, double factor_g, double factor_b) {
        int width = originalImg.getWidth();
        int height = originalImg.getHeight();

        Color[][] pixelOld= originalImg.getPixelArray();
        Color[][] pixelNew = new Color[width][height];
        Color colorSat = Color.GREEN;

        for(int x=0; x < width; x++) {
            for(int y=0;y < height; y++) {
                //Calling the "validSaturation" function to validate a new saturated color => old color * factor of saturation
                colorSat = new Color(validSaturation(pixelOld[x][y].getRed(), factor_r), validSaturation(pixelOld[x][y].getGreen(), factor_g), validSaturation(pixelOld[x][y].getBlue(), factor_b));
                pixelNew[x][y] = colorSat;
            }
        }

        Picture newImg = new Picture();
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }

    //Private Funtion for returning a valid, saturated color value
    private int validSaturation(int pixelColor, double factor) {
        //Multiplying the pixel color value with the saturation factor
        double validColor = pixelColor * factor;
        //Check if the new color is inside the 8bit color boundaries
        if (validColor > 255) {
            validColor = 255;
        } else if (validColor < 0) {
            validColor = 0;           
        } else {
            //Error occured
        }  
        //Returning the new color
        return (int)validColor;
    }
}
