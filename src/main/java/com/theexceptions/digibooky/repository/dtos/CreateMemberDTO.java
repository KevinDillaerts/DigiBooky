package com.theexceptions.digibooky.repository.dtos;

import com.theexceptions.digibooky.repository.users.Address;

public class CreateMemberDTO {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String ssid;
    private final Address address;

    public CreateMemberDTO(String email, String password, String firstName, String lastName, String ssid, Address address) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssid = ssid;
        this.address = address;
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


    public String getSSID() {
        return ssid;
    }

    public Address getAddress() {
        return address;
    }
}
