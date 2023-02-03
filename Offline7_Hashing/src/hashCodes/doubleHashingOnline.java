package hashCodes;

import java.util.Random;

public class doubleHashingOnline {
    private int N;
    private openAddressingNode[] A;
    private int lastNodeValue;
    private long lastSearchProbes;
    private int elementCount;
    private double loadFactor;

    public doubleHashingOnline(int n) {
        N = n;
        A = new openAddressingNode[N];
        for (int i = 0; i < N; i++) {
            A[i] = new openAddressingNode();
        }
        lastNodeValue = 0;
        lastSearchProbes = 0;
        elementCount = 0;
        loadFactor = 0;
    }


    public double getLoadFactor() {
        return loadFactor;
    }

    private void updateLoadFactor() {
        loadFactor = (double) elementCount/N;
    }

    private boolean isPrime(int n) {
        for(int i=2;i*i < n; i++) {
            if(n%i == 0) {
                return false;
            }
        }
        return true;
    }

    public void reHasHIncreaseSize() {
        int p = 2*N;
        int newSize;
        for(int i=p;;i++) {
            if(isPrime(i)) {
                newSize = i;
                break;
            }
        }

        openAddressingNode[] temp = A;
        A = new openAddressingNode[newSize];
        for(int i=0;i<A.length;i++) {
            A[i] = new openAddressingNode();
        }

        int oldSize = this.N;
        this.N = newSize;

        for(int i=0;i<oldSize;i++) {
            for(int j=0;j<N;j++) {
                if(temp[i].key != null) {
                    int k = hash(temp[i].key,j);
                    if (A[k].key == null) {
                        A[k].key = temp[i].key;
                        A[k].value = temp[i].value;
                        A[j].isDeleted = false;
                        break;
                    }
                }

            }
        }
        updateLoadFactor();
    }

    public void reHashDecreaseSize() {
        int p = N/2;
        int newSize;
        for(int i=p;;i++) {
            if(isPrime(i)) {
                newSize = i;
                break;
            }
        }

        openAddressingNode[] temp = A;
        A = new openAddressingNode[newSize];
        for(int i=0;i<A.length;i++) {
            A[i] = new openAddressingNode();
        }

        int oldSize = this.N;
        this.N = newSize;

        for(int i=0;i<oldSize;i++) {
            for(int j=0;j<N;j++) {
                if(temp[i].key != null) {
                    int k = hash(temp[i].key,j);
                    if (A[k].key == null) {
                        A[k].key = temp[i].key;
                        A[k].value = temp[i].value;
                        A[j].isDeleted = false;
                        break;
                    }
                }
            }
        }
        updateLoadFactor();
    }

    private int hash(String s, int i) {
        return (hashFunctions.hashFunc1(s) % N + i * hashFunctions.hashFunc2(s, N)) %  N;
    }

    public void insert(String s) {
        lastSearchProbes = 0;
        insertHelp(s);
        updateLoadFactor();
    }

    public int search(String s) {
        lastSearchProbes = 0;
        int idx = searchHelp(s);
        if(idx != -1) {
            return A[idx].value;
        }
        return -1;
    }

    public long getLastSearchProbes() {
        return lastSearchProbes;
    }

    public void delete(String s) {
        deleteHelp(s);
        updateLoadFactor();
    }

    private int searchHelp(String s) {
        for (int i = 0; i < N; i++) {
            int j = hash(s, i);

            lastSearchProbes++;
            if (A[j].key == null && !A[j].isDeleted) {
                return -1;
            } else if (s.equals(A[j].key)) {
                return j;
            }
        }
        return -1;
    }

    private void insertHelp(String s) {
        if (searchHelp(s) == -1) {
            for (int i = 0; i < N; i++) {
                int j = hash(s, i);
                if (A[j].key == null) {
                    A[j].key = s;
                    lastNodeValue++;
                    A[j].value = lastNodeValue;
                    A[j].isDeleted = false;
                    elementCount++;
                    return;
                }
            }
        }
    }

    private void deleteHelp(String s) {
        int idx = searchHelp(s);
        if (idx != -1) {
            elementCount--;
            A[idx].key = null;
            A[idx].value = -1;
            A[idx].isDeleted = true;
        }
    }

    static void showAvgSearchCount(String[] str, doubleHashingOnline sep) {
        int searchLength = str.length/10;

        Random random = new Random();
        int base = random.nextInt(str.length);
        double totalSearchTime = 0;
        double totalProbes = 0;

        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            sep.search(str[idx]);
            totalSearchTime += (double)(System.nanoTime() - startTime)/1e3;
            totalProbes += sep.getLastSearchProbes();
        }

        System.out.println("Average -> time: " + totalSearchTime/searchLength + " probes: " + totalProbes/searchLength);

    }

    public static void main(String[] args) {
        int SIZE = 1000;
        doubleHashingOnline sep = new doubleHashingOnline(101);

        String[] a = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            a[i] = drivers.StringGenerator.getRandomString(10);
        }

        double totalProbes = 0;
        double avgProbes = 0;

        for (int i = 0; i < SIZE; i++) {
            sep.insert(a[i]);
            totalProbes += sep.getLastSearchProbes();
            avgProbes = totalProbes/(i+1);

            if(Double.compare(avgProbes,2) > 0) {
                System.out.println("Rehash will occur during insert as probes: " + avgProbes);
                System.out.println("Before");
                showAvgSearchCount(a,sep);
                sep.reHasHIncreaseSize();
                System.out.println("After");
                showAvgSearchCount(a,sep);
                totalProbes = 0.0;
            }
        }

        for (int i = 0; i < SIZE/1.2; i++) {
            sep.delete(a[i]);
            double loadFactor = sep.getLoadFactor();
            if(Double.compare(loadFactor,0.4) < 0)
            {
                System.out.println("Rehash to a smaller table - lf before " + loadFactor);
                showAvgSearchCount(a,sep);
                sep.reHashDecreaseSize();
                loadFactor = sep.getLoadFactor();
                System.out.println(" " + "lf now " + loadFactor);
                showAvgSearchCount(a,sep);

            }
        }


    }
}

