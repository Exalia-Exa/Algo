import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TwoSat {
    private Map<Integer, List<Integer>> graph = new HashMap<>();
    private Map<Integer, List<Integer>> transposedGraph = new HashMap<>();
    private Stack<Integer> finishStack = new Stack<>();
    private boolean[] visited;
    private List<List<Integer>> sccs = new ArrayList<>();

    public void openGraph(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            int compteur = 0;
            while (line != null) {
                if (compteur > 1) {
                    addArcGraph(line);
                }
                compteur++;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addArcGraph(String line) {
        String[] literals = line.split(" ");
        int value1 = Integer.parseInt(literals[0]);
        int value2 = Integer.parseInt(literals[1]);

        int value1bis = -value1;
        int value2bis = -value2;

        addArc(value1bis, value2);
        addArc(value2bis, value1);
    }

    public void addArc(int from, int to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        transposedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
    }

    private void dfs(int node) {
        visited[node + visited.length / 2] = true;
        for (int neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited[neighbor + visited.length / 2]) {
                dfs(neighbor);
            }
        }
        finishStack.push(node);
    }

    private void dfsTranspose(int node, List<Integer> scc) {
        visited[node + visited.length / 2] = true;
        scc.add(node);
        for (int neighbor : transposedGraph.getOrDefault(node, Collections.emptyList())) {
            if (!visited[neighbor + visited.length / 2]) {
                dfsTranspose(neighbor, scc);
            }
        }
    }

    public void findSCCs() {
        int n = graph.size();
        visited = new boolean[n * 2 + 1];

        for (int i = -n; i <= n; i++) {
            if (i != 0 && !visited[i + n]) {
                dfs(i);
            }
        }

        Arrays.fill(visited, false);
        while (!finishStack.isEmpty()) {
            int node = finishStack.pop();
            if (!visited[node + n]) {
                List<Integer> scc = new ArrayList<>();
                dfsTranspose(node, scc);
                sccs.add(scc);
            }
        }
    }

    public boolean isSatisfiable() {
        findSCCs();
        for (List<Integer> scc : sccs) {
            Set<Integer> literals = new HashSet<>();
            for (int node : scc) {
                if (literals.contains(-node)) {
                    return false;
                }
                literals.add(node);
            }
        }
        return true;
    }

    public static void main(String[] args) {
        TwoSat twoSat = new TwoSat();
        twoSat.openGraph("formule-2-sat.txt");
        System.out.println(twoSat.isSatisfiable() ? "Satisfiable" : "Insatisfiable");
    }
}