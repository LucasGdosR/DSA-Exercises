package graphs;

import java.util.LinkedList;
import java.util.List;

// Undirected Graph representation.
public class Graph {
    protected final List<Integer>[] adjacencyLists;
    protected int edges;

    public Graph(int numberOfVertices) {
        adjacencyLists = (LinkedList<Integer>[]) new LinkedList[numberOfVertices];
        for (int v = 0; v < numberOfVertices; v++)
            adjacencyLists[v] = new LinkedList<>();
        edges = 0;
    }

    public void addEdge(int v, int w) {
        adjacencyLists[v].add(w);
        adjacencyLists[w].add(v);
        edges++;
    }

    public Iterable<Integer> adjacentTo(int v) {
        return adjacencyLists[v];
    }

    public int V() {
        return adjacencyLists.length;
    }

    public int E() {
        return edges;
    }

    public static int degree(Graph G, int v) {
        return G.adjacencyLists[v].size();
    }

    public static int maxDegree(Graph G) {
        int maxDegree = 0;
        for (int v = 0; v < G.V(); v++)
            if (maxDegree < degree(G, v))
                maxDegree = degree(G, v);
        return maxDegree;
    }
}
