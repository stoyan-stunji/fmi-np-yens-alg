package Algorithms;

import Graph.Edge;
import Graph.Graph;
import Graph.Path;
import Graph.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class Yen {
    private final Graph graph;
    private final Dijkstra dijkstra;

    public Yen(Graph graph) {
        this.graph = graph;
        this.dijkstra = new Dijkstra(graph);
    }

    public List<Path> calculateKShortestPaths(Vertex source, Vertex target, int k) {
        List<Path> paths = new ArrayList<>();
        Path shortestPath = dijkstra.getShortestPath(source, target, null);
        if (shortestPath == null) {
            return paths;
        }

        paths.add(shortestPath);
        Set<Edge> exclusionCandidates = new HashSet<>(shortestPath.getEdges(graph));

        for (int i = 1; i < k; i++) {
            Path minPath = findNextShortestPath(paths.get(i - 1), source, target, exclusionCandidates);
            if (minPath == null) {
                break;
            }
            paths.add(minPath);
            exclusionCandidates.addAll(minPath.getEdges(graph));
        }
        return paths;
    }

    private Path findNextShortestPath(Path previousPath, Vertex source, Vertex target, Set<Edge> exclusionCandidates) {
        Path minPath = null;
        List<Vertex> vertices = previousPath.getVertices();

        for (int j = 0; j < vertices.size() - 1; j++) {
            Vertex root = vertices.get(j);
            Path spurPath = buildSpurPath(source, root, target, exclusionCandidates);
            if (spurPath == null || !spurPath.getVertices().contains(target)) {
                continue;
            }
            if (minPath == null || spurPath.getWeight() < minPath.getWeight()) {
                minPath = spurPath;
            }
        }
        return minPath;
    }

    private Path buildSpurPath(Vertex source, Vertex root, Vertex target, Set<Edge> exclusionCandidates) {
        Path rootPath = dijkstra.getShortestPath(source, root, null);
        Set<Edge> excludedEdgesFromRoot = exclusionCandidates.stream()
                .filter(edge -> edge.source().equals(root))
                .collect(Collectors.toSet());

        Path spur = dijkstra.getShortestPath(root, target, excludedEdgesFromRoot);
        if (spur == null) {
            return null;
        }
        return rootPath.join(spur, true);
    }
}
