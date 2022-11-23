package com.theexceptions.digibooky.repository.users;

public class Address {
    private final String streetName;
    private final String houseNumber;
    private final String postalCode;
    private final String city;

    public Address(String streetName, String houseNumber, String postalCode, String city) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
