/*
The modified Cohen-Sutherland line clipping program

Modification 1.
The logical coordinates for the lines are entered in via text file. It is named Q2.txt.
The clip rectangle is fixed to the given coordinates

Modification 2.
The lines that pass through the clipping rectangle are clipped and colored black.
The parts not inside the rectangle are dotted and colored blue.

By
Devanshu Sheth
dds160030
 
 */


// ClipLine.java: Cohen-Sutherland line clipping.
// Copied from Section 4.4 of
//Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//Chichester: John Wiley.

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ClipLine extends Frame
{  
   //main method
   //throws FileNotFoundException
   public static void main(String[] args) throws FileNotFoundException{new ClipLine();}
   
   //constructor for the ClipLine
   //throws FileNotFoundException
   ClipLine() throws FileNotFoundException
   {  
      //Display Line Clipping in the title bar 
      super("Line Clipping");
      
      //check if window is resized 
      addWindowListener(new WindowAdapter()
         {
      //overrde the window closing method
      @Override
      public void windowClosing(WindowEvent e){System.exit(0);}});
      //set size of the canvas
      setSize(1000, 1000);
      add("Center", new CvClipLine());
      //show method() is deprecated
      //using setVisible
      setVisible(true);
   }
}
//class for the Canvas
class CvClipLine extends Canvas
{  
   //declare the float variables for the logical coordinates for the clip rectangle
   //declare float variables for the pixelSize
   float xmin=220.0F, xmax=420.0F, ymin=190.0F, ymax=290.0F,
      rWidth = 500.0F, rHeight = 400.0F, pixelSize;
   
   //device coordinates for center, and the max values for x and y
   int maxX, maxY, centerX, centerY, np=0;
   
   //declare 2D array for lines
   int[][] lines;
   
   //constructor for canvas class
   //throws FileNotFoundException
   CvClipLine() throws FileNotFoundException
   {
    //try with resources
    try (Scanner scan = new Scanner(new File("Q2.txt"))) 
    {
        //first int value reads the number of lines
                int n = scan.nextInt();
                //declare the 2D array for lines
                lines = new int[n][4];
                for(int k=0;k<n;k++)
                {
                    //read each x and y coordinate
                    //for each line
                    lines[k] = new int[] {
                        scan.nextInt(),
                        scan.nextInt(),
                        scan.nextInt(),
                        scan.nextInt()
                    };
                }
    }
}
   //get dimensions of the screen
   //method to declare the dimensions
   void initgr()  
   {  Dimension d = getSize();
      maxX = d.width - 1; maxY = d.height - 1;
      pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
      centerX = maxX/2; centerY = maxY/2;
   }

   //mehtods for converting to and from logical and device coordinates
   int iX(float x){return Math.round(x/pixelSize);}
   int iY(float y){return maxY - Math.round(y/pixelSize);}
   float fx(int x){return x * pixelSize;}
   float fy(int y){return (maxY - y) * pixelSize;}  

   //custom drawLine method for direct input of float values
   void drawLine(Graphics g, float xP, float yP, float xQ, float yQ)
   {  g.drawLine(iX(xP), iY(yP), iX(xQ), iY(yQ));
   }

   //clipCode as given in the textbook
   int clipCode(float x, float y)
   {  return
         (x < xmin ? 8 : 0) | (x > xmax ? 4 : 0) | (y < ymin ? 2 : 0) | (y > ymax ? 1 : 0);
   }

   //clipLine method as given in textbook with modifications
   void clipLine(Graphics g, float xP, float yP, float xQ, float yQ, float xmin, float ymin, float xmax, float ymax)
   { 
   
    g.setColor(Color.black);
    int cP = clipCode(xP, yP), cQ = clipCode(xQ, yQ);
    float dx, dy;
    
      while ((cP | cQ) != 0)
      {  
      if ((cP & cQ) != 0) 
      {
      //if clipCode does NOT return 0 for both P and Q
      //means both are outside
      //draw dashed and blue
      g.setColor(Color.blue);
      drawdashedLine(g, (xP), (yP), (xQ), (yQ), 10);

            return;
      }
      
         dx = xQ - xP; dy = yQ - yP;
         if (cP != 0)
         {  
            if ((cP & 8) == 8)
            {
            //draw dashed till the boundaries
            //boundary is bottom edge of clip rectangle
            g.setColor(Color.blue);
            drawdashedLine(g, (xP), (yP), xmin, yP+(xmin-xP)* dy/dx, 10);
            yP += (xmin-xP) * dy / dx; xP = xmin;
            }  
            //draw dashed till the boundaries
            //boundary is top edge of clip rectangle
            else if ((cP & 4) == 4)
            {
            g.setColor(Color.blue);
            drawdashedLine(g, (xP), (yP), xmax, yP+(xmax-xP) * dy / dx, 10);
            yP += (xmax-xP) * dy / dx; xP = xmax;
            } 
            //draw dashed till the boundaries
            //boundary is left edge of clip rectangle 
            else if ((cP & 2) == 2)
            {
            g.setColor(Color.blue);
            drawdashedLine(g, (xP), (yP), xP+(ymin-yP) * dx / dy, ymin, 10);
            xP += (ymin-yP) * dx / dy; yP = ymin;
            }  
            //draw dashed till the boundaries
            //boundary is right edge of clip rectangle
            else if ((cP & 1) == 1)
            {
            g.setColor(Color.blue);
            drawdashedLine(g, (xP), (yP), xP+(ymax-yP) * dx / dy,  ymax, 10);
            xP += (ymax-yP) * dx / dy; yP = ymax;
            }  
            cP = clipCode(xP, yP);
         }  
         else if (cQ != 0)
            { 
            //draw dashed till the boundaries
            //boundary is bottom edge of clip rectangle 
            if ((cQ & 8) == 8)
               {
               g.setColor(Color.blue);
               drawdashedLine(g, (xQ), (yQ), xmin, yQ+(xmin-xQ) * dy / dx, 10);
               yQ += (xmin-xQ) * dy / dx; xQ = xmin;
               }
            //draw dashed till the boundaries
            //boundary is TOP edge of clip rectangle  
            else if ((cQ & 4) == 4)
               {
               g.setColor(Color.blue);
               drawdashedLine(g, (xQ), (yQ),  xmax, yQ+(xmax-xQ) * dy / dx, 10);
               yQ += (xmax-xQ) * dy / dx; xQ = xmax;
               }
            //draw dashed till the boundaries
            //boundary is right edge of clip rectangle  
            else if ((cQ & 2) == 2)
               {
               g.setColor(Color.blue);
               drawdashedLine(g, (xQ), (yQ), xQ+(ymin-yQ) * dx / dy,  ymin, 10);
               xQ += (ymin-yQ) * dx / dy; yQ = ymin;
               }
            //draw dashed till the boundaries
            //boundary is left edge of clip rectangle 
            else if ((cQ & 1) == 1)
               {
               g.setColor(Color.blue);
               drawdashedLine(g, (xQ), (yQ), xQ+(ymax-yQ) * dx / dy,  ymax, 10);
               xQ += (ymax-yQ) * dx / dy; yQ = ymax;
               }  
               cQ = clipCode(xQ, yQ);
         }
      }
      //change color back to black
      g.setColor(Color.black);
      drawLine(g, xP, yP, xQ, yQ);
   }         
   
 //method drawdashedLine
 //this method is taken out of the program submitted in assignment 1
 //it has been changed to 
 public void drawdashedLine(Graphics g, float xA, float yA, float xB, float yB, float dashedLength)
	{	
      //Length of the dashed line
		float L =  (float)Math.hypot((xB-xA), (yB-yA));

		//Case 1: line is shorter than the dash length
                if(L <= dashedLength)
		{//draw line as is
			g.drawLine(iX(xA), iY(yA), iX(xB), iY(yB));
		}
		//line is at most equal to two dashes.
      //If this is the case we will not have two dashes at endpoints
		else if(L <= 2*dashedLength)
		{
      //draw line as is
			g.drawLine(iX(xA), iY(yA), iX(xB), iY(yB));
		}
		//line is greater than two dashes, means there is space for dashes
		else
		{
			//calculate the number of dashes using textbook formula
			int n = (int) Math.ceil(((L / (float) dashedLength) + 1) / 2);
			
         //calculate gap length as given in hints in textbook
			float gap = ((L - dashedLength*n) / (float) (n-1));
			
         //calculate x and y axis length for the gap
         float g1 = (xB-xA) / L * gap;
			float g2 = (yB-yA) / L * gap;
			
			//calculate x and y axis length for the dash
			float h1 = (xB-xA) / L * dashedLength;
			float h2 = (yB-yA) / L * dashedLength;
         
         //loop for dashes
			for(int i=0; i<n; i++)
			{
         
				float xA1 = xA + i*(h1+g1);
				float yA1 = yA + i*(h2+g2);
				float xB1 = xA + i*(h1+g1) + h1;
				float yB1 = yA + i*(h2+g2) + h2;
				//draw dashes from A1 to B1 with coordinates (xA1, yA1) and (xB1, yB1)
            g.drawLine(iX(xA1), iY(yA1), iX(xB1), iY(yB1));
			}
			
		}
	}
   

//override the paint method
@Override
   public void paint(Graphics g)
   {  
   initgr();
          //draw the clip rectangle lines red    
          g.setColor(Color.red);

         //draw the clip rectangle lines
         drawLine(g, fx(-maxX), ymin, fx(maxX), ymin);
         drawLine(g, xmin, fy(-maxY), xmin, fy(maxY));
         drawLine(g, fx(-maxX),(ymax), fx(maxX), (ymax));
         drawLine(g, (xmax), fy(-maxY), xmax, fy(maxY));
     
      //for each line in the array call the clipLine method      
      for(int[] line : lines)
		{
			clipLine(g, (line[0]), (line[1]), (line[2]), (line[3]), xmin, ymin, xmax, ymax);
		}
 
       }
}
