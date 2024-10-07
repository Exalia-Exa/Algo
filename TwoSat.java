

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TwoSat {

    public void openGraph(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            int compteur = 0;
            Graph<String> graph = null;
            while (line != null) {
                if (compteur == 0) {
                    graph = createGraph(line);
                }
                else{
                    addArcGraph(graph, line);
                }
                compteur++;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Graph<String> createGraph(String line){
        char c = line.charAt(0);
        int value = Character.getNumericValue(c);
        int result = value * 2;
        Graph<String> graph = new Graph<>(result);
        return graph;
    }

    public void addArcGraph(Graph<String> graph, String line) {
        int value1 = 0;
        int value2 = 0;
        int value1bis = 0;
        int value2bis = 0;
        int nextcharAt= 0;
        
        if(line.charAt(nextcharAt)== '-'){
            nextcharAt ++;
            
            value1 = transformValueNeg(line.charAt(nextcharAt));
            value1bis = transformValuePos(line.charAt(nextcharAt));
            nextcharAt+=2;
        }
        else{
            value1 = transformValuePos(line.charAt(nextcharAt));
            value1bis = transformValueNeg(line.charAt(nextcharAt));
            nextcharAt+=2;
        }
        if(line.charAt(nextcharAt)== '-'){
            nextcharAt ++;
            value2 = transformValueNeg(line.charAt(nextcharAt));
            value2bis = transformValuePos(line.charAt(nextcharAt));
        }
        else{
            value2 = transformValuePos(line.charAt(nextcharAt));
            value2bis = transformValueNeg(line.charAt(nextcharAt));
        }
        
        graph.addArc(value1bis,value2 , "A");
        graph.addArc(value2bis,value1 , "A");
        return;
    }
    

    public int transformValuePos(char c){
        int value = Character.getNumericValue(c);
        int result = 0;
        result = 2 * (value - 1);
        
        return result;
    }
    
    public int transformValueNeg(char c){
        int value = Character.getNumericValue(c);
        int result = 0;
        result = - (2 * -value) - 1;
        
        return result;
    }

    public static void main(String[] args) {
        TwoSat twoSat = new TwoSat();
        twoSat.openGraph("formule-2-sat.txt");
    }
}

