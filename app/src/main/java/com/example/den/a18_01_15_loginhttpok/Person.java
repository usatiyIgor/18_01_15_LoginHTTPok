package com.example.den.a18_01_15_loginhttpok;

/**
 * Created by Den on 1/16/2018.
 */

public class Person extends PersonWithoutId {
    private Long contactId;

    public Person() {
    }

    public Person(Long contactId, String email, String fullName,
                  String phoneNumber, String description, String address) {
        super(email, fullName, phoneNumber, description, address);
        this.contactId = contactId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
