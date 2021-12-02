package project.lab6.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
     * Calculates the longest path of friends starting from the given user
     *
     * @param visited    Un hashSet containing users who have already been visited. Initially it must be empty
     *                   and in the end it will remain empty
     * @param user       The user from whom the road calculation will start
     * @return the length of this road
     */
    private int findLongestPath(HashSet<Long> visited, User user) {
        int best = 0;
        visited.add(user.getId());
        for (var friend : user.getFriends(DirectedStatus.APPROVED)) {
            if (!visited.contains(friend.getUser().getId()))
                best = Math.max(best, 1 + findLongestPath(visited, friend.getUser()));
        }
        visited.remove(user.getId());
        return best;
    }

    /**
     * @return the longest path of the community(the longest friendship)
     */
    public int findLongestPath() {
        int best = 0;
        HashSet<Long> visited = new HashSet<>();
        for (var user : communityUsers.values())
            best = Math.max(best, findLongestPath(visited, user));
        return best;
    }
}
