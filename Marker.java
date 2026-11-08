import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /** Mission 3a. Prefix function (LPS array). pi[i] = longest proper prefix of p[0..i] that is also its suffix. */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        int k = 0;                                     // length of the current border
        for (int i = 1; i < m; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (p.charAt(i) == p.charAt(k)) k++;
            pi[i] = k;
        }
        return pi;
    }

    /** Mission 3b. KMP. All start positions of p in text, increasing, overlaps allowed. */
    public static int[] findAll(String text, String p) {
        int n = text.length(), m = p.length();
        if (m == 0 || m > n) return new int[0];
        int[] pi = prefixFunction(p);
        int[] buf = new int[n];
        int cnt = 0, k = 0;                            // k = number of matched marker letters
        for (int i = 0; i < n; i++) {                  // i only goes forward
            while (k > 0 && text.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (text.charAt(i) == p.charAt(k)) k++;
            if (k == m) {
                buf[cnt++] = i - m + 1;
                k = pi[m - 1];                         // next match may overlap this one
            }
        }
        return Arrays.copyOf(buf, cnt);
    }

    private static final long MOD = 1_000_000_007L;
    private static final long BASE = 257;

    /** Mission 4. Rabin-Karp with a rolling hash. Number of positions i of a whose k-letter word occurs in b. */
    public static int sharedWords(String a, String b, int k) {
        int n = a.length(), m = b.length();
        if (k <= 0 || n < k || m < k) return 0;
        long pow = 1;                                  // BASE^(k-1) mod MOD
        for (int i = 1; i < k; i++) pow = pow * BASE % MOD;

        // hashes of all windows of b -> start positions with this hash
        Map<Long, List<Integer>> table = new HashMap<>();
        long h = 0;
        for (int i = 0; i < k; i++) h = (h * BASE + b.charAt(i)) % MOD;
        table.computeIfAbsent(h, x -> new ArrayList<>()).add(0);
        for (int i = k; i < m; i++) {
            h = ((h - b.charAt(i - k) * pow % MOD + MOD) % MOD * BASE + b.charAt(i)) % MOD;
            table.computeIfAbsent(h, x -> new ArrayList<>()).add(i - k + 1);
        }

        int count = 0;
        h = 0;
        for (int i = 0; i < k; i++) h = (h * BASE + a.charAt(i)) % MOD;
        for (int i = 0; ; i++) {                       // window a[i..i+k-1]
            List<Integer> cand = table.get(h);
            if (cand != null) {
                for (int s : cand) {
                    if (sameLetters(a, i, b, s, k)) { count++; break; }   // compare: equal hash is no proof
                }
            }
            if (i + k >= n) break;
            h = ((h - a.charAt(i) * pow % MOD + MOD) % MOD * BASE + a.charAt(i + k)) % MOD;
        }
        return count;
    }

    private static boolean sameLetters(String a, int i, String b, int j, int k) {
        for (int t = 0; t < k; t++) if (a.charAt(i + t) != b.charAt(j + t)) return false;
        return true;
    }
}
