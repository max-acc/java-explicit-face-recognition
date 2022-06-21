/**
 * Class with functions for image correction.
 * 
 * @author Max Wenk 
 * @version 6/14/2022
 * 
 * Funtions:
 * - linearColorAdaption (Picture originalImg, int lowerThreshold, int upperThreshold)
 * - automaticColorAdaption (Picture originalImg)
 * - gammaCorrecture (Picture originalImg, double gamma)
 */

//Importing libraries
import imp.*;
import java.awt.Color;
import java.util.Random;

public class Histograms {
    //Private function for calculating the largest number from an array
    private int largestNumber(int[] input) {
        int output = 0; //Setting a default output, just in case

        //Iterating through the input array and comparing the numbers
        for (int i = 0; i < input.length - 1; i++) {
            if(input[i] > output) output = input[i];
        }

        //Returning the output
        return output;
    }

    //Private Function for drawing a histogram of the color distribution -> just for visualization -> there's no need for this in the programm
    public Picture drawHistogram(int[] distributionFrequency) {
        //Creating a new picture to display the histogram
        Picture newPicture = new Picture(256,100,"C0C0C0");
        newPicture.stroke(0);

        //Color[][] histogramArray = new Color[256][100];
        //int[] frequency = distributionFrequency(originalImg);   //Getting the distribution frequency

        int largestValue = largestNumber(distributionFrequency);    //Getting the largest number
        System.out.println(largestValue);
        System.out.println(distributionFrequency.length);
        for (int word : distributionFrequency) {
             //System.out.println(word);
        }
       
        //Converting the numbers to fit them into the histogram
        //int[] numberSet = convertNumbers(frequency, largestValue);

        //Printing the distribution frequency
        for (int i = 0; i < 256; i++){
            //System.out.print(histogramArray[i][100 - (int)(largestValue/frequency[i])*100]);
            int a;
            a = distributionFrequency[i]*100/largestValue;
            newPicture.line(i, 100-a, i, 100);
            //histogramArray[i][100 - numberSet[i]] = new Color (0,0,0);

        }

        //Returning the histogram with the distribution frequency
        return newPicture;
    }

    //Private function for returning an array with the color distribution
    public int[] distributionFrequency (Picture originalImg) {
        
        
        //Creating an array with an 8bit size for the brightness values and setting default values
        int[] brightness = new int[256];
        for (int i = 0; i < brightness.length; i++) {
            brightness[i] = 0;
        }

        //Iterating through the array and setting the average brightness as integer to the array
        Color[][] pixelOld = originalImg.getPixelArray();
        for (int width = 0; width < originalImg.getWidth(); width++) {
            for (int height = 0; height < originalImg.getHeight(); height++) {
                brightness[(int)(pixelOld[width][height].getRed() + pixelOld[width][height].getGreen() + pixelOld[width][height].getBlue()) / 3] += 1;
            }
        }

        return brightness;
    }

    //Private function for converting the number, so they are in the required range -> not necessary in this case because they are directly converted where they are used
    private int[] convertNumbers (int[] frequency, int largestNumber) {
        int[] convertedNumbers = new int[256];

        //double ratio = largestNumber/100;

        for (int i = 0; i < 256; i++) {
            //convertedNumbers[i] = (int) (frequency[i] * ratio);
            convertedNumbers[i] = (int)((frequency[i]* 100) / largestNumber);
        }

        return convertedNumbers;
    } 

    //Private function for adjusting the image to the histogram values 
    private Picture histogramAdjustment (Picture originalImg, int[] adjustment) {
        Color[][] pixelOld = originalImg.getPixelArray();
        Color[][] pixelNew = new Color[originalImg.getWidth()][originalImg.getHeight()];

        //Iterating through every pixel of the image
        for (int width = 0; width < originalImg.getWidth() ; width++){ 
            for (int height = 0; height < originalImg.getHeight() ; height++) { 
                //Adjusting the color values to the histogram
                int red   = (int) adjustment[pixelOld[width][height].getRed()];
                int green = (int) adjustment[pixelOld[width][height].getGreen()];
                int blue  = (int) adjustment[pixelOld[width][height].getBlue()];

                //Setting a new color
                pixelNew[width][height] = new Color(red, green, blue);
            } 
        }

        //Returning the picture
        Picture newImg = new Picture(); 
        newImg.setPixelArray(pixelNew); 
        return newImg;
    }   

    //Function for linear color adaption with an upper and a lower threshold as input
    public Picture linearColorAdaption (Picture originalImg, int lowerThreshold, int upperThreshold){
        int[] classification = new int[256];
        
        //For DAU ("dumbest imagineable user") -> so there are no input errors
        if (lowerThreshold == upperThreshold) {
            lowerThreshold -= 1;
            upperThreshold += 1;
        }
        if (lowerThreshold < 0) {
            lowerThreshold = 0;            
        }else if (lowerThreshold > 255) {
            lowerThreshold = 255;            
        }
        if (upperThreshold < 0) {
            upperThreshold = 0;            
        }else if (upperThreshold > 255) {
            upperThreshold = 255;            
        }
        if (lowerThreshold > upperThreshold) {
            int a = lowerThreshold;
            lowerThreshold = upperThreshold;
            upperThreshold = a;
        }
        

        for(int i = 0; i < 256; i++) {
            if(i < lowerThreshold) {
                classification[i] = 0;  //Setting all array values which are below the lower threshold to 0
            }else if(i >= upperThreshold) {
                classification[i] = 255;    //Setting all array values which are above the upper threshold to 255
            }else if(i >= lowerThreshold  &&  i < upperThreshold) {
                classification[i] = 255*(i-lowerThreshold)/(upperThreshold-lowerThreshold); //Mapping the values between to lower and upper threshold to the array
            }
        }

        //Returning the color adjusted image
        return histogramAdjustment(originalImg, classification);
    }

    //Function for automatic color adaption
    public Picture automaticColorAdaption (Picture originalImg) {
        int[] frequency = distributionFrequency(originalImg);   //Getting the color distribution frequency of the image
        int[] classification = new int[256];    //Creating a new integer array for classifying the color values
        int overallPixels = originalImg.getWidth() * originalImg.getHeight();   //Number of pixels in the image

        int sum = 0;
        for(int i = 0; i < 256; i++) {
            sum += frequency[i];    //Adding the frequncy of every element to the sum
            classification[i] = 255 * sum / overallPixels;  //Calculating values for the specific classification values
        }
        //Returning the color adjusted image
        return histogramAdjustment(originalImg, classification);
    }

    //Function for gamma correcture with a gamma value as input
    public Picture gammaCorrecture (Picture originalImg, double gamma) {
        //For DAU ("dumbest imagineable user") -> so there are no input errors
        if (gamma < 0.0) {
            gamma = 0.0;
        }
        
        int[] classification = new int[256]; //Creating a new integer array for classifying the color values
        //Iterating though the array and applying the gamma value
        for(int i=0; i < 256; i++) {
            classification[i] = (int) (Math.pow( ((double) i)/255.0, gamma) * 255.0); 
        }
        //Returning the color adjusted image
        return histogramAdjustment(originalImg, classification);
    }
}