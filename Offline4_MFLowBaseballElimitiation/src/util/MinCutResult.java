package util;

import java.util.ArrayList;

public class MinCutResult {
    public double maxFlow;
    public ArrayList<Integer> cutIndexes;

    public MinCutResult(double maxFlow, ArrayList<Integer> cutIndexes) {
        this.maxFlow = maxFlow;
        this.cutIndexes = cutIndexes;
    }
}
