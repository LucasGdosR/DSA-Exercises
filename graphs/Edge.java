package graphs;

public class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // Returns one vertex.
    public int either() {
        return v;
    }

    // Returns the other vertex, or -1 if not one of the vertices.
    public int other(int v) {
        if (this.v == v)
            return w;
        if (this.w == v)
            return v;
        return -1;
    }

    public double weight() {
        return weight;
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }
}
