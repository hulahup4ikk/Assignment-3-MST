# Assignment 3 - Minimum Spanning Tree (MST)

## 3. Output and Evaluation

### 1. Summary of Input Data and Algorithm Results

| Graph ID | Vertices | Edges | Algorithm | Total Cost | Execution Time (ms) | Operation Count |
|-----------|-----------|--------|------------|--------------|---------------------|-----------------|
| 1 | 5 | 6 | Prim | 9 | 0.392 | 13 |
|   |   |   | Kruskal | 9 | 0.616 | 32 |
| 2 | 10 | 14 | Prim | 30 | 1.031 | 51 |
|   |   |   | Kruskal | 30 | 1.680 | 89 |
| 3 | 25 | 27 | Prim | 112 | 1.463 | 114 |
|   |   |   | Kruskal | 112 | 1.884 | 201 |

- Both algorithms produced identical MST total costs, confirming correctness.

### 2. Comparison of Prim’s and Kruskal’s Algorithms

**Theoretical analysis:**

| Aspect                     | Prim’s Algorithm                                     | Kruskal’s Algorithm                         |
| -------------------------- |------------------------------------------------------| ------------------------------------------- |
| **Time complexity**        | O(V × E) | O(E log E) ≈ O(E log V)                     |
| **Typical data structure** | List<Edge> + HashSet                | Disjoint Set (Union–Find)                   |
| **Best suited for**        | Dense graphs (many edges)                            | Sparse graphs (few edges)                   |
| **Edge selection**         | Expands from one vertex outward                      | Sorts all edges globally                    |
| **Memory usage**           | Depends on adjacency representation                  | Requires extra space for parent/rank arrays |

**Practical analysis:**

- On small and medium graphs (≤ 25 vertices), Prim’s algorithm consistently ran faster in this implementation.
- Kruskal’s algorithm performed more operations (comparisons, unions, finds) due to its reliance on sorting and disjoint set operations.
- The differences in execution time remained small because both algorithms are well optimized for moderate input sizes.

### 3. Conclusions

- Both algorithms correctly compute the MST and produce identical total costs.

**In practice:**

- Prim’s algorithm was faster on all tested graphs and required fewer basic operations.
- In isolated single-graph tests, Kruskal’s algorithm appeared slower due to JVM startup and JIT compilation overhead.
  However, when multiple datasets were executed sequentially, the JVM optimized hot methods (find, union, sorting`), significantly improving Kruskal’s performance.
  Therefore, performance comparisons in Java must account for JIT warm-up effects — sequential runs give more realistic and stable measurements.
- For dense graphs (many edges), Prim’s approach using a priority queue is preferable due to localized edge selection.
- For sparse or edge-list–based representations, Kruskal’s algorithm is conceptually simpler and easier to implement with Union–Find.

**Overall, algorithm choice depends on graph density and data representation:**
- Use Prim for dense graphs or when adjacency structures are available.
- Use Kruskal for sparse graphs or when working directly with edge lists.

---

