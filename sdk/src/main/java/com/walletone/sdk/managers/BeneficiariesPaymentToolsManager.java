package com.walletone.sdk.managers;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.walletone.sdk.Manager;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteErrorOnlyHandler;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.library.URLComposer;
import com.walletone.sdk.models.PaymentTool;
import com.walletone.sdk.models.PaymentToolsResult;
import com.walletone.sdk.models.RequestBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class BeneficiariesPaymentToolsManager extends Manager {

    private Composer composer = new Composer();

    public BeneficiariesPaymentToolsManager(P2PCore core) {
        super(core);
    }

    /**
     * Get all payment tools of beneficiary
     *
     * @return all payment tools of beneficiary
     */
    public PaymentToolsResult paymentTools(CompleteHandler<PaymentToolsResult, Throwable> callback) {
        return core.networkManager.request(composer.beneficiariesTools(core.getBenificaryId()), NetworkManager.MethodType.GET, null, PaymentToolsResult.class, callback);
    }

    /**
     * Get payment tool of beneficiary by id
     *
     * @param paymentToolId Id paymentTool
     * @return paymentTool of beneficiary by id
     */
    public PaymentTool paymentTool(int paymentToolId, CompleteHandler<PaymentTool, Throwable> callback) {
        return core.networkManager.request(composer.beneficiariesToolsTool(core.getBenificaryId(), paymentToolId), NetworkManager.MethodType.GET, null, PaymentTool.class, callback);
    }

    /**
     * Delete linked paymentTool of beneficiary
     *
     * @param paymentToolId Id paymentTool
     */

    public void delete(int paymentToolId, CompleteErrorOnlyHandler<Throwable> callback) {
        core.networkManager.request(composer.beneficiariesToolsTool(core.getBenificaryId(), paymentToolId), NetworkManager.MethodType.DELETE, null, callback);
    }

    /**
     * Link new bank payment tool request
     *
     * @param returnUrl     Url to back user redirect
     * @param paymentTypeId new payment type title
     * @return request to API
     */
    public RequestBuilder addNewPaymentToolRequest(@NonNull String returnUrl, @Nullable String paymentTypeId, @Nullable Boolean redirectToPaymentToolAddition) {

        final String urlString = composer.beneficiary();
        final String timestamp = NetworkManager.ISO8601TimeStamp.getISO8601TimeStamp(new Date());

        Map<String, String> items = new TreeMap<>();
        items.put("PhoneNumber", core.getBenificaryPhoneNumber());
        items.put("PlatformBeneficiaryId", core.getBenificaryId());
        items.put("PlatformId", core.getPlatformId());
        items.put("ReturnUrl", returnUrl);
        items.put("Timestamp", timestamp);
        items.put("Title", core.getBenificaryTitle());

        if (paymentTypeId != null) {
            items.put("PaymentTypeId", paymentTypeId);
        }

        if (redirectToPaymentToolAddition != null) {
            items.put("RedirectToPaymentToolAddition", redirectToPaymentToolAddition ? "true" : "false");
        }

        final String signature = core.networkManager.makeSignatureForWeb(items);
        items.put("Signature", signature);

        ArrayList<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : items.entrySet()) {
            params.add(String.format("%s=%s", entry.getKey(), Uri.encode(entry.getValue(), "UTF-8")));
        }

        final String queryString = TextUtils.join("&", params);

        RequestBuilder.Builder builder = RequestBuilder.newBuilder()
                .setMethodType(NetworkManager.MethodType.POST)
                .setSignature(signature)
                .setTimestamp(timestamp)
                .setUrlString(urlString)
                .setHttpBody(queryString);

        return builder.build();
    }

    class Composer extends URLComposer {
        String beneficiary() {
            return getInstance().relativeToBase("v2/beneficiary");
        }

        String beneficiaries() {
            return getInstance().relativeToApi("beneficiaries");
        }

        String beneficiaries(String id) {
            return getInstance().relative(beneficiaries(), id);
        }

        String beneficiariesTools(String id) {
            return getInstance().relative(beneficiaries(id), "tools");
        }

        String beneficiariesToolsTool(String id, int tool) {
            return getInstance().relative(beneficiariesTools(id), String.valueOf(tool));
        }
    }
}
