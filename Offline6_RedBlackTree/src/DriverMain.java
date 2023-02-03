import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import static java.lang.System.exit;

public class DriverMain {
    private static final String INPUT_FILE_NAME = "input.txt";
    public static void main(String[] args) {
        // Redirecting input
        try {
            System.setIn(new FileInputStream(new File(INPUT_FILE_NAME)));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("Error redirecting input1");
            exit(1);
        }

        // Redirecting output
        try {
            System.setOut(new PrintStream(new File("output.txt")));
        } catch (FileNotFoundException e) {
            System.out.println("Error redirecting output");
            exit(1);
        }

        Scanner scr = new Scanner(System.in);

        int n = scr.nextInt();
        System.out.println(n);

        RedBlackTree rbTree = new RedBlackTree();

        for(int i=0;i<n;i++) {
            int e = scr.nextInt();
            int x = scr.nextInt();
            int r = 0;
            if(e==0) {
                if(rbTree.delete(x)) {
                    r = 1;
                }
            } else if(e==1) {
                if(rbTree.insert(x)) {
                    r = 1;
                }
            } else if(e==2){
                if(rbTree.search(x)) {
                    r = 1;
                }
            } else if(e == 3) {
                r = rbTree.countValuesLessThan(x);
            } else {
                r = -1;
            }

            System.out.println(e + " " + x + " " + r);
        }
    }
}
