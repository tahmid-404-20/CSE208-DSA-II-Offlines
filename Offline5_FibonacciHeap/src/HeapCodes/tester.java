package HeapCodes;

public class tester {
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap(6);

        heap.insertNode(0,1);
        heap.insertNode(1,2);
        heap.insertNode(2,3);
        heap.insertNode(3,4);
        heap.insertNode(4,5);
        heap.insertNode(5,6);

        heap.printHeap();
//        heap.consolidate();
        heap.ExtractMin();
        heap.ExtractMin();
        heap.ExtractMin();
        heap.printHeap();
        heap.ExtractMin();
        heap.printHeap();
        heap.decreaseKey(5,-1);
        System.out.println("Decreased");
        heap.printHeap();
        heap.ExtractMin();
        heap.printHeap();
        heap.ExtractMin();
        heap.ExtractMin();
        heap.printHeap();
//        heap.decreaseKey(3, -1);
//        heap.printHeap();
    }
}
