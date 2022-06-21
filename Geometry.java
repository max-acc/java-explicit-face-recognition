
/**
 * Write a description of class Geometry here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.lang.Math;

public class Geometry
{
    public int[][] ellipse(int sizeX, int sizeY) {
        int horizontalSize= (int)(sizeX / 2);
        int verticalSize  = (int)(sizeY / 2);
        int[] centerPoint = new int[2];
        centerPoint[0] = horizontalSize;
        centerPoint[1] = verticalSize;
        
        double x;
        double y;
        int[][] output = new int[sizeX+1][sizeY+1];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                output[i][j] = 0;
            }
        }
        
        //Equation of an ellipse in standard form
        for (int t = 0; t < 360; t++) {
            double b = Math.toRadians(t);  
            x = horizontalSize + horizontalSize  * Math.cos(t);
            y = verticalSize   + verticalSize    * Math.sin(t);
            //System.out.println(x);
            //System.out.println(y);
            output[(int)x][(int)y] = 1;
        }
        
        return output;
    }
}
