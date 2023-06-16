package stacks_queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Dequeue. A double-ended queue or deque (pronounced “deck”) is a generalization of a stack and a queue that supports
 * adding and removing items from either the front or the back of the data structure. Create a generic data type Deque
 * that implements the following API: (omitted)
 *
 * Performance requirements. Your deque implementation must support each deque operation (including construction) in
 * constant worst-case time. A deque containing n items must use at most 48n + 192 bytes of memory. Additionally, your
 * iterator implementation must support each operation (including construction) in constant worst-case time.
 */
public class Deque<Item> implements Iterable<Item> {
    private Item[] deque;
    private int head, tail;

    public Deque() {
        deque = (Item[]) new Object[8];
        head = tail = 4;
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public int size() {
        return tail - head;
    }

    public void addFirst(Item item) {
        verifyArgs(item);

        if (head == 0)
            resize();

        deque[--head] = item;
    }

    public void addLast(Item item) {
        verifyArgs(item);

        if (tail == deque.length)
            resize();

        deque[tail++] = item;
    }

    public Item removeFirst() {
        verifyIsEmpty();

        Item item = deque[head];
        deque[head++] = null;

        if (4 * size() < deque.length && size() > 0)
            resize();

        return item;
    }

    public Item removeLast() {
        verifyIsEmpty();

        Item item = deque[--tail];
        deque[tail] = null;

        if (4 * size() < deque.length && size() > 0)
            resize();

        return item;
    }

    private void verifyArgs(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
    }

    private void verifyIsEmpty() {
        if (isEmpty())
            throw new NoSuchElementException();
    }

    private void resize() {
        int capacity = 2 * size();
        if (capacity < 4) capacity = 4;
        Item[] newDeque = (Item[]) new Object[capacity];
        int newHead = capacity / 4;
        int newTail = newHead + size();

        System.arraycopy(deque, head, newDeque, newHead, size());

        head = newHead;
        tail = newTail;
        deque = newDeque;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private int i = head;

        @Override
        public boolean hasNext() {
            return i < tail;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return deque[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3);
        deque.addLast(4);

        Iterator<Integer> iterator = deque.iterator();

        System.out.println("Iterator:");
        while (iterator.hasNext())
            System.out.printf("%d ", iterator.next()); // 2 1 3 4

        System.out.printf("\n\nRemove First:\n%d\n", deque.removeFirst()); // 2

        System.out.printf("\nSize:\n%d\n\n", deque.size()); // 3

        System.out.println("Is Empty + Remove Last:");
        while (!deque.isEmpty())
            System.out.printf("%d ", deque.removeLast()); // 4 3 1
    }
}
