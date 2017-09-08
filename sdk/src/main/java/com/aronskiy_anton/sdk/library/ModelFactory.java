package com.aronskiy_anton.sdk.library;

import com.aronskiy_anton.sdk.models.BankCard;
import com.aronskiy_anton.sdk.models.Deal;
import com.aronskiy_anton.sdk.models.Payout;
import com.aronskiy_anton.sdk.models.PayoutResult;
import com.aronskiy_anton.sdk.models.Refund;
import com.aronskiy_anton.sdk.models.RefundsResult;

import org.json.JSONObject;

/**
 * Created by aaronskiy on 31.08.2017.
 */

public class ModelFactory {
    static public Object newInstance(Class<?> cls, JSONObject json){
        if (cls.equals(BankCard.class)){
            return json != null ? new BankCard().init(json) : new BankCard();
        } else if (cls.equals(Deal.class)) {
            return json != null ? new Deal().init(json) : new Deal();
        } else if (cls.equals(PayoutResult.class)) {
            return json != null ? new PayoutResult().init(json) : new PayoutResult();
        } else if (cls.equals(RefundsResult.class)) {
            return json != null ? new RefundsResult().init(json) : new RefundsResult();
        } else if (cls.equals(Payout.class)) {
            return json != null ? new Payout().init(json) : new Payout();
        } else if (cls.equals(Refund.class)) {
            return json != null ? new Refund().init(json) : new Refund();
        } else {
            return null;
        }
    }



}
