package com.walletone.sdk.managers;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.walletone.sdk.Manager;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.constants.CurrencyId;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.library.URLComposer;
import com.walletone.sdk.models.Deal;
import com.walletone.sdk.models.DealsResult;
import com.walletone.sdk.models.RequestBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
            return getInstance().relativeToBase("deal");
        }

        String dealPay(){
            return getInstance().relative(deal(), "pay");
        }

        String deals(){
            return getInstance().relativeToApi("deals");
        }

        String deals(String dealId){
            return getInstance().relative(deals(), dealId);
        }

        String dealsComplete(String dealId){
            return getInstance().relative(deals(dealId), "complete");
        }

        String dealsComplete(){
            return getInstance().relative(deals(), "complete");
        }

        String dealsCancel(String dealId){
            return getInstance().relative(deals(dealId), "cancel");
        }

        String dealsBeneficiaryCard(String dealId){
            return getInstance().relative(deals(dealId),  "beneficiaryCard");
        }

        String beneficiaries(){
            return getInstance().relativeToApi("beneficiaries");
        }

        String beneficiaries(String id){
            return getInstance().relative(beneficiaries(), id);
        }

        String beneficiariesDeals(String beneficiaryId, Integer pageNumber, Integer itemsPerPage, @Nullable List<String> dealStates, @Nullable String searchString){
            List<String> params = new ArrayList<>();

            params.add(String.format(Locale.US, "pageNumber=%d", pageNumber));
            params.add(String.format(Locale.US, "itemsPerPage=%d", itemsPerPage));

            if(dealStates != null){
                if(dealStates.size() > 0){
                    params.add(String.format(Locale.US, "dealStates=%s", TextUtils.join(",", dealStates)));
                }
            }

            if(searchString != null){
                params.add(String.format(Locale.US, "searchString=%s", searchString));
            }

            return getInstance().relative(beneficiaries(beneficiaryId), "deals?" + TextUtils.join("&", params));
        }
    }

    /**
     * Create money request for payer
     *
     * @param dealId Deal identifier in your system
     * @param payerId Payer identifier in your system
     * @param beneficiaryId
     * @param payerPhoneNumber Phone number of payer
     * @param payerPaymentToolId payment tool ID, to which funds will be transferred
     * @param beneficiaryPaymentToolId  payment tool ID, from which funds will be transferred
     * @param amount
     * @param currencyId
     * @param shortDescription
     * @param fullDescription
     * @param deferPayout true - deferred deal, false - instant deal
     * @param callback
     * @return
     */


     public Deal create(String dealId, String payerId, String beneficiaryId, String payerPhoneNumber,
                        Integer payerPaymentToolId, Integer beneficiaryPaymentToolId, BigDecimal amount, CurrencyId currencyId,
                        String shortDescription, String fullDescription, boolean deferPayout, CompleteHandler<Deal, Throwable> callback){

         Map<String, Object> params = new HashMap<>();
         params.put("PlatformDealId", dealId);
         params.put("PlatformPayerId", payerId);
         params.put("PayerPhoneNumber", payerPhoneNumber);
         params.put("PlatformBeneficiaryId", beneficiaryId);
         params.put("BeneficiaryPaymentToolId", beneficiaryPaymentToolId);
         params.put("Amount", amount);
         params.put("CurrencyId", currencyId.getId());
         params.put("ShortDescription", shortDescription);
         params.put("FullDescription", fullDescription);
         params.put("DeferPayout", deferPayout ? "true" : "false");

         if(payerPaymentToolId != null){
             params.put("PayerPaymentToolId", payerPaymentToolId);
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

    /** Multiple complete deals
     *  This action acceptable when deal status one of (Paid | PayoutProcessError)
     *
     * @param dealIds identifiers of transactions in the side of the platform that you want to complete
     * @param paymentToolId the tool ID of the output to be set for all above transactions.
     *                      If not supplied, the method will attempt to withdraw to the account
     *                      specified in the transactions (only if all transactions have the same tool output)
     * @return completed deals list
     */

     public List<Deal> complete(@NonNull List<String> dealIds, @Nullable Integer paymentToolId, CompleteHandler<List<Deal>, Throwable> callback){

         Map<String, Object> params = new HashMap<>();
         params.put("PlatformDeals", dealIds);
         params.put("PaymentToolId", paymentToolId);

        return core.networkManager.requestList(composer.dealsComplete(), NetworkManager.MethodType.PUT, params, Deal.class, callback);
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
     *  Change payment tool in deal.
     *  This action acceptable when deal status one of (Created | PaymentProcessing | PaymentProcessError | Paid | CancelError | PayoutProcessError)
     *
     * @param autoComplete  Perfrom transaction after the paymentTool has been updated
     *
     * @return Deal
     *  full information about deal
     */

    public Deal set(Integer paymentToolId, String dealId, boolean autoComplete, CompleteHandler<Deal, Throwable> callback) {

        Map<String, Object> params = new HashMap<>();
        params.put("PaymentToolId", paymentToolId);
        params.put("AutoComplete", autoComplete);

        return core.networkManager.request(composer.dealsBeneficiaryCard(dealId), NetworkManager.MethodType.PUT, params, Deal.class, callback);
    }

    /**
     * Pay Deal
     * @param dealId
     * @param paymentTypeId
     * @param redirectToPaymentToolAddition
     * @param authData
     * @param returnUrl
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */

    public RequestBuilder payRequest(String dealId, @Nullable String paymentTypeId, @Nullable Boolean redirectToPaymentToolAddition, @Nullable String authData, String returnUrl){

        final String urlString = composer.dealPay();
        final String timestamp = NetworkManager.ISO8601TimeStamp.getISO8601TimeStamp(new Date());

        Map<String, String> items = new TreeMap<>();
        items.put("PlatformDealId", dealId);
        items.put("PlatformId", core.getPlatformId());
        items.put("ReturnUrl", returnUrl);
        items.put("Timestamp", timestamp);

        if (paymentTypeId != null) {
            items.put("PaymentTypeId", paymentTypeId);
        }

        if (redirectToPaymentToolAddition != null){
            items.put("RedirectToPaymentToolAddition", redirectToPaymentToolAddition ? "true" : "false");
        }

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

    /**
     *  Get deals list from beneficiary
     * @param beneficiaryId id beneficiary
     * @param pageNumber number of page
     * @param itemsPerPage items per page
     * @param dealStates states with delimeter ',' (comma)
     * @param searchString search substring
     * @param callback
     * @return DealsResult result
     */
    public DealsResult getDeals(@NonNull String beneficiaryId, @NonNull Integer pageNumber,
                                @NonNull Integer itemsPerPage, @Nullable List<String> dealStates,
                                @Nullable String searchString, CompleteHandler<DealsResult, Throwable> callback){
        return core.networkManager.request(composer.beneficiariesDeals(beneficiaryId, pageNumber, itemsPerPage, dealStates, searchString),
                NetworkManager.MethodType.GET, null, DealsResult.class,  callback);
    }

}
