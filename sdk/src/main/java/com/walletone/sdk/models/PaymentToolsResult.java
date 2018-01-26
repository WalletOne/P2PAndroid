package com.walletone.sdk.models;

import com.walletone.sdk.library.Mapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronskiy on 04.12.2017.
 */

public class PaymentToolsResult implements Mapper.Mappable {

    private List<PaymentTool> paymentTools = new ArrayList<>();

    private Integer totalCount = 0;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject object) {
        PaymentToolsResult result = new PaymentToolsResult();
        try {
            result.paymentTools = Mapper.map(object.opt("PaymentTools"), paymentTools, PaymentTool.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.totalCount = Mapper.map(object.opt("TotalCount"), totalCount);
        return (T) result;
    }

    public List<PaymentTool> getPaymentTools() {
        return paymentTools;
    }
}
