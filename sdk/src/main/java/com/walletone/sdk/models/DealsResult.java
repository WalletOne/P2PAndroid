package com.walletone.sdk.models;

import com.walletone.sdk.library.Mapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronskiy on 04.12.2017.
 */

public class DealsResult implements Mapper.Mappable {

    private List<Deal> deals = new ArrayList<>();

    private Integer totalCount = 0;

    @Override
    public <T> T init(JSONObject object) {
        DealsResult result = new DealsResult();
        try {
            result.deals = Mapper.map(object.opt("Deals"), deals, Deal.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.totalCount = Mapper.map(object.opt("TotalCount"), totalCount);
        return (T) result;
    }

    public Integer getTotalCount() {
        return totalCount;
    }
}
