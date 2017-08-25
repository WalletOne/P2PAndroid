package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.constants.CurrencyId;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class Deal {

    public static final String DEAL_STATE_ID_CREATED = "Created";
    public static final String DEAL_STATE_ID_PROCESSING = "PaymentProcessing";
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

    private String platformDealId  = "";

    private String dealStateId = "";

    private Date createDate;

    private Date updatedate;

    private Date expireDate;

    private BigDecimal amount  = new BigDecimal(0.0);

    private CurrencyId currencyId = CurrencyId.RUB;

    private BigDecimal payerCommissionAmount = new BigDecimal(0.0);

    private BigDecimal platformBonusAmount = new BigDecimal(0.0);

    private String platformPayerId  = "";

    private Integer payerCardId = 0;

    private String payerPhoneNumber = "";

    private String platformBeneficiaryId = "";

    private Integer beneficiaryCardId = 0;

    private String shortDescription = "";

    private String fullDescription = "";

    private String dealTypeId = "";

    public Deal fromJson(JSONObject jsonObject){
        Deal deal = new Deal();
        /*try{
            jsonObject.get("PlatformDealId");

            deal.platformDealId = jsonObject.optString("PlatformDealId", "");
            deal.dealStateId = jsonObject.optString("DealStateId", "");
            deal.createDate = ISO8601DateParser.parse(jsonObject.getString("CreateDate"));
            deal.createDate = ISO8601DateParser.parse(jsonObject.getString("UpdateDate"));
            deal.createDate = ISO8601DateParser.parse(jsonObject.getString("ExpireDate"));
            deal.amount = new BigDecimal(jsonObject.getDouble("Amount"));
            deal.currencyId = jsonObject.(json["CurrencyId"], .rub);
            deal.payerCommissionAmount = jsonObject.(json["PayerCommissionAmount"], 0.0);
            deal.platformBonusAmount = jsonObject.(json["PlatformBonusAmount"], 0.0);
            deal.platformPayerId = jsonObject.(json["PlatformPayerId"], "");
            deal.payerPhoneNumber = jsonObject.(json["PayerPhoneNumber"], "");
            deal.payerCardId = jsonObject.(json["PayerCardId"], 0);
            deal.platformBeneficiaryId = jsonObject.(json["PlatformBeneficiaryId"], "");
            deal.beneficiaryCardId = jsonObject.(json["BeneficiaryCardId"], 0);
            deal.shortDescription = jsonObject.(json["ShortDescription"], "");
            deal.fullDescription = jsonObject.(json["FullDescription"], "");
            deal.dealTypeId = jsonObject.(json["DealTypeId"], "");
        } catch (JSONException exception){

        } catch (ParseException ParceException){

        }*/
        return deal;
    }
}
