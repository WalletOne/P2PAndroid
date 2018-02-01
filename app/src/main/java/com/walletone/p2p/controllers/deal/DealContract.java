package com.walletone.p2p.controllers.deal;

import com.walletone.p2p.BasePresenter;
import com.walletone.p2p.BaseView;
import com.walletone.p2p.models.Deal;
import com.walletone.p2p.models.DealRequest;
import com.walletone.p2p.models.UserTypeId;
import com.walletone.p2pui.library.Owner;

import java.util.List;

/**
 * Created by anton on 14.09.2017.
 */

public interface DealContract {

    interface Presenter extends BasePresenter{

        void loadRequests(boolean forceUpdate);

        void createDealRequest(String amount);

        void addCreatedDealRequest();

        void selectDealRequest(DealRequest request);

        UserTypeId getUserTypeId();

        void setSelectedPaymentToolId(int paymentToolId);

        boolean isFabShouldShown();

        void onRequestClick(DealRequest clickedRequest);

        void acceptDeal(DealRequest request);

        void cancelDeal(DealRequest request);

        void confirmDeal(DealRequest request);

        void createP2PDeal(Integer paymentToolId);

        void onPayRequestResultOk();

        void cancelRequest(DealRequest request);

        void completeRequest(DealRequest request);
    }

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);

        void showAddRequestDialog();

        void showDealInfo(Deal deal);

        void showMissingDeal();

        boolean isActive();

        void showLoadingRequestsError();

        void showNoRequests();

        void showRequests(List<DealRequest> requests);

        void showEmployerDialog(DealRequest request);

        void showFreelancerDialog(DealRequest request);

        void showPaymentToolActivityForSelect(Owner owner);

        void showPayDealActivity(String authData, String dealId);

        void updateAdapter();

        void setEmployerDealTitle();

        void setFreelancerDealTitle();

        void setRequestLoadingIndicator(boolean show);

        void showError(Throwable error);
    }
}
