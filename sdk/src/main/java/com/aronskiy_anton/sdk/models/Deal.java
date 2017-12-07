package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.Mapper;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class Deal implements Mapper.Mappable {

    public static final String DEAL_STATE_ID_CREATED = "Created";
    public static final String DEAL_STATE_ID_PROCESSING = "PaymentProcessing";
    public static final String DEAL_STATE_ID_PAYMENT_HOLD = "PaymentHold";
    public static final String DEAL_STATE_ID_PAYMENT_PROCESS_ERROR = "PaymentProcessError";
    public static final String DEAL_STATE_ID_PAID = "Paid";
    public static final String DEAL_STATE_ID_PAYOUT_PROCESSING = "PayoutProcessing";
    public static final String DEAL_STATE_ID_PAYOUT_PROCESS_ERROR = "PayoutProcessError";
    public static final String DEAL_STATE_ID_COMPLETED = "Completed";
    public static final String DEAL_STATE_ID_CANCELING = "Canceling";
    public static final String DEAL_STATE_ID_CANCEL_ERROR = "CancelError";
    public static final String DEAL_STATE_ID_CANCELED = "Canceled";

    public static final String DEAL_TYPE_ID_DEFERRED = "Deferred";
    public static final String DEAL_STATE_ID_INSTANT = "Instant";

    private String platformDealId = "";

    private String dealStateId = "";

    private Date createDate;

    private Date updatedate;

    private Date expireDate;

    private BigDecimal amount = new BigDecimal(0.0);

    private CurrencyId currencyId = CurrencyId.RUB;

    private BigDecimal payerCommissionAmount = new BigDecimal(0.0);

    private BigDecimal platformBonusAmount = new BigDecimal(0.0);

    private String platformPayerId = "";

    private Integer payerPaymentToolId = 0;

    private String payerPhoneNumber = "";

    private String platformBeneficiaryId = "";

    private Integer beneficiaryPaymentToolId = 0;

    private String shortDescription = "";

    private String fullDescription = "";

    private String dealTypeId = "";

    @SuppressWarnings("unchecked")
    @Override
    public <T> T init(JSONObject o) {
        Deal deal = new Deal();

        deal.platformDealId = Mapper.map(o.opt("PlatformDealId"), platformDealId);
        deal.dealStateId = Mapper.map(o.opt("DealStateId"), dealStateId);
        deal.createDate = Mapper.map(o.opt("CreateDate"));
        deal.updatedate = Mapper.map(o.opt("UpdateDate"));
        deal.expireDate = Mapper.map(o.opt("ExpireDate"));
        deal.amount = Mapper.map(o.opt("Amount"), amount);
        deal.currencyId = Mapper.map(o.opt("CurrencyId"), currencyId);
        deal.payerCommissionAmount = Mapper.map(o.opt("PayerCommissionAmount"), payerCommissionAmount);
        deal.platformBonusAmount = Mapper.map(o.opt("PlatformBonusAmount"), platformBonusAmount);
        deal.platformPayerId = Mapper.map(o.opt("PlatformPayerId"), platformPayerId);
        deal.payerPhoneNumber = Mapper.map(o.opt("PayerPhoneNumber"), payerPhoneNumber);
        deal.payerPaymentToolId = Mapper.map(o.opt("PayerPaymentToolId"), payerPaymentToolId);
        deal.platformBeneficiaryId = Mapper.map(o.opt("PlatformBeneficiaryId"), platformBeneficiaryId);
        deal.beneficiaryPaymentToolId = Mapper.map(o.opt("BeneficiaryPaymentToolId"), beneficiaryPaymentToolId);
        deal.shortDescription = Mapper.map(o.opt("ShortDescription"), shortDescription);
        deal.fullDescription = Mapper.map(o.opt("FullDescription"), fullDescription);
        deal.dealTypeId = Mapper.map(o.opt("DealTypeId"), dealTypeId);
        return (T) deal;
    }

    public String getDealStateId() {
        return dealStateId;
    }
}
