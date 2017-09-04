package com.aronskiy_anton.sdk.constants;

/**
 * Created by aaronskiy on 28.08.2017.
 */

public enum CurrencyId {

    RUB(1); 	// Russian Ruble

    private final int id;

    CurrencyId(int id) {
        this.id = id;
    }

    public String getAlphabeticCode() {
        switch (this){
            case RUB:
                return "RUB";
                default:
                    return "";
        }
    }

    public String getSymbol() {
        switch (this){
            case RUB:
                return "\u20BD";
            default:
                return "";
        }
    }
}