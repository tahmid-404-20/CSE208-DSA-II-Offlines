package hashCodes;

public class hashFunctions {
    public static int hashFunc1(String s) {
        int hash = 1;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash << 5) | (hash >>> 27);
            hash += s.charAt(i);
        }
        return Math.abs(hash);
    }


    public static int hashFunc2(String s, int n) {
        int hash;

        int a = 5;
        int b = 3;
        int p = 39;

        int k = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            k = (p * k + (int) s.charAt(i));
        }

        hash = Math.abs((a * k + b)) % n;

        if (hash == 0) {
            hash++;
        }

        return hash;
    }
}
