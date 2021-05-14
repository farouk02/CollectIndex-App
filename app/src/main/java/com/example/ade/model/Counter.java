package com.example.ade.model;

public class Counter {
    public String counter_num;
    public String address;
    public int old_index;
    public int status;

    public Counter(String counter_num, String address, int old_index, int status) {
        this.counter_num = counter_num;
        this.address = address;
        this.old_index = old_index;
        this.status = status;
    }
}
