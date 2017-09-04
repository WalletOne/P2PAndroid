package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.library.Mapper;

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
        result.refunds = Mapper.map(object.opt("Refunds"), refunds);
        result.totalCount = Mapper.map(object.opt("TotalCount"), totalCount);
        return (T) result;
    }
}
