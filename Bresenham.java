/* Bresenham.java

This program creates a grid of Superpixels.
It will demonstrate the Bresenham algorithm for line drawing and circle drawing.
This is done by filling the pixels in the grid.

Modification 1.
This program reads the points between which the line must be drawn and 
the radius and center of the circle. This is read from a text file Q1.txt

Modification 2.
It will also color the pixels according to their slopes.
Line with slope 1 will be black.
Line with slope 0 or 90 will be gray.
Rest of the lines will have colors in between according to the slope.


By
Devanshu Sheth
dds160030

*/
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Bresenham extends Frame
{
	public static void main(String[] args) throws FileNotFoundException
	{
            
		new Bresenham();
	}

	public Bresenham() throws FileNotFoundException
	{
      //display SuperPixels.java in title bar 
		super("Bresenham.java - SuperPixels");
                
      //check if window is resized 
		addWindowListener(new WindowAdapter() {

      //overrde the window closing method
      @Override
		public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
      //set size of the canvas
		setSize(1200, 1000);
      //call constructor to Canvas class
		add("Center", new CvSuperPixels());
		//show method() is deprecated
      //using setVisible
      setVisible(true);
	}
}
//class for the Canvas
class CvSuperPixels extends Canvas
{
   // declare the variables needed
   //device coordinates for the center
   //pixels for each data grid
	int dGrid = 20;
	int centerX, centerY;
	
        //float values for the logical width and height, and the actual values 
        float pixelSize, rWidth = 20.0F, rHeight = 20.0F, realrWidth, realrHeight;

        
        //2D array to store the lines
	     int[][] lines;
        
        //device coordinates for center of the circle and the radius
        int circleX, circleY, circleR;
	
        //constructor for the canvas
        CvSuperPixels() throws FileNotFoundException
	      {
            
            //try with resources for scanning int values from the Q1.txt file.
            try (Scanner scan = new Scanner(new File("Q1.txt"))) {
                //first int value reads the number of lines
                int n = scan.nextInt();
                //declare the 2D array for lines
                lines = new int[n][4];
                for(int k=0;k<n;k++)
                {
                    //read each x and y coordinate
                    //for each line
                    lines[k] = new int[] 
                    {
                        scan.nextInt(),
                        scan.nextInt(),
                        scan.nextInt(),
                        scan.nextInt()
                    };
                }
                //next int reads the x coordinate of the circle's center
                circleX = scan.nextInt();
                //next int reads the y coordinate of the circle's center
                circleY = scan.nextInt();
                //next int reads the radius of the circle
                circleR = scan.nextInt();
            }
	}

	//get dimensions of the screen
   //method to declare the dimensions       
	protected void initgr()
            {
            Dimension d = getSize();
		      //device coordinates for maxX and maxY
            //calculate the pixelSize and center device coordinates
            int maxX = d.width - 1, maxY = d.height - 1;
		      pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
		      centerX = maxX / 2;
		      centerY = maxY / 2;
		
		      realrWidth = maxX * pixelSize;
		      realrHeight = maxY * pixelSize;
	         }

	//Conversion from logical coordinates to device coordinates
   //and from device coordinates to logical coordinates
	int iX(float x){return Math.round(centerX + x / pixelSize);}
	int iY(float y){return Math.round(centerY - y / pixelSize);}
	float fx(int X){return (X - centerX) * pixelSize;}
	float fy(int Y){return (centerY - Y) * pixelSize;}

      //override the paint method
      @Override
	   public void paint(Graphics g)
	   {
      //call the method to declare the pixelSize 
		initgr();
		
      //call the method to draw the grid
		showGrid(g);
                
      //for each line call the drawLine methods
		for(int[] line : lines)
		{
			drawLine(g, line[0], line[1], line[2], line[3]);
		}
      //drawCircle
		drawCircle(g, circleX, circleY, circleR); // g, xC, yC, r
	}
	
   
	
   //modified putPixel method
   //draws circles to represent the pixels
   //also fills them with color according to the slope of their lines
	private void putPixel(Graphics g, int x, int y)
	{
      int x1 = x * dGrid, y1 = y * dGrid, h = dGrid/2;
      g.fillOval(x1-h, y1-h, dGrid+1, dGrid+1);
		g.drawOval(x1-h, y1-h, dGrid, dGrid);
	}
	
   //Bresenham line drawing algorithm
   //modified parts have comments
   //as given in textbook
	void drawLine(Graphics g, int xP, int yP, int xQ, int yQ)
	{  int x = xP, y = yP, d = 0, dx = xQ - xP, dy = yQ - yP,
	      c, m, xInc = 1, yInc = 1;
	   if (dx < 0){xInc = -1; dx = -dx;}
	   if (dy < 0){yInc = -1; dy = -dy;}
	   if (dy <= dx)
	   {  c = 2 * dx; m = 2 * dy;
	     if (xInc < 0) dx++;
	     for (;;)
	     {
              //set color to gray for horizontal lines and vertical lines
              if( m == 0 || dx == 0)
              {
                  g.setColor(new Color(160,160,160));
              }
              //set color to black for lines with slope 45
              else if(( c == m) || ( m == -c))
              {
                g.setColor(Color.black);
              }
              //set color to a calculated RGB value based on the slope
              else{
                  double M = (double)m;
                     double C = (double)c;
                     double k = -160*(M/C)+160;
                     int a = (int) k;
                     if ( a > 160) a = 160;
                     if ( a < 0) a = 0;
                     g.setColor(new Color(a,a,a));
   
              }
              //put each pixel point by point
                 putPixel(g, x, y);
	        if (x == xQ) break;
	        x += xInc;
	        d += m;
	        if (d >= dx){y += yInc; d -= c;}
	     }
	   }
	   else
	   {  c = 2 * dy; m = 2 * dx;
	      if (yInc < 0) dy++;
	      for (;;)
	      {  
              //set color to gray for horizontal lines and vertical lines
              if( m == 0 || dx == 0)
              {
                  g.setColor(new Color(160, 160, 160));
              }
              //set color to black for lines with slope 45
              else if( c == m || -c == m)
             {
                  g.setColor(Color.black);
             }
              //set color to a calculated RGB value based on the slope
              else{
                     double M = (double)m;
                     double C = (double)c;
                     double r = -160*(M/C)+160;
                     int a = (int) r;
                     if ( a > 160) a = 160;
                     if ( a < 0) a = 0;
                     g.setColor(new Color(a,a,a));
              }
                 putPixel(g, x, y);
	         if (y == yQ) break;
	         y += yInc;
	         d += m;
	         if (d >= dy){x += xInc; d -= c;}
	   }
	 }
	}
	
   //bresenham circle drawing algorithm as given in textbook
	void drawCircle(Graphics g, int xC, int yC, int r)
	{  int x = 0, y = r, u = 1, v = 2 * r - 1, e = 0;
	   while (x < y)
              {  
              g.setColor(Color.black);
              putPixel(g, xC + x, yC + y); // NNE
	           putPixel(g, xC + y, yC - x); // ESE
	           putPixel(g, xC - x, yC - y); // SSW
	           putPixel(g, xC - y, yC + x); // WNW
	           x++; e += u; u += 2;
	           if (v < 2 * e){y--; e -= v; v -= 2;}
	           if (x > y) break;
	           putPixel(g, xC + y, yC + x); // ENE
	           putPixel(g, xC + x, yC - y); // SSE
	           putPixel(g, xC - y, yC - x); // WSW
	           putPixel(g, xC - x, yC + y); // NNW
	   }
	}

   //showGrid draws the grid
   //as given in the hints to solutions in the book
        private void showGrid(Graphics g)
	{
		Dimension d = getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		
                for(int x = dGrid; x <= maxX; x += dGrid)
		{
			for(int y = dGrid; y <= maxY; y += dGrid)
			{
				g.drawLine(x, y, x, y);
			}
		}
	}
}