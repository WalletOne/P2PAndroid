package com.walletone.sdk.models;

import com.walletone.sdk.library.Mapper;

import org.json.JSONObject;

/**
 * Created by aaronskiy on 04.12.2017.
 */

public class PaymentTool implements Mapper.Mappable {

    private Integer paymentToolId = 0;

    private String paymentTypeId = "";

    private String mask = "";

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject o) {
        PaymentTool paymentTool = new PaymentTool();
        paymentTool.paymentToolId = Mapper.map(o.opt("PaymentToolId"), paymentToolId);
        paymentTool.paymentTypeId = Mapper.map(o.opt("PaymentTypeId"), paymentTypeId);
        paymentTool.mask = Mapper.map(o.opt("Mask"), mask);
        return (T) paymentTool;
    }

    public PaymentTool() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentTool that = (PaymentTool) o;
        return that.paymentToolId.equals(this.paymentToolId) && this.paymentToolId != 0;
    }

    public Integer getPaymentToolId() {
        return paymentToolId;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public String getMask() {
        return mask;
    }
}
