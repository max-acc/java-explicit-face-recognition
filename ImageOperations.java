/**
 * Beschreiben Sie hier die Klasse ColorEdit.
 * 
 * @author Max Wenk 
 * @version 5/8/2022
 * 
 * Funtions:
 * - mirrorHorizontal
 * - mirrorVertical
 * - turnLeft
 * - turnRight
 * - turnUpSideDown
 */

//Importing libraries
import imp.*;
import java.awt.Color;
import java.util.Random;

public class ImageOperations {
	//Mirroring the picture horizontally
        public Picture mirrorHorizontal (Picture originalImg) {
		//Getting the width and the height of the original image
                int width = originalImg.getWidth();   
		int height = originalImg.getHeight();
                
		//Transforming the image to a two dimensional array and creating one for the new image
        	Color[][] pixelOld = originalImg.getPixelArray();
        	Color[][] pixelNew = new Color[width][height];
                
        	//Changing every pixel of the old pixel array to the new one
        	for(int x=0; x < width; x++) {
            		for(int y=0;y < height; y++) {
                		//Mirroring the x coordinate of the picture
                		pixelNew[x][y] = pixelOld[(width-1) - x][y];
            		}
        	}
                
        	//Transforming the new pixel arry into a new picture and return it
        	Picture newImg = new Picture();
        	newImg.setPixelArray(pixelNew); 
        	return newImg;
    	}
    	//Mirroring the picure vertically
	public Picture mirrorVertical (Picture originalImg) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();

        	Color[][] pixelOld= originalImg.getPixelArray();
        	Color[][] pixelNew = new Color[width][height];

        	for(int x=0; x < width; x++) {
            		for(int y=0;y < height; y++) {
                		//Mirroring the y coordinate of the picture
                		pixelNew[x][y] = pixelOld[x][(height-1) - y];
            		}
        	}

        	Picture newImg = new Picture();
        	newImg.setPixelArray(pixelNew); 
        	return newImg;
    	}
	//Turning the picture to the left
	public Picture turnLeft (Picture originalImg) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();

        	Color[][] pixelOld= originalImg.getPixelArray();
        	Color[][] pixelNew = new Color[height][width];

        	for(int x=0; x < width; x++) {
            		for(int y=0;y < height; y++) {
                		//If the picture should turn to the left, the old y coordinate is the new x coordinate, and the old x is the new y
                		pixelNew[x][y] = pixelOld[y][x];
            		}
        	}

        	Picture newImg = new Picture();
                newImg.setPixelArray(pixelNew); 
        	return newImg;
    	}
    	//Turning the picture to the right
	public Picture turnRight (Picture originalImg) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();

        	Color[][] pixelOld= originalImg.getPixelArray();
        	Color[][] pixelNew = new Color[height][width];

        	for(int x=0; x < width; x++) {
            		for(int y=0;y < height; y++) {
                		//If the picture should turn to the left, the old mirrored y coordinate is the new x coordinate, and the old x is the new y
                		pixelNew[x][y] = pixelOld[(height-1) - y][x];
            		}
        	}

        	Picture newImg = new Picture();
        	newImg.setPixelArray(pixelNew); 
        	return newImg;
    	}
    	//Turning the picture Upside-Down
	public Picture turnUpSideDown (Picture originalImg) {
		int width = originalImg.getWidth();
		int height = originalImg.getHeight();

        	Color[][] pixelOld= originalImg.getPixelArray();
        	Color[][] pixelNew = new Color[width][height];

        	for(int x=0; x < width; x++) {
            		for(int y=0;y < height; y++) {
                		//If the picture should turn to the left, the old mirrored y coordinate is the new x coordinate, and the old mirrored x is the new y
                		pixelNew[x][y] = pixelOld[(width-1) - x][(height-1) - y];
            		}
        	}

        	Picture newImg = new Picture();
        	newImg.setPixelArray(pixelNew); 
        	return newImg;
        }
}
