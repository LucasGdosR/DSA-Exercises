package union_find;

public class UnionFind {
    protected int[] id;
    protected int[] size;
    private int[] toCompress;

    public UnionFind(int n) {
        id = new int[n];
        size = new int[n];
        toCompress = new int[n];
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
        int i = 0;
        
        while (p != id[p]) {
            toCompress[i++] = p;
            p = id[p];
        }

        for (int j = 0; j < i; j++)
            id[j] = p;

        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(10);

        assert !uf.connected(0, 1);

        uf.union(0, 1);

        assert uf.connected(0, 1);

        assert uf.find(0) == 1;
    }
}
