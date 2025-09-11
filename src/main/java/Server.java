import Graph.Edge;
import Graph.Graph;
import Graph.Path;
import Graph.Vertex;
import Algorithms.Yen;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 1968;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            server.setReuseAddress(true);
            System.out.println("Server started. Listening on port: " + PORT);

            while (true) {
                Socket client = server.accept();
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(client)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error:");
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final List<Vertex> vertices = new ArrayList<>();
        private final List<Edge> edges = new ArrayList<>();

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                processClientInput(in, out);
            } catch (IOException e) {
                System.err.println("Client handler I/O error:");
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket:");
                    e.printStackTrace();
                }
            }
        }

        private void processClientInput(BufferedReader in, PrintWriter out) throws IOException {
            String line;
            boolean isFirstLine = true;
            int edgeCount = 0;
            int k = 0;
            String sourceId = "";
            String targetId = "";

            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ");
                if (isFirstLine) {
                    edgeCount = Integer.parseInt(parts[0]);
                    sourceId = parts[1];
                    targetId = parts[2];
                    k = Integer.parseInt(parts[3]);
                    addVertex(sourceId);
                    addVertex(targetId);
                    isFirstLine = false;
                } else {
                    addVertex(parts[0]);
                    addVertex(parts[1]);
                    addEdge(parts[0], parts[1], Double.parseDouble(parts[2]));
                    if (--edgeCount == 0) {
                        break;
                    }
                }
            }
            Graph graph = new Graph(edges);
            Yen yen = new Yen(graph);
            List<Path> kPaths = yen.calculateKShortestPaths(getVertex(sourceId), getVertex(targetId), k);
            out.println(kPaths.toString());
        }

        private void addVertex(String id) {
            if (vertices.stream().noneMatch(v -> v.id().equals(id))) {
                vertices.add(new Vertex(id, id));
            }
        }

        private Vertex getVertex(String id) {
            return vertices.stream()
                    .filter(v -> v.id().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Server::getVertex::vertex_NOT_found: " + id));
        }

        private void addEdge(String source, String destination, Double weight) {
            Vertex from = getVertex(source);
            Vertex to = getVertex(destination);
            edges.add(new Edge(source + "->" + destination, from, to, weight));
        }
    }
}
