package apsp_algos;

import util.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.exit;

public class DriverMain {
    private static final String INPUT_FILE_NAME = "input.txt";
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

            points[i] = new Point(u-1, v-1, w);
        }

        Graph graph = new Graph(nNodes,points);

//        double[][] apsp_mat = graph.getAPSPMatrixMultiplication();
//        double[][] apsp_mat = graph.getAPSPMatrixMultiplication_Faster();
        double[][] apsp_mat = graph.getAPSPFloydWarshal();

        System.out.println("Shortest distance matrix");
        for(int i=0;i<nNodes;i++) {
            for(int j=0;j<nNodes;j++) {
                if(Double.compare(apsp_mat[i][j],Double.MAX_VALUE) == 0) {
                    System.out.print("INF" + " ");
                } else {
                    System.out.print((int)apsp_mat[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
