package HeapCodes;
import util.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class DriverMain {
    private static final String INPUT_FILE1_NAME = "input1.txt";
    private static final String INPUT_FILE2_NAME = "input2.txt";

    public static void main(String[] args) {
        // Redirecting input
        try {
            System.setIn(new FileInputStream(new File(INPUT_FILE1_NAME)));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("Error redirecting input1");
            exit(1);
        }
        Scanner scr = new Scanner(System.in);

        int nNodes = scr.nextInt();
        int nEdges = scr.nextInt();

        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < nEdges; i++) {
            int u = scr.nextInt();
            int v = scr.nextInt();
            double w = scr.nextDouble();

            points.add(new Point(u, v, w));
        }
        Graph graph = new Graph(nNodes, points);

        // Redirecting output
        try {
            System.setOut(new PrintStream(new File("output.txt")));
        } catch (FileNotFoundException e) {
            System.out.println("Error redirecting output");
            exit(1);
        }

        //Redirecting input2
        try {
            System.setIn(new FileInputStream(new File(INPUT_FILE2_NAME)));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("Error redirecting input2");
            exit(1);
        }
        scr = new Scanner(System.in);

        int k = scr.nextInt();
        for(int i=0;i<k;i++) {
            int sourceIndex = scr.nextInt();
            int destIndex = scr.nextInt();

            graph.shortestPathDijkstra_FiboHeap(sourceIndex,destIndex);
            int pathLength = graph.getShortestPathLength();
            double cost = graph.getShortestPathcost();
            graph.shortestPathDijkstra_BinHeap(sourceIndex,destIndex);


            long timeBin = graph.getTimeBinaryHeap();
            long timeFibo = graph.getTimeFibonacciHeap();

            System.out.println(pathLength+ " " + (int)cost + " " + timeBin + " " + timeFibo);
        }
    }
}

