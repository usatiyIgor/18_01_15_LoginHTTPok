package com.example.den.a18_01_15_loginhttpok;

/**
 * Created by Den on 1/20/2018.
 */

public class PersonWithoutId {
    private String address, description, email, fullName, phoneNumber;


    public PersonWithoutId(String address, String description,
                           String email, String fullName, String phoneNumber) {
        this.address = address;
        this.description = description;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public PersonWithoutId(){}

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
