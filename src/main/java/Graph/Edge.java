package Graph;

public record Edge(String id, Vertex source, Vertex destination, Double weight) {

    public String toString() {
        return this.source + " -> " + this.destination + " (" + this.weight + ")";
    }

    public boolean compareBySourceAndDestination(Vertex source, Vertex destination) {
        return this.source.equals(source) && this.destination.equals(destination);
    }
}
