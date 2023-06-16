package sorting;

import java.util.concurrent.ThreadLocalRandom;

/**
Dutch national flag. Given an array of n buckets, each containing a red, white, or blue pebble, sort them by color.
The allowed operations are:

swap(i,j):  swap the pebble in bucket i with the pebble in bucket j.
color(i): determine the color of the pebble in bucket i.

The performance requirements are as follows:
At most n calls to color().

At most n calls to swap().

Constant extra space.
 */
public class DutchNationalFlag {
    enum PebbleColor {
        RED, WHITE, BLUE
    }

    private static PebbleColor[] arr;

    private static void swap(int i, int j) {
        PebbleColor temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static PebbleColor color(int i) {
        return arr[i];
    }

    private static PebbleColor[] getRandomPebbleArray(int capacity) {
        PebbleColor[] arr = new PebbleColor[capacity];

        for (int i = 0; i < arr.length; i++)
            arr[i] = PebbleColor.values()[ThreadLocalRandom.current().nextInt(0,3)];

        return arr;
    }

    // If a pebble is red, it should be in the beginning. If a pebble is blue, it should be in the end. For this,
    // there is a pointer to the red frontier, and another to the blue frontier. Every time a red pebble is found,
    // it should be swapped with the one on the red frontier, and the pointer should be incremented. Every time a blue
    // pebble is found, it should be swapped with the one on the blue frontier, and the pointer is decremented. color()
    // is called once for every pebble, and swap() is called once for every red or blue pebble.

    public static void main(String[] args) {
        arr = getRandomPebbleArray(20);

        int redFrontier = 0;
        int blueFrontier = arr.length;

        for (int i = 0; i < blueFrontier;) {
            PebbleColor currentColor = color(i);

            switch (currentColor) {
                case RED -> swap(i++, redFrontier++);
                case BLUE -> swap(i, --blueFrontier);
                case WHITE -> i++;
            }
        }

        for (PebbleColor pebbleColor : arr) {
            System.out.println(pebbleColor.name());
        }
    }
}
