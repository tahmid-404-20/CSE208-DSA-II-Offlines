package drivers;

import hashCodes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

class Result {
    double loadFactor;
    double avgSearchTimeBefDel;
    double avgProbesBefDel;
    double avgSearchTimeAfterDel;
    double avgProbesAfterDel;

    public Result(double loadFactor, double avgSearchTimeBefDel, double avgProbesBefDel, double avgSearchTimeAfterDel, double avgProbesAfterDel) {
        this.loadFactor = loadFactor;
        this.avgSearchTimeBefDel = avgSearchTimeBefDel;
        this.avgProbesBefDel = avgProbesBefDel;
        this.avgSearchTimeAfterDel = avgSearchTimeAfterDel;
        this.avgProbesAfterDel = avgProbesAfterDel;
    }
}

public class reportGenerator {
    public static final int STRING_LENGTH = 7;
    public static final int NUMBER_OF_LOADFACTORS = 6;

    static Result seperateChainingReport(double loadFactor, int N, String []str) {
        Random random = new Random();
        separateChaining ht = new separateChaining(N);

        for(String s: str) {
            ht.insert(s);
        }

        int searchLength = str.length/10;
        double totalSearchTimeBefDel = 0;
        double totalSearchTimeAfterDel = 0;

        int base = random.nextInt(str.length);
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeBefDel += (double)(System.nanoTime() - startTime)/1e3;
        }

        // deletion
        base = random.nextInt(str.length);
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;
            ht.delete(str[idx]);
        }

        // searching after delete
        base = (base + searchLength/2) % N ; // 50% old, 50% new
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeAfterDel += (double)(System.nanoTime() - startTime)/1e3;
        }

        double avgSearchTimeBefDel = totalSearchTimeBefDel/searchLength;
        double avgSearchTimeAfterDel = totalSearchTimeAfterDel/searchLength;

        return new Result(loadFactor,avgSearchTimeBefDel,-1,avgSearchTimeAfterDel,-1);
    }

    static Result linearProbingReport(double loadFactor, int N, String []str) {
        Random random = new Random();

        linearProbing ht = new linearProbing(N);

        for(String s: str) {
            ht.insert(s);
        }

        int base = random.nextInt(str.length);
        int searchLength = str.length/10;

        double totalSearchTimeBefDel = 0;
        long totalProbesBefDel = 0;
        double totalSearchTimeAfterDel = 0;
        long totalProbesAfterDel = 0;


        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeBefDel += (double)(System.nanoTime() - startTime)/1e3;
            totalProbesBefDel += ht.getLastSearchProbes();
        }

        // deletion
        base = random.nextInt(str.length);
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;
            ht.delete(str[idx]);
        }

        // searching after delete
        base = (base + searchLength/2) % N ; // 50% old, 50% new
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeAfterDel += (double)(System.nanoTime() - startTime)/1e3;
            totalProbesAfterDel += ht.getLastSearchProbes();
        }

        // Result generation
        double avgSearchTimeBefDel = totalSearchTimeBefDel/searchLength;
        double avgProbesBefDel = (double) totalProbesBefDel/searchLength;
        double avgSearchTimeAfterDel = totalSearchTimeAfterDel/searchLength;
        double avgProbesAfterDel = (double) totalProbesAfterDel/searchLength;

        return new Result(loadFactor,avgSearchTimeBefDel,avgProbesBefDel,avgSearchTimeAfterDel,avgProbesAfterDel);
    }

    static Result quadraticProbingReport(double loadFactor, int N, String []str) {
        Random random = new Random();

        quadraticProbing ht = new quadraticProbing(N);

        for(String s: str) {
            ht.insert(s);
        }

        int base = random.nextInt(str.length);
        int searchLength = str.length/10;

        double totalSearchTimeBefDel = 0;
        long totalProbesBefDel = 0;
        double totalSearchTimeAfterDel = 0;
        long totalProbesAfterDel = 0;


        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeBefDel += (double)(System.nanoTime() - startTime)/1e3;
            totalProbesBefDel += ht.getLastSearchProbes();
        }

        // deletion
        base = random.nextInt(str.length);
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;
            ht.delete(str[idx]);
        }

        // searching after delete
        base = (base + searchLength/2) % N ; // 50% old, 50% new
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeAfterDel += (double)(System.nanoTime() - startTime)/1e3;
            totalProbesAfterDel += ht.getLastSearchProbes();
        }

        // Result generation
        double avgSearchTimeBefDel = totalSearchTimeBefDel/searchLength;
        double avgProbesBefDel = (double) totalProbesBefDel/searchLength;
        double avgSearchTimeAfterDel = totalSearchTimeAfterDel/searchLength;
        double avgProbesAfterDel = (double) totalProbesAfterDel/searchLength;

        return new Result(loadFactor,avgSearchTimeBefDel,avgProbesBefDel,avgSearchTimeAfterDel,avgProbesAfterDel);
    }

    static Result doubleHashingReport(double loadFactor, int N, String []str) {
        Random random = new Random();

        doubleHashing ht = new doubleHashing(N);

        for(String s: str) {
            ht.insert(s);
        }

        int base = random.nextInt(str.length);
        int searchLength = str.length/10;

        double totalSearchTimeBefDel = 0;
        long totalProbesBefDel = 0;
        double totalSearchTimeAfterDel = 0;
        long totalProbesAfterDel = 0;


        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeBefDel += (double)(System.nanoTime() - startTime)/1e3;
            totalProbesBefDel += ht.getLastSearchProbes();
        }

        // deletion
        base = random.nextInt(str.length);
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;
            ht.delete(str[idx]);
        }

        // searching after delete
        base = (base + searchLength/2) % N ; // 50% old, 50% new
        for(int i=0;i<searchLength;i++) {
            int idx = (base + i) % str.length;

            long startTime = System.nanoTime();
            ht.search(str[idx]);
            totalSearchTimeAfterDel += (double)(System.nanoTime() - startTime)/1e3;
            totalProbesAfterDel += ht.getLastSearchProbes();
        }

        // Result generation
        double avgSearchTimeBefDel = totalSearchTimeBefDel/searchLength;
        double avgProbesBefDel = (double) totalProbesBefDel/searchLength;
        double avgSearchTimeAfterDel = totalSearchTimeAfterDel/searchLength;
        double avgProbesAfterDel = (double) totalProbesAfterDel/searchLength;

        return new Result(loadFactor,avgSearchTimeBefDel,avgProbesBefDel,avgSearchTimeAfterDel,avgProbesAfterDel);
    }

     static void showIndividualReports(Result[] results) {
         for(int i=0;i<NUMBER_OF_LOADFACTORS;i++) {
             Result l = results[i];
             if(l.avgProbesBefDel == -1) {
                 System.out.println(l.loadFactor+","+l.avgSearchTimeBefDel+","+l.avgSearchTimeAfterDel);
             } else{
                 System.out.println(l.loadFactor+","+l.avgSearchTimeBefDel+","+ l.avgProbesBefDel+","+l.avgSearchTimeAfterDel+","+l.avgProbesAfterDel);
             }
         }
     }

     static void outputIndividualProbingHeader(String tableName) {
         System.out.print("For_"+tableName);
         System.out.println(","+"Before_Deletion,,After_Deletion,");
         System.out.println("Load_Factor,Avg_Search_Time,Avg_Number_Of_Probes,Avg_Search_Time,Avg_Number_Of_Probes");
     }

     static void outputMergedHeader(Double loadFactor) {
         System.out.print("Load_Factor:" + loadFactor);
         System.out.println(","+"Before_Deletion,,After_Deletion,");
         System.out.println("Method,Avg_Search_Time,Avg_Number_Of_Probes,Avg_Search_Time,Avg_Number_Of_Probes");
     }

     static void showFinalMergedReportFor(String ht_name, Result result) {
        System.out.print(ht_name+",");
        Result l = result;
        if(l.avgProbesBefDel == -1) {
             System.out.println(l.avgSearchTimeBefDel+","+ "N/A,"+l.avgSearchTimeAfterDel+",N/A");
         } else{
             System.out.println(l.avgSearchTimeBefDel+","+ l.avgProbesBefDel+","+l.avgSearchTimeAfterDel+","+l.avgProbesAfterDel);
         }
     }

    public static void main(String[] args) {
        System.out.print("Enter the value of N: ");

        Scanner scr = new Scanner(System.in);
        int N = scr.nextInt();

        Result[] separateChainingResults = new Result[NUMBER_OF_LOADFACTORS];
        Result[] linearProbingResults = new Result[NUMBER_OF_LOADFACTORS];
        Result[] quadraticProbingResults = new Result[NUMBER_OF_LOADFACTORS];
        Result[] doubleHashingResults = new Result[NUMBER_OF_LOADFACTORS];

        // Fillup result Array
        for(int i=4;i<=9;i++) {
            double loadFactor = i/10.0;
            int stringArraySize = (int)(N*loadFactor);

            String[] str = new String[stringArraySize];

            // Generate strings here
            for(int j=0;j<stringArraySize;j++) {
                str[j] = StringGenerator.getRandomString(STRING_LENGTH);
            }

            int idx = i-4;

            //HashTable
            separateChainingResults[idx] = seperateChainingReport(loadFactor,N,str);
            linearProbingResults[idx] = linearProbingReport(loadFactor,N,str);
            quadraticProbingResults[idx] = quadraticProbingReport(loadFactor,N,str);
            doubleHashingResults[idx] = doubleHashingReport(loadFactor,N,str);
        }


        // Redirecting output
        try {
            System.setOut(new PrintStream(new File("individualPerformance.txt")));
        } catch (FileNotFoundException e) {
            System.out.println("Error redirecting output");
            exit(1);
        }

        // Individual Reports
        System.out.print("For_Separate_Chaining");
        System.out.println(","+"Before_Deletion,After_Deletion,");
        System.out.println("Load_Factor,Avg_Search_Time,Avg_Search_Time");
        showIndividualReports(separateChainingResults);
        System.out.println();

        outputIndividualProbingHeader("LinearProbing");
        showIndividualReports(linearProbingResults);
        System.out.println();

        outputIndividualProbingHeader("QuadraticProbing");
        showIndividualReports(quadraticProbingResults);
        System.out.println();

        outputIndividualProbingHeader("DoubleHashing");
        showIndividualReports(doubleHashingResults);

        // Redirecting output
        try {
            System.setOut(new PrintStream(new File("combinedPerformance.txt")));
        } catch (FileNotFoundException e) {
            System.out.println("Error redirecting output");
            exit(1);
        }

        // Final Merged Report
        for(int i=0;i<NUMBER_OF_LOADFACTORS;i++) {
            double loadFactor = separateChainingResults[i].loadFactor;

            outputMergedHeader(loadFactor);
            showFinalMergedReportFor("SeparateChaining",separateChainingResults[i]);
            showFinalMergedReportFor("LinearProbing",linearProbingResults[i]);
            showFinalMergedReportFor("QuadraticProbing",quadraticProbingResults[i]);
            showFinalMergedReportFor("DoubleHashing",doubleHashingResults[i]);
            System.out.println();
            System.out.println();
        }
    }

}
