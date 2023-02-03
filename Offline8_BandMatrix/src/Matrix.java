public class Matrix implements Comparable<Matrix> {
    int[][] m;
    int n;
    int bound;
    int level;
    int order;
    int fixedColumn;
    int fixedRow;

    public Matrix(int[][] m, int level, int order, int fixedColumn, int fixedRow) {
        this.m = m;
        this.n = m.length;
        this.level = level;
        this.order = order;
        this.fixedColumn = fixedColumn;
        this.fixedRow = fixedRow;
        this.bound = calculateBound(this.fixedRow,this.fixedColumn);
    }

    private int calculateBound(int fr, int fc) {
        int fixedColBound = getBoundFixedCol(this.fixedRow,this.fixedColumn);
        int fixedRowBound = getBoundFixedRow(this.fixedRow,this.fixedColumn);

        int fixedBound = Integer.max(fixedRowBound,fixedColBound);
        int unfixedRegionBound = getBoundUnfixedRegion(this.fixedRow,this.fixedColumn);

        return Integer.max(fixedBound,unfixedRegionBound);
    }

    private int getBoundFixedCol(int fr, int fc) {
        int colBound = 1;
        for (int i = 0; i <= fc; i++) {
            int currentColBound;
            int bound_upper = 1;
            int bound_lower = 1;

            // upper fixed portion
            for (int j = i - 1; j >= 0; j--) {
                if (m[j][i] == 1) {
                    bound_upper = i - j + 1;
                }
            }

            // lower fixed portion
            for (int j = i + 1; j <= fr; j++) {
                if (m[j][i] == 1) {
                    bound_lower = j - i + 1;
                }
            }

            // lower unfixed portion
            int count = 0;
            for (int j = fr + 1; j < n; j++) {
                if (m[j][i] == 1) {
                    count++;
                }
            }
            if (count > 0) {
                bound_lower = (fr - i + 1) + count;
            }

            currentColBound = Integer.max(bound_lower, bound_upper);
            colBound = Integer.max(colBound, currentColBound);
        }

        return colBound;
    }

    private int getBoundFixedRow(int fr, int fc) {
        int rowBound = 1;
        for (int i = 0; i <= fr; i++) {
            int currentRowBound;
            int bound_left = 1;
            int bound_right = 1;

            // left fixed portion
            for (int j = i - 1; j >= 0; j--) {
                if (m[i][j] == 1) {
                    bound_left = i - j + 1;
                }
            }

            // right fixed portion
            for (int j = i + 1; j <= fc; j++) {
                if (m[i][j] == 1) {
                    bound_right = j - i + 1;
                }
            }

            // right unfixed portion
            int count = 0;
            for (int j = fc + 1; j < n; j++) {
                if (m[i][j] == 1) {
                    count++;
                }
            }
            if (count > 0) {
                bound_right = (fc - i + 1) + count;
            }


            currentRowBound = Integer.max(bound_left,bound_right);
            rowBound = Integer.max(rowBound,currentRowBound);
        }

        return  rowBound;
    }

    private int getBoundUnfixedRegion(int fr, int fc) {
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

        // go for the columns
        for(int j = fc+1; j<n;j++) {
            int count = 0;
            for(int i=fr+1;i<n;i++) {
                if(m[i][j] == 1) {
                    count++;
                }
            }

            unfixed_bound = Integer.max(unfixed_bound, (count + 1)/2);
        }

        return unfixed_bound;
    }

    @Override
    public int compareTo(Matrix o) {
        return (this.bound == o.bound)? (this.level == o.level)? (o.order - this.order) : (o.level - this.level) : (this.bound - o.bound);
    }
}

