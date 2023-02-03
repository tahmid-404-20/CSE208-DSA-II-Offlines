package MST_Algos;

import util.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class DriverMain {
    private static final String INPUT_FILE_NAME = "mst.in";

    public static void main(String[] args) {

        // Redirecting input
        try {
            System.setIn(new FileInputStream(new File(INPUT_FILE_NAME)));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("Error redirecting input");
            exit(1);
        }
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

        // Kruskal
        List<Edge> takenEdges = graph.MST_Kruskal();
        double mstCostKruskal = 0.0;
        for (Edge edge : takenEdges) {
            mstCostKruskal += edge.weight;
        }

        // Prim
        List<Edge> takenEdgesPrim = graph.MST_Prim();
        double mstCostPrim = 0.0;
        for (Edge edge : takenEdgesPrim) {
            mstCostPrim += edge.weight;
        }

        // Printing
        System.out.println("Cost of the minimum spanning tree : " + mstCostPrim);

        //Prim's
        System.out.print("List of edges selected by Prim’s:{");
        for (Edge edge : takenEdgesPrim) {
            System.out.print("(" + edge.source + "," + edge.dest + "),");
        }
        System.out.println("\b}");

        //Kruskal's
        System.out.print("List of edges selected by Kruskal’s:{");
        for (Edge edge : takenEdges) {
            System.out.print("(" + edge.source + "," + edge.dest + "),");
        }
        System.out.println("\b}");
    }
}
