package MST;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class MSTAlgorithmsTest {

    private static Graph connectedGraph;
    private static Graph disconnectedGraph;

    @BeforeAll
    static void setup() {
        // === Connected graph
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 3),
                new Edge("B", "C", 2),
                new Edge("B", "D", 4),
                new Edge("C", "E", 5),
                new Edge("D", "E", 6)
        );
        connectedGraph = new Graph(nodes, edges);

        // === Disconnected graph
        List<String> nodes2 = Arrays.asList("A", "B", "C", "X");
        List<Edge> edges2 = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 2)
                // "X" is disconnected
        );
        disconnectedGraph = new Graph(nodes2, edges2);
    }

    // ---------- Correctness Tests

    @Test
    void testTotalCostSameForPrimAndKruskal() {
        List<Edge> primMST = Prim.findMST(connectedGraph);
        List<Edge> kruskalMST = Kruskal.findMST(connectedGraph);

        int primCost = primMST.stream().mapToInt(e -> e.weight).sum();
        int kruskalCost = kruskalMST.stream().mapToInt(e -> e.weight).sum();

        assertEquals(primCost, kruskalCost, "Total cost of MSTs should match");
    }

    @Test
    void testNumberOfEdgesEqualsVMinus1() {
        List<Edge> primMST = Prim.findMST(connectedGraph);
        List<Edge> kruskalMST = Kruskal.findMST(connectedGraph);

        int expected = connectedGraph.nodes.size() - 1;
        assertEquals(expected, primMST.size(), "Prim MST should have V-1 edges");
        assertEquals(expected, kruskalMST.size(), "Kruskal MST should have V-1 edges");
    }

    @Test
    void testPrimMSTIsAcyclicAndConnected() {
        List<Edge> mst = Prim.findMST(connectedGraph);
        assertTrue(isAcyclicAndConnected(connectedGraph.nodes, mst),
                "Prim MST should be acyclic and connected");
    }

    @Test
    void testKruskalMSTIsAcyclicAndConnected() {
        List<Edge> mst = Kruskal.findMST(connectedGraph);
        assertTrue(isAcyclicAndConnected(connectedGraph.nodes, mst),
                "Kruskal MST should be acyclic and connected");
    }

    @Test
    void testDisconnectedGraphHandledGracefully() {
        List<Edge> primMST = Prim.findMST(disconnectedGraph);
        List<Edge> kruskalMST = Kruskal.findMST(disconnectedGraph);

        assertTrue(primMST.size() < disconnectedGraph.nodes.size() - 1
                        || kruskalMST.size() < disconnectedGraph.nodes.size() - 1,
                "Disconnected graphs should not produce full MSTs");
    }

    // ---------- Performance & Consistency

    @Test
    void testExecutionTimeNonNegative() {
        long start = System.nanoTime();
        Prim.findMST(connectedGraph);
        long end = System.nanoTime();
        assertTrue(end - start >= 0, "Execution time must be non-negative");
    }

    @Test
    void testOperationCountsNonNegative() {
        Prim.findMST(connectedGraph);
        Kruskal.findMST(connectedGraph);

        assertTrue(Prim.stats.getTotal() >= 0, "Prim operation count must be non-negative");
        assertTrue(Kruskal.stats.getTotal() >= 0, "Kruskal operation count must be non-negative");
    }

    @Test
    void testResultsAreReproducible() {
        List<Edge> prim1 = Prim.findMST(connectedGraph);
        List<Edge> prim2 = Prim.findMST(connectedGraph);

        List<Edge> kruskal1 = Kruskal.findMST(connectedGraph);
        List<Edge> kruskal2 = Kruskal.findMST(connectedGraph);

        assertEquals(prim1, prim2, "Prim results must be consistent");
        assertEquals(kruskal1, kruskal2, "Kruskal results must be consistent");
    }

    // ---------- Helper Method

    private boolean isAcyclicAndConnected(List<String> nodes, List<Edge> mst) {
        // Build adjacency list
        Map<String, List<String>> adj = new HashMap<>();
        for (String n : nodes) adj.put(n, new ArrayList<>());
        for (Edge e : mst) {
            adj.get(e.from).add(e.to);
            adj.get(e.to).add(e.from);
        }

        // DFS traversal
        Set<String> visited = new HashSet<>();
        dfs(nodes.get(0), adj, visited, null);

        boolean allConnected = visited.size() == nodes.size();
        boolean acyclic = mst.size() == nodes.size() - 1;

        return allConnected && acyclic;
    }

    private void dfs(String node, Map<String, List<String>> adj,
                     Set<String> visited, String parent) {
        visited.add(node);
        for (String nei : adj.get(node)) {
            if (!visited.contains(nei)) {
                dfs(nei, adj, visited, node);
            }
        }
    }
}
