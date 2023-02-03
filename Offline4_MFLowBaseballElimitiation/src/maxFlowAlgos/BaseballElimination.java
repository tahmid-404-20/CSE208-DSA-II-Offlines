package maxFlowAlgos;

import util.MinCutResult;
import util.Point;
import util.TeamsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BaseballElimination {
    static TeamsInfo getTeamInfo(String[] tokens, int nTeams) {
        String teamName = "";
        int w = 0, l = 0, r = 0;
        int[] against = new int[nTeams];

        for (int i = 0; i < tokens.length; i++) {
            if (i == 0)
                teamName = tokens[i];
            else if (i == 1)
                w = Integer.parseInt(tokens[i]);
            else if (i == 2)
                l = Integer.parseInt(tokens[i]);
            else if (i == 3)
                r = Integer.parseInt(tokens[i]);
            else {
                against[i - 4] = Integer.parseInt(tokens[i]);
            }
        }
        return new TeamsInfo(teamName, w, l, r, against);
    }

    static void simulateResult(TeamsInfo[] teams, int nTeams, int testTeamIndex, int[][] g) {

        int nGamesVertex = nTeams * (nTeams - 1) / 2;
        int nBaseGame = nTeams;
        int sourceIndex = nBaseGame + nGamesVertex;
        int sinkIndex = sourceIndex + 1;
        int nNodes = sinkIndex + 1;

        // Calculate edges
        ArrayList<Point> points = new ArrayList<>();

        // Connect to t
        // 0 -- n-1 to teams
        for (int i = 0; i < nTeams; i++) {
            if (i != testTeamIndex) {
                int k = teams[testTeamIndex].w + teams[testTeamIndex].r - teams[i].w;
                if (k > 0)
                    points.add(new Point(i, sinkIndex, k));
            }
        }

        // Connect s-Vxy and Vxy to x,y
        int dist = 0;
        int g_ = 0;
        for (int i = 0; i < nTeams; i++) {
            for (int j = i + 1; j < nTeams; j++) {
                if (i != testTeamIndex && j != testTeamIndex) {
                    points.add(new Point(sourceIndex, nBaseGame + dist, g[i][j])); // s-> Vxy
                    points.add(new Point(nBaseGame + dist, i, Double.MAX_VALUE)); // Vxy -> x
                    points.add(new Point(nBaseGame + dist, j, Double.MAX_VALUE)); // Vxy -> y
                    g_ += g[i][j];
                }
                dist++;
            }
        }

        Graph graph = new Graph(nNodes, points);
        MinCutResult result = graph.getMaxFlowMinCutEdmondKarp(sourceIndex, sinkIndex);

        double maxFlow = result.maxFlow;

        if (Double.compare(maxFlow, g_) != 0) {
            ArrayList<Integer> T = new ArrayList<>();
            for (int i = 0; i < nTeams; i++) {
                if (result.cutIndexes.contains(i)) {
                    T.add(i);
                }
            }

            System.out.println(teams[testTeamIndex].name + " is eliminated");
            System.out.print("They can win at most " + teams[testTeamIndex].w + " + " + teams[testTeamIndex].r + " = ");
            System.out.println(teams[testTeamIndex].w + teams[testTeamIndex].r + " games.");

            int prevWinSum = 0;
            for (Integer i : T) {
                System.out.print(teams[i].name + ", ");
                prevWinSum += teams[i].w;
            }

            Collections.sort(T);
            int nPlay = 0;
            for (int i = 0; i < T.size(); i++) {
                for (int j = i+1; j < T.size(); j++) {
                    int p = T.get(i);
                    int q = T.get(j);
                    nPlay += g[p][q];
                }
            }

            int sum = prevWinSum + nPlay;
            System.out.println("\b\b have won a total of " + prevWinSum + " games");
            System.out.println("They play each other " + nPlay + " times");
            System.out.println("So on average, each of the teams wins " + sum + "/" + T.size() + " = " + (double) sum / T.size() + " games");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scr = new Scanner(System.in);

        int nTeams = Integer.parseInt(scr.nextLine());

        TeamsInfo[] teams = new TeamsInfo[nTeams];

        for (int i = 0; i < nTeams; i++) {
            String line = scr.nextLine();
            String[] tokens = line.split(" ");
            teams[i] = getTeamInfo(tokens, nTeams);
        }

        int maxPrevWin = -1;
        int maxPrevWinner = -1;
        for (int i = 0; i < nTeams; i++) {
            if (maxPrevWin < teams[i].w) {
                maxPrevWin = teams[i].w;
                maxPrevWinner = i;
            }
        }

        // Remaining games matrix
        int[][] g = new int[nTeams][];
        for (int i = 0; i < nTeams; i++) {
            g[i] = new int[nTeams];
        }
        for (int i = 0; i < nTeams; i++) {
            System.arraycopy(teams[i].against, 0, g[i], 0, nTeams);
        }

        for (int i = 0; i < nTeams; i++) {
            int probableWin = teams[i].w + teams[i].r;
            if (probableWin < maxPrevWin) {
                System.out.println(teams[i].name + " is eliminated");
                System.out.println("They can win at most " + teams[i].w + " + " + teams[i].r + " = " + probableWin + " times");
                System.out.println(teams[maxPrevWinner].name + " has won a total of " + teams[maxPrevWinner].w + " games");
                System.out.println("They play each other 0 times");
                System.out.println("So on average, each of the teams wins " + teams[maxPrevWinner].w + "/1" + " = " + teams[maxPrevWinner].w + " games");
            } else {
                simulateResult(teams, nTeams, i,g);
            }
        }
    }
}
