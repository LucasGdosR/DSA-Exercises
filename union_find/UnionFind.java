package union_find;

public class UnionFind {
    protected int[] id;
    protected int[] size;

    public UnionFind(int n) {
        id = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    public void union(int p, int q) {
        p = find(p);
        q = find(q);

        if (p == q)
            return;

        if (size[p] > size[q]) {
            id[q] = p;
            size[p] += size[q];
        } else {
            id[p] = q;
            size[q] += size[p];
        }
    }

    public int find(int p) {
        while (p != id[p]) {
            id[p] = id[id[p]];
            p = id[p];
        }
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
}
