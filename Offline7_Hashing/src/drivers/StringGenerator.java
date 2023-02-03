package drivers;

import hashCodes.hashFunctions;

import java.util.Arrays;
import java.util.Random;

public class StringGenerator {
    public static String getRandomString(int n) {
        String alphabets = "abcdefghijklmnopqrstuvxyz";
        int p = alphabets.length();
        StringBuilder sb = new StringBuilder(n);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(p);
            sb.append(alphabets.charAt(index));
        }
        return sb.toString();
    }

    static int getNumberOfUniqueValues(long[] a) {
        Arrays.sort(a);

        int countUnique = 0;
        for (int i = 0; i < a.length; i++) {
            while (i < a.length - 1 && a[i] == a[i + 1]) {
                i++;
            }
            countUnique++;
        }
        return countUnique;
    }

    public static void main(String[] args) {
        long[] a = new long[100];

        // Initialize String Array
        String[] str = new String[100];
        for (int i = 0; i < str.length; i++) {
            str[i] = getRandomString(7);
        }

        for (int i = 0; i < 100; i++) {
            a[i] = hashFunctions.hashFunc1(str[i]);
        }
        System.out.println("For h1: " + getNumberOfUniqueValues(a));

        for (int i = 0; i < 100; i++) {
            a[i] = hashFunctions.hashFunc2(str[i], 1229);
        }
        System.out.println("For h2: " + getNumberOfUniqueValues(a));
    }
}
