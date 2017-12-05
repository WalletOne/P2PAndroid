package com.aronskiy_anton.p2p.models;

import java.util.UUID;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class Deal {

    private String id = "";

    private String title = "";

    private String shortDescription = "";

    private String fullDescription = "";

    private Employer employer;

    public Deal(Employer employer){
        this.employer = employer;
        this.id = UUID.randomUUID().toString();
    }

    public Deal(String title, String shortDescription, Employer employer){
        this.employer = employer;
        this.title = title;
        this.shortDescription = shortDescription;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal that = (Deal) o;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Employer getEmployer() {
        return employer;
    }
}
