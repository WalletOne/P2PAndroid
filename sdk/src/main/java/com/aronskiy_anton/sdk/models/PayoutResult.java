package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.library.Mapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class PayoutResult implements Mapper.Mappable {

    private List<Payout> payouts = new ArrayList<>();

    private Integer totalCount = 0;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject object){
        PayoutResult result = new PayoutResult();
        try {
            result.payouts = Mapper.map(object.opt("Payouts"), payouts, Payout.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.totalCount = Mapper.map(object.opt("TotalCount"), totalCount);
        return (T) result;
    }

    public List<Payout> getPayouts() {
        return payouts;
    }
}
