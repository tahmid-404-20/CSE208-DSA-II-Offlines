package hashCodes;

public class doubleHashing {
    private int N;
    private openAddressingNode[] A;
    private int lastNodeValue;
    private long lastSearchProbes;

    public doubleHashing(int n) {
        N = n;
        A = new openAddressingNode[N];
        for (int i = 0; i < N; i++) {
            A[i] = new openAddressingNode();
        }
        lastNodeValue = 0;
        lastSearchProbes = 0;
    }

    public static void main(String[] args) {
        int SIZE = 1000;
        doubleHashing sep = new doubleHashing(1229);

        String[] a = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            a[i] = drivers.StringGenerator.getRandomString(10);
        }

        for (int i = 0; i < SIZE; i++) {
            sep.insert(a[i]);
        }

        sep.insert("abcdef");
        System.out.println(sep.search("abcdef"));
        sep.insert("abcdef");
        sep.insert("abcdef");
        sep.insert("abcdef");
        System.out.println(sep.search("abcdef"));
        sep.delete("abcdef");
        System.out.println(sep.search("abcdef"));
        sep.insert("abcdef");
        System.out.println(sep.search("abcdef"));

    }

    private int hash(String s, int i) {
        return (hashFunctions.hashFunc1(s) % N + i * hashFunctions.hashFunc2(s, N)) %  N;
    }

    public void insert(String s) {
        insertHelp(s);
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
                    return;
                }
            }
        }
    }

    private void deleteHelp(String s) {
        int idx = searchHelp(s);
        if (idx != -1) {
            A[idx].key = null;
            A[idx].value = -1;
            A[idx].isDeleted = true;
        }
    }
}
