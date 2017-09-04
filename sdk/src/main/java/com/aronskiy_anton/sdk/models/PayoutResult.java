package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.library.Mapper;

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
    public <T> T init(JSONObject object) {
        PayoutResult result = new PayoutResult();
        result.payouts = Mapper.map(object.opt("Payouts"), payouts);
        result.totalCount = Mapper.map(object.opt("TotalCount"), totalCount);
        return (T) result;
    }
}
