package com.example.ade.model;

public class Counter {
    String num_counter, address;
    int ancien_index;
    boolean status;

    public Counter(String num_counter, String address, int ancien_index, boolean status) {
        this.num_counter = num_counter;
        this.address = address;
        this.ancien_index = ancien_index;
        this.status = status;
    }
}
