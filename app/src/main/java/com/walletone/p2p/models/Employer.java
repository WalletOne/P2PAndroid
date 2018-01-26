package com.walletone.p2p.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String getFormattedPhoneNumber() {
        Pattern phoneNumberPattern = Pattern.compile("(\\d{1})(\\d{3})(\\d{3})(\\d{4})");
        Matcher matcher = phoneNumberPattern.matcher(phoneNumber);
        if (matcher.matches()) {
            return "+" + matcher.group(1) + " (" + matcher.group(2) + ") " +matcher.group(3) + "-" + matcher.group(4);
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}