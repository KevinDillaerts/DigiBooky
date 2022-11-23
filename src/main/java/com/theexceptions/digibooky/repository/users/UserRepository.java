package com.theexceptions.digibooky.repository.users;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<String, User> users;

    public UserRepository() {
        users = new HashMap<>();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public boolean containsUser(String ssid, String email) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            return true;
        }
        List<Member> members = users.values().stream().filter(user -> user instanceof Member).map(user -> (Member) user).toList();
        for (Member member : members) {
            return member.getSSID().equalsIgnoreCase(ssid);
        }
        return false;
    }
}
