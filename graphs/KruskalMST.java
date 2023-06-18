package graphs;

import union_find.UnionFind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KruskalMST {
    private List<Edge> edges;
    private double weight;

    public KruskalMST(EdgeWeightedGraph G) {
        edges = new ArrayList<>(G.V() - 1);

        // Get all edges sorted by weight in ascending order.
        // You could use a MinPriorityQueue for this.
        Iterator<Edge> allEdgesInOrder = G.edgesInOrder().iterator();

        // Use a UnionFind auxiliary data structure to efficiently check for cycles.
        UnionFind uf = new UnionFind(G.V());

        // Iterate until there are V - 1 edges (MST is complete)
        while (allEdgesInOrder.hasNext() && edges.size() < G.V() - 1) {
            // Get the edge with the smallest weight.
            Edge e = allEdgesInOrder.next();
            // Extract the vertices.
            int v = e.either(), w = e.other(v);
            // Check if the connecting them would create a cycle.
            if (!uf.connected(v, w)) {
                // Merge the components.
                uf.union(v,w);
                // Add the edge to the MST and increment the weight.
                edges.add(e);
                weight += e.weight();
            }
        }
    }

    public Iterable<Edge> edges() {
        return edges;
    }

    public double weight() {
        return weight;
    }
}
