import java.util.PriorityQueue;
import java.util.Scanner;

public class BandMatrixGenerator {

    static int[][] get2DIntegerMatrix(int n) {
        int[][] m = new int[n][];
        for(int i=0;i<n;i++){
            m[i] = new int[n];
        }
        return m;
    }

    public static int[][] generateBandMatrix(int[][] m) {
        int n = m.length;

        Matrix mat = new Matrix(m,0,1,-1,-1);
        PriorityQueue<Matrix> pq = new PriorityQueue<>();

        pq.add(mat);

        while(true) {
            Matrix matrix = pq.poll();

            if(matrix.fixedColumn == n-2 && matrix.fixedRow == n-2) {
                // print the bound
                System.out.println("Bandwidth " + matrix.bound);
                return matrix.m;
            }

            int fc = matrix.fixedColumn;
            int fr = matrix.fixedRow;

            if(fc == fr) {
                // fix a column
                int newFixedColumn = fc+1;
                int newLevel = matrix.level + 1;
                int order = 1;
                for(int i= fc+1; i<n;i++, order++) { // number of new Matrices that will be generated
                    int[][] newMat = get2DIntegerMatrix(n);

                    //copy fixed part
                    for(int j=0;j<=fc;j++) {
                        for(int k=0;k<n;k++) {
                            newMat[k][j] = matrix.m[k][j];
                        }
                    }

                    //copy ith column of matrix at fc+1 th column of newMatrix (newFixedColumn)
                    for(int k=0;k<n;k++) {
                        newMat[k][fc+1] = matrix.m[k][i];
                    }

                    // copy the rest serially, skipping ith
                    for(int j = fc+2,a= fc+1; a<n;a++) {
                        if(i==a) {
                            continue;
                        } else {
                            for(int k=0;k<n;k++) {
                                newMat[k][j] = matrix.m[k][a];
                            }
                            j++;
                        }
                    }
                    pq.add(new Matrix(newMat,matrix.level+1, order, fc+1, fr));
                }

            } else {
                // fix a row
                int newFixedRow = fr +1;
                int order = 1;
                for(int i=fr+1;i<n;i++,order++) {
                    int[][] newMat = get2DIntegerMatrix(n);

                    //copy fixed part
                    for(int j=0;j<=fr;j++) {
                        System.arraycopy(matrix.m[j], 0, newMat[j], 0, n);
                    }

                    //copy ith row of matrix at fr+1 th row of newMatrix (newFixedColumn)
                    System.arraycopy(matrix.m[i], 0, newMat[fr + 1], 0, n);

                    // copy the rest serially, skipping ith
                    for(int j = fr+2,a= fr+1; a<n;a++) {
                        if(i==a) {
                            continue;
                        } else {
                            System.arraycopy(matrix.m[a], 0, newMat[j], 0, n);
                            j++;
                        }
                    }
                    pq.add(new Matrix(newMat,matrix.level+1, order, fc, fr+1));
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scr = new Scanner(System.in);

        int n = scr.nextInt();

        int[][] m = get2DIntegerMatrix(n);

        scr.nextLine();

        for(int i=0;i<n;i++) {
            String line = scr.nextLine();
            line = line.trim();
            line = line.replace(" ","");
            for(int j=0;j<n;j++) {
                char c = line.charAt(j);
                if(c == 'X') {
                    m[i][j] = 1;
                } else{
                    m[i][j] = 0;
                }
            }
        }
        long startTime = System.nanoTime();
        int[][] bandedMatrix = generateBandMatrix(m);
        long duration = (long) ((System.nanoTime()-startTime) /1e3);

        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                if(bandedMatrix[i][j] == 1) {
                    System.out.print("X ");
                } else
                {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Time to generate " + duration + " microSeconds");
    }
}
