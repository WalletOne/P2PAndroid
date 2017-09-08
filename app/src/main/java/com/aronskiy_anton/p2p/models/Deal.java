package com.aronskiy_anton.p2p.models;

import com.aronskiy_anton.sdk.models.BankCard;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class Deal {

    private String id = "";

    private String title = "";

    private String shortDescription = "";

    private String fullDescription = "";

    private Employer employer;

    public void Deal(Employer employer){
        this.employer = employer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deal that = (Deal) o;
        return that.id.equals(this.id);
    }

}
