import java.util.Scanner;

public class BoundTester {

    static int calculateBound(int[][] m, int fr, int fc) {
        int n = m.length;

        // Worked with cols - fixed
        int colBound = 1;
        int rowBound = 1;
        for (int i = 0; i <= fc; i++) {
            int currentColBound;
            int bound_upper = 1;
            int bound_lower = 1;

            // upper fixed portion
            for (int j = i - 1; j >= 0; j--) {
                if (m[j][i] == 1) {
                    System.out.println("Upper fixed " + j + "," + i);
                    bound_upper = i - j + 1;
                }
            }

            // lower fixed portion
            for (int j = i + 1; j <= fr; j++) {
                if (m[j][i] == 1) {
                    System.out.println("Lower fixed " + j + "," + i);
                    bound_lower = j - i + 1;
                }
            }

            // lower unfixed portion
            int count = 0;
            for (int j = fr + 1; j < n; j++) {
                if (m[j][i] == 1) {
                    System.out.println("Lower unfixed " + j + "," + i);
                    count++;
                }
            }
            if (count > 0) {
                bound_lower = (fr - i + 1) + count;
            }

            currentColBound = Integer.max(bound_lower, bound_upper);

            System.out.println("C" + i + " = " + currentColBound);
            System.out.println(bound_upper + " ---- " + bound_lower + "!!!! " + count);
            colBound = Integer.max(colBound, currentColBound);
        }

        System.out.println("Column Bound - " + colBound);

        // work with rows - fixed
        for (int i = 0; i <= fr; i++) {
            int currentRowBound;
            int bound_left = 1;
            int bound_right = 1;

            // left fixed portion
            for (int j = i - 1; j >= 0; j--) {
                if (m[i][j] == 1) {
                    System.out.println("Left fixed " + i + "," + j);
                    bound_left = i - j + 1;
                }
            }

            // right fixed portion
            for (int j = i + 1; j <= fc; j++) {
                if (m[i][j] == 1) {
                    System.out.println("Right fixed " + i + "," + j);
                    bound_right = j - i + 1;
                }
            }

            // right unfixed portion
            int count = 0;
            for (int j = fc + 1; j < n; j++) {
                if (m[i][j] == 1) {
                    System.out.println("Right unfixed " + i + "," + j);
                    count++;
                }
            }
            if (count > 0) {
                bound_right = (fc - i + 1) + count;
            }


            currentRowBound = Integer.max(bound_left,bound_right);
            rowBound = Integer.max(rowBound,currentRowBound);

            System.out.println("R" + i + " = " + currentRowBound);
            System.out.println(bound_left + " ---- " + bound_right + "!!!! " + count);

        }

        System.out.println("Row Bound - " + rowBound);
        int fixedBound = Integer.max(rowBound,colBound);


        // Unfixed Portion
        int unfixed_bound = 0;

        // go for the rows
        for(int i=fr+1;i<n;i++) {
            int count = 0;
            for(int j = fc+1;j<n;j++) {
                if(m[i][j] == 1)
                    count++;
            }

            unfixed_bound = Integer.max(unfixed_bound, (count + 1)/2);
        }

        // go for the colums
        for(int j = fc+1; j<n;j++) {
            int count = 0;
            for(int i=fr+1;i<n;i++) {
                if(m[i][j] == 1) {
                    count++;
                }
            }

            unfixed_bound = Integer.max(unfixed_bound, (count + 1)/2);
        }

        System.out.println("Fixed Bound - " + fixedBound);
        System.out.println("Unfixed Bound - " + unfixed_bound);

        return Integer.max(fixedBound,unfixed_bound);
    }

    public static void main(String[] args) {
        Scanner scr = new Scanner(System.in);

        int n = scr.nextInt();
        int fixedCol = scr.nextInt();
        int fixedRow = scr.nextInt();

        int[][] m = new int[n][];
        for (int i = 0; i < n; i++) {
            m[i] = new int[n];
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = scr.nextInt();
            }
        }

        System.out.println(calculateBound(m, fixedRow, fixedCol));
    }
}
