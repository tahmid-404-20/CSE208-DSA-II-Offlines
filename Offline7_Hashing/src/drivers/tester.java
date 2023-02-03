package drivers;

import java.util.Arrays;

public class tester {
    public static void main(String[] args) {
        int h1_k = 31;
        int c1 = 7, c2 = 11;
        int N = 47;

        int[] a = new int[N];

        for(int i=0;i<N;i++)
        {
            a[i] = (h1_k + i*(c1 + c2*i)) % N;
        }
        Arrays.sort(a);
        for(int i=0;i<N;i++) {
            System.out.print(a[i] + " ");
        }

    }
}
