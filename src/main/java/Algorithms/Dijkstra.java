package Algorithms;

import Graph.Edge;
import Graph.Graph;
import Graph.Path;
import Graph.Vertex;

import java.util.*;

public class Dijkstra {
    private final Graph graph;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unsettledNodes;
    private Map<Vertex, Vertex> parents;
    private Map<Vertex, Double> distances;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public Path getShortestPath(Vertex source, Vertex target, Set<Edge> excludedEdges) {
        initialize(source);
        while (!unsettledNodes.isEmpty()) {
            Vertex current = getMinimumDistanceVertex();
            unsettledNodes.remove(current);
            settledNodes.add(current);

            updateNeighbors(current, excludedEdges);
        }
        List<Vertex> pathVertices = buildPath(target);
        Double totalDistance = getDistanceTo(target);
        return new Path(pathVertices, totalDistance);
    }

    private void initialize(Vertex source) {
        settledNodes = new HashSet<>();
        unsettledNodes = new HashSet<>();
        parents = new HashMap<>();
        distances = new HashMap<>();
        distances.put(source, 0.0);
        unsettledNodes.add(source);
    }

    private Vertex getMinimumDistanceVertex() {
        return Collections.min(unsettledNodes, Comparator.comparingDouble(this::getDistanceTo));
    }

    private void updateNeighbors(Vertex node, Set<Edge> excludedEdges) {
        for (Vertex neighbor : getUnsettledNeighbors(node)) {
            Double newDistance = getDistanceTo(node) + getEdgeWeight(node, neighbor, excludedEdges);
            if (newDistance < getDistanceTo(neighbor)) {
                distances.put(neighbor, newDistance);
                parents.put(neighbor, node);
                unsettledNodes.add(neighbor);
            }
        }
    }

    private List<Vertex> getUnsettledNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : graph.edges()) {
            if (edge.source().equals(node) && !settledNodes.contains(edge.destination())) {
                neighbors.add(edge.destination());
            }
        }
        return neighbors;
    }

    private Double getEdgeWeight(Vertex from, Vertex to, Set<Edge> excludedEdges) {
        if (excludedEdges != null) {
            for (Edge edge : excludedEdges) {
                if (edge.compareBySourceAndDestination(from, to)) {
                    return Double.MAX_VALUE;
                }
            }
        }
        for (Edge edge : graph.edges()) {
            if (edge.source().equals(from) && edge.destination().equals(to)) {
                return edge.weight();
            }
        }
        throw new RuntimeException("Dijkstra::getEdgeWeight::edge_NOT_found_between_given_vertices");
    }

    private List<Vertex> buildPath(Vertex target) {
        if (!parents.containsKey(target)) {
            return null;
        }
        List<Vertex> path = new ArrayList<>();
        Vertex current = target;
        while (current != null) {
            path.add(current);
            current = parents.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private Double getDistanceTo(Vertex vertex) {
        return distances.getOrDefault(vertex, Double.MAX_VALUE);
    }
}
