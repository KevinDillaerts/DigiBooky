package com.theexceptions.digibooky.repository.users;

import com.theexceptions.digibooky.exceptions.FieldIsEmptyException;

public class Member extends User {
    private final String ssid;
    private final Address address;

    public Member(String email, String password, String firstName, String lastName, String ssid, Address address) {
        super(email, password, firstName, lastName, Role.MEMBER);
        this.ssid = ssid;
        if (address.getCity() == null || address.getCity().equals("")) {
            throw new FieldIsEmptyException("the city field cannot be empty");
        }
        this.address = address;
    }

    public String getSSID() {
        return ssid;
    }
}
