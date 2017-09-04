package com.aronskiy_anton.sdk.library;

import com.aronskiy_anton.sdk.models.BankCard;
import com.aronskiy_anton.sdk.models.Deal;

import org.json.JSONObject;

/**
 * Created by aaronskiy on 31.08.2017.
 */

public class ModelFactory {
    static public Object newInstance(Class<?> cls, JSONObject json){
        if (cls.equals(BankCard.class)){
            return new BankCard().init(json);
        } else if (cls.equals(Deal.class)) {
            return new Deal().init(json);
        } else {
            return null;
        }
    }



}
