package com.aronskiy_anton.sdk.models;

import org.json.JSONObject;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class BankCard {

    private String cardMask = "";

    private int cardId = 0;

    private String cardHolder = "";

    private String expireDate = "";

    public BankCard fromJson(JSONObject o) {

        BankCard card = new BankCard();
        card.cardMask = o.optString("CardMask", "");
        card.cardId = o.optInt("CardId", 0);
        card.cardHolder = o.optString("CardHolder", "");
        card.expireDate = o.optString("ExpireDate", "");
        return card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankCard that = (BankCard) o;
        return that.cardId == this.cardId && this.cardId != 0;
    }
}
