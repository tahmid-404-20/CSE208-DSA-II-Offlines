package util;

public class DisjointSet {
    int[] parent, rank;
    int nElements;

    public DisjointSet(int nElements) {
        this.nElements = nElements;

        parent = new int[this.nElements];
        rank = new int[this.nElements];
        // makeSet
        for (int i = 0; i < nElements; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

    }

    public int find(int x) {
        if (parent[x] == x) {
            return parent[x];
        } else {
            return parent[x] = find(parent[x]);
        }
    }

    public void union(int iRep, int jRep) {
        int irank = rank[iRep];
        int jrank = rank[jRep];

        if (irank < jrank) {
            parent[iRep] = jRep;
        } else if (irank > jrank) {
            parent[jRep] = iRep;
        } else {
            parent[jRep] = iRep;
            rank[iRep]++;
        }
    }


}
