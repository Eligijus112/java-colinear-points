import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class BruteCollinearPoints {
    // Total number of line segments
    private int nLines = 0;

    // Creating a placeholder for points in memory
    private Point[] pointsMemory;

    // Total number of points
    private int nPoints;

    // Method to extract the max point out of 4 points
    private Point getMaxPoint(Point a, Point b, Point c, Point d) {
        Point max = a;
        if (max.compareTo(b) < 0) {
            max = b;
        }
        if (max.compareTo(c) < 0) {
            max = c;
        }
        if (max.compareTo(d) < 0) {
            max = d;
        }
        return max;
    }

    // Method to extract the min point out of 4 points
    private Point getMinPoint(Point a, Point b, Point c, Point d) {
        Point min = a;
        if (min.compareTo(b) > 0) {
            min = b;
        }
        if (min.compareTo(c) > 0) {
            min = c;
        }
        if (min.compareTo(d) > 0) {
            min = d;
        }
        return min;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        // Creating a temporary array list object to add the line segments in
        ArrayList<LineSegment> linesTemp = new ArrayList<LineSegment>();

        // Creating arrays for max and min points to avoid duplicates
        ArrayList<Point> maxPoints = new ArrayList<>();
        ArrayList<Point> minPoints = new ArrayList<>();

        // Iterating in a brutal way to find the collinear points
        for (int i = 0; i < nPoints; i++) {
            for (int j = i + 1; j < nPoints; j++) {
                // Calculating the slope between the first two points
                double k1 = pointsMemory[i].slopeTo(pointsMemory[j]);
                for (int h = j + 1; h < nPoints; h++) {
                    // Calculating the slope between the second two points
                    double k2 = pointsMemory[j].slopeTo(pointsMemory[h]);
                    // Going further only when k1 = k2
                    if (Double.compare(k1, k2) == 0) {
                        for (int k = h + 1; k < nPoints; k++) {
                            // Calculating the slope between the last 2 points
                            double k3 = pointsMemory[h].slopeTo(pointsMemory[k]);

                            // If these two slopes are equal then the 4 points are on the same line
                            if (Double.compare(k2, k3) == 0) {
                                // Extracting the points
                                Point p1 = pointsMemory[i];
                                Point p2 = pointsMemory[j];
                                Point p3 = pointsMemory[h];
                                Point p4 = pointsMemory[k];

                                // Getting the maximum and the minimum point
                                Point minPoint = getMinPoint(p1, p2, p3, p4);
                                Point maxPoint = getMaxPoint(p1, p2, p3, p4);

                                // Checking if the min max combination is not already defined
                                boolean isNew = true;
                                for (int z = 0; z < maxPoints.size(); z++) {
                                    if ((maxPoints.get(z).compareTo(maxPoint) == 0) && (minPoints.get(z).compareTo(minPoint) == 0)) {
                                        isNew = false;
                                        break;
                                    }
                                }

                                if (isNew) {
                                    // Adding to the global max and min point list
                                    maxPoints.add(maxPoint);
                                    minPoints.add(minPoint);

                                    linesTemp.add(new LineSegment(minPoint, maxPoint));
                                    nLines++;
                                }
                            }
                        }
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

    // the number of line segments
    public int numberOfSegments() {
        return nLines;
    }

    // Unit testing
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
        BruteCollinearPoints collinearPoints = new BruteCollinearPoints(points);
        LineSegment[] lines = collinearPoints.segments();

        // Printing out the line segments
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i].toString());
        }

    }
}

