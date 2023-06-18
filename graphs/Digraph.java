package graphs;

// Directed graph representation.
public class Digraph extends Graph {
    public Digraph(int numberOfVertices) {
        super(numberOfVertices);
    }

    @Override
    public void addEdge(int v, int w) {
        adjacencyLists[v].add(w);
        edges++;
    }

    // Returns a new digraph with reversed edges.
    public Digraph reverse() {
        Digraph reverseGraph = new Digraph(V());

        for (int v = 0; v < V(); v++)
            for (Integer w : adjacentTo(v))
                reverseGraph.addEdge(w, v);

        return reverseGraph;
    }
}
