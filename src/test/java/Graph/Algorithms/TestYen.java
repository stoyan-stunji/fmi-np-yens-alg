package Graph.Algorithms;

import Algorithms.Yen;
import Graph.Edge;
import Graph.Graph;
import Graph.Path;
import Graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestYen {
    private List<Vertex> vertices;
    private List<Edge> edges;
    private Graph graph;

    private void addVertex(String id) {
        this.vertices.add(new Vertex(id, id));
    }

    private void addEdge(String id, String from, String to, Double weight) {
        Vertex source = getVertex(from);
        Vertex destination = getVertex(to);
        this.edges.add(new Edge(id, source, destination, weight));
    }

    private Vertex getVertex(String id) {
        return this.vertices.stream()
                .filter(v -> v.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("TestYen::getVertex::vertex_NOT_found: " + id));
    }

    private List<Vertex> pathOf(String... ids) {
        List<Vertex> path = new ArrayList<>();
        for (String id : ids) {
            path.add(getVertex(id));
        }
        return path;
    }

    @BeforeEach
    void setUp() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

        addVertex("A");
        addVertex("B");
        addVertex("C");
        addVertex("D");
        addVertex("E");
        addVertex("F");

        addEdge("AB", "A", "B", 3.0);
        addEdge("AD", "A", "D", 2.0);
        addEdge("BC", "B", "C", 4.0);
        addEdge("CE", "C", "E", 2.0);
        addEdge("CF", "C", "F", 1.0);
        addEdge("DB", "D", "B", 1.0);
        addEdge("DC", "D", "C", 2.0);
        addEdge("DE", "D", "E", 3.0);
        addEdge("EF", "E", "F", 2.0);

        graph = new Graph(edges);
    }

    @Test
    void calculateKShortestPaths() {
        Yen yen = new Yen(graph);
        List<Path> expectedPaths = List.of(
                new Path(pathOf("A", "D", "C", "F"), 5.0),
                new Path(pathOf("A", "D", "E", "F"), 7.0),
                new Path(pathOf("A", "B", "C", "F"), 8.0)
        );
        List<Path> actualPaths = yen.calculateKShortestPaths(getVertex("A"), getVertex("F"), 3);

        System.out.println("Calculated K Shortest Paths:");
        for (int i = 0; i < actualPaths.size(); i++) {
            Path path = actualPaths.get(i);
            String pathStr = path.getVertices().stream()
                    .map(Vertex::id)
                    .reduce((a, b) -> a + " -> " + b)
                    .orElse("");
            System.out.printf("%d: %s (Cost: %.1f)%n", i + 1, pathStr, path.getWeight());
        }
        assertEquals(expectedPaths, actualPaths);
    }
}
