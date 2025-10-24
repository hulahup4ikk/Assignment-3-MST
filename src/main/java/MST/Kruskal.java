package MST;

import java.util.*;

public class Kruskal {
    public static class OperationStats {
        public int comparisons = 0;
        public int findOps = 0;
        public int unionOps = 0;

        public int getTotal() {
            return comparisons + findOps + unionOps;
        }
    }

    public static OperationStats stats = new OperationStats();

    public static List<Edge> findMST(Graph graph) {
        stats = new OperationStats(); // сброс перед запуском

        List<Edge> edges = new ArrayList<>(graph.edges);

        edges.sort((a, b) -> {
            stats.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> rank = new HashMap<>();
        for (String node : graph.nodes) {
            parent.put(node, node);
            rank.put(node, 0);
        }

        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            String root1 = find(parent, edge.from);
            String root2 = find(parent, edge.to);
            stats.findOps += 2;

            stats.comparisons++;
            if (!root1.equals(root2)) {
                mst.add(edge);
                union(parent, rank, root1, root2);
                stats.unionOps++;
            }
        }

        return mst;
    }

    private static String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent, parent.get(node)));
        }
        return parent.get(node);
    }

    private static void union(Map<String, String> parent, Map<String, Integer> rank, String x, String y) {
        String rootX = find(parent, x);
        String rootY = find(parent, y);

        if (rank.get(rootX) < rank.get(rootY)) {
            parent.put(rootX, rootY);
        } else if (rank.get(rootX) > rank.get(rootY)) {
            parent.put(rootY, rootX);
        } else {
            parent.put(rootY, rootX);
            rank.put(rootX, rank.get(rootX) + 1);
        }
    }
}
