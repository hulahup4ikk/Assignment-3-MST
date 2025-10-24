package MST;

import java.util.*;

public class Graph {
    List<String> nodes;
    List<Edge> edges;

    public Graph(List<String> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<String> getNodes() {
        return nodes;
    }
}

