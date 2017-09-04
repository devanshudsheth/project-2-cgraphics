/*
This program has been modified to form more than one Curve Segment.
We can make very smooth curves with this program.

Modification.
If we press P3, another curve segment starts, P1 is calculated and displayed.
If we click on any other point another curve starts. The original curve is still displayed.
If we double click on any other point this is a signal to display all final curves. 

By Devanshu Sheth
dds160030

uses Point2D class

*/


// Bezier.java: Bezier curve segments.

// Copied from Section 4.6 of
//Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//Chichester: John Wiley.

// Uses: Point2D (Section 1.5).
import java.awt.*;
import java.awt.event.*;
import java.util.*;

//class to extend Frame
public class Bezier extends Frame
{
   //main method
  @SuppressWarnings("ResultOfObjectAllocationIgnored")
  public static void main(String[] args){
  //call the constructor
  new Bezier();}
   
   //constructor for Bezier class
   Bezier()
      {
      //diplay Bezier Curves in the title bar  
      super("Bezier Curves");
      //add a Window Listener
      addWindowListener(new WindowAdapter()
         {
             //Override windowClosing method
             @Override
             public void windowClosing(WindowEvent e){System.exit(0);}});
      //set size for the Frame
      setSize(1000, 600);
      
      //call the canvas class' constructor
      add("Center", new CvBezier());
      
      //get cross hair cursor
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      
      //show is deprecated
      //use setVisible instead
      setVisible(true);
   }
}

//class Point2D
//This is as given in textbook.
class Point2D
{

float x,y;

//constructor for Point2D
Point2D(float x, float y)
{
this.x = x;
this.y =y;
}
}

//define a class for each CurveSegment
class CurveSegment
{
//Point2D array to store the four points
Point2D[] P;
//constructor for class CurveSegment
//gets P
CurveSegment(Point2D [] P)
{

this.P = P;
}

}

// class CvBezier that extends canvas
class CvBezier extends Canvas
{  
   //define Vector curves for various curve segments
   Vector curves = new Vector();

   //flag to signal that curve must be continued
   boolean continuecurve = false;
   
   //declare Point2D array
   Point2D[] p ;
   
   //these values store the last two clicks
   //used for checking is same point is pressed again
   float x1check= 0, y1check = 0, x2check, y2check;
   
   //flag to signal we wish to display all final curves
   boolean savecurve = false;
   
   //define float variables for center, pixelSize and rWidth, rHeight
   int np = 0, centerX, centerY;
   float rWidth = 10.0F, rHeight = 7.5F, eps = rWidth/100F, pixelSize;
   
   //point2D points
   //used to record values
   Point2D p3, p0, p1, p2;
   
   //constructor for Canvas class
   CvBezier()
   {  
      //add a mouse click listener
      addMouseListener(new MouseAdapter()
      {  
         //override the mouse pressed event method 
         @Override
         public void mousePressed(MouseEvent evt)
         {  
            //get logical coordinates of point pressed
            float x = fx(evt.getX()), y = fy(evt.getY());
            
            //if more than one click has been made record into x2check, y2check
            if(x1check != 0 && y1check !=0 )
            {
                x2check = x1check;
                y2check = y1check;
            }
            //give x1check and y1check current x and y values
            x1check = x;
            y1check = y;
            
            //if np becomes 4, make it zero.
            //only indexes 0 to 3 are possible for four points
            if(np == 4 )
            {
                np = 0;
           
            }
        //if curves is not empty
        if(!curves.isEmpty())
        {
            //check difference in coordinates of point p3 from previous curve and current mouse click 
            float dx = p3.x - x;
            float dy = p3.y - y;
          
            //if less than 8 * pixelSize * pixelSize set flag continuecurve
           if(  (dx * dx + dy * dy < 8 * pixelSize * pixelSize)  )
                    {
                       //new curve, thus np = 0
                       np = 0; 
                       continuecurve = true; 
                    }
                    
          //if same point is clicked again and continuecurve flag is not set, we wish to save the current curves
          if(x1check == x2check && y1check == y2check && !continuecurve)
          {
              //set np 4 to satisfy bezier if condition
              np = 4;
              savecurve = true;
          }
          
           }
         
          //switch np
          switch (np) {
              case 0:
                     //if zero, and continuecurve is raised
                   if(continuecurve )
                   {
                      //new Point2D array p
                      //new Point2D P0, this was old Point2D p3
                      p = new Point2D[4];
                      p0 = new Point2D(p3.x,p3.y);
                      p[0] = p0;
                      np++;
                      
                    //new Point2D P1, this is calculated as required
                    //old P3 will be midpoint of old P2 and new P1
                    p1 = new Point2D(2*p3.x - p2.x,2*p3.y - p2.y);
                    p[1] = p1;
                    np++;
                    
                    //set continuecurve flag to false
                    continuecurve = false;
                      
                   }
                  //if continuecurve flag is not raised 
                  else 
                  {
                  //create new Point2D array 
                  p = new Point2D[4];  
                  //new Point2D, coordinates are current x and y coordinates
                  p0 = new Point2D(x,y);
                  p[np] = p0;
                  np++;
                    }
                  break;
              case 1:
                  //new Point2D, coordinates are current x and y coordinates
                  p1 = new Point2D(x,y);
                  p[np] = p1 ;
                  np++;
                 
               break;
              case 2:
                 //new Point2D, coordinates are current x and y coordinates
                  p2 = new Point2D(x,y);
                  p[np]= p2 ;
                  np++;
                  break;
              case 3:
                  //new Point2D, coordinates are current x and y coordinates
                  p3 = new Point2D(x,y);
                  p[np] = p3 ;
                  np++;
                  curves.addElement(new CurveSegment(p));
                 
                  break;
              default:
                  break;
          }
          repaint();
       
         }
      });
   }

   //get dimensions of screen
   void initgr()  
   {  Dimension d = getSize();
      //get device coordinates for maxX and maxY, calculate pixelSize and center coordinates
      int maxX = d.width - 1, maxY = d.height - 1;
      pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
      centerX = maxX/2; centerY = maxY/2;
   }
   
   //methods for conversions between logical and device coordinates
   int iX(float x){return Math.round(centerX + x/pixelSize);}
   int iY(float y){return Math.round(centerY - y/pixelSize);}
   float fx(int x){return (x - centerX) * pixelSize;}
   float fy(int y){return (centerY - y) * pixelSize;}   
   
   Point2D middle(Point2D a, Point2D b)
   {  return new Point2D((a.x + b.x)/2, (a.y + b.y)/2);
   }
   
  
   //recursive bezier method 
   //modified slightly to take Point2D array instead of four Point2D individually
   void bezier(Graphics g, Point2D[] k)
   {  
       /*
       //Iterative Method
       //Used to see difference ( learning purposes only )
           int n = 200;
           float   dt = 1.0F/n,
                   cx3 = -k[0].x + 3 * (k[1].x - k[2].x) + k[3].x,
                   cy3 = -k[0].y + 3 * (k[1].y - k[2].y) + k[3].y,
                   cx2 = 3 * (k[0].x - 2 * k[1].x + k[2].x),
                   cy2 = 3 * (k[0].y - 2 * k[1].y + k[2].y),
                   cx1 = 3 * (k[1].x - k[0].x),
                   cy1 = 3 * (k[1].y - k[0].y),
                   cx0 = k[0].x,
                   cy0 = k[0].y,
                   x = k[0].x, y = k[0].y, x0, y0;
           
           for(int i = 1; i <= n ; i++)
           {
               float t = i * dt;
               x0 = x; y0 = y;
               x = ((cx3 * t + cx2) * t + cx1) * t + cx0;
               y = ((cy3 * t + cy2) * t + cy1) * t + cy0;
               g.drawLine(iX(x0), iY(y0), iX(x), iY(y));
           }
           */
       Point2D q0,q1,q2,q3;
       
       q0 = k[0]; q1 = k[1]; q2 = k[2]; q3 = k[3];   
           
       int x0 = iX(q0.x), y0 = iY(q0.y),
          x3 = iX(q3.x), y3 = iY(q3.y);
      if (Math.abs(x0 - x3) <= 1 && Math.abs(y0 - y3) <= 1)
         g.drawLine(x0, y0, x3, y3);
      else
      {  Point2D a = middle(q0, q1), b = middle(q3, q2),
            c = middle(q1, q2), a1 = middle(a, c),
            b1 = middle(b, c), c1 = middle(a1, b1);
            Point2D[] k1 = new Point2D[4];
            k1[0] = q0;
            k1[1] = a;
            k1[2] = a1;
            k1[3] = c1;
            
            Point2D[] k2 = new Point2D[4];
            k2[0] = c1;
            k2[1] = b1;
            k2[2] = b;
            k2[3] = q3;
            
         bezier(g, k1);
         bezier(g, k2);
      }
       
   }

   
   //override the paint method 
   @Override
   public void paint(Graphics g)
   {  
     initgr();
     
     //draw the rectangle
     int left = iX(-rWidth/2), right = iX(rWidth/2),
     bottom = iY(-rHeight/2), top = iY(rHeight/2);
     g.drawRect(left, top, right - left, bottom - top);
     
     //get size of curves
     int n = curves.size();
     if (n == 0 )
     {
     for (int i=0; i<np; i++)
         {
         //Show tiny rectangle around point:
         g.drawRect(iX(p[i].x)-2, iY(p[i].y)-2, 8, 8);
         
         if ( i > 0) 
         { //Draw line p[i-1]p[i]:
            g.drawLine(iX(p[i-1].x), iY(p[i-1].y), iX(p[i].x), iY(p[i].y));
         }    
       
        }

    return;
}
     //if savecurve flag is raised
     //do not display any rectangles or lines, just the curves
     if(savecurve)
     {
         //display to user
         g.drawString("Your final curve(s)", 20,20);
         for(int i = 0; i < n; i++)
         {
             CurveSegment y = (CurveSegment)curves.elementAt(i);
             Point2D[] a = y.P;
        
             if (np == 4)  bezier(g, a);
             
         
         }
         //once final curves are displaed clear the curves vector
         savecurve = false;
         curves.clear();
     }
     //if flag is not raised
     else{
   
     for(int j = 0; j < n ; j++)
     {
        CurveSegment y = (CurveSegment)curves.elementAt(j);
        Point2D[] b = y.P;
        
        for (int i=0; i<np; i++)
         {
         // Show tiny rectangle around point:
         g.drawRect(iX(p[i].x)-2, iY(p[i].y)-2, 8, 8);
         
         if ( i > 0) 
         { //Draw line p[i-1]p[i]:
            g.drawLine(iX(p[i-1].x), iY(p[i-1].y), iX(p[i].x), iY(p[i].y));
         }    
       
        }
      if (np == 4) bezier(g, b);   
     }
}
     
      
     
     
         }
}
