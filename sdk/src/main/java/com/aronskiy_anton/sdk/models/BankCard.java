package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.library.Mapper;

import org.json.JSONObject;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class BankCard implements Mapper.Mappable {

    private String cardMask = "";

    private int cardId = 0;

    private String cardHolder = "";

    private String expireDate = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankCard that = (BankCard) o;
        return that.cardId == this.cardId && this.cardId != 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject o) {
        BankCard card = new BankCard();
        card.cardMask = Mapper.map(o.opt("CardMask"), "");
        card.cardId = Mapper.map(o.opt("CardId"), 0);
        card.cardHolder = Mapper.map(o.opt("CardHolder"), "");
        card.expireDate = Mapper.map(o.opt("ExpireDate"), "");
        return (T) card;
    }

    public BankCard() {
    }
}
