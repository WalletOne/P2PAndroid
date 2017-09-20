package com.aronskiy_anton.p2p.controllers.deal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aronskiy_anton.p2p.data.P2PDataSource;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.DealRequest;
import com.aronskiy_anton.p2p.models.UserTypeId;
import com.aronskiy_anton.p2pui.linkcard.LinkCardActivity;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.CompleteHandler;

import java.math.BigDecimal;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.aronskiy_anton.p2pui.bankcard.BankCardPresenter.Owner.PAYER;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_COMPLETED;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_CREATED;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_PAID;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_PAYMENT_PROCESS_ERROR;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_PAYOUT_PROCESSING;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_PAYOUT_PROCESS_ERROR;
import static com.aronskiy_anton.sdk.models.Deal.DEAL_STATE_ID_PROCESSING;

/**
 * Created by anton on 14.09.2017.
 */

public class DealPresenter implements DealContract.Presenter {

    private UserTypeId userTypeId;

    private final Repository repository;

    private final DealContract.View detailView;

    private boolean mFirstLoad = true;

    private DealRequest creatingRequest;

    private DealRequest selectedRequest;

    private Deal deal;

    private String dealId;

    public DealPresenter(@NonNull Repository repository, @NonNull DealContract.View detailView, String dealId, UserTypeId userTypeId) {
        this.repository = repository;
        this.detailView = detailView;
        this.dealId = dealId;
        this.userTypeId = userTypeId;
        detailView.setPresenter(this);
        setDealById(dealId);
    }

    private void setDealById(String dealId) {
        deal = repository.getDealByIdFromCache(dealId);
    }

    @Override
    public void start() {
        openDeal();
        loadRequests(false);
    }

    private void openDeal() {
        if (dealId == null || dealId.length() == 0) {
            detailView.showMissingDeal();
            return;
        }
        detailView.setLoadingIndicator(true);

        repository.getDeal(dealId, new P2PDataSource.GetDealCallback() {
            @Override
            public void onDealLoaded(Deal deal) {
                if (!detailView.isActive()) {
                    return;
                }

                detailView.setLoadingIndicator(false);

                if (null == deal) {
                    detailView.showMissingDeal();
                } else {
                    detailView.showDealInfo(deal);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!detailView.isActive()) {
                    return;
                }
                detailView.showMissingDeal();
            }
        });
    }

    @Override
    public void loadRequests(boolean forceUpdate) {
        loadRequests(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadRequests(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            detailView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            //repository.refreshDeals();
        }

        repository.getDealRequests(dealId, new P2PDataSource.LoadDealRequestsCallback() {

            @Override
            public void onRequestsLoaded(List<DealRequest> requests) {
                // The view may not be able to handle UI updates anymore
                if (!detailView.isActive()) {
                    return;
                }

                processRequests(requests);

                if (showLoadingUI) {
                    detailView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!detailView.isActive()) {
                    return;
                }
                detailView.showLoadingRequestsError();
            }
        });
    }

    private void processRequests(List<DealRequest> requests) {
        if (requests.isEmpty()) {
            detailView.showNoRequests();
        } else {
            // Show the list of requests
            detailView.showRequests(requests);
        }
    }

    public DealRequest getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(DealRequest selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    @Override
    public void createDealRequest(String amount) {
        creatingRequest = new DealRequest(dealId, repository.getFreelancer(), new BigDecimal(amount));
    }

    @Override
    public void addCreatedDealRequest() {
        if (creatingRequest != null) {
            repository.saveRequest(creatingRequest);
        }
        loadRequests(true);
    }

    @Override
    public void selectDealRequest(DealRequest request) {

    }

    @Override
    public UserTypeId getUserTypeId() {
        return userTypeId;
    }

    @Override
    public void setSelectedCardId(int cardId) {
        if (creatingRequest != null) {
            creatingRequest.setFreelancerCardId(cardId);
        }
    }

    @Override
    public boolean isFabShouldShown() {
        return userTypeId == UserTypeId.FREELANCER;
    }

    @Override
    public void onRequestClick(DealRequest clickedRequest) {
        switch (userTypeId) {
            case EMPLOYER:
                detailView.showEmployerDialog(clickedRequest);
                break;
            case FREELANCER:
                detailView.showFreelancerDialog(clickedRequest);
                break;
        }
    }

    @Override
    public void acceptDeal(DealRequest request) {
        this.selectedRequest = request;
        detailView.showBankCardActivityForSelect(PAYER);
    }

    @Override
    public void cancelDeal(final DealRequest request) {
        request.setStateId(DealRequest.DealStateId.canceling);
        //selectedRequest.setStateId(DealRequest.DealStateId.canceling);
        P2PCore.INSTANCE.dealsManager.cancel(request.getDealId(), new CompleteHandler<com.aronskiy_anton.sdk.models.Deal, Throwable>() {
            @Override
            public void completed(com.aronskiy_anton.sdk.models.Deal var1, Throwable var2) {
                request.setStateId(DealRequest.DealStateId.canceled);
                //selectedRequest.setStateId(DealRequest.DealStateId.canceled);
                loadRequests(true);
            }
        });
    }

    @Override
    public void confirmDeal(final DealRequest request) {
        selectedRequest = request;

        request.setStateId(DealRequest.DealStateId.confirming);
        detailView.updateAdapter();
        P2PCore.INSTANCE.dealsManager.complete(dealId, new CompleteHandler<com.aronskiy_anton.sdk.models.Deal, Throwable>() {
            @Override
            public void completed(com.aronskiy_anton.sdk.models.Deal deal, Throwable var2) {
                //request.setStateId(DealRequest.DealStateId.completed);
                checkStatus();
            }
        });
    }

    private void checkStatus() {

        P2PCore.INSTANCE.dealsManager.status(dealId, new CompleteHandler<com.aronskiy_anton.sdk.models.Deal, Throwable>() {
            @Override
            public void completed(com.aronskiy_anton.sdk.models.Deal deal, Throwable var2) {

                if (selectedRequest == null) return;
                DealRequest request = selectedRequest;
                switch (deal.getDealStateId()) {
                    case DEAL_STATE_ID_PROCESSING:
                        selectedRequest.setStateId(DealRequest.DealStateId.paymentProcessing);
                        detailView.updateAdapter();
                        checkStatus();
                        break;
                    case DEAL_STATE_ID_PAYMENT_PROCESS_ERROR:
                        selectedRequest.setStateId(DealRequest.DealStateId.paymentError);
                        break;
                    case DEAL_STATE_ID_PAID:
                        selectedRequest.setStateId(DealRequest.DealStateId.paid);
                        break;
                    case DEAL_STATE_ID_PAYOUT_PROCESSING:
                        selectedRequest.setStateId(DealRequest.DealStateId.payoutProcessing);
                        detailView.updateAdapter();
                        checkStatus();
                        break;
                    case DEAL_STATE_ID_PAYOUT_PROCESS_ERROR:
                        selectedRequest.setStateId(DealRequest.DealStateId.payoutProcessingError);
                        break;
                    case DEAL_STATE_ID_COMPLETED:
                        selectedRequest.setStateId(DealRequest.DealStateId.done);
                        break;
                }
                loadRequests(true);
            }
        });
    }

    @Override
    public void createP2PDeal(final Integer cardId) {
        final DealRequest request = selectedRequest;
        P2PCore.INSTANCE.dealsManager.create(
                dealId,
                deal.getEmployer().getId(),
                request.getFreelancer().getId(),
                deal.getEmployer().getPhoneNumber(),
                cardId,
                request.getFreelancerCardId(),
                request.getAmount(),
                CurrencyId.RUB,
                deal.getShortDescription(),
                deal.getFullDescription(),
                true,
                new CompleteHandler<com.aronskiy_anton.sdk.models.Deal, Throwable>() {
                    @Override
                    public void completed(com.aronskiy_anton.sdk.models.Deal deal, Throwable var2) {
                        detailView.showAlertToEnterCVV(cardId == null);
                    }
                }
        );
    }

    @Override
    public void onPayDealButtonClicked(String authData) {
        detailView.showPayDealActivity(authData, dealId);
    }

    @Override
    public void onPayRequestResultOk() {
        checkStatus();
    }

    @Override
    public void cancelRequest(DealRequest request) {
        repository.cancelRequest(request);
        loadRequests(true);
    }

    @Override
    public void completeRequest(DealRequest request) {
        request.setStateId(DealRequest.DealStateId.completed);
        //selectedRequest.setStateId(DealRequest.DealStateId.completed);
        repository.completeRequest(request);
        loadRequests(true);
    }

}
