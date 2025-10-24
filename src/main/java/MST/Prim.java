package MST;

import java.util.*;

public class Prim {
    public static class OperationStats {
        public int comparisons = 0;
        public int edgeSelections = 0;

        public int getTotal() {
            return comparisons + edgeSelections;
        }
    }

    public static OperationStats stats = new OperationStats();

    public static List<Edge> findMST(Graph graph) {
        stats = new OperationStats(); // reset
        List<Edge> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        visited.add(graph.nodes.get(0));

        while (result.size() < graph.nodes.size() - 1) {
            Edge minEdge = null;
            for (Edge e : graph.edges) {
                boolean cond1 = visited.contains(e.from) && !visited.contains(e.to);
                boolean cond2 = visited.contains(e.to) && !visited.contains(e.from);
                if (cond1 || cond2) {
                    stats.comparisons++;
                    if (minEdge == null || e.weight < minEdge.weight) {
                        minEdge = e;
                    }
                }
            }

            if (minEdge == null) {
                break;
            }

            result.add(minEdge);
            visited.add(minEdge.from);
            visited.add(minEdge.to);
            stats.edgeSelections++;
        }

        return result;
    }
}
