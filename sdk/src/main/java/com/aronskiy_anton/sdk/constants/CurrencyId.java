package com.aronskiy_anton.sdk.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aaronskiy on 28.08.2017.
 */

public enum CurrencyId {

    RUB(643),    // Russian Ruble
    UNDEFINED(0);    // Undefined

    private final Integer id;

    private static Map<Integer, CurrencyId> map = new HashMap<>();

    static {
        for (CurrencyId currency : values()) {
            map.put(currency.id, currency);
        }
    }

    CurrencyId(Integer id) {
        this.id = id;
    }

    public String getAlphabeticCode() {
        switch (this) {
            case RUB:
                return "RUB";
            default:
                return "";
        }
    }

    public String getSymbol() {
        switch (this) {
            case RUB:
                return "\u20BD";
            default:
                return "";
        }
    }

    public static CurrencyId getCurrencyById(Integer id) {
        CurrencyId result = map.get(id);
        return result == null ? UNDEFINED : result;
    }

    public Integer getId() {
        return id;
    }
}