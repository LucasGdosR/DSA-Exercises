package stacks_queues;

import edu.princeton.cs.algs4.StdIn;

/**
 Client. Write a client program Permutation.java that takes an integer k as a command-line argument; reads a sequence
 of strings from standard input using StdIn.readString(); and prints exactly k of them, uniformly at random. Print
 each item from the sequence at most once.

 Command-line argument.  You may assume that 0 ≤ k ≤ n, where n is the number of string on standard input. Note that
 you are not given n.

 Performance requirements.  The running time of Permutation must be linear in the size of the input. You may use only a
 constant amount of memory plus either one Deque or RandomizedQueue object of maximum size at most n. (For an extra
 challenge and a small amount of extra credit, use only one Deque or RandomizedQueue object of maximum size at most k.)
 */

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty())
            queue.enqueue(StdIn.readString());

        for (int i = 0; i < k; i++)
            System.out.println(queue.dequeue());
    }
}
