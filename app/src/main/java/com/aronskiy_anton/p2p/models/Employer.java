package com.aronskiy_anton.p2p.models;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class Employer {

    private String id = "";

    private String title = "";

    private String phoneNumber = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employer that = (Employer) o;
        return that.id.equals(this.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}