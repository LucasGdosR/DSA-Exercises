package union_find.social_network_connectivity;

import java.time.LocalDateTime;
import java.util.List;

/*
Social network connectivity. Given a social network containing n members and a log file containing m timestamps at which
times pairs of members formed friendships, design an algorithm to determine the earliest time at which all members are
connected (i.e., every member is a friend of a friend of a friend ... of a friend). Assume that the log file is sorted
by timestamp and that friendship is an equivalence relation. The running time of your algorithm should be m log n or
better and use extra space proportional to n.
 */
public class SocialNetWorkConnectivity {
    public LocalDateTime whenWasConnected(List<Log> logs, int numberOfMembers) {
        UnionFindNetWork netWork = new UnionFindNetWork(numberOfMembers);

        for (Log log: logs) {
            netWork.union(log.p(), log.q());
            if (netWork.connectedSize == numberOfMembers)
                return log.timestamp();
        }

        return LocalDateTime.MAX;
    }
}
