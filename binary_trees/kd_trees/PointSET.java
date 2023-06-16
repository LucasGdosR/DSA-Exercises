package binary_trees.kd_trees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Brute-force implementation. Write a mutable data type PointSET.java that represents a set of points in the unit
 * square.
 *
 * Implementation requirements. You must use either SET or java.util.TreeSet; do not implement your own red–black BST.
 *
 * Corner cases. Throw an IllegalArgumentException if any argument is null. Performance requirements.  Your
 * implementation should support insert() and contains() in time proportional to the logarithm of the number of points
 * in the set in the worst case; it should support nearest() and range() in time proportional to the number of points
 * in the set.
 */
public class PointSET {
    private final Set<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    private void validateArgument(Object p) {
        if (p == null)
            throw new IllegalArgumentException();
    }

    public void insert(Point2D p) {
        validateArgument(p);
        set.add(p);
    }

    public boolean contains(Point2D p) {
        validateArgument(p);
        return set.contains(p);
    }

    public void draw() {
        set.forEach(Point2D::draw);
    }

    public Iterable<Point2D> range(RectHV rect) {
        validateArgument(rect);

        List<Point2D> iterable = new ArrayList<>();

        for (Point2D point : set) {
            if (rect.contains(point))
                iterable.add(point);
        }

        return iterable;
    }

    public Point2D nearest(Point2D p) {
        validateArgument(p);

        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : set) {
            double distance = point.distanceSquaredTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }

        return nearest;
    }
}