package graphs.baseball;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

/**
 * The baseball elimination problem.  In the baseball elimination problem, there is a division consisting of n teams.
 * At some point during the season, team i has w[i] wins, l[i] losses, r[i] remaining games, and g[i][j] games left to
 * play against team j. A team is mathematically eliminated if it cannot possibly finish the season in (or tied for)
 * first place. The goal is to determine exactly which teams are mathematically eliminated. For simplicity, we assume
 * that no games end in a tie (as is the case in Major League Baseball) and that there are no rainouts (i.e., every
 * scheduled game is played).
 *
 * A maxflow formulation. We now solve the baseball elimination problem by reducing it to the maxflow problem.
 * To check whether team x is eliminated, we consider two cases.
 *
 * Trivial elimination. If the maximum number of games team x can win is less than the number of wins of some other
 * team i, then team x is trivially eliminated (as is Montreal in the example above). That is, if w[x] + r[x] < w[i],
 * then team x is mathematically eliminated.
 *
 * Nontrivial elimination. Otherwise, we create a flow network and solve a maxflow problem in it. In the network,
 * feasible integral flows correspond to outcomes of the remaining schedule. There are vertices corresponding to teams
 * (other than team x) and to remaining divisional games (not involving team x). Intuitively, each unit of flow in the
 * network corresponds to a remaining game. As it flows through the network from s to t, it passes from a game vertex,
 * say between teams i and j, then through one of the team vertices i or j, classifying this game as being won by that
 * team. If all edges in the maxflow that are pointing from s are full, then this corresponds to assigning winners to
 * all the remaining games in such a way that no team wins more games than x. If some edges pointing from s are not
 * full, then there is no scenario in which team x can win the division.
 */
public class BaseballElimination {
    private final HashMap<String, Integer> teams;
    private final String[] idToString;
    private final int[] w, l, r;
    private final int[][] g;
    private final boolean[] eliminated;
    private final Bag<String>[] certificateOfElimination;
    private final int numberOfGames, networkVertices;
    private final boolean[] examined;

    // create a baseball division from given filename in format specified
    public BaseballElimination(String filename) {
        teams = new HashMap<>();
        In in = new In(filename);
        int numberOfTeams = in.readInt();
        numberOfGames = numberOfTeams * (numberOfTeams - 1) / 2;
        networkVertices = numberOfGames + numberOfTeams + 2;
        idToString = new String[numberOfTeams];
        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];
        eliminated = new boolean[numberOfTeams];
        certificateOfElimination = (Bag<String>[]) new Bag[numberOfTeams];
        examined = new boolean[numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            String team = in.readString();
            teams.put(team, i);
            idToString[i] = team;
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++)
                g[i][j] = in.readInt();
        }

    }

    // number of teams
    public int numberOfTeams() {
        return w.length;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        return w[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        return l[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        return r[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return g[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);

        int x = teams.get(team);
        if (examined[x]) return eliminated[x];
        examined[x] = true;

        FlowNetwork G = new FlowNetwork(networkVertices);
        int beginningOfTeamNodes = numberOfGames + 1, source = 0, sink = networkVertices - 1, currentGame = 1;
        for (int i = 0; i < w.length; i++) {
            for (int j = i + 1; j < w.length; j++) {
                // Games left between i and j.
                G.addEdge(new FlowEdge(source, currentGame, g[i][j]));
                // Team i wins.
                G.addEdge(new FlowEdge(currentGame, beginningOfTeamNodes + i, Double.POSITIVE_INFINITY));
                // Team j wins. Advance the game counter for next iteration.
                G.addEdge(new FlowEdge(currentGame++, beginningOfTeamNodes + j, Double.POSITIVE_INFINITY));
            }
            // Edge restriction for team i.
            G.addEdge(new FlowEdge(beginningOfTeamNodes + i, sink, Math.max(w[x] + r[x] - w[i], 0)));
        }

        FordFulkerson minCut = new FordFulkerson(G, source, sink);
        for (FlowEdge e : G.adj(source))
            if (e.flow() != e.capacity()) {
                for (int i = beginningOfTeamNodes; i < sink; i++)
                    if (minCut.inCut(i)) {
                        if (certificateOfElimination[x] == null) certificateOfElimination[x] = new Bag<>();
                        certificateOfElimination[x].add(idToString[i - beginningOfTeamNodes]);
                    }
                eliminated[x] = true;
                return true;
            }

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        int x = teams.get(team);
        if (!examined[x]) isEliminated(team);
        return certificateOfElimination[x];
    }

    private void validateTeam(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException();
    }
}
