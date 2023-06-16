package stacks_queues;

import java.util.NoSuchElementException;

public class Stack<E> {
    private E[] stack;
    private int capacity;
    private int current;

    public Stack() {
        this(8);
    }

    public Stack(int capacity) {
        this.capacity = capacity;
        stack = (E[]) new Object[capacity];
        current = 0;
    }

    public void push(E e) {
        if (current == capacity)
            resize(2 * capacity);

        stack[current++] = e;
    }

    public E pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack undeflow");

        E e = stack[--current];
        stack[current] = null;

        if (capacity >= current * 4)
            resize(current * 2);

        return e;
    }

    public boolean isEmpty() {
        return current == 0;
    }

    public int size() {
        return current;
    }

    private void resize(int newSize) {
        E[] newStack = (E[]) new Object[newSize];

        for (int i = 0; i < current; i++)
            newStack[i] = stack[i];

        capacity = newSize;
        stack = newStack;
    }
}
