package com.anilugale.testapplication.model;

/**
    Created by Anil Ugale on 14/10/2015.
 */
public class SMS {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    private String address;
   private String body;
   private String person;

}
