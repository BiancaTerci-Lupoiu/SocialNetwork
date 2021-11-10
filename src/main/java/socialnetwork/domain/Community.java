package socialnetwork.domain;

import java.util.*;

/**
 * Describes a community of users
 */
public class Community {
    Map<Long, User> communityUsers;


    public Community() {
        communityUsers = new HashMap<>();
    }

    /**
     * add a user to the community
     *
     * @param user the user to be added
     */
    public void addUser(User user) {
        communityUsers.put(user.getId(), user);
    }

    /**
     * @return all the users from the community
     */
    public Iterable<User> getCommunityUsers() {
        return communityUsers.values();
    }

    /**
     * visits all the neighbours of currentNode and set the distance to them
     *
     * @param nodes       the list with nodes
     * @param currentNode the node at the current step
     */
    private void dfsVisit(Map<Long, Integer> nodes, Long currentNode) {
        //System.out.println(currentNode + " " + nodes.get(currentNode));
        User currentUser = communityUsers.get(currentNode);
        for (User userFriend : currentUser.getFriends()) {
            if (nodes.get(userFriend.getId()) == -1) {
                nodes.put(userFriend.getId(), nodes.get(currentNode) + 1);
                dfsVisit(nodes, userFriend.getId());
            }
        }
    }

    /**
     * does a dfs in the community graph
     *
     * @param first the first node to start the dfs algorithm
     * @return a Tuple<Long,Integer> which contains the furthest node from the first node and the
     * distance to that node
     */
    private Tuple<Long, Integer> dfs(Long first) {
        // salvam id-nod si distanta pana la el
        Map<Long, Integer> nodesAndDistances = new HashMap<>();
        for (Map.Entry<Long, User> user : communityUsers.entrySet())
            nodesAndDistances.put(user.getKey(), -1);

        nodesAndDistances.put(first, 0);
        dfsVisit(nodesAndDistances, first);
        // !!!!!!!foarte important sa nu fie 0 ca se strica pe noduri izolate
        int maximumDistance = -1;
        Long farthestNode = -1L;
        for (Map.Entry<Long, Integer> pair : nodesAndDistances.entrySet())
            if (pair.getValue() > maximumDistance) {
                maximumDistance = pair.getValue();
                farthestNode = pair.getKey();
            }

        return new Tuple(farthestNode, maximumDistance);

        /*Map<Long, Integer> nodesAndDistances = new HashMap<>();

        for (Map.Entry<Long, User> user : communityUsers.entrySet())
            nodesAndDistances.put(user.getKey(), -1);
        Queue<Long> queueNodes = new LinkedList<>();

        nodesAndDistances.put(first, 0);
        queueNodes.add(first);
        while (!queueNodes.isEmpty()) {
            System.out.println(queueNodes);
            Long top = queueNodes.poll();
            User user = communityUsers.get(top);
            System.out.println(user.getFriends());
            if(user.getFriends().spliterator().getExactSizeIfKnown()!=0) {
                for (User friend : user.getFriends()) {
                    Long nextNode = friend.getId();
                    if (nodesAndDistances.get(nextNode) == -1) {
                        queueNodes.add(nextNode);
                        nodesAndDistances.put(nextNode, nodesAndDistances.get(top) + 1);
                    }
                }
            }
        }
        int maximumDistance = 0;
        Long farthestNode = -1L;
        for (Map.Entry<Long, Integer> pair : nodesAndDistances.entrySet())
            if (pair.getValue() > maximumDistance) {
                maximumDistance = pair.getValue();
                farthestNode = pair.getKey();
            }
        return new Tuple(farthestNode, maximumDistance);
*/
    }

    /**
     * @return the longest path of the community(the longest friendship)
     */
    public int findLongestPath() {
        Long first = communityUsers.entrySet().stream().findFirst().get().getKey();
        Tuple<Long, Integer> firstBfs = dfs(first);

        Tuple<Long, Integer> longestPathAndNode = dfs(firstBfs.getLeft());
        return longestPathAndNode.getRight();

    }
}
