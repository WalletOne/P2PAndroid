package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.Mapper;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by aaronskiy on 28.08.2017.
 */

public class Payout implements Mapper.Mappable {

    public static final String PAYOUT_STATE_ID_ACCEPTED = "Accepted";
    public static final String PAYOUT_STATE_ID_PROCESSING = "Processing";
    public static final String PAYOUT_STATE_ID_PROCESS_ERROR = "ProcessError";

    private Integer payoutId = 0;

    private String payoutStateId = "";

    private Date createDate;

    private BigDecimal amount = new BigDecimal(0.0);

    private CurrencyId currencyId = CurrencyId.RUB;

    private String platformDealId = "";

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject o){
        Payout payout = new Payout();
        payout.payoutId = Mapper.map(o.opt("PayoutId"), payoutId);
        payout.payoutStateId = Mapper.map(o.opt("PayoutStateId"), payoutStateId);
        payout.createDate = Mapper.map(o.opt("CreateDate"));
        payout.amount = Mapper.map(o.opt("Amount"), amount);
        payout.currencyId = Mapper.map(o.opt("CurrencyId"), currencyId);
        payout.platformDealId = Mapper.map(o.opt("PlatformDealId"), platformDealId);
        return (T) payout;
    }

    public String getPayoutStateId() {
        return payoutStateId;
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
