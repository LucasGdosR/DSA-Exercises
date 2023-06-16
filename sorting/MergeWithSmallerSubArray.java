package sorting;

import java.util.ArrayList;
import java.util.List;

/*
Merging with smaller auxiliary array. Suppose that the subarray a[0] to a[n−1] is sorted
and the subarray a[n] to a[2∗n−1] is sorted. How can you merge the two subarrays so that a[0] to a[2∗n−1] is sorted
using an auxiliary array of length n (instead of 2n)?
 */
public class MergeWithSmallerSubArray {
    public static <E extends Comparable> void merge(List<E> a) {
        int n = a.size() / 2;
        List<E> subArr = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
            subArr.add(a.get(i));

        for (int i = 0, j = n, k = 0; i < n; k++) {
            if (subArr.get(i).compareTo(a.get(j)) <= 0)
                a.set(k, subArr.get(i++));
            else a.set(k, a.get(j++));
        }
    }

    public static void main(String[] args) {
        List<Integer> arr = new ArrayList(List.of(-5, 1, 2, 6, 7, 10,
                                                         -26, -3, 3, 4, 50, 456));
        merge(arr);
        for (Integer integer : arr) {
            System.out.println(integer);
        }
    }
}
