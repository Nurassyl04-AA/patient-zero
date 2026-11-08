/**
 * Missions 1 and 2. Compare two strains with dynamic programming.
 */
public class Strains {

    /**
     * Mission 1. Edit distance (Levenshtein). d[i][j] = the smallest number of changes that turn
     * the first i letters of a into the first j letters of b.
     */
    public static int editDistance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) d[i][0] = i;      // delete all i letters
        for (int j = 0; j <= m; j++) d[0][j] = j;      // insert all j letters
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int sub = d[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                int del = d[i - 1][j] + 1;
                int ins = d[i][j - 1] + 1;
                d[i][j] = Math.min(sub, Math.min(del, ins));
            }
        }
        return d[n][m];
    }

    /**
     * Mission 2. Longest common subsequence. L[i][j] = LCS length of the first i letters of a
     * and the first j letters of b.
     */
    public static int lcsLength(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] L = new int[n + 1][m + 1];             // row 0 and column 0 stay 0
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) L[i][j] = L[i - 1][j - 1] + 1;
                else L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
            }
        }
        return L[n][m];
    }
}
