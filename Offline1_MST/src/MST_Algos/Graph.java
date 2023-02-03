package MST_Algos;

import util.DisjointSet;
import util.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

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

    public List<AdjNode> Adj;

    public Node(int nodeNo) {
        this.nodeNo = nodeNo;
        reset();
        Adj = new ArrayList<>();
    }

    public void reset() {
        this.parent = null;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.key, o.key);
    }
}

public class Graph {
    Node[] nodes;
    ArrayList<Edge> edgesForKruskal;
    int nNodes;

    // Constructor
    public Graph(int nNodes) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 1

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }
    }
    public Graph(int nNodes, Point[] points) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 1

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }

        // Make the graph
        buildConnection(points);

        // Necessary for applying Kruskal's algo
        edgesForKruskal = new ArrayList<>();
        for (Point p : points) {
            edgesForKruskal.add(new Edge(p.x, p.y, p.weight));
        }
    }

    public Graph(int nNodes, ArrayList<Point> points) {
        this.nNodes = nNodes;
        nodes = new Node[nNodes]; // for indexing from 1

        for (int i = 0; i < nNodes; i++) {
            nodes[i] = new Node(i);
        }

        // Make the graph
        buildConnection(points);

        // Necessary for applying Kruskal's algo
        edgesForKruskal = new ArrayList<>();
        for (Point p : points) {
            edgesForKruskal.add(new Edge(p.x, p.y, p.weight));
        }
    }

    // used to generate a reversed graph
//    private void addEdge(Node[] nodes, int sourceIndex, int destinationIndex) {
//        nodes[sourceIndex].Adj.add(destinationIndex);
//    }

    public void addEdge(int sourceIndex, int destinationIndex, double edgeWeight) {
        nodes[sourceIndex].Adj.add(new AdjNode(destinationIndex, edgeWeight));
    }

    private void removeEdge(int sourceIndex, int destinationIndex, double edgeWeight) {
        nodes[sourceIndex].Adj.removeIf(node -> node.nodeNo == destinationIndex);

        nodes[destinationIndex].Adj.removeIf(node -> node.nodeNo == sourceIndex);
    }

    // Sane, using array and arrayList
    private void buildConnection(Point[] points) {
        for (Point p : points) {
            addEdge(p.x, p.y, p.weight);
            addEdge(p.y, p.x, p.weight);
        }
    }
    private void buildConnection(ArrayList<Point> points) {
        for (Point p : points) {
            addEdge(p.x, p.y, p.weight);
            addEdge(p.y, p.x, p.weight);
        }
    }

    // Reset nodes
    private void reset() {
        for (int i = 0; i < nNodes; i++)
            nodes[i].reset();
    }

    public List<Edge> MST_Kruskal() {
        List<Edge> takenEdges = new ArrayList<>();

        DisjointSet disjointSet = new DisjointSet(nNodes);
        Collections.sort(edgesForKruskal);


        for (Edge edge : edgesForKruskal) {
            int s = disjointSet.find(edge.source);
            int d = disjointSet.find(edge.dest);

            if (s != d) {
                disjointSet.union(s, d);
                takenEdges.add(edge);
            }
        }

        return takenEdges;
    }

    private void MST_PrimHelper(int sourceIndex, boolean[] inMST, List<Edge> takenEdges) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        nodes[sourceIndex].key = sourceIndex;
        pq.add(nodes[sourceIndex]);

        while (!pq.isEmpty()) {
            Node u = pq.poll();

            if (inMST[u.nodeNo])         // already taken in MST
                continue;

            inMST[u.nodeNo] = true;

            if (u.nodeNo != sourceIndex)
                takenEdges.add(new Edge(u.parent.nodeNo, u.nodeNo, u.key));

            for (AdjNode v : u.Adj) {       // deg(v), for All v, sum(deg(v)) = O(E)
                int indexV = v.nodeNo;
                if (!inMST[indexV] && Double.compare(v.weight, nodes[indexV].key) < 0) {
                    nodes[indexV].key = v.weight;
                    nodes[indexV].parent = u;
                    pq.add(nodes[indexV]); // O(logn)
                }
            }
        }
    }

    public List<Edge> MST_Prim() {
        reset();
        for (Node node : nodes) {
            node.key = Double.MAX_VALUE;
        }

        List<Edge> takenEdges = new ArrayList<>();
        boolean[] inMST = new boolean[nNodes];

        for(Node node: nodes) {
            if(!inMST[node.nodeNo]) {
                MST_PrimHelper(node.nodeNo,inMST,takenEdges);
            }
        }

        /*
        PriorityQueue<Node> pq = new PriorityQueue<>();
        nodes[0].key = 0;
        pq.add(nodes[0]);

        while (!pq.isEmpty()) {
            Node u = pq.poll();

            if (inMST[u.nodeNo])         // already taken in MST
                continue;

            inMST[u.nodeNo] = true;

            if (u.nodeNo != 0)
                takenEdges.add(new Edge(u.parent.nodeNo, u.nodeNo, u.key));

            for (AdjNode v : u.Adj) {       // deg(v), for All v, sum(deg(v)) = O(E)
                int indexV = v.nodeNo;
                if (!inMST[indexV] && Double.compare(v.weight, nodes[indexV].key) < 0) {
                    nodes[indexV].key = v.weight;
                    nodes[indexV].parent = u;
                    pq.add(nodes[indexV]); // O(logn)
                }
            }
        } */

        return takenEdges;
    }

    private int connectedComponents() {
//        List<Edge> takenEdges = new ArrayList<>();

        DisjointSet disjointSet = new DisjointSet(nNodes);

        int count = nNodes;
        for (Edge edge : edgesForKruskal) {
            int s = disjointSet.find(edge.source);
            int d = disjointSet.find(edge.dest);

            if (s != d) {
                disjointSet.union(s, d);
                count--;
//                takenEdges.add(edge);
            }
        }
        return count;

    }

    public List<Edge> mst_byDeletion() {
        List<Edge> takenEdges = new ArrayList<>();

        DisjointSet disjointSet = new DisjointSet(nNodes);
        Collections.sort(edgesForKruskal);

        for(int i=edgesForKruskal.size()-1; i>=0;i--) {
            Edge temp = edgesForKruskal.get(i);
            edgesForKruskal.remove(temp);
            int connectedComponents = connectedComponents();
            if(connectedComponents>1) {
                edgesForKruskal.add(temp);
            } else {
//                edgesForKruskal.remove(edgesForKruskal.get(i));
            }
        }

        return edgesForKruskal;

    }
}
