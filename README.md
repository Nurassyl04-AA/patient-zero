# Assignment 4. Patient Zero

Student ID: <252924>
Name: <Nurassyl>
Group: <SE _ 2532>

## Program output

    Mutations: <вставьте>
    Shared core: <вставьте>
    Marker hits: <вставьте>
    First hit: <вставьте>
    Border length: <вставьте>
    Shared words: <вставьте>
    Tests needed: <вставьте>
    Cheapest testing: <вставьте>
    Longest chain: <вставьте>

![outbreak](output/outbreak.png)

## Questions

**Mission 1.** d[i][j] is the smallest number of single-letter changes (replace, insert, delete) that turn the first i letters of A into the first j letters of B. It is the minimum of three cells: d[i-1][j-1] (+1 if the letters differ), d[i-1][j]+1 (delete) and d[i][j-1]+1 (insert). Row 0 is 0..m (insert j letters) and column 0 is 0..n (delete i letters).

**Mission 2.** A subsequence may have gaps, a substring may not, so the LCS is never smaller than the longest common substring. My Shared core is bigger than the number of equal letters at equal places because the letters only have to keep their order: after an insertion or deletion everything shifts, and the subsequence still matches the shifted letters.

**Mission 3.** pi[i] is the length of the longest border (a beginning that is also an ending) of the first i+1 letters of the marker. On a mismatch KMP does not restart: it moves k to pi[k-1], because those k-pi[k-1] letters can no longer start a match, while the first pi[k-1] letters are already known to match. Only k goes down, the text pointer i goes only forward, so each letter of the text is read once and the total number of k-moves is at most n. Border length of my marker = <число>: <число> first letters equal its last letters, so two matches can overlap by that many letters.

**Mission 4.** Different words can have the same hash (collision, because of the modulus), so equal hashes only mean "maybe equal"; I compare the letters before counting. If many windows have the same hash, every check costs O(k) and the time goes from O(n) on average towards O(n*k).

**Mission 5.** States: tested[v] = cost[v] + sum over children of min(tested[c], not[c]); not[v] = sum over children of tested[c] (every link to a child needs a tested end). I go from leaves to the root; the answer is min(tested[0], not[0]). Greedy "most links first" fails because it ignores costs and the shape below: person 1 is linked with 0, 2, 3 and costs 100, the others cost 1; greedy tests person 1 (100), the best is to test 0, 2, 3 (3). Even with equal costs a local choice can be wrong, the DP compares both states for every subtree.

## AI use

<ЧЕСТНО напишите: инструмент (Claude) и для чего: объяснение идей, помощь с реализацией/проверкой кода. Перепишите и убедитесь, что понимаете каждую строку.>

## Timing (Mission 7)

<замените на ВАШИ числа из java -cp out Bench>

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | |
| KMP | 20 000 / 40 000 / 80 000 | |
| edit distance | 500 / 1000 / 2000 / 4000 | |

Naive time doubles when the text doubles (O(n*m) in the worst case, here m is fixed), KMP stays small and about flat (O(n+m)). Edit distance grows about 4 times when the length doubles, as O(n*m) predicts.

## Complexity

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(n*m) | O(n*m) (O(min(n,m)) with two rows) | n, m lengths of the strains |
| LCS | O(n*m) | O(n*m) (two rows if only the length is needed) | same |
| Prefix function and KMP | O(m) + O(n) = O(n+m) | O(m) | pi stored; amortized: k goes down at most as many times as it went up |
| Rabin-Karp (shared words) | O(n+m) average, O(n*k) worst | O(m) (hashes of windows of B) | window k=12, hash update O(1) |
| Tree DP | O(v) | O(v) | every person visited once, BFS order |
