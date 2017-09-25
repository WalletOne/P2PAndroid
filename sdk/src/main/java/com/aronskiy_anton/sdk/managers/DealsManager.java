package com.aronskiy_anton.sdk.managers;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.aronskiy_anton.sdk.Manager;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.library.URLComposer;
import com.aronskiy_anton.sdk.models.Deal;
import com.aronskiy_anton.sdk.models.RequestBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class DealsManager extends Manager {

    private Composer composer = new Composer();

    public DealsManager(P2PCore core) {
        super(core);
    }

    class Composer extends URLComposer {

        String deal(){
            return relativeToBase("deal");
        }

        String dealPay(){
            return relative(deal(), "pay");
        }

        String deals(){
            return relativeToApi("deals");
        }

        String deals(String dealId){
            return relative(deals(), dealId);
        }

        String dealsComplete(String dealId){
            return relative(deals(dealId), "complete");
        }

        String dealsCancel(String dealId){
            return relative(deals(dealId), "cancel");
        }

        String dealsBeneficiaryCard(String dealId){
            return relative(deals(dealId),  "beneficiaryCard");
        }
    }

    /**
     * Create money request for payer
     *
     * @param dealId Deal identifier in your system
     * @param payerId Payer identifier in your system
     * @param beneficiaryId
     * @param payerPhoneNumber Phone number of payer
     * @param payerCardId Bank card, to which funds will be transferred
     * @param beneficiaryCardId
     * @param amount
     * @param currencyId
     * @param shortDescription
     * @param fullDescription
     * @param deferPayout true - deferred deal, false - instant deal
     * @param callback
     * @return
     */


     public Deal create(String dealId, String payerId, String beneficiaryId, String payerPhoneNumber,
                        Integer payerCardId, int beneficiaryCardId, BigDecimal amount, CurrencyId currencyId,
                        String shortDescription, String fullDescription, boolean deferPayout, CompleteHandler<Deal, Throwable> callback){

         Map<String, Object> params = new HashMap<>();
         params.put("PlatformDealId", dealId);
         params.put("PlatformPayerId", payerId);
         params.put("PayerPhoneNumber", payerPhoneNumber);
         params.put("PlatformBeneficiaryId", beneficiaryId);
         params.put("BeneficiaryCardId", beneficiaryCardId);
         params.put("Amount", amount);
         params.put("CurrencyId", currencyId.getId());
         params.put("ShortDescription", shortDescription);
         params.put("FullDescription", fullDescription);
         params.put("DeferPayout", deferPayout ? "true" : "false");

         if(payerCardId != null){
             params.put("PayerCardId", payerCardId);
         }

        return core.networkManager.request(composer.deals(), NetworkManager.MethodType.POST, params, Deal.class, callback);
    }

    /** Complete deal
     *
     * @return completed deal
     */

     public Deal complete(String dealId, CompleteHandler<Deal, Throwable> callback){
        return core.networkManager.request(composer.dealsComplete(dealId), NetworkManager.MethodType.PUT, null, Deal.class, callback);
    }

    /** Cancel deal
     *
     * @return canceled deal
     */

     public Deal cancel(String dealId, CompleteHandler<Deal, Throwable> callback) {
        return core.networkManager.request(composer.dealsCancel(dealId), NetworkManager.MethodType.PUT, null, Deal.class, callback);
    }

    /** Get deal status
     *
     * @return deal
     */

     public Deal status(String dealId, CompleteHandler<Deal, Throwable> callback) {
        return core.networkManager.request(composer.deals(dealId), NetworkManager.MethodType.GET, null, Deal.class, callback);
    }

    /**
     *  Change bank card in deal.
     *  This action acceptable when deal status one of (Created | PaymentProcessing | PaymentProcessError | Paid | PayoutProcessError)
     *
     * @param autoComplete  Perfrom transaction after the card has been updated
     *
     * @return deal
     */

    public Deal set(int bankCard, String dealId, boolean autoComplete, CompleteHandler<Deal, Throwable> callback) {

        Map<String, Object> params = new HashMap<>();
        params.put("BeneficiaryCardId", bankCard);
        params.put("AutoComplete", autoComplete);

        return core.networkManager.request(composer.dealsBeneficiaryCard(dealId), NetworkManager.MethodType.PUT, params, Deal.class, callback);
    }

    /**
     * Pay Deal
     * @param dealId
     * @param redirectToCardAddition
     * @param authData
     * @param returnUrl
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */

    public RequestBuilder payRequest(String dealId, boolean redirectToCardAddition, @Nullable String authData, String returnUrl){

        final String urlString = composer.dealPay();
        final String timestamp = NetworkManager.ISO8601TimeStamp.getISO8601TimeStamp(new Date());

        Map<String, String> items = new TreeMap<>();
        items.put("PlatformDealId", dealId);
        items.put("PlatformId", core.getPlatformId());
        items.put("RedirectToCardAddition", redirectToCardAddition ? "true" : "false");
        items.put("ReturnUrl", returnUrl);
        items.put("Timestamp", timestamp);

        if (authData != null){
            items.put("AuthData", authData);
        }

        final String signature = core.networkManager.makeSignatureForWeb(items);
        items.put("Signature", signature);

        ArrayList<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry: items.entrySet()) {
            params.add(String.format("%s=%s", entry.getKey(), Uri.encode(entry.getValue(), "UTF-8")));
        }

        final  String queryString = TextUtils.join("&", params);
        System.out.print(queryString);
        Log.d("REQUESTS", queryString);

        RequestBuilder.Builder builder = RequestBuilder.newBuilder()
                .setMethodType(NetworkManager.MethodType.POST)
                .setSignature(signature)
                .setTimestamp(timestamp)
                .setUrlString(urlString)
                .setHttpBody(queryString);

        return builder.build();
    }

}
