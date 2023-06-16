package heaps.eight_puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node solution;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<>();
        Node current = new Node(initial, 0, null);
        pq.insert(new Node(initial.twin(), 0, null));

        while (!current.board.isGoal()) {
            for (Board neighbor : current.board.neighbors())
                if (current.previousNode == null || !neighbor.equals(current.previousNode.board))
                    pq.insert(new Node(neighbor, current.moves + 1, current));
            current = pq.delMin();
        }

        solution = current;

        while (current.previousNode != null)
            current = current.previousNode;

        if (!current.board.equals(initial))
            solution = null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution == null ? -1 : solution.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solution == null) return null;

        Stack<Board> iterable = new Stack<>();
        Node current = solution;
        iterable.push(current.board);

        while (current.previousNode != null) {
            current = current.previousNode;
            iterable.push(current.board);
        }

        return iterable;
    }

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node previousNode;
        // private int hammingPriority;
        private final int manhattanPriority;

        Node(Board newBoard, int moves, Node previousNode) {
            board = newBoard;
            this.moves = moves;
            this.previousNode = previousNode;
            // hammingPriority = newBoard.hamming() + moves;
            manhattanPriority = newBoard.manhattan() + moves;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.manhattanPriority, that.manhattanPriority);
        }
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
