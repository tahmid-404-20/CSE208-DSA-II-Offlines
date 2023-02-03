package shortest_path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import util.Point;

class AdjNode {
    int nodeNo;
    double weight;

    public AdjNode(int dest, double weight) {
        this.nodeNo = dest;
        this.weight = weight;
    }
}

// will be used for kruskal
class Edge implements Comparable<Edge> {
    int source;
    int dest;
    double weight;

    public Edge(int source, int dest, double weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight); // change the sign if descending
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
    ArrayList<Edge> edgesForBellman;
    int nNodes;
    double[] lengthFromSource;

    // Constructor
    public Graph(int nNodes) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 0

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }
    }
    public Graph(int nNodes, Point[] points) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 0

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }

        // Make the graph
        buildConnection(points);

        // Necessary for applying Bellman Ford's algo
        edgesForBellman = new ArrayList<>();
        for (Point p : points) {
            edgesForBellman.add(new Edge(p.x, p.y, p.weight));
        }
    }

    public Graph(int nNodes, ArrayList<Point> points) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 0

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }

        // Make the graph
        buildConnection(points);

        // Necessary for applying Bellman Ford algo
        edgesForBellman = new ArrayList<>();
        for (Point p : points) {
            edgesForBellman.add(new Edge(p.x, p.y, p.weight));
        }
    }

    // used to generate a reversed graph
    private void addEdge(Node[] nodes, int sourceIndex, int destinationIndex, double weight) {
        nodes[sourceIndex].Adj.add(new AdjNode(destinationIndex,weight));
    }

    private Node[] reversedGraph() {
        Node[] reversedNodes = new Node[nNodes];

        for (int i = 1; i <= nNodes; i++) {
            reversedNodes[i] = new Node(i);
        }

        for (int i = 0; i < nNodes; i++) {
            for (AdjNode node : this.nodes[i].Adj) {
                addEdge(reversedNodes, node.nodeNo, i, node.weight);
            }
        }

        return reversedNodes;
    }

    private ArrayList<Edge> reverseEdges() {
        ArrayList<Edge> reversedEdges = new ArrayList<>();

        for(Edge edge:edgesForBellman) {
            reversedEdges.add(new Edge(edge.dest, edge.source, edge.weight));
        }

        return reversedEdges;
    }

    public void addEdge(int sourceIndex, int destinationIndex, double edgeWeight) {
        nodes[sourceIndex].Adj.add(new AdjNode(destinationIndex, edgeWeight));
    }

    // Same, using array and arrayList
    private void buildConnection(Point[] points) {
        for (Point p : points) {
            addEdge(p.x, p.y, p.weight);
//            Undirected graph
//            addEdge(p.y, p.x, p.weight);
        }
    }
    private void buildConnection(ArrayList<Point> points) {
        for (Point p : points) {
            addEdge(p.x, p.y, p.weight);
//            Undirected graph
//            addEdge(p.y, p.x, p.weight);
        }
    }

    // Reset nodes
    private void reset() {
        for (int i = 0; i < nNodes; i++)
            nodes[i].reset();
    }

    // Dijkstra
    private void SP_Dijkstra_Helper(int sourceIndex, boolean[] inTree) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        nodes[sourceIndex].key = 0;
        nodes[sourceIndex].isReachable = true;
        pq.add(nodes[sourceIndex]);

        while (!pq.isEmpty()) {
            Node u = pq.poll();

            if (inTree[u.nodeNo])         // already taken in Tree
                continue;

            inTree[u.nodeNo] = true;

            for (AdjNode v : u.Adj) {       // deg(v), for All v, sum(deg(v)) = O(E)
                int indexV = v.nodeNo;
                if (!inTree[indexV] && Double.compare(nodes[indexV].key, nodes[u.nodeNo].key + v.weight) > 0) {
                    nodes[indexV].key = v.weight + nodes[u.nodeNo].key;
                    nodes[indexV].parent = u;
                    nodes[indexV].isReachable = true;
                    pq.add(nodes[indexV]); // O(logn)
                }
            }
        }
    }

    private boolean isPathBetween(int sourceIndex, int destIndex) {
        reset();
        for (Node node : nodes) {
            node.key = Double.MAX_VALUE;
        }

        boolean[] inTree = new boolean[nNodes];
        nodes[sourceIndex].key = 0.0;
        SP_Dijkstra_Helper(sourceIndex, inTree);
        lengthFromSource = new double[nNodes];
        for(int i=0;i<nNodes;i++) {
            lengthFromSource[i] = nodes[i].key;
        }

        return nodes[destIndex].isReachable;
    }

    private void printPath(Node source, Node dest)
    {
        if (source.nodeNo != dest.nodeNo) {
            printPath(source, dest.parent);
        }
        System.out.print(dest.nodeNo + " -> ");
    }

    public void shortestPathDijkstra(int sourceIndex, int destIndex) {
        if(isPathBetween(sourceIndex,destIndex)) {
            System.out.println("Shortest path cost: " + nodes[destIndex].key);
            printPath(nodes[sourceIndex],nodes[destIndex]);
            System.out.println("\b\b\b   ");
        } else {
            System.out.println("No path");
        }
    }

    // Bellman Ford
    private void Relax(Edge edge) {
        Node u = nodes[edge.source];
        Node v = nodes[edge.dest];

        if(Double.compare(v.key,u.key + edge.weight) > 0) {
            v.key = u.key + edge.weight;
            v.parent = u;
        }
    }

    private void Relax(Node u, Node v, Double weight) {
        if(Double.compare(v.key,u.key + weight) > 0) {
            v.key = u.key + weight;
            v.parent = u;
        }
    }

    private void printCycle(Node s, Node v) {
        if (s.nodeNo != v.nodeNo) {
            printCycle(s,v.parent);
        }
        System.out.println(v.nodeNo);
    }

    private boolean bellmanFord_Helper(int sourceIndex) {
        reset();
        for(Node node:nodes) {
            node.key = Double.MAX_VALUE;
        }
        nodes[sourceIndex].key = 0;

        for(int i=1; i <= nNodes-1;i++) {
            for(Edge edge: edgesForBellman) {
                Relax(edge);
            }
        }

        for(Edge edge:edgesForBellman) {
            Node u = nodes[edge.source];
            Node v = nodes[edge.dest];

            if(Double.compare(v.key,u.key + edge.weight) > 0) {
                printCycle(v,u);
                System.out.println(v.nodeNo);
                return  false;
            }
        }
        return true;
    }

    private boolean isShortestPathBetween_Bellman(Node source, Node dest) {
        if(source.nodeNo == dest.nodeNo) {
            return true;
        }
        if(dest.parent == null) {
            return false;
        }
        return isShortestPathBetween_Bellman(source,dest.parent);
    }

    public void shortestPathBellman(int sourceIndex, int destIndex) {
        if(bellmanFord_Helper(sourceIndex))
        {
            System.out.println("The graph does not contain a negative cycle");
            if(isShortestPathBetween_Bellman(nodes[sourceIndex], nodes[destIndex])) {
                System.out.println("Shortest path cost: " + nodes[destIndex].key);
                printPath(nodes[sourceIndex], nodes[destIndex]);
                System.out.println("\b\b\b   ");
            } else {
                System.out.println("No path between " + sourceIndex + " and " + destIndex);
            }
        } else {
            System.out.println("The graph contains a negative cycle");
        }
    }
}
