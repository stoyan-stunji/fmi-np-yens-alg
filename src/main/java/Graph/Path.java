package Graph;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Vertex> vertices;
    private final Double weight;

    public Path(List<Vertex> path, Double weight) {
        this.vertices = path;
        this.weight = weight;
    }

    public List<Vertex> getVertices() {
        return this.vertices != null ? this.vertices : new ArrayList<>();
    }

    public Double getWeight() {
        return this.weight;
    }

    public List<Edge> getEdges(Graph graph) {
        List<Edge> edges = new ArrayList<>();
        if (this.vertices == null || this.vertices.size() < 2) {
            return edges;
        }
        for (int i = 0; i < this.vertices.size() - 1; i++) {
            Vertex source = this.vertices.get(i);
            Vertex destination = this.vertices.get(i + 1);
            Edge edge = findEdgeBetweenVertices(graph, source, destination);
            edges.add(edge);
        }
        return edges;
    }

    private Edge findEdgeBetweenVertices(Graph graph, Vertex source, Vertex destination) {
        return graph.edges().stream()
                .filter(edge -> edge.compareBySourceAndDestination(source, destination))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Path::findEdgeBetweenVertices::edge_NOT_found_between_vertices: "
                                + source + " -> " + destination));
    }

    public Path join(Path other, boolean removeFirst) {
        if (this.vertices == null) {
            return other;
        }
        if (other.vertices == null) {
            return this;
        }
        List<Vertex> newPath = mergePaths(other, removeFirst);
        Double newWeight = this.weight + other.weight;
        return new Path(newPath, newWeight);
    }

    private List<Vertex> mergePaths(Path other, boolean removeFirst) {
        List<Vertex> newPath = new ArrayList<>(this.vertices);
        if (removeFirst && !other.vertices.isEmpty()) {
            newPath.addAll(other.vertices.subList(1, other.vertices.size()));
        } else {
            newPath.addAll(other.vertices);
        }
        return newPath;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Path otherPath) {
            return this.vertices.equals(otherPath.vertices);
        }
        return false;
    }

    public String toString() {
        if (this.vertices == null || this.vertices.isEmpty()) {
            return "EMPTY PATH";
        }
        return String.format("%s (%.2f)", this.vertices, this.weight);
    }
}
