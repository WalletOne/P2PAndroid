package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.Mapper;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by aaronskiy on 31.08.2017.
 */

public class Refund implements Mapper.Mappable {

    public static final String REFUND_STATE_ID_ACCEPTED  = "Accepted";
    public static final String REFUND_STATE_ID_PROCESSING  = "Processing";
    public static final String REFUND_STATE_ID_PROCESS_ERROR  = "ProcessError";

    private Integer refundId = 0;

    private String refundStateId = "";

    private Date createDate;

    private BigDecimal amount = BigDecimal.valueOf(0.0);

    private CurrencyId currencyId = CurrencyId.RUB;

    private String platformDealId = "";

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject o) {
        Refund refund = new Refund();
        refund.refundId = Mapper.map(o.opt("RefundId"), 0);
        refund.refundStateId = Mapper.map(o.opt("RefundStateId"), refundStateId);
        refund.createDate = Mapper.map(o.opt("CreateDate"));
        refund.amount = Mapper.map(o.opt("Amount"), amount);
        refund.currencyId = Mapper.map("CurrencyId", currencyId);
        refund.platformDealId = Mapper.map("PlatformDealId", platformDealId);
        return (T) refund;
    }

    public String getRefundStateId() {
        return refundStateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CurrencyId getCurrencyId() {
        return currencyId;
    }
}
