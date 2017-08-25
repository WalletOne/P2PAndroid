package com.aronskiy_anton.sdk.constants;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public enum CurrencyId {

    RUB("643", "\u20BD" , false); 	// Russian Ruble

    private String stringValue;
    private String sign;
    private boolean signToLeft;

    CurrencyId (String stringValue, String sign, boolean signToLeft){
        this.stringValue = stringValue;
        this.sign = sign;
        this.signToLeft = signToLeft;
    }

    public String getId() {
        return stringValue;
    }

    public String getSign() {
        return sign;
    }

    public boolean isSignToLeft() {
        return signToLeft;
    }
}
