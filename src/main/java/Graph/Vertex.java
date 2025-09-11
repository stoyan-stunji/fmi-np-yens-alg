package Graph;

public record Vertex(String label, String id) implements Comparable<Vertex> {

    public int compareTo(Vertex vertex) {
        return this.id.compareTo(vertex.id);
    }

    public String toString() {
        return this.label;
    }
}
