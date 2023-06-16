package union_find.social_network_connectivity;

import union_find.UnionFind;

public class UnionFindNetWork extends UnionFind {
    int connectedSize = 0;
    UnionFindNetWork(int n) {
        super(n);
    }
    @Override
    public void union(int p, int q) {
        p = find(p);
        q = find(q);

        if (p == q)
            return;

        if (size[p] > size[q]) {
            id[q] = p;
            size[p] += size[q];
            if (size[p] > connectedSize)
                connectedSize = size[p];
        } else {
            id[p] = q;
            size[q] += size[p];
            if (size[q] > connectedSize)
                connectedSize = size[q];
        }
    }
}