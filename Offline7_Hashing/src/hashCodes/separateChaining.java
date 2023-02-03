package hashCodes;

class sepChainNode {
    String key;
    int value;
    sepChainNode prev;
    sepChainNode next;

    public sepChainNode(String key, int value) {
        this.key = key;
        this.value = value;
        prev = next = null;
    }
}

public class separateChaining {
    int N;
    sepChainNode[] A;
    int lastNodeValue;

    public separateChaining(int N) {
        this.N = N;
        A = new sepChainNode[N];
        lastNodeValue = 0;
    }

    public static void main(String[] args) {
        int SIZE = 1000;
        separateChaining sep = new separateChaining(101);

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

    private int hash(String s) {
        return hashFunctions.hashFunc1(s) % N;
    }

    public int search(String s) {
        return searchHelp(s);
    }

    public void insert(String s) {
        insertHelp(s);
    }

    public void delete(String s) {
        deleteHelp(s);
    }

    private void insertHelp(String s) {
        if (searchHelp(s) == -1) {
            int k = hash(s);

            lastNodeValue++;
            sepChainNode x = new sepChainNode(s, lastNodeValue);

            if (A[k] != null) {
                x.next = A[k];
                A[k].prev = x;
            }
            A[k] = x;
        }
    }

    private int searchHelp(String s) {
        int k = hash(s);
        sepChainNode x = A[k];

        while (x != null) {
            if (x.key.equals(s)) {
                return x.value;
            }
            x = x.next;
        }
        return -1;
    }

    private void deleteHelp(String s) {
        int k = hash(s);
        sepChainNode x = A[k];
        while (x != null) {
            if (x.key.equals(s)) {
                if (A[k] == x) {
                    A[k] = x.next;
                }
                if (x.next != null) {
                    x.next.prev = x.prev;
                }
                if (x.prev != null) {
                    x.prev.next = x.next;
                }
                break;
            }
            x = x.next;
        }
    }
}
