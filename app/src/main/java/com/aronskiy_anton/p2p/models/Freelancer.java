package com.aronskiy_anton.p2p.models;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class Freelancer {

    private String id = "";

    private String title = "";

    private String phoneNumber = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Freelancer that = (Freelancer) o;
        return that.id.equals(this.id);
    }
}
