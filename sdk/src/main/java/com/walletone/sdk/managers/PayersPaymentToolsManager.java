package com.walletone.sdk.managers;

import com.walletone.sdk.Manager;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteErrorOnlyHandler;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.library.URLComposer;
import com.walletone.sdk.models.PaymentTool;
import com.walletone.sdk.models.PaymentToolsResult;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class PayersPaymentToolsManager extends Manager {

    private Composer composer = new Composer();

    public PayersPaymentToolsManager(P2PCore core) {
        super(core);
    }

    class Composer extends URLComposer {

        String payers() {
            return getInstance().relativeToApi("payers");
        }

        String payers(String id) {
            return getInstance().relative(payers(), id);
        }

        String payersTools(String id) {
            return getInstance().relative(payers(id), "tools");
        }

        String payersToolsTool(String id, Integer tool) {
            return getInstance().relative(payersTools(id), String.valueOf(tool));
        }
    }

    /**
     * Get all payments tools of payer
     *
     * @param callback callback from server
     * @return all payments tools of payer
     */
    public PaymentToolsResult paymentTools(CompleteHandler<PaymentToolsResult, Throwable> callback) {
        return core.networkManager.request(composer.payersTools(core.getPayerId()), NetworkManager.MethodType.GET, null, PaymentToolsResult.class, callback);
    }

    /**
     * Get payment tool of payer by id
     *
     * @param paymentToolId Id payment Tool
     * @param callback      callback from server
     * @return payment tool of payer by id
     */
    public PaymentTool paymentTool(Integer paymentToolId, CompleteHandler<PaymentTool, Throwable> callback) {
        return core.networkManager.request(composer.payersToolsTool(core.getBenificaryId(), paymentToolId), NetworkManager.MethodType.GET, null, PaymentTool.class, callback);
    }

    /**
     * Delete linked payment Tool of payer
     *
     * @param paymentToolId Id payment Tool
     * @param callback      callback from server
     */

    public void delete(Integer paymentToolId, CompleteErrorOnlyHandler callback) {
        core.networkManager.request(composer.payersToolsTool(core.getBenificaryId(), paymentToolId), NetworkManager.MethodType.DELETE, null, callback);
    }
}
