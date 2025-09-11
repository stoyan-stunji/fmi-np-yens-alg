package Graph.Algorithms;

import Algorithms.Dijkstra;
import Graph.Edge;
import Graph.Graph;
import Graph.Path;
import Graph.Vertex;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDijkstra {
    private List<Vertex> vertices;
    private List<Edge> edges;
    private Graph graph;

    private void initializeGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();

        vertices.add(new Vertex("A", "A"));
        vertices.add(new Vertex("B", "B"));
        vertices.add(new Vertex("C", "C"));
        vertices.add(new Vertex("D", "D"));
        vertices.add(new Vertex("G", "G"));
        vertices.add(new Vertex("F", "F"));

        addEdge("AB", 0, 1, 3.0);
        addEdge("AD", 0, 3, 2.0);
        addEdge("BC", 1, 2, 4.0);
        addEdge("CG", 2, 4, 2.0);
        addEdge("CF", 2, 5, 1.0);
        addEdge("DB", 3, 1, 1.0);
        addEdge("DC", 3, 2, 2.0);
        addEdge("DG", 3, 4, 3.0);
        addEdge("GF", 4, 5, 2.0);

        graph = new Graph(edges);
    }

    private void addEdge(String edgeId, int sourceIndex, int destinationIndex, Double weight) {
        Vertex source = vertices.get(sourceIndex);
        Vertex destination = vertices.get(destinationIndex);
        edges.add(new Edge(edgeId, source, destination, weight));
    }

    @Test
    void testDijkstraCalculatesShortestPath() {
        initializeGraph();
        Dijkstra dijkstra = new Dijkstra(graph);
        List<Vertex> expectedPath = new ArrayList<>(List.of(
                vertices.get(0),
                vertices.get(3),
                vertices.get(2),
                vertices.get(5)
        ));
        Path path = dijkstra.getShortestPath(vertices.get(0), vertices.get(5), null);
        assertEquals(expectedPath, path.getVertices());
        assertEquals(5, path.getWeight());
        System.out.println("Shortest Path (no excluded edges):");
        path.getVertices().forEach(System.out::println);
    }

    @Test
    void testDijkstraCalculatesShortestPathWithExcludedEdge() {
        initializeGraph();
        Dijkstra dijkstra = new Dijkstra(graph);
        List<Vertex> expectedPath = new ArrayList<>(List.of(
                vertices.get(0),
                vertices.get(3),
                vertices.get(4),
                vertices.get(5)
        ));
        Set<Edge> excludedEdges = new HashSet<>(List.of(edges.get(6)));
        Path path = dijkstra.getShortestPath(vertices.get(0), vertices.get(5), excludedEdges);
        assertEquals(expectedPath, path.getVertices());
        assertEquals(7, path.getWeight());
        System.out.println("Shortest Path (with excluded edge DC):");
        path.getVertices().forEach(System.out::println);
    }
}
