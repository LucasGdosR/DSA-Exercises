package stacks_queues;


/**
 * Implement a queue with two stacks so that each queue operations takes a constant amortized number of stack operations.
 */
public class QueueWithTwoStacks<E> {
    Stack<E> inbox;
    Stack<E> outbox;

    public QueueWithTwoStacks() {
        inbox = new Stack<>();
        outbox = new Stack<>();
    }

    public void enqueue(E e) {
        inbox.push(e);
    }

    public E dequeue() {
        if (outbox.isEmpty())
            while (!inbox.isEmpty())
                outbox.push(inbox.pop());
        return outbox.pop();
    }

    public boolean isEmpty() {
        return inbox.isEmpty() && outbox.isEmpty();
    }
}
