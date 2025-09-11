package Graph;

import java.util.List;

public record Graph(List<Edge> edges) {

    public String toString() {
        return this.edges.toString();
    }
}
