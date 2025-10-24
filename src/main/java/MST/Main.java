package MST;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Reader reader = new FileReader("src/main/resources/input.json");
        JsonObject data = gson.fromJson(reader, JsonObject.class);
        JsonArray graphs = data.getAsJsonArray("graphs");

        JsonArray results = new JsonArray();

        for (JsonElement g : graphs) {
            JsonObject graphObj = g.getAsJsonObject();
            int id = graphObj.get("id").getAsInt();
            List<String> nodes = new ArrayList<>();
            graphObj.getAsJsonArray("nodes").forEach(n -> nodes.add(n.getAsString()));

            List<Edge> edges = new ArrayList<>();
            graphObj.getAsJsonArray("edges").forEach(e -> {
                JsonObject obj = e.getAsJsonObject();
                edges.add(new Edge(
                        obj.get("from").getAsString(),
                        obj.get("to").getAsString(),
                        obj.get("weight").getAsInt()
                ));
            });

            Graph graph = new Graph(nodes, edges);

            // === Prim ===
            long start1 = System.nanoTime();
            List<Edge> primMST = Prim.findMST(graph);
            long end1 = System.nanoTime();

            // === Kruskal ===
            long start2 = System.nanoTime();
            List<Edge> kruskalMST = Kruskal.findMST(graph);
            long end2 = System.nanoTime();

            JsonObject result = new JsonObject();
            result.addProperty("graph_id", id);
            result.addProperty("num_vertices", nodes.size());
            result.addProperty("num_edges", edges.size());

            // === Prim ===
            JsonObject prim = new JsonObject();
            prim.addProperty("total_cost", primMST.stream().mapToInt(e -> e.weight).sum());
            prim.addProperty("execution_time_ms", (end1 - start1) / 1_000_000.0);

            JsonObject primOps = new JsonObject();
            primOps.addProperty("comparisons", Prim.stats.comparisons);
            primOps.addProperty("edge_selections", Prim.stats.edgeSelections);
            primOps.addProperty("total_operations", Prim.stats.getTotal());
            prim.add("operations", primOps);

            JsonArray primEdges = new JsonArray();
            for (Edge e : primMST) {
                JsonObject edgeObj = new JsonObject();
                edgeObj.addProperty("from", e.from);
                edgeObj.addProperty("to", e.to);
                edgeObj.addProperty("weight", e.weight);
                primEdges.add(edgeObj);
            }
            prim.add("mst_edges", primEdges);
            result.add("prim", prim);

            // === Kruskal ===
            JsonObject kruskal = new JsonObject();
            kruskal.addProperty("total_cost", kruskalMST.stream().mapToInt(e -> e.weight).sum());
            kruskal.addProperty("execution_time_ms", (end2 - start2) / 1_000_000.0);

            JsonObject kruskalOps = new JsonObject();
            kruskalOps.addProperty("comparisons", Kruskal.stats.comparisons);
            kruskalOps.addProperty("find_operations", Kruskal.stats.findOps);
            kruskalOps.addProperty("union_operations", Kruskal.stats.unionOps);
            kruskalOps.addProperty("total_operations", Kruskal.stats.getTotal());
            kruskal.add("operations", kruskalOps);

            JsonArray kruskalEdges = new JsonArray();
            for (Edge e : kruskalMST) {
                JsonObject edgeObj = new JsonObject();
                edgeObj.addProperty("from", e.from);
                edgeObj.addProperty("to", e.to);
                edgeObj.addProperty("weight", e.weight);
                kruskalEdges.add(edgeObj);
            }
            kruskal.add("mst_edges", kruskalEdges);
            result.add("kruskal", kruskal);

            results.add(result);
        }

        JsonObject output = new JsonObject();
        output.add("results", results);

        try (FileWriter writer = new FileWriter("src/main/resources/output.json")) {
            gson.toJson(output, writer);
        }

        System.out.println("Results written to output.json (formatted)");
    }
}
