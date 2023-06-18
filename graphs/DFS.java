package graphs;

public class DFS extends Paths{
    public DFS(Graph g, int s) {
        super(g, s);
    }

    @Override
    protected void search(Graph G, int v) {
        visited[v] = true;
        for (Integer neighbor : G.adjacentTo(v)) {
            if (visited[neighbor])
                continue;
            edgeTo[neighbor] = v;
            search(G, neighbor);
        }
    }
}
