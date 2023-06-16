package union_find.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
Percolation. Given a composite systems comprised of randomly distributed insulating and metallic materials: what
fraction of the materials need to be metallic so that the composite system is an electrical conductor? Given a porous
landscape with water on the surface (or oil below), under what conditions will the water be able to drain through to the
bottom (or the oil to gush through to the surface)? Scientists have defined an abstract process known as percolation to
model such situations.

The model. We model a percolation system using an n-by-n grid of sites. Each site is either open or blocked. A full site
is an open site that can be connected to an open site in the top row via a chain of neighboring (left, right, up, down)
open sites. We say the system percolates if there is a full site in the bottom row. In other words, a system percolates
if we fill all open sites connected to the top row and that process fills some open site on the bottom row.
(For the insulating/metallic materials example, the open sites correspond to metallic materials, so that a system that
percolates has a metallic path from top to bottom, with full sites conducting. For the porous substance example, the
open sites correspond to empty space through which water might flow, so that a system that percolates lets water fill
open sites, flowing from top to bottom.)

The problem. In a famous scientific problem, researchers are interested in the following question: if sites are
independently set to be open with probability p (and therefore blocked with probability 1 − p), what is the probability
that the system percolates? When p equals 0, the system does not percolate; when p equals 1, the system percolates.

When n is sufficiently large, there is a threshold value p* such that when p < p* a random n-by-n grid almost never
percolates, and when p > p*, a random n-by-n grid almost always percolates. No mathematical solution for determining
the percolation threshold p* has yet been derived. Your task is to write a computer program to estimate p*.

Corner cases. By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left
site: Throw an IllegalArgumentException if any argument to open(), isOpen(), or isFull() is outside its prescribed
range. Throw an IllegalArgumentException in the constructor if n ≤ 0.

Performance requirements. The constructor must take time proportional to n2; all instance methods must take constant
time plus a constant number of calls to union() and find().
 */
public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final boolean[] open;
    private final int dimension;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        int gridSizePlusVirtualElements = n * n + 2;
        uf = new WeightedQuickUnionUF(gridSizePlusVirtualElements);
        open = new boolean[gridSizePlusVirtualElements];
        dimension = n;

        // Link virtual elements to entire row
        for (int i = 1; i <= dimension; i++) {
            uf.union(0, i);
            uf.union(open.length - 1, open.length -1 - i);
        }

        open[0] = open[open.length - 1] = true;
    }
    public void open(int row, int col) {
        verifyInput(row, col);

        int index = getIndexFromRowAndCol(row, col);

        if (open[index])
            return;

        open[index] = true;
        numberOfOpenSites++;

        int[] neighbors = new int[4];

        if (row != 1) neighbors[0] = (index - dimension);
        if (col != 1) neighbors[1] = (index - 1);
        if (row != dimension) neighbors[2] = (index + dimension);
        if (col != dimension) neighbors[3] = (index + 1);

        for (int neighbor : neighbors)
            if (open[neighbor] && neighbor != 0)
                uf.union(index, neighbor);
    }

    public boolean isOpen(int row, int col) {
        verifyInput(row, col);

        int index = getIndexFromRowAndCol(row, col);

        return open[index];
    }

    public boolean isFull(int row, int col) {
        verifyInput(row, col);

        int index = getIndexFromRowAndCol(row, col);

        if (open[index])
            return uf.find(0) == uf.find(index);
        else return false;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        if (open.length != 3 || open[1])
            return uf.find(0) == uf.find(open.length - 1);
        return false;
    }

    private void verifyInput(int row, int col) {
        if (row < 1 || col < 1 || row > dimension || col > dimension)
            throw new IllegalArgumentException();
    }

    private int getIndexFromRowAndCol(int row, int col) {
        return (row - 1) * dimension + col;
    }
}
