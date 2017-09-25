package com.aronskiy_anton.p2p.controllers.deal;

import com.aronskiy_anton.p2p.BasePresenter;
import com.aronskiy_anton.p2p.BaseView;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.DealRequest;
import com.aronskiy_anton.p2p.models.UserTypeId;
import com.aronskiy_anton.p2pui.bankcard.BankCardPresenter;

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

        void setSelectedCardId(int cardId);

        boolean isFabShouldShown();

        void onRequestClick(DealRequest clickedRequest);

        void acceptDeal(DealRequest request);

        void cancelDeal(DealRequest request);

        void confirmDeal(DealRequest request);

        void createP2PDeal(Integer cardId);

        void onPayDealButtonClicked(String authData);

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

        void showBankCardActivityForSelect(BankCardPresenter.Owner owner);

        void showAlertToEnterCVV(boolean redirectToCardAddition);

        void showPayDealActivity(String authData, String dealId);

        void updateAdapter();

        void setEmployerDealTitle();

        void setFreelancerDealTitle();

        void setRequestLoadingIndicator(boolean show);
    }
}
