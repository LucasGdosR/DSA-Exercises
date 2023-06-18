package graphs;

import java.util.ArrayDeque;
import java.util.Queue;

public class BFS extends Paths{
    public BFS(Graph g, int s) {
        super(g, s);
    }

    @Override
    protected void search(Graph G, int s) {
        // Create auxiliary queue.
        Queue<Integer> queue = new ArrayDeque<>();

        // Initialize the queue and mark the origin as visited.
        queue.add(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int current = queue.remove();
            for (Integer neighbor : G.adjacentTo(current)) {
                if (visited[neighbor])
                    continue;
                visited[neighbor] = true;
                edgeTo[neighbor] = current;
                queue.add(neighbor);
            }
        }
    }
}
