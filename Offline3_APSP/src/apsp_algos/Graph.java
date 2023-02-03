package apsp_algos;

import util.Point;

import java.util.ArrayList;

public class Graph {
    double[][] adjMat;
    int nNodes;

    // results
    double[][] L_MatrixMultiplication;

    double[][] D_FloydWarshal;
    int[][] predecessorFloydWarshal;

    // Constructors
    public Graph(int nNodes) {
        createAdjMatrix(nNodes);
    }

    public Graph(int nNodes, Point[] points) {
        createAdjMatrix(nNodes);
        buildConnection(points);
    }

    public Graph(int nNodes, ArrayList<Point> points) {
        createAdjMatrix(nNodes);
        buildConnection(points);
    }

    // Graph construction Helpers
    private void createAdjMatrix(int nNodes) {
        this.nNodes = nNodes;
        adjMat = new double[nNodes][];

        for (int i = 0; i < nNodes; i++) {
            adjMat[i] = new double[nNodes];
        }

        for (int i = 0; i < nNodes; i++) {
            for (int j = 0; j < nNodes; j++) {
                if (i == j) {
                    adjMat[i][j] = 0.0;
                } else {
                    adjMat[i][j] = Double.MAX_VALUE;
                }
            }
        }
    }

    public void addEdge(int sourceIndex, int destIndex, double weight) {
        adjMat[sourceIndex][destIndex] = weight;
    }

    // Same, one for array, one for arrayList
    private void buildConnection(Point[] points) {
        for (Point p : points) {
            addEdge(p.x, p.y, p.weight);
//            If undirected, Uncomment
//            addEdge(p.y,p.x,p.weight);
        }
    }

    private void buildConnection(ArrayList<Point> points) {
        for (Point p : points) {
            addEdge(p.x, p.y, p.weight);
//            If undirected, Uncomment
//            addEdge(p.y,p.x,p.weight);
        }
    }

    private void removeVertex(int index) {
        for(int i=0;i<nNodes;i++) {
            adjMat[index][i] = Double.MAX_VALUE;
        }

        for(int i=0;i<nNodes;i++) {
            adjMat[i][index] = Double.MAX_VALUE;
        }

    }

    //Algorithm helpers
    private double[][] get2DDoubleArray(int n) {
        double[][] a = new double[n][];
        for (int i = 0; i < n; i++) {
            a[i] = new double[n];
        }
        return a;
    }

    private int[][] get2DIntArray(int n) {
        int[][] a = new int[n][];
        for (int i = 0; i < n; i++) {
            a[i] = new int[n];
        }
        return a;
    }

    private int[][] computePredecessorMatrix() {
        int[][] p = get2DIntArray(nNodes);

        for (int i = 0; i < nNodes; i++) {
            for (int j = 0; j < nNodes; j++) {
                if (Double.compare(adjMat[i][j], Double.MAX_VALUE) == 0 || (i == j)) {
                    p[i][j] = -1;
                } else {
                    p[i][j] = i;
                }
            }
        }
        return p;
    }


    // Matrix Multiplication
    private double[][] extendShortestPath(double[][] L, double[][] W) {
        int n = this.nNodes;
        // l_new -> L'
        double[][] l_new = get2DDoubleArray(n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                l_new[i][j] = Double.MAX_VALUE;         // l' = inf
                for (int k = 0; k < n; k++) {
                    l_new[i][j] = Double.min(l_new[i][j], L[i][k] + W[k][j]);
                }
            }
        }

        return l_new;
    }

    private double[][] apsp_matrixMultiplication() {

        double[][] L = get2DDoubleArray(nNodes);
        // L1 = W
        for (int i = 0; i < nNodes; i++) {
            System.arraycopy(adjMat[i], 0, L[i], 0, nNodes);
        }

        for (int i = 0; i < nNodes - 1; i++) {
            L = extendShortestPath(L, adjMat);
        }

        return L_MatrixMultiplication = L;
    }

    private double[][] apsp_matrixMultiplication_Faster() {

        double[][] L = get2DDoubleArray(nNodes);
        // L1 = W
        for (int i = 0; i < nNodes; i++) {
            System.arraycopy(adjMat[i], 0, L[i], 0, nNodes);
        }

        for (int i = 1; i < nNodes; i *= 2) {
            L = extendShortestPath(L, L);
        }

        return L_MatrixMultiplication = L;
    }

    public double[][] getAPSPMatrixMultiplication() {
        return apsp_matrixMultiplication();
    }

    public double[][] getAPSPMatrixMultiplication_Faster() {
        return apsp_matrixMultiplication_Faster();
    }


    // Floyd Warshal
    private void apspFloydWarshalHelper() {
        int n = this.nNodes;

        double[][] d = get2DDoubleArray(n);
        for (int i = 0; i < n; i++) {
            System.arraycopy(adjMat[i], 0, d[i], 0, n);
        }
        int[][] p = computePredecessorMatrix();

        for (int k = 0; k < n; k++) {
            int[][] p_new = get2DIntArray(n);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {

                    if (Double.compare(d[i][j], d[i][k] + d[k][j]) > 0) {
                        d[i][j] = d[i][k] + d[k][j];
                        p_new[i][j] = p[k][j];
                    } else {
                        p_new[i][j] = p[i][j];
                    }
                }
            }
            p = p_new;
        }

        D_FloydWarshal = d;
        predecessorFloydWarshal = p;
    }

    public double[][] getAPSPFloydWarshal() {
        apspFloydWarshalHelper();

//        for(int i=0;i<nNodes;i++) {
//            for(int j=0;j<nNodes;j++)
//                System.out.print(predecessorFloydWarshal[i][j] + " ");
//            System.out.println();
//        }

        return D_FloydWarshal;
    }

    private void printPath(int s, int d, int[][] parent) {
        if (s != d) {
            printPath(s, parent[s][d], parent);
        }
        System.out.println(d + " ");
    }

    public void printPathBetween(int sourceIndex, int destIndex) {
        if (Double.compare(D_FloydWarshal[sourceIndex][destIndex], Double.MAX_VALUE) == 0) {
            System.out.println("No path");
        } else {
            printPath(sourceIndex, destIndex, predecessorFloydWarshal);
        }
    }
}
