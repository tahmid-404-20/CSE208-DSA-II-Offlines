package MST_Algos;

import util.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class LabOnline {
    public static void main(String[] args) {

        // Redirecting input
        Scanner scr = new Scanner(System.in);

        int nNodes = scr.nextInt();
        int nEdges = scr.nextInt();

        Point[] points = new Point[nEdges];
        for (int i = 0; i < nEdges; i++) {
            int u = scr.nextInt();
            int v = scr.nextInt();
            double w = scr.nextDouble();

            points[i] = new Point(u, v, w);
        }

        Graph graph = new Graph(nNodes, points);

        List<Edge> edges = graph.mst_byDeletion();
        double mst_cost = 0.0;
        for(Edge edge:edges) {
            System.out.println("{" + edge.source + "," + edge.dest + "}");
            mst_cost += edge.weight;
        }

        System.out.println("MST cost: " + mst_cost);

    }
}
