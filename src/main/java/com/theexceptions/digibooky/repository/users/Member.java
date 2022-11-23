package com.theexceptions.digibooky.repository.users;

public class Member extends User {
    private final String ssid;
    private final Address address;

    public Member(String email, String password, String firstName, String lastName, String ssid, Address address) {
        super(email, password, firstName, lastName, Role.MEMBER);
        this.ssid = ssid;
        this.address = address;
    }
}
