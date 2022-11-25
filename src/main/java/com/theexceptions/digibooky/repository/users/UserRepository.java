package com.theexceptions.digibooky.repository.users;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<String, User> users;

    public UserRepository() {
        users = new HashMap<>();
        // harcoded admin
        users.put("0", new User("admin@digibooky.com", "admin", "admin", "admin", Role.ADMIN));
        users.put("1", new User("librarian@digibooky.com", "librarian", "Marriaan", "Vdr", Role.LIBRARIAN));
        users.put("2", new Member("member@digibooky.com", "member", "Raf", "Vdr", "125478", new Address("Oktoberstraat", "656", "8200", "Brugge")));
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public boolean containsUser(String ssid, String email) {
        if (containsEmail(email)) return true;
        List<Member> members = users.values().stream().filter(user -> user instanceof Member).map(user -> (Member) user).toList();
        return members.stream().anyMatch(member -> member.getSSID().equalsIgnoreCase(ssid));
    }

    public boolean containsEmail(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public Optional<User> getUser(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
}
