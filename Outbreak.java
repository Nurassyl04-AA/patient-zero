import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections, rooted in person 0.
 */
public class Outbreak {

    /** Minimum-cost set of tested people so that every link has a tested end (weighted vertex cover on a tree). */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] parent = new int[n];
        int[] order = bfsOrder(n, nb, parent);
        long[] in = new long[n];                       // v tested
        long[] out = new long[n];                      // v not tested
        for (int idx = n - 1; idx >= 0; idx--) {       // leaves first
            int v = order[idx];
            in[v] = cost[v];
            out[v] = 0;
            for (int c : nb[v]) {
                if (c == parent[v]) continue;
                in[v] += Math.min(in[c], out[c]);      // child is free to choose
                out[v] += in[c];                       // v not tested -> every child tested
            }
        }
        // rebuild: walk down from the root
        boolean[] tested = new boolean[n];
        boolean[] decided = new boolean[n];
        for (int idx = 0; idx < n; idx++) {
            int v = order[idx];
            if (v == 0) tested[v] = in[v] <= out[v];
            else {
                int p = parent[v];
                tested[v] = !tested[p] || in[v] <= out[v];  // parent not tested -> v must be tested
            }
        }
        return tested;
    }

    /** Longest path in the tree, counted in links: the two deepest branches below each person. */
    public static int longestChain(int n, int[][] nb) {
        int[] parent = new int[n];
        int[] order = bfsOrder(n, nb, parent);
        int[] down1 = new int[n], down2 = new int[n];  // two longest ways down (in links)
        int best = 0;
        for (int idx = n - 1; idx >= 0; idx--) {
            int v = order[idx];
            for (int c : nb[v]) {
                if (c == parent[v]) continue;
                int len = down1[c] + 1;
                if (len > down1[v]) { down2[v] = down1[v]; down1[v] = len; }
                else if (len > down2[v]) down2[v] = len;
            }
            best = Math.max(best, down1[v] + down2[v]);
        }
        return best;
    }

    /** BFS order from person 0; fills parent (root has parent -1). Iterative: no recursion depth problems. */
    private static int[] bfsOrder(int n, int[][] nb, int[] parent) {
        int[] order = new int[n];
        boolean[] seen = new boolean[n];
        int head = 0, tail = 0;
        order[tail++] = 0; seen[0] = true; parent[0] = -1;
        while (head < tail) {
            int v = order[head++];
            for (int c : nb[v]) if (!seen[c]) { seen[c] = true; parent[c] = v; order[tail++] = c; }
        }
        return order;
    }
}
