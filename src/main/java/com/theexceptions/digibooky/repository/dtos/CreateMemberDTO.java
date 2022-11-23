package com.theexceptions.digibooky.repository.dtos;

import com.theexceptions.digibooky.repository.users.Address;
import com.theexceptions.digibooky.repository.users.Role;

public class CreateMemberDTO {
    private final String id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final String ssid;


    private Address address;

    public CreateMemberDTO(String id, String email, String password, String firstName, String lastName, Role role, String ssid, String streetName, String houseNumber, String postalCode, String city) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.ssid = ssid;
        address = new Address(streetName, houseNumber, postalCode, city);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public String getSSID() {
        return ssid;
    }

    public Address getAddress() {
        return address;
    }
}
