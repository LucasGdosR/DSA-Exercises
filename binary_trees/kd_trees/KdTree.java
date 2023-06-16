package binary_trees.kd_trees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Write a data type to represent a set of points in the unit square (all points have x- and y-coordinates between 0
 * and 1) using a 2d-tree to support efficient range search (find all of the points contained in a query rectangle)
 * and nearest-neighbor search (find a closest point to a query point). 2d-trees have numerous applications, ranging
 * from classifying astronomical objects to computer animation to speeding up neural networks to mining data to image
 * retrieval.
 *
 * 2d-tree implementation. Write a mutable data type KdTree.java that uses a 2d-tree to implement the same API (but
 * replace PointSET with KdTree). A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to build a
 * BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.
 *
 * Search and insert. The algorithms for search and insert are similar to those for BSTs, but at the root we use the
 * x-coordinate (if the point to be inserted has a smaller x-coordinate than the point at the root, go left; otherwise
 * go right); then at the next level, we use the y-coordinate (if the point to be inserted has a smaller y-coordinate
 * than the point in the node, go left; otherwise go right); then at the next level the x-coordinate, and so forth.
 *
 * Draw. A 2d-tree divides the unit square in a simple way: all the points to the left of the root go in the left
 * subtree; all those to the right go in the right subtree; and so forth, recursively. Your draw() method should draw
 * all the points to standard draw in black and the subdivisions in red (for vertical splits) and blue (for horizontal
 * splits). This method need not be efficient—it is primarily for debugging.
 *
 * The prime advantage of a 2d-tree over a BST is that it supports efficient implementation of range search and
 * nearest-neighbor search. Each node corresponds to an axis-aligned rectangle in the unit square, which encloses all
 * the points in its subtree. The root corresponds to the unit square; the left and right children of the root
 * corresponds to the two rectangles split by the x-coordinate of the point at the root; and so forth.
 *
 * Range search. To find all points contained in a given query rectangle, start at the root and recursively search for
 * points in both subtrees using the following pruning rule: if the query rectangle does not intersect the rectangle
 * corresponding to a node, there is no need to explore that node (or its subtrees). A subtree is searched only if it
 * might contain a point contained in the query rectangle.
 *
 * Nearest-neighbor search. To find a closest point to a given query point, start at the root and recursively search in
 * both subtrees using the following pruning rule: if the closest point discovered so far is closer than the distance
 * between the query point and the rectangle corresponding to a node, there is no need to explore that node (or its
 * subtrees). That is, search a node only only if it might contain a point that is closer than the best one found so
 * far. The effectiveness of the pruning rule depends on quickly finding a nearby point. To do this, organize the
 * recursive method so that when there are two possible subtrees to go down, you always choose the subtree that is on
 * the same side of the splitting line as the query point as the first subtree to explore—the closest point found while
 * exploring the first subtree may enable pruning of the second subtree.
 */

public class KdTree {
    private static final double MIN_X = 0d;
    private static final double MAX_X = 1d;
    private static final double MIN_Y = 0d;
    private static final double MAX_Y = 1d;

    private Node root;
    private int size;

    private class Node {
        private Point2D key;
        private Node left;
        private Node right;

        private Node(Point2D p) {
            key = p;
        }
    }

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void validateArgument(Object p) {
        if (p == null)
            throw new IllegalArgumentException();
    }

    public void insert(Point2D p) {
        validateArgument(p);

        if (isEmpty()) {
            root = new Node(p);
            size++;
            return;
        }

        if (contains(p))
            return;

        Node node = root;
        Node parent;
        boolean compareByX = true;
        int compare;

        do {
                compare = compareByX ? Point2D.X_ORDER.compare(p, node.key) : Point2D.Y_ORDER.compare(p, node.key);
                compareByX = !compareByX;
                parent = node;
                node = compare > 0 ? node.right : node.left;
        } while (node != null);

        if (compare > 0)
            parent.right = new Node(p);
        else
            parent.left = new Node(p);

        size++;
    }

    public boolean contains(Point2D p) {
        validateArgument(p);

        if (isEmpty())
            return false;

        Node node = root;
        boolean compareByX = true;

        do {
            if (p.equals(node.key))
                return true;

            int compare = compareByX ? Point2D.X_ORDER.compare(p, node.key) : Point2D.Y_ORDER.compare(p, node.key);

            compareByX = !compareByX;

            node = compare > 0 ? node.right : node.left;
        } while (node != null);

        return false;
    }

    public void draw() {
       StdDraw.setPenColor(StdDraw.BLACK);
       // Lower border.
       StdDraw.line(MIN_X, MIN_Y, MAX_X, MIN_Y);
       // Upper border.
       StdDraw.line(MIN_X, MAX_Y, MAX_X, MAX_Y);
       // Left border.
       StdDraw.line(MIN_X, MIN_Y, MIN_X, MAX_Y);
       // Right border.
       StdDraw.line(MAX_X, MIN_Y, MAX_X, MAX_Y);

       draw(root, true, MIN_X, MAX_X, MIN_Y, MAX_Y);
    }

    private void draw(Node node, boolean isVerticalSplit, double minX, double maxX, double minY, double maxY) {
        if (node == null)
            return;

        StdDraw.setPenColor(StdDraw.BLACK);
        node.key.draw();

        StdDraw.setPenColor(isVerticalSplit ? StdDraw.RED : StdDraw.BLUE);

        if (isVerticalSplit) {
            StdDraw.line(node.key.x(), minY, node.key.x(), maxY);

            draw(node.left, false, minX, node.key.x(), minY, maxY);
            draw(node.right, false, node.key.x(), maxX, minY, maxY);
        } else {
            StdDraw.line(minX, node.key.y(), maxX, node.key.y());

            draw(node.left, true, minX, maxX, minY, node.key.y());
            draw(node.right, true, minX, maxX, node.key.y(), maxY);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        validateArgument(rect);
        List<Point2D> iterable = new ArrayList<>();
        range(root, true, iterable, rect);
        return iterable;
    }

    private void range(Node node, boolean compareByX, List<Point2D> iterable, RectHV rect) {
        if (node == null)
            return;

        boolean greaterThanMin = compareByX ? (rect.xmin() <= node.key.x()) : (rect.ymin() <= node.key.y());
        boolean smallerThanMax = compareByX ? (rect.xmax() >= node.key.x()) : (rect.ymax() >= node.key.y());

        if (greaterThanMin && smallerThanMax)
            if (rect.contains(node.key))
                iterable.add(node.key);

        if (greaterThanMin)
            range(node.left, !compareByX, iterable, rect);

        if (smallerThanMax)
            range(node.right, !compareByX, iterable, rect);
    }

    public Point2D nearest(Point2D p) {
        validateArgument(p);
        return nearest(root, true, Double.POSITIVE_INFINITY, null, p);
    }

    private Point2D nearest(Node node, boolean compareByX, double minDistance, Point2D champion, Point2D p) {
        if (node == null)
            return champion;

        double distance = p.distanceSquaredTo(node.key);

        if (distance < minDistance) {
            minDistance = distance;
            champion = node.key;
        }

        int compare = compareByX ? Point2D.X_ORDER.compare(p, node.key) : Point2D.Y_ORDER.compare(p, node.key);

        Point2D challenger;
        if (compare <= 0)
            challenger = nearest(node.left, !compareByX, minDistance, champion, p);
        else
            challenger = nearest(node.right, !compareByX, minDistance, champion, p);

        distance = p.distanceSquaredTo(challenger);
        if (distance < minDistance) {
            minDistance = distance;
            champion = challenger;
        }

        double minDistanceToOtherSide = compareByX
                ? p.distanceSquaredTo(new Point2D(node.key.x(), p.y()))
                : p.distanceSquaredTo(new Point2D(p.x(), node.key.y()));

        if (minDistanceToOtherSide < minDistance) {
            if (compare <= 0)
                challenger = nearest(node.right, !compareByX, minDistance, champion, p);
            else
                challenger = nearest(node.left, !compareByX, minDistance, champion, p);
            distance = p.distanceSquaredTo(challenger);
            if (distance < minDistance) {
                champion = challenger;
            }
        }

        return champion;
    }
}
