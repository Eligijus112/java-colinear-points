import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    // Number of line segments
    private int nLines = 0;

    // Creating a placeholder for points in memory
    private final Point[] pointsMemory;

    // The number of points
    private final int nPoints;

    // The fast collinear point search
    public FastCollinearPoints(Point[] points) {
        // Checking for proper input
        if (points == null) throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }

        // Checking for duplicates
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // Saving the total number of points
        nPoints = points.length;

        // Saving the points to memory from the constructor call
        pointsMemory = new Point[nPoints];
        for (int i = 0; i < nPoints; i++) {
            pointsMemory[i] = points[i];
        }
    }

    // the line segments
    public LineSegment[] segments() {
        // Copying the original array of points
        Point[] pointsMemoryOrig = Arrays.copyOf(pointsMemory, nPoints);

        // Creating a temporary array list object to add the line segments in
        ArrayList<LineSegment> linesTemp = new ArrayList<>();

        // Creating arrays for max and min points to avoid duplicates
        ArrayList<Point> maxPoints = new ArrayList<>();
        ArrayList<Point> minPoints = new ArrayList<>();

        // Iterating through all the points
        for (int i = 0; i < nPoints; i++) {
            // Sorting the arrays based on the point i and the slope it makes with other points
            Arrays.sort(pointsMemory, pointsMemoryOrig[i].slopeOrder());

            for (int j = 1; j < nPoints; j++) {
                // Placeholder for collinear points
                ArrayList<Point> collinearPoints = new ArrayList<>();

                // Adding the base point and the jth point to the array list
                collinearPoints.add(pointsMemory[0]);
                collinearPoints.add(pointsMemory[j]);

                // Slope from the base point to the jth point
                double slopeToBasePoint = pointsMemory[0].slopeTo(pointsMemory[j]);

                // Iterating through the rest of the points and checking for slopes being equal
                while ((j + 1 < nPoints) && slopeToBasePoint == pointsMemory[0].slopeTo(pointsMemory[j + 1])) {
                    collinearPoints.add(pointsMemory[++j]);
                    slopeToBasePoint = pointsMemory[0].slopeTo(pointsMemory[j]);
                }

                if (collinearPoints.size() >= 4) {
                    Point maxPoint = getMaxPoint(collinearPoints);
                    Point minPoint = getMinPoint(collinearPoints);

                    // Checking if the min max combination is not already defined
                    if (!hasDuplicates(maxPoints, minPoints, maxPoint, minPoint)) {
                        // Adding to the global max and min point list
                        maxPoints.add(maxPoint);
                        minPoints.add(minPoint);

                        linesTemp.add(new LineSegment(minPoint, maxPoint));
                        nLines++;
                    }
                }
            }
        }

        // Filling the lines object with the line segments
        LineSegment[] lines = new LineSegment[nLines];
        if (nLines > 0) {
            for (int i = 0; i < nLines; i++) {
                lines[i] = linesTemp.get(i);
            }
        }

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
        System.out.println("Total found: " + collinearPoints.nLines);

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
