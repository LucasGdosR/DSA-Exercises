package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// Undirected graph with weighted edges representation.
public class EdgeWeightedGraph {
    private final List<Edge>[] adjacencyList;
    // A list of all edges is not needed, but makes it easier to iterate through all of them.
    private final ArrayList<Edge> allEdges;

    public EdgeWeightedGraph(int numberOfVertices) {
        allEdges = new ArrayList<>();
        this.adjacencyList = (LinkedList<Edge>[]) new LinkedList[numberOfVertices];
        for (int v = 0; v < numberOfVertices; v++)
            adjacencyList[v] = new LinkedList<>();
    }

    public void addEdge(int v, int w, double weight) {
        Edge edge = new Edge(v, w, weight);
        adjacencyList[v].add(edge);
        adjacencyList[w].add(edge);
        allEdges.add(edge);
    }

    public Iterable<Edge> adjacentTo(int v) {
        return adjacencyList[v];
    }

    // All edges in the graph with no duplicates.
    public Iterable<Edge> edges() {
        return allEdges;
    }

    // All edges in the graph in ascending order of weight. Useful for building a MST.
    public Iterable<Edge> edgesInOrder() {
        Collections.sort(allEdges);
        return allEdges;
    }

    public int V() {
        return adjacencyList.length;
    }

    public int E() {
        return allEdges.size();
    }
}
