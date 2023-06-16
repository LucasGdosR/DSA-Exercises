package union_find.specific_canonical_element;

import union_find.UnionFind;

/*
Union-find with specific canonical element. Add a method find() to the union-find data type so that
find(i) returns the largest element in the connected component containing i. The operations, union(),
connected(), and find() should all take logarithmic time or better.

For example, if one of the connected components is {1,2,6,9}, then the find() method should return 9
for each of the four elements in the connected components.
 */
public class UnionFindSpecificCanonicalElement {

    private int[] id;
    private int[] size;
    private int[] canonicalElement;

    public UnionFindSpecificCanonicalElement(int n) {
        id = new int[n];
        size = new int[n];
        canonicalElement = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = canonicalElement[i] = i;
            size[i] = 1;
        }
    }

    public void union(int p, int q) {
        p = findRoot(p);
        q = findRoot(q);

        if (p == q)
            return;

        if (size[p] > size[q]) {
            id[q] = p;
            size[p] += size[q];
        } else {
            id[p] = q;
            size[q] += size[p];
        }

        if (canonicalElement[p] > canonicalElement[q])
            canonicalElement[q] = canonicalElement[p];
        else canonicalElement[p] = canonicalElement[q];
    }

    private int findRoot(int i) {
        while (i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    public int find(int i) {
        return canonicalElement[findRoot(i)];
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
}
