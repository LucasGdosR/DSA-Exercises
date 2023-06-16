package union_find.successor_with_delete;

/*
Successor with delete. Given a set of n integers S={0,1,...,n−1} and a sequence of requests of the following form:

Remove x from S;
Find the successor of x: the smallest y in S such that y ≥ x.

design a data type so that all operations (except construction) take logarithmic time or better in the worst case.
 */
public class UnionFindSuccessorWithDelete {
    private int[] id;
    private int[] size;
    private int[] max;
    private boolean[] removed;

    public UnionFindSuccessorWithDelete(int n) {
        id = new int[n];
        size = new int[n];
        max = new int[n];
        removed = new boolean[n];
        for (int i = 0; i < n; i++) {
            id[i] = max[i] = i;
            size[i] = 1;
        }
    }

    private void union(int p, int q) {
        p = find(p);
        q = find(q);

        if (p == q)
            return;

        if (size[p] > size[q]) {
            id[q] = id[p];
            size[p] += size[q];
        } else {
            id[p] = id[q];
            size[q] += size[p];
        }

        if (max[p] > max[q])
            max[q] = max[p];
        else max[p] = max[q];
    }

    private int find(int i) {
        while (i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    private boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void remove(int x) {
        removed[x] = true;
        union(x, x + 1);
    }

    public int findSuccessor(int x) {
        if (!removed[x + 1])
            return x + 1;

        int successorMax = max[find(x + 1)];

        if (removed[successorMax])
            return successorMax + 1;

        return successorMax;
    }
}
