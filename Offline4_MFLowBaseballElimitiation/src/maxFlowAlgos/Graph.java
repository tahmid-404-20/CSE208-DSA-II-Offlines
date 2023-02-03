package maxFlowAlgos;

import util.*;

import java.util.ArrayList;
import java.util.LinkedList;

class Node {
    public int nodeNo;
    public int parentIndex;
    boolean visited;

    public Node(int nodeNo) {
        this.nodeNo = nodeNo;
        reset();
    }

    public void reset() {
        this.parentIndex = -1;
        this.visited = false;
    }
}

public class Graph {
    double[][] adjMat;
    int nNodes;

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

//        for (int i = 0; i < nNodes; i++) {
//            for (int j = 0; j < nNodes; j++) {
//                if (i == j) {
//                    adjMat[i][j] = 0.0;
//                } else {
//                    adjMat[i][j] = Double.MAX_VALUE;
//                }
//            }
//        }
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

//    private void removeVertex(int index) {
//        for(int i=0;i<nNodes;i++) {
//            adjMat[index][i] = Double.MAX_VALUE;
//        }
//
//        for(int i=0;i<nNodes;i++) {
//            adjMat[i][index] = Double.MAX_VALUE;
//        }
//    }

    //Algorithm helpers
    private double[][] get2DDoubleArray(int n) {
        double[][] a = new double[n][];
        for (int i = 0; i < n; i++) {
            a[i] = new double[n];
        }
        return a;
    }

    // Using array
    private void BFS_visit(int sourceIndex, boolean[] visited, int[] parent, double[][] adjMat) {
        for(int i=0;i<nNodes;i++) {
            visited[i] = false;
            parent[i] = -1;
        }

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(sourceIndex);

        visited[sourceIndex] = true;

        while(!queue.isEmpty()) {
            int u = queue.poll();

            for(int v=0;v<nNodes;v++) {
                if(Double.compare(adjMat[u][v],0.0) != 0 && !visited[v]) {
                    visited[v] = true;
                    queue.add(v);
                    parent[v] = u;
                }
            }
        }
    }

    private void BFS_visit(int sourceIndex, Node[] nodes, double[][] adjMat) {

        for(int i=0;i<nNodes;i++) {
            nodes[i].reset();
        }

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(sourceIndex);

        nodes[sourceIndex].visited = true;

        while(!queue.isEmpty()) {
            int u = queue.poll();

            for(int v=0;v<nNodes;v++) {
                if(Double.compare(adjMat[u][v],0.0) != 0 && !nodes[v].visited) {
                    nodes[v].visited = true;
                    queue.add(v);
                    nodes[v].parentIndex = u;
                }
            }
        }
    }

    private boolean isPathBetween(int sourceIndex, int destIndex, Node[] nodes, double[][] adjMat) {
        BFS_visit(sourceIndex,nodes,adjMat);
        return nodes[destIndex].visited;
    }

    public double getMaxFlowEdmondKarp(int sourceIndex, int destIndex) {
        double maxFlow = 0.0;

        // Residual Matrix
        double[][] Cf = get2DDoubleArray(nNodes);
        for(int i=0;i<nNodes;i++) {
            System.arraycopy(adjMat[i], 0, Cf[i], 0, nNodes);
        }

        // For BFS
        Node[] nodes = new Node[nNodes];
        for(int i=0;i<nNodes;i++) {
            nodes[i] = new Node(i);
        }

        while (isPathBetween(sourceIndex,destIndex,nodes,Cf)) {

            double min_residualCapacity = Double.MAX_VALUE;
            for(int v=destIndex; v!=sourceIndex; v = nodes[v].parentIndex) {
                int u = nodes[v].parentIndex;
                min_residualCapacity = Double.min(min_residualCapacity,Cf[u][v]);
            }

            for(int v=destIndex; v!=sourceIndex; v = nodes[v].parentIndex) {
                int u = nodes[v].parentIndex;
                Cf[u][v] = Cf[u][v] - min_residualCapacity;
                Cf[v][u] = Cf[v][u] + min_residualCapacity;
            }

            maxFlow += min_residualCapacity;
        }

        return maxFlow;
    }

    public MinCutResult getMaxFlowMinCutEdmondKarp(int sourceIndex, int destIndex) {
        double maxFlow = 0.0;

        // Residual Matrix
        double[][] Cf = get2DDoubleArray(nNodes);
        for(int i=0;i<nNodes;i++) {
            System.arraycopy(adjMat[i], 0, Cf[i], 0, nNodes);
        }

        // For BFS
        Node[] nodes = new Node[nNodes];
        for(int i=0;i<nNodes;i++) {
            nodes[i] = new Node(i);
        }

        while (isPathBetween(sourceIndex,destIndex,nodes,Cf)) {

            double min_residualCapacity = Double.MAX_VALUE;
            for(int v=destIndex; v!=sourceIndex; v = nodes[v].parentIndex) {
                int u = nodes[v].parentIndex;
                min_residualCapacity = Double.min(min_residualCapacity,Cf[u][v]);
            }

            for(int v=destIndex; v!=sourceIndex; v = nodes[v].parentIndex) {
                int u = nodes[v].parentIndex;
                Cf[u][v] = Cf[u][v] - min_residualCapacity;
                Cf[v][u] = Cf[v][u] + min_residualCapacity;
            }

            maxFlow += min_residualCapacity;
        }

        ArrayList<Integer> minCutIndexSetA = new ArrayList<>();
        for(int i=0;i<nNodes;i++) {
            if(nodes[i].visited) {
                minCutIndexSetA.add(i);
            }
        }

        return new MinCutResult(maxFlow,minCutIndexSetA);
    }
}
