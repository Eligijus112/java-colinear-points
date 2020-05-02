import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    // Number of line segments
    private int nLines = 0;

    // Linesegment placeholder
    private LineSegment[] lines;

    // The fast collinear point search
    public FastCollinearPoints(Point[] points) {
        // Checking if the points are are not a null object
        if (points == null) throw new IllegalArgumentException();

        // Checking for duplicates and null points
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // Saving the points to memory
        Point[] pointsMemory = new Point[points.length];
        System.arraycopy(points, 0, pointsMemory, 0, points.length);

        // Sorting the points
        Arrays.sort(pointsMemory);

        // Saving the total number of points
        int nPoints = pointsMemory.length;

        // Copying the original array of points
        Point[] pointsOrig = Arrays.copyOf(pointsMemory, nPoints);

        // Creating a temporary array list object to add the line segments in
        ArrayList<LineSegment> linesTemp = new ArrayList<>();

        // Creating arrays for max and min points to avoid duplicates
        ArrayList<Point> maxPoints = new ArrayList<>();
        ArrayList<Point> minPoints = new ArrayList<>();

        // Iterating through all the points
        if (nPoints > 1) {
            for (int i = 0; i < nPoints; i++) {
                // Sorting the arrays based on the original point i and the slope it makes with other points
                Arrays.sort(pointsMemory, pointsOrig[i].slopeOrder());

                // Initiating the initial slope
                double curSlope = pointsMemory[0].slopeTo(pointsMemory[1]);

                // Starting the countdown
                int j = 1;
                // Populating the same line points
                while (j < nPoints - 1) {
                    if (curSlope == pointsMemory[0].slopeTo(pointsMemory[j + 1])) {
                        // Initiating a placeholder for points
                        ArrayList<Point> currentPoints = new ArrayList<>();

                        // Adding the first point
                        currentPoints.add(pointsMemory[j]);

                        // Checking how far do the points go with the same slope
                        int h = j;
                        while (h < nPoints - 1) {
                            if (curSlope == pointsMemory[0].slopeTo(pointsMemory[h + 1])) {
                                currentPoints.add(pointsMemory[h + 1]);
                            } else {
                                break;
                            }
                            h++;
                        }

                        // Checking if the points are more than 3
                        if (currentPoints.size() >= 3) {
                            // Adding the original point
                            currentPoints.add(pointsMemory[0]);

                            // Searching for the min and max points on the line
                            Point maxPoint = getMaxPoint(currentPoints);
                            Point minPoint = getMinPoint(currentPoints);

                            if (!hasDuplicates(maxPoints, minPoints, maxPoint, minPoint)) {
                                // Adding to the global max and min point list
                                maxPoints.add(maxPoint);
                                minPoints.add(minPoint);

                                linesTemp.add(new LineSegment(minPoint, maxPoint));
                                nLines++;
                            }
                        }
                        j = h + 1;
                    } else {
                        curSlope = pointsMemory[0].slopeTo(pointsMemory[j + 1]);
                        j++;
                    }
                }
            }
        }
        // Creating an array out of the line segments found
        lines = linesTemp.toArray(new LineSegment[0]);
    }

    // Method that returns the line segments
    public LineSegment[] segments() {
        return lines;
    }

    private boolean hasDuplicates(ArrayList<Point> maxPoints, ArrayList<Point> minPoints, Point max, Point min) {
        for (int i = 0; i < maxPoints.size(); i++) {
            if (max.compareTo(maxPoints.get(i)) == 0 && min.compareTo(minPoints.get(i)) == 0) return true;
        }
        return false;
    }

    private Point getMaxPoint(ArrayList<Point> points) {
        Point maxTemp = points.get(0);
        for (int j = 1; j < points.size(); j++) {
            if (points.get(j).compareTo(maxTemp) > 0) {
                maxTemp = points.get(j);
            }
        }
        return maxTemp;
    }

    private Point getMinPoint(ArrayList<Point> points) {
        Point minTemp = points.get(0);
        for (int j = 1; j < points.size(); j++) {
            if (points.get(j).compareTo(minTemp) < 0) {
                minTemp = points.get(j);
            }
        }
        return minTemp;
    }

    public int numberOfSegments() {
        return nLines;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // Printing out the points
        for (int i = 0; i < n; i++) {
            Point p = points[i];
            System.out.println(p.toString());
        }

        // Finding the line segments
        FastCollinearPoints collinearPoints = new FastCollinearPoints(points);
        LineSegment[] lines = collinearPoints.segments();

        // Printing out the line segments
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i].toString());
        }

        // Printing out the total number of line segments found
        System.out.println("Total found: " + collinearPoints.numberOfSegments());

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        for (LineSegment segment : lines) {
            segment.draw();
        }
        StdDraw.show();
    }

}
