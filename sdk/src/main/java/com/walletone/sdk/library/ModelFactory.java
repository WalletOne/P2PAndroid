package com.walletone.sdk.library;

import com.walletone.sdk.models.Deal;
import com.walletone.sdk.models.PaymentTool;
import com.walletone.sdk.models.PaymentToolsResult;
import com.walletone.sdk.models.Payout;
import com.walletone.sdk.models.PayoutResult;
import com.walletone.sdk.models.Refund;
import com.walletone.sdk.models.RefundsResult;

import org.json.JSONObject;

/**
 * Created by aaronskiy on 31.08.2017.
 */

public class ModelFactory {
    static public Object newInstance(Class<?> cls, JSONObject json){
        if (cls.equals(PaymentTool.class)){
            return json != null ? new PaymentTool().init(json) : new PaymentTool();
        } else if (cls.equals(Deal.class)) {
            return json != null ? new Deal().init(json) : new Deal();
        } else if (cls.equals(PayoutResult.class)) {
            return json != null ? new PayoutResult().init(json) : new PayoutResult();
        } else if (cls.equals(RefundsResult.class)) {
            return json != null ? new RefundsResult().init(json) : new RefundsResult();
        } else if (cls.equals(PaymentToolsResult.class)) {
            return json != null ? new PaymentToolsResult().init(json) : new PaymentToolsResult();
        } else if (cls.equals(Payout.class)) {
            return json != null ? new Payout().init(json) : new Payout();
        } else if (cls.equals(Refund.class)) {
            return json != null ? new Refund().init(json) : new Refund();
        } else {
            return null;
        }
    }



}
