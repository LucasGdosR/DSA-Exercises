package graphs;

import java.util.Stack;

public abstract class Paths {
    protected final int source;
    protected final boolean[] visited;
    protected final int[] edgeTo;

    public Paths(Graph graph, int source) {
        this.source = source;
        visited = new boolean[graph.V()];
        edgeTo = new int[graph.V()];
        search(graph, source);
    }
    public boolean hasPathTo(int v) {
        return visited[v];
    }
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v))
            return null;

        // We'll look from the end to the beginning, so use a stack to reverse the order.
        Stack<Integer> path = new Stack<>();

        while (v != source) {
            path.push(edgeTo[v]);
            v = edgeTo[v];
        }
        path.push(source);

        return path;
    }

    protected abstract void search(Graph G, int s);
}
