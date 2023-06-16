package heaps.eight_puzzle;

import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int[][] tiles;

    /**
     * Constructor. You may assume that the constructor receives an n-by-n array containing the n2 integers between 0
     * and n2 − 1, where 0 represents the blank square. You may also assume that 2 ≤ n < 128.
     */
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = cloneTiles(tiles);
    }

    private int[][] cloneTiles(int[][] clonedTiles) {
        int[][] newTiles = new int[clonedTiles.length][clonedTiles.length];

        for (int i = 0; i < clonedTiles.length; i++)
            System.arraycopy(clonedTiles[i], 0, newTiles[i], 0, clonedTiles.length);

        return newTiles;
    }

    /**
     * String representation. The toString() method returns a string composed of n + 1 lines. The first line contains
     * the board size n; the remaining n lines contains the n-by-n grid of tiles in row-major order, using 0 to
     * designate the blank square.
     */
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder(tiles.length * tiles.length * 2 + tiles.length + 2);

        s.append(tiles.length + "\n");

        for (int[] row : tiles) {
            for (int tile : row)
                s.append(String.format("%2d ", tile));
            s.append("\n");
        }

        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    /**
     * Hamming and Manhattan distances. To measure how close a board is to the goal board, we define two notions of
     * distance. The Hamming distance between a board and the goal board is the number of tiles in the wrong position.
     * The Manhattan distance between a board and the goal board is the sum of the Manhattan distances (sum of the
     * vertical and horizontal distance) from the tiles to their goal positions.
     */
    // number of tiles out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (tiles[i][j] != 0 && tiles[i][j] != (tiles.length * i + j + 1))
                    hamming++;

        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++) {
                int tile = tiles[i][j];
                if (tile != 0 && tile != (tiles.length * i + j + 1))
                    manhattan += getManhattanDistance(tile, i, j);
            }

        return manhattan;
    }

    private int getManhattanDistance(int tile, int i, int j) {
        int correctRow = (tile - 1) / tiles.length;
        int correctColumn = (tile - 1) % tiles.length;

        return Math.abs(i - correctRow) + Math.abs(j - correctColumn);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * Comparing two boards for equality. Two boards are equal if they are the same size and their corresponding
     * tiles are in the same positions. The equals() method is inherited from java.lang.Object, so it must obey all of
     * Java’s requirements.
     */
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != tiles.length) return false;

        return this.toString().equals(that.toString());
    }

    /**
     * Neighboring boards. The neighbors() method returns an iterable containing the neighbors of the board. Depending
     * on the location of the blank square, a board can have 2, 3, or 4 neighbors.
     */
    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] zeroIndex = findZeroIndex();
        Stack<Board> boardIterable = new Stack<>();

        // Add top neighbor
        if (zeroIndex[0] != 0) {
            int[][] tilesClone = cloneTiles(tiles);
            tilesClone [zeroIndex[0]][zeroIndex[1]] = tilesClone[zeroIndex[0] - 1][zeroIndex[1]];
            tilesClone[zeroIndex[0] - 1][zeroIndex[1]] = 0;
            boardIterable.push(new Board(tilesClone));
        }

        // Add bottom neighbor
        if (zeroIndex[0] != tiles.length - 1) {
            int[][] tilesClone = cloneTiles(tiles);
            tilesClone [zeroIndex[0]][zeroIndex[1]] = tilesClone[zeroIndex[0] + 1][zeroIndex[1]];
            tilesClone[zeroIndex[0] + 1][zeroIndex[1]] = 0;
            boardIterable.push(new Board(tilesClone));
        }

        // Add left neighbor
        if (zeroIndex[1] != 0) {
            int[][] tilesClone = cloneTiles(tiles);
            tilesClone [zeroIndex[0]][zeroIndex[1]] = tilesClone[zeroIndex[0]][zeroIndex[1] - 1];
            tilesClone[zeroIndex[0]][zeroIndex[1] - 1] = 0;
            boardIterable.push(new Board(tilesClone));
        }

        // Add right neighbor
        if (zeroIndex[1] != tiles.length - 1) {
            int[][] tilesClone = cloneTiles(tiles);
            tilesClone [zeroIndex[0]][zeroIndex[1]] = tilesClone[zeroIndex[0]][zeroIndex[1] + 1];
            tilesClone[zeroIndex[0]][zeroIndex[1] + 1] = 0;
            boardIterable.push(new Board(tilesClone));
        }

        return boardIterable;
    }

    private int[] findZeroIndex() {
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                if (tiles[i][j] == 0)
                    return new int[]{i, j};
        return new int[0];
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i = (tiles[0][0] == 0 || tiles[0][1] == 0) ? 1 : 0;
        Board twin = new Board(tiles);
        int temp = twin.tiles[i][0];
        twin.tiles[i][0] = twin.tiles[i][1];
        twin.tiles[i][1] = temp;
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) { }
}
