package stacks_queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Randomized queue. A randomized queue is similar to a stack or queue, except that the item removed is chosen
 * uniformly at random among items in the data structure. Create a generic data type RandomizedQueue that implements
 * the following API: (omitted)
 *
 * Iterator. Each iterator must return the items in uniformly random order. The order of two or more iterators to the
 * same randomized queue must be mutually independent; each iterator must maintain its own random order.
 *
 * Performance requirements. Your randomized queue implementation must support each randomized queue operation (besides
 * creating an iterator) in constant amortized time. That is, any intermixed sequence of m randomized queue operations
 * (starting from an empty queue) must take at most cm steps in the worst case, for some constant c. A randomized queue
 * containing n items must use at most 48n + 192 bytes of memory. Additionally, your iterator implementation must
 * support operations next() and hasNext() in constant worst-case time; and construction in linear time; you may (and
 * will need to) use a linear amount of extra memory per iterator.
 */

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size;

    public RandomizedQueue() {
        queue = (Item[]) new Object[8];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (size == queue.length && size > 0)
            resize();

        queue[size++] = item;
    }

    public Item dequeue() {
        verifyIsEmpty();

        int random = StdRandom.uniformInt(0, size);
        Item item = queue[random];
        queue[random] = queue[--size];
        queue[size] = null;

        if (4 * size < queue.length && size > 0)
            resize();

        return item;
    }

    public Item sample() {
        verifyIsEmpty();
        return queue[StdRandom.uniformInt(0, size)];
    }

    private void verifyIsEmpty() {
        if (isEmpty())
            throw new NoSuchElementException();
    }

    private void resize() {
        Item[] newQueue = (Item[]) new Object[2 * size];
        System.arraycopy(queue, 0, newQueue, 0, size);
        queue = newQueue;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        RandomizedQueue<Item> queue;

        public RandomizedQueueIterator() {
            this.queue = new RandomizedQueue<>();
            this.queue.queue = (Item[]) new Object[size];
            System.arraycopy(RandomizedQueue.this.queue,0, this.queue.queue, 0, size);
            this.queue.size = RandomizedQueue.this.size;
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return this.queue.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        for (int i = 0; i < 10; i++)
            queue.enqueue(i);

        System.out.println("Size: " + queue.size());

        for (int i = 0; i < 10; i++)
            System.out.println("Sample: " + queue.sample());

        System.out.println("Is empty: " + queue.isEmpty());

        Iterator<Integer> iterator = queue.iterator();

        while (iterator.hasNext())
            System.out.println("First iterator: " + iterator.next());

        iterator = queue.iterator();

        while (iterator.hasNext())
            System.out.println("Second iterator: " + iterator.next());

        while (!queue.isEmpty())
            System.out.println("stacks_queues.Deque: " + queue.dequeue());

        System.out.println("Is Empty: " + queue.isEmpty());

        System.out.println("Size: " + queue.size());
    }
}
