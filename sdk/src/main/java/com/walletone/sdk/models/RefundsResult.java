package com.walletone.sdk.models;

import com.walletone.sdk.library.Mapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class RefundsResult implements Mapper.Mappable {

    private List<Refund> refunds = new ArrayList<>();

    private Integer totalCount = 0;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject object) {
        RefundsResult result = new RefundsResult();
        try {
            result.refunds = Mapper.map(object.opt("Refunds"), refunds, Refund.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.totalCount = Mapper.map(object.opt("TotalCount"), totalCount);
        return (T) result;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }
}
