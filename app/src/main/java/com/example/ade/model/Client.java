package com.example.ade.model;

public class Client extends Person{

    String code_client;

    public Client(String code_client, String firstname, String lastname) {
        super(firstname, lastname);
        this.code_client = code_client;
    }
}
