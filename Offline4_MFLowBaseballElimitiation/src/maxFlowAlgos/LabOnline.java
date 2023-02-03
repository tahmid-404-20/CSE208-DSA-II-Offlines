package maxFlowAlgos;

import util.Point;

import java.util.ArrayList;
import java.util.Scanner;

class PeopleInfo {
    int height;
    int age;
    int isDivorced;

    public PeopleInfo(int height, int age, int isDivorced) {
        this.height = height;
        this.age = age;
        this.isDivorced = isDivorced;
    }
}
public class LabOnline {

    static int generateResult(PeopleInfo[] peopleInfos, int m, int n) {
        int womanIndex = m;
        int sourceIndex = peopleInfos.length;
        int destIndex = sourceIndex + 1;
        int nNodes = peopleInfos.length + 2;
        ArrayList<Point> points = new ArrayList<>();

        for(int i=0;i<m;i++) {
            for(int j = womanIndex;j< peopleInfos.length;j++) {
                if(Math.abs(peopleInfos[i].age - peopleInfos[j].age) <= 5 &&
                        Math.abs(peopleInfos[i].height - peopleInfos[j].height) <= 12 &&
                        (peopleInfos[i].isDivorced == peopleInfos[j].isDivorced)
                ) {
                    points.add(new Point(i,j,1));
                }
            }
        }

        for(int i=0;i<m;i++) {
            points.add(new Point(sourceIndex,i,1));
        }

        for(int j=womanIndex;j<peopleInfos.length;j++) {
            points.add(new Point(j,destIndex,1));
        }

        Graph graph = new Graph(nNodes, points);

        return (int)graph.getMaxFlowMinCutEdmondKarp(sourceIndex,destIndex).maxFlow;

    }

    public static void main(String[] args) {
        Scanner scr = new Scanner(System.in);

        int t = scr.nextInt();
        for(int i=0;i<t;i++) {
            int m = scr.nextInt();
            int n = scr.nextInt();

            PeopleInfo[] peopleInfos = new PeopleInfo[m+n];

            for(int k=0;k<m;k++)
            {
                int height = scr.nextInt();
                int age = scr.nextInt();
                int isDivorced = scr.nextInt();

                peopleInfos[k] = new PeopleInfo(height, age, isDivorced);
            }

            for(int k=n;k<m+n;k++)
            {
                int height = scr.nextInt();
                int age = scr.nextInt();
                int isDivorced = scr.nextInt();

                peopleInfos[k] = new PeopleInfo(height, age, isDivorced);
            }

            if(i == 0) System.out.println();

            System.out.println("Case " + (i + 1) + ": " + generateResult(peopleInfos,m,n));
        }
    }
}
