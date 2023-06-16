package sorting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
Intersection of two sets. Given two arrays a[] and b[], each containing n distinct 2D points in the plane, design a
subquadratic algorithm to count the number of points that are contained both in array a[] and array b[].
 */
public class IntersectionOfPoints {
    private record Point(int x, int y) {}

    private static class SortByXBreakByY implements Comparator<Point> {

        @Override
        public int compare(Point a, Point b) {
            if (a.x < b.x) return -1;
            if (a.x > b.x) return 1;
            if (a.y < b.y) return -1;
            if (a.y > b.y) return 1;
            return 0;
        }
    }

    private static List<Point> getRandomPointArray(int numberOfPoints) {
        Point[] arr = new Point[numberOfPoints];

        for (int i = 0; i < numberOfPoints; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, 10);
            int y = ThreadLocalRandom.current().nextInt(0, 10);
            arr[i] = new Point(x, y);
        }

        return Arrays.stream(arr).distinct().collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Point> a = getRandomPointArray(100);
        List<Point> b = getRandomPointArray(100);
        
        Comparator<Point> comparator = new SortByXBreakByY();
        a.sort(comparator);
        b.sort(comparator);

        int i = 0;
        int j = 0;
        int duplicates = 0;

        while (i < a.size() && j < b.size()) {
            if (a.get(i).x() < b.get(j).x()) i++;
            else if (a.get(i).x() > b.get(j).x()) j++;

            else if (a.get(i).y() < b.get(j).y()) i++;
            else if (a.get(i).y() > b.get(j).y()) j++;

            else {
                System.out.println("Duplicate: " + a.get(i).x() + ", " + a.get(i).y() + " and " + b.get(j).x() + ", " + b.get(j).y());
                duplicates++;
                i++;
                j++;
            }
        }
        System.out.println(duplicates);
    }
}
