import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TwoSat {
    private Map<Integer, List<Integer>> graph = new HashMap<>();
    private Map<Integer, List<Integer>> transposedGraph = new HashMap<>();
    private Stack<Integer> finishStack = new Stack<>();
    private boolean[] visited;
    private List<List<Integer>> sccs = new ArrayList<>();
    private int[] sccId;

    public TwoSat(int size) {
        visited = new boolean[size];
        sccId = new int[size];
    }

    public void addArc(int from, int to) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        transposedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
    }

    private void dfs(int node) {
        visited[node] = true;
        for (int neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited[neighbor]) {
                dfs(neighbor);
            }
        }
        finishStack.push(node);
    }

    private void dfsTranspose(int node, List<Integer> scc, int id) {
        visited[node] = true;
        scc.add(node);
        sccId[node] = id;
        for (int neighbor : transposedGraph.getOrDefault(node, Collections.emptyList())) {
            if (!visited[neighbor]) {
                dfsTranspose(neighbor, scc, id);
            }
        }
    }

    public void findSCCs() {
        Arrays.fill(visited, false);
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i] && graph.containsKey(i)) {
                dfs(i);
            }
        }

        Arrays.fill(visited, false);
        int id = 0;
        while (!finishStack.isEmpty()) {
            int node = finishStack.pop();
            if (!visited[node]) {
                List<Integer> scc = new ArrayList<>();
                dfsTranspose(node, scc, id++);
                sccs.add(scc);
            }
        }
    }

    public boolean isSatisfiable() {
        findSCCs();
        for (int i = 0; i < sccId.length; i += 2) {
            if (sccId[i] == sccId[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public void printSCCs() {
        System.out.print("[");
        for (List<Integer> scc : sccs) {
            System.out.print(scc);
        }
        System.out.println("]");
    }

    public void openGraph(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            int compteur = 0;
            while (line != null) {
                if (compteur == 0) {
                    createGraph(line);
                } else {
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

    public void createGraph(String line) {
        char c = line.charAt(0);
        int value = Character.getNumericValue(c);
        int result = value * 2;
        visited = new boolean[result];
        sccId = new int[result];
    }

    public void addArcGraph(String line) {
        int value1 = 0;
        int value2 = 0;
        int value1bis = 0;
        int value2bis = 0;
        int nextcharAt = 0;

        if (line.charAt(nextcharAt) == '-') {
            nextcharAt++;
            value1 = transformValueNeg(line.charAt(nextcharAt));
            value1bis = transformValuePos(line.charAt(nextcharAt));
            nextcharAt += 2;
        } else {
            value1 = transformValuePos(line.charAt(nextcharAt));
            value1bis = transformValueNeg(line.charAt(nextcharAt));
            nextcharAt += 2;
        }
        if (line.charAt(nextcharAt) == '-') {
            nextcharAt++;
            value2 = transformValueNeg(line.charAt(nextcharAt));
            value2bis = transformValuePos(line.charAt(nextcharAt));
        } else {
            value2 = transformValuePos(line.charAt(nextcharAt));
            value2bis = transformValueNeg(line.charAt(nextcharAt));
        }

        addArc(value1bis, value2);
        addArc(value2bis, value1);
    }

    public int transformValuePos(char c) {
        int value = Character.getNumericValue(c);
        int result = 0;
        result = 2 * (value - 1);
        return result;
    }

    public int transformValueNeg(char c) {
        int value = Character.getNumericValue(c);
        int result = 0;
        result = -(2 * -value) - 1;
        return result;
    }

    public static void main(String[] args) {
        TwoSat twoSat = new TwoSat(0);
        twoSat.openGraph("formule-sat.txt");
        // twoSat.openGraph("formule-unsat.txt");
        

        // twoSat.addArc(0, 1);
        // twoSat.addArc(1, 2);
        // twoSat.addArc(2, 0);
        // twoSat.addArc(1, 3);
        // twoSat.addArc(3, 4);
        // twoSat.addArc(4, 5);
        // twoSat.addArc(5, 3);
        // twoSat.addArc(2, 6);
        // twoSat.addArc(6, 7);
        // twoSat.addArc(7, 4);
        // twoSat.addArc(2, 4);

        System.out.println(twoSat.isSatisfiable() ? "Satisfiable" : "Insatisfiable");
        twoSat.printSCCs();
    }
}