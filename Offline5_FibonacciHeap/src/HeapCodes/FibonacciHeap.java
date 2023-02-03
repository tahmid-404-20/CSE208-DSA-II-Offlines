package HeapCodes;

import java.util.ArrayList;

class HeapNode {
    int nodeNo;
    int degree;
    double key;
    boolean mark;
    HeapNode left;
    HeapNode right;
    HeapNode parent;
    HeapNode child;

    public HeapNode(int nodeNo, double key) {
        this.nodeNo = nodeNo;
        this.key = key;
        this.degree = 0;
        this.mark = false;
        this.parent = this.child = null;
        this.left = this.right = this;
    }
}

public class FibonacciHeap {
    HeapNode min;
    HeapNode[] nodes;
    int nNodes;

//    public FibonacciHeap() {
//        this.min = null;
//        this.nNodes = 0;
//        nodes = new ArrayList<>();
//    }

    public FibonacciHeap(int graphNodes) {
        this.min = null;
        this.nNodes = 0;
        nodes = new HeapNode[graphNodes];
    }

    public void insertNode(int nodeNo, double key) {
        HeapNode x = new HeapNode(nodeNo,key);
        addToRootList(x);
        nodes[nodeNo] = x;
        nNodes++;
    }

    public HeapNode getMin() {
        return min;
    }

    public boolean isEmpty() {
        return min == null;
    }

    public HeapNode ExtractMin() {
        HeapNode z = min;

        if(z != null) {
            while (z.child != null) {
                HeapNode x = z.child;
                removeFromChildList(z,x);
                addToRootList(x);
                x.parent = null;
            }
            removeFromRootList(z);
            if(z==z.right) {
                min = null;
            } else {
                min = z.right;
                consolidate();
            }
            nNodes--;
            nodes[z.nodeNo] = null;
        }
        return z;
    }

    private void addToRootList(HeapNode x) {
        if(min == null) {
            min = x;
        } else {
            min.left.right = x;
            x.right = min;
            x.left = min.left;
            min.left = x;

            if(Double.compare(min.key, x.key) > 0) {
                min = x;
            }
        }

    }

    private void addToChildList(HeapNode parent, HeapNode x) {
        HeapNode y = parent.child;
        if(y==null) {
            parent.child = x;
            x.left = x;
            x.right = x;
        } else {
            y.left.right = x;
            x.right = y;
            x.left = y.left;
            y.left = x;
        }
        x.parent = parent;

    }

    private void removeFromRootList(HeapNode y) {
        y.left.right = y.right;
        y.right.left = y.left;
    }

    private void removeFromChildList(HeapNode parent, HeapNode y) {
        if(y == y.right) {
            parent.child = null;
        } else {
            parent.child = y.right;
            y.left.right = y.right;
            y.right.left = y.left;
        }
    }

    private void link(HeapNode y,HeapNode x) {
        removeFromRootList(y);
        addToChildList(x,y);
        x.degree++;
        y.mark = false;
    }

    public void consolidate() {
        int n = (int)(Math.log(nNodes) / Math.log(2)) + 1;
        HeapNode [] A = new HeapNode[n+1];

        HeapNode w = min;
        do {
            HeapNode x = w;
            HeapNode next = x.right;
            int d = x.degree;
            while (A[d] != null) {
                HeapNode y = A[d];
                if(Double.compare(x.key,y.key) > 0) {
                    HeapNode temp;
                    temp = x;
                    x = y;
                    y = temp;
                }
                if(next == y) {
                    next = y.right;
                }
                if(min == y) {
                    min = y.right;
                }
                link(y,x);
                A[d] = null;
                d++;
            }
            A[d] = x;
            w = next;
        } while(w!= min);

        min = null;
        for(int i=0;i<=n;i++) {
            if(min == null || (A[i] != null &&min.key > A[i].key)) {
                min = A[i];
            }
        }
    }

    public void decreaseKey(int nodeNo, double key) {
        decreaseKeyUtil(nodes[nodeNo],key);
    }

    private void decreaseKeyUtil(HeapNode x, double key) {
        if(Double.compare(key,x.key) >= 0) {
            return;
        }
        x.key = key;
        HeapNode y = x.parent;
        if(y!= null && Double.compare(y.key,x.key) > 0) {
            cut(x,y);
            cascadingCut(y);
        }
        if(x.key < min.key) {
            min = x;
        }
    }

    private void cut(HeapNode x, HeapNode y) {
        removeFromChildList(y,x);
        y.degree--;
        addToRootList(x);
        x.parent = null;
        x.mark = false;
    }

    private void cascadingCut(HeapNode y) {
        HeapNode z = y.parent;
        if(z != null) {
            if(!y.mark) {
                y.mark = true;
            } else
            {
                cut(y,z);
                cascadingCut(z);
            }
        }
    }
    public void printHeap() {

        HeapNode y = min;
        if(min == null)
        {
            System.out.println("Empty Heap");
            return;
        }
        while(y.right != min) {
            System.out.print((int)y.key + "--> ");
            y = y.right;
        }
        System.out.print((int)y.key);
        System.out.println();

    }

}
