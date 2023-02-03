package HeapCodes;

import util.Point;

import java.util.ArrayList;
import java.util.List;

class AdjNode {
    int nodeNo;
    double weight;

    public AdjNode(int dest, double weight) {
        this.nodeNo = dest;
        this.weight = weight;
    }
}

class Node implements Comparable<Node> {
    public int nodeNo;
    public Node parent;
    public double key;
    public boolean isReachable;

    public List<AdjNode> Adj;

    public Node(int nodeNo) {
        this.nodeNo = nodeNo;
        reset();
        Adj = new ArrayList<>();
    }

    public void reset() {
        this.parent = null;
        this.isReachable = false;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.key, o.key);
    }
}

public class Graph {
    Node[] nodes;
    int nNodes;
    long timeFibonacciHeap;
    long timeBinaryHeap;
    double shortestPathcost;
    int shortestPathLength;

    // Constructor
    public Graph(int nNodes, ArrayList<Point> points) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 0

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }

        // Make the graph
        buildConnection(points);
    }

    // Getters


    public long getTimeFibonacciHeap() {
        return timeFibonacciHeap;
    }

    public long getTimeBinaryHeap() {
        return timeBinaryHeap;
    }

    public double getShortestPathcost() {
        return shortestPathcost;
    }

    public int getShortestPathLength() {
        return shortestPathLength;
    }

    private void addEdge(int sourceIndex, int destinationIndex, double edgeWeight, double[][] adjMat) {
        adjMat[sourceIndex][destinationIndex] = Double.min(adjMat[sourceIndex][destinationIndex], edgeWeight);
    }

    private void addEdgeList(int sourceIndex, int destinationIndex, double edgeWeight) {
        nodes[sourceIndex].Adj.add(new AdjNode(destinationIndex, edgeWeight));
    }

    private void buildConnection(ArrayList<Point> points) {
        for (Point p : points) {
            addEdgeList(p.x, p.y, p.weight);
//            Undirected graph
            addEdgeList(p.y, p.x, p.weight);
        }

//        The following code is helpful if multi-edge is present, but may run into out of memory error -> O(n^2)
        /*
        // Takes a temporary adjMat to build adjList
        //Initialize AdjMat
        double[][] adjMat = new double[nNodes][];

        for(int i=0;i<nNodes;i++) {
            adjMat[i] = new double[nNodes];
            for(int j=0;j<nNodes;j++) {
                adjMat[i][j] = Double.MAX_VALUE;
            }
        }

        for (Point p : points) {
            addEdge(p.x, p.y, p.weight,adjMat);
//            Undirected graph
//            addEdge(p.y, p.x, p.weight,adjMat);
        }

        //Build AdjList
        for(int i=0;i<nNodes;i++) {
            for(int j=0;j<nNodes;j++) {
                if(Double.compare(adjMat[i][j],Double.MAX_VALUE) < 0) {
                    nodes[i].Adj.add(new AdjNode(j, adjMat[i][j]));
                }
            }
        }
        */
    }

    // Reset nodes
    private void reset() {
        for (int i = 0; i < nNodes; i++)
            nodes[i].reset();
    }

    // Dijkstra
    private void SP_Dijkstra_Helper_BinaryHeap(int sourceIndex, boolean[] inTree) {
        // Initialize the vertices
        for (int i = 0; i < nNodes; i++) {
            nodes[i].key = Double.MAX_VALUE;
        }
        nodes[sourceIndex].key = 0.0;
        nodes[sourceIndex].isReachable = true;

        // heap initialization
        BinaryHeap heap = new BinaryHeap(nNodes);
        BinNode[] elements = new BinNode[nNodes];
        for (int i = 0; i < nNodes; i++) {
            elements[i] = new BinNode(nodes[i].nodeNo, nodes[i].key);
        }
        heap.buildHeap(elements); // O(n)

        while (!heap.isEmpty()) {
            BinNode u = heap.extractMin();
            inTree[u.nodeNo] = true;

            for (AdjNode v : nodes[u.nodeNo].Adj) {       // deg(v), for All v, sum(deg(v)) = O(E)
                int indexV = v.nodeNo;
                if (!inTree[indexV] && Double.compare(nodes[indexV].key, nodes[u.nodeNo].key + v.weight) > 0) {
                    nodes[indexV].key = v.weight + nodes[u.nodeNo].key;
                    nodes[indexV].parent = nodes[u.nodeNo];
                    nodes[indexV].isReachable = true;
                    heap.decreaseKey(indexV, nodes[indexV].key); // O(logn)
                }
            }
        }
    }

    private void SP_Dijkstra_Helper_FH(int sourceIndex, boolean[] inTree) {
        // initialize vertices
        for (int i = 0; i < nNodes; i++) {
            nodes[i].key = Double.MAX_VALUE;
        }
        nodes[sourceIndex].key = 0.0;
        nodes[sourceIndex].isReachable = true;

        // initialize heap
        FibonacciHeap heap = new FibonacciHeap(nNodes);
        for (int i = 0; i < nNodes; i++) {
            heap.insertNode(i, nodes[i].key);
        }

        while (!heap.isEmpty()) {
            HeapNode u = heap.ExtractMin();
            inTree[u.nodeNo] = true;

            for (AdjNode v : nodes[u.nodeNo].Adj) {       // deg(v), for All v, sum(deg(v)) = O(E)
                int indexV = v.nodeNo;
                if (!inTree[indexV] && Double.compare(nodes[indexV].key, nodes[u.nodeNo].key + v.weight) > 0) {
                    nodes[indexV].key = v.weight + nodes[u.nodeNo].key;
                    nodes[indexV].parent = nodes[u.nodeNo];
                    nodes[indexV].isReachable = true;
                    heap.decreaseKey(indexV, nodes[indexV].key); // O(1) ammortized
                }
            }
        }
    }

    private boolean isPathBetween_FH(int sourceIndex, int destIndex) {
        reset();
        boolean[] inTree = new boolean[nNodes];
        long startTime = System.nanoTime();
        SP_Dijkstra_Helper_FH(sourceIndex, inTree);
        timeFibonacciHeap = (long) ((System.nanoTime() - startTime) / 1e3);
        return nodes[destIndex].isReachable;
    }

    private boolean isPathBetween_BH(int sourceIndex, int destIndex) {
        reset();
        boolean[] inTree = new boolean[nNodes];
        long startTime = System.nanoTime();
        SP_Dijkstra_Helper_BinaryHeap(sourceIndex, inTree);
        timeBinaryHeap = (long) ((System.nanoTime() - startTime) / 1e3);
        return nodes[destIndex].isReachable;
    }

    private void printPath(Node source, Node dest) {
        if (source.nodeNo != dest.nodeNo) {
            printPath(source, dest.parent);
        }
        System.out.print(dest.nodeNo + " -> ");
    }

    private int computeLength_rec(Node source, Node dest) {
        if (source.nodeNo != dest.nodeNo) {
            return 1 + computeLength_rec(source, dest.parent);
        }
        return 0;
    }

    public void shortestPathDijkstra_FiboHeap(int sourceIndex, int destIndex) {
        if (isPathBetween_FH(sourceIndex, destIndex)) {
            shortestPathcost = nodes[destIndex].key;
            shortestPathLength = 0;
            shortestPathLength = computeLength_rec(nodes[sourceIndex], nodes[destIndex]);
        } else {
            timeFibonacciHeap = -1;
            shortestPathcost = Double.MAX_VALUE;
            shortestPathLength = -1;
        }
    }

    public void shortestPathDijkstra_BinHeap(int sourceIndex, int destIndex) {
        if (isPathBetween_BH(sourceIndex, destIndex)) {
            shortestPathcost = nodes[destIndex].key;
            shortestPathLength = 0;
            shortestPathLength = computeLength_rec(nodes[sourceIndex], nodes[destIndex]);
        } else {
            timeBinaryHeap = -1;
            shortestPathcost = Double.MAX_VALUE;
            shortestPathLength = -1;
        }
    }
}

