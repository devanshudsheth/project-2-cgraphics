# project-2-cgraphics
1. (4%) Exercise 4.3 in page 112 of textbook, with the following
   additional requirements

- The program will read a text file (name it as Q1.txt), 
  including the measurements of a number of lines and a 
  circle in the format:
  
  N               (int: number of lines up to 10)
  X1a Y1a X1b Y1b (int: endpoints of line 1)
  X2a Y2a X2b Y2b
  ......
  XNa YNa XNb YNb (int: endpoints of line N)
  Xc Yc R         (int: Center point and Radius of circle)

- To overcome the problem of different line intensities at 
  different slopes, the "superpixels" of each line should be 
  filled and drawn with a gray-scaled color according to its 
  slope. A vertical/horizontal line should be gray, a lines 
  of 45 degree should be completely black, and lines of 
  other angles should have colors in-between black and
  gray (continuous as slope). You may use Java method
                  g.setColor(new Color(R, G, B))
  to implement this (e.g. black: R=0, G=0, B=0).

2. (3%) Modify the Java program ClipLine.java for Cohen-Sutherland line 
   clipping algorithm (page 95) to achieve the following

- The clip rectangle is fixed at (Xmin=220, Ymin=190, Xmas=420,
  Ymax=290)

- The program will read a text file (name it as Q2.txt), including 
  a number of lines to be clipped in the format:
  
  N               (int: number of lines up to 10)
  X1a Y1a X1b Y1b (float: endpoints of line 1)
  X2a Y2a X2b Y2b
  ......
  XNa YNa XNb YNb (float: endpoints of line N)

- The program should clip all the lines provided in the line file.
  As a result of clipping, it displays the line segments outside of 
  the clip rectangle as dotted lines, and those inside the clip 
  rectangle as solid lines.

3. (3%) Exercise 4.4 in page 136-137 of textbook.

You can assume the above numbers in the input file to be integers
(thus as in the device coordinate system).
In addition to producing working programs in Java, you should also 
explain clearly, as comments at the beginning of each program, how 
the algorithm is modified/implemented. The text file names and 
formats must be exactly the same as specified, otherwise you will 
be penalized.
