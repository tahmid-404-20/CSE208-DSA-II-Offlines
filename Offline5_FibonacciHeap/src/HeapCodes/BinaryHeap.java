package HeapCodes;

class BinNode {
    int nodeNo;
    double key;

    public BinNode(int nodeNo, double key) {
        this.nodeNo = nodeNo;
        this.key = key;
    }
}

public class BinaryHeap {
    int nNodes;
    int heapSize;
    int[] indexOf;
    BinNode[] A;

    public BinaryHeap(int graphNodes) {
        this.nNodes = graphNodes;
        indexOf = new int[this.nNodes];
        A = new BinNode[this.nNodes + 1];
        heapSize = 0;
    }

    public void buildHeap(BinNode[] nodes) {
        int i=1;
        for(BinNode node:nodes) {
            A[i] = node;
            indexOf[node.nodeNo] = i;
            i++;
            heapSize++;
        }

        for(i = heapSize/2; i>=1;i--) {
            heapify(i);
        }
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public BinNode extractMin() {
        BinNode min = A[1];
        exchange(1,heapSize);
        indexOf[min.nodeNo] = -1;
        A[heapSize] = null;
        heapSize--;
        heapify(1);
        return min;
    }

    public void decreaseKey(int nodeNo, double key) {
        int i = indexOf[nodeNo];
        if(Double.compare(key,A[i].key) > 0)
            return;

        A[i].key = key;
        while(i>1 && Double.compare(A[parent(i)].key,A[i].key)>0) {
            exchange(i,parent(i));
            i = parent(i);
        }
    }

    private int parent(int i) {
        return i / 2;
    }

    private int left(int i) {
        return 2 * i;
    }

    private int right(int i) {
        return 2 * i + 1;
    }

    private void swapIndex(int i, int j) {
        int temp = indexOf[i];
        indexOf[i] = indexOf[j];
        indexOf[j] = temp;
    }

    private void exchange(int i, int smallest) {
        swapIndex(A[i].nodeNo,A[smallest].nodeNo);
        BinNode temp = A[i];
        A[i] = A[smallest];
        A[smallest] = temp;
    }
    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;

        if(l <= heapSize && Double.compare(A[l].key,A[smallest].key) < 0) {
            smallest = l;
        }
        if(r <= heapSize && Double.compare(A[r].key,A[smallest].key) < 0) {
            smallest = r;
        }
        if(smallest != i) {
            exchange(i,smallest);
            heapify(smallest);
        }
    }



    public void printHeap() {
        for(int i=1;i<=heapSize;i++) {
            System.out.print(A[i].nodeNo+ ":" +(int)A[i].key + " ");
        }
        System.out.println();
        for(int i=0;i<heapSize;i++) {
            System.out.print(indexOf[i]+ " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        BinNode[] nodes = new BinNode[5];
        nodes[0] = new BinNode(0,6);
        nodes[1] = new BinNode(1,7);
        nodes[2] = new BinNode(2,1);
        nodes[3] = new BinNode(3,2);
        nodes[4] = new BinNode(4,-1);

        BinaryHeap heap = new BinaryHeap(5);
        heap.buildHeap(nodes);
        heap.printHeap();
//        BinNode min = heap.extractMin();
//        System.out.println("MIN-> "+ min.nodeNo +":" + (int)min.key);
        heap.decreaseKey(1,1);
        heap.printHeap();

    }

}
