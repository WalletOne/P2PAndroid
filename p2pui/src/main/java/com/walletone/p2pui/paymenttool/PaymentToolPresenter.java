package com.walletone.p2pui.paymenttool;

import android.support.annotation.NonNull;

import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteErrorOnlyHandler;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.models.PaymentTool;
import com.walletone.sdk.models.PaymentToolsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class PaymentToolPresenter implements PaymentToolContract.Presenter {

    public enum Owner {
        BENEFICIARY,
        PAYER
    }

    private Owner owner;

    private PaymentToolContract.View paymentToolView;

    private boolean firstLoad = true;

    private boolean isLoading = false;

    private boolean isAddPaymentToolAvailable = false;

    private List<PaymentTool> paymentTools;

    public PaymentToolPresenter(@NonNull Owner owner, @NonNull PaymentToolContract.View view) {
        this.owner = owner;
        this.paymentToolView = view;
        this.paymentToolView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPaymentTools(false);
    }

    @Override
    public void loadPaymentTools(boolean forceUpdate) {
        loadPaymentTools(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadPaymentTools(boolean forceUpdate, final boolean showLoadingUI) {

        paymentToolView.setLoadingIndicator(true);

        CompleteHandler<PaymentToolsResult, Throwable> handler = new CompleteHandler<PaymentToolsResult, Throwable>() {
            @Override
            public void completed(PaymentToolsResult result, Throwable error) {

                List<PaymentTool> paymentToolsList = result.getPaymentTools();

                paymentToolView.setLoadingIndicator(false);
                isLoading = false;
                if(error == null) {
                    paymentTools = paymentToolsList != null ? paymentToolsList : new ArrayList<PaymentTool>();
                    if (paymentTools.size() > 0) {
                        paymentToolView.showPaymentTools(paymentTools);
                    } else {
                        paymentToolView.showEmptyList();
                    }
                } else {
                    paymentToolView.showError(error);
                }
            }
        };

        isLoading = true;

        paymentTools = new ArrayList<>();

        switch (owner) {
            case BENEFICIARY:
                P2PCore.INSTANCE.beneficiariesPaymentTools.paymentTools(handler);
                break;
            case PAYER:
                P2PCore.INSTANCE.payersPaymentTools.paymentTools(handler);
                break;
        }
    }

    @Override
    public void addNewPaymentTool() {
        switch (owner) {
            case BENEFICIARY:
                paymentToolView.showLinkPaymentToolActivity();
                break;
            case PAYER:
                paymentToolView.closePaymentToolAndShowPayDealActivity();
                break;
        }
    }

    @Override
    public boolean isAddNewPaymentToolAvailable() {
        return isAddPaymentToolAvailable || owner == Owner.BENEFICIARY;
    }

    @Override
    public void setAddPaymentToolAvailable(boolean isAvailable) {
        this.isAddPaymentToolAvailable = isAvailable;
    }

    @Override
    public void deletePaymentTool(final PaymentTool paymentTool) {

        CompleteErrorOnlyHandler<Throwable> callback = new CompleteErrorOnlyHandler<Throwable>() {
            @Override
            public void completed(Throwable error) {
                if (error == null){
                    paymentTools.remove(paymentTool);
                } else {
                    paymentToolView.showError(error);
                }
            }
        };

        switch (owner) {
            case BENEFICIARY:
                P2PCore.INSTANCE.beneficiariesPaymentTools.delete(paymentTool.getPaymentToolId(), callback);
                break;
            case PAYER:
                P2PCore.INSTANCE.payersPaymentTools.delete(paymentTool.getPaymentToolId(), callback);
                break;
        }
    }
}
