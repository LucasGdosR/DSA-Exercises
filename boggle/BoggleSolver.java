import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    private static final int R = 26; // Uppercase letters only
    private Node dictionaryRoot;

    private static class Point {
        private final int i;
        private final int j;
        private Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
    private static class Node {
        private final Node[] next = new Node[R];
        private boolean isString;
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary)
            dictionaryRoot = add(dictionaryRoot, s, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) x.isString = true;
        else {
            int i = key.charAt(d) - 'A';
            x.next[i] = add(x.next[i], key, d + 1);
        }
        return x;
    }

    private boolean contains(String key) {
        Node x = get(dictionaryRoot, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 'A'], key, d + 1);
    }

    private boolean isValidPrefix(String prefix) {
        Node x = dictionaryRoot;
        for (int i = 0; i < prefix.length(); i++) {
            int c = prefix.charAt(i) - 'A';
            if (x.next[c] == null) return false;
            x = x.next[c];
        }
        return true;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> set = new HashSet<>();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        ArrayList<Point>[][] adj = getAdj(board);

        // Iterate through every row.
        for (int i = 0; i < board.rows(); i++)
            // Iterate through every character.
            for (int j = 0; j < board.cols(); j++)
                dfs(board, set, visited, adj, i, j, getLetterFixingQ(board, i, j));

        return set;
    }

    private ArrayList<Point>[][] getAdj(BoggleBoard board) {
        ArrayList<Point>[][] adjacencyLists = new ArrayList[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++) {
                ArrayList<Point> adjacentToCurrent = new ArrayList<>(8);
                for (int k = Math.max(i - 1, 0); k < Math.min(i + 2, board.rows()); k++)
                    for (int m = Math.max(j - 1, 0); m < Math.min(j + 2, board.cols()); m++)
                        if (k != i || m != j) adjacentToCurrent.add(new Point(k, m));
                adjacencyLists[i][j] = adjacentToCurrent;
            }

        return adjacencyLists;
    }

    private void dfs(BoggleBoard board, HashSet<String> set, boolean[][] visited, ArrayList<Point>[][] adj, int i, int j, String s) {
        if (!isValidPrefix(s)) return;

        if (contains(s) && s.length() > 2) set.add(s);
        visited[i][j] = true;

        for (Point a : adj[i][j])
            if (!visited[a.i][a.j]) dfs(board, set, visited, adj, a.i, a.j, s + getLetterFixingQ(board, a.i, a.j));

        visited[i][j] = false;
    }

    private String getLetterFixingQ(BoggleBoard board, int i, int j) {
        return board.getLetter(i, j) == 'Q' ? "QU" : String.valueOf(board.getLetter(i, j));
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word))
            switch (word.length()) {
                case 0:
                case 1:
                case 2: return 0;
                case 3:
                case 4: return 1;
                case 5: return 2;
                case 6: return 3;
                case 7: return 5;
                default: return 11;
            }
        return 0;
    }
}
