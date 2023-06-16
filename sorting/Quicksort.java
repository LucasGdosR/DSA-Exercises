package sorting;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Quicksort {
    public static void main(String[] args) {
        int [] arr = {5, 6, 4, 3, 7, 2, 1, 8, 0, 57, -3, 45};
        quicksort(arr, 0, arr.length - 1);
        Arrays.stream(arr).forEach(System.out::println);
    }

    public static void quicksort(int[] arr, int start, int end) {
        if (start >= end) return;

        int pivot = choosePivot(arr, start, end);

        int partitionIndex = partition(arr, start, end, pivot);

        quicksort(arr, start, partitionIndex - 1);
        quicksort(arr, partitionIndex + 1, end);
    }

    public static int choosePivot(int[] arr, int start, int end) {
        // return start;
        // return (start + end) / 2;
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }

    public static int partition(int[] arr, int start, int end, int pivot) {
        swap(arr, pivot, start);
        int partitionIndex = start;

        for (int i = start + 1; i <= end; i++)
            if (arr[i] < arr[start])
                swap(arr, ++partitionIndex, i);

        swap(arr, start, partitionIndex);

        return partitionIndex;
    }

    public static void swap (int[] arr, int i, int j) {
        int buffer = arr[i];
        arr[i] = arr[j];
        arr[j] = buffer;
    }
}