package com.aronskiy_anton.p2pui.paymenttool;

import com.aronskiy_anton.p2pui.BasePresenter;
import com.aronskiy_anton.p2pui.BaseView;
import com.aronskiy_anton.sdk.models.PaymentTool;

import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public interface PaymentToolContract {

    interface Presenter extends BasePresenter{
        void loadPaymentTools(boolean forceUpdate);

        void addNewPaymentTool();

        boolean isAddNewPaymentToolAvailable();

        void setAddPaymentToolAvailable(boolean isAvailable);

        void deletePaymentTool(PaymentTool paymentTool);
    }

    interface View extends BaseView<Presenter>{
        void showPaymentTools(List<PaymentTool> tasks);

        void showEmptyList();

        void showLinkPaymentToolActivity();

        void setLoadingIndicator(boolean show);

        void closePaymentToolAndShowPayDealActivity();

        void showError(Throwable error);
    }
}
