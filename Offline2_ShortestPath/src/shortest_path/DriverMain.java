package shortest_path;

import util.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
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

            points[i] = new Point(u, v, w);
        }

        int sourceIndex = scr.nextInt();
        int destIndex = scr.nextInt();

        Graph graph = new Graph(nNodes, points);
//        graph.shortestPathDijkstra(sourceIndex,destIndex);
        graph.shortestPathBellman(sourceIndex,destIndex);

    }
}
