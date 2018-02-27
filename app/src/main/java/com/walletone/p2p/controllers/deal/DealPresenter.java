package com.walletone.p2p.controllers.deal;

import android.support.annotation.NonNull;

import com.walletone.p2p.data.P2PDataSource;
import com.walletone.p2p.data.Repository;
import com.walletone.p2p.models.Deal;
import com.walletone.p2p.models.DealRequest;
import com.walletone.p2p.models.UserTypeId;
import com.walletone.p2pui.library.Owner;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.constants.CurrencyId;
import com.walletone.sdk.library.CompleteHandler;

import java.math.BigDecimal;
import java.util.List;

import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_COMPLETED;
import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_PAID;
import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_PAYMENT_HOLD;
import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_PAYMENT_PROCESS_ERROR;
import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_PAYOUT_PROCESSING;
import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_PAYOUT_PROCESS_ERROR;
import static com.walletone.sdk.models.Deal.DEAL_STATE_ID_PROCESSING;

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
        loadRequests(true);
        setViewTitle();
    }

    private void setViewTitle() {
        switch (userTypeId) {
            case EMPLOYER:
                detailView.setEmployerDealTitle();
                break;
            case FREELANCER:
                detailView.setFreelancerDealTitle();
                break;
        }
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
            detailView.setRequestLoadingIndicator(true);
        }
        /*
        if (forceUpdate) {
            //repository.refreshDeals();
        }
*/
        repository.getDealRequests(dealId, new P2PDataSource.LoadDealRequestsCallback() {

            @Override
            public void onRequestsLoaded(List<DealRequest> requests) {
                // The view may not be able to handle UI updates anymore
                if (!detailView.isActive()) {
                    return;
                }

                processRequests(requests);

                if (showLoadingUI) {
                    detailView.setRequestLoadingIndicator(false);
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
    public void setSelectedPaymentToolId(int paymentToolId) {
        if (creatingRequest != null) {
            creatingRequest.setFreelancerPaymentToolId(paymentToolId);
        }
    }

    @Override
    public boolean isFabShouldShown() {
        if (userTypeId == UserTypeId.FREELANCER) {
            return !repository.isDealRequestAlreadyExist(dealId);
        } else {
            return false;
        }
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
        detailView.showPaymentToolActivityForSelect(Owner.PAYER);
    }

    @Override
    public void cancelDeal(final DealRequest request) {
        request.setStateId(DealRequest.DealStateId.canceling);
        //selectedRequest.setStateId(DealRequest.DealStateId.canceling);
        P2PCore.INSTANCE.dealsManager.cancel(request.getDealId(), new CompleteHandler<com.walletone.sdk.models.Deal, Throwable>() {
            @Override
            public void completed(com.walletone.sdk.models.Deal var1, Throwable var2) {
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
        P2PCore.INSTANCE.dealsManager.complete(dealId, new CompleteHandler<com.walletone.sdk.models.Deal, Throwable>() {
            @Override
            public void completed(com.walletone.sdk.models.Deal deal, Throwable var2) {
                //request.setStateId(DealRequest.DealStateId.completed);
                checkStatus();
            }
        });
    }

    private void checkStatus() {

        P2PCore.INSTANCE.dealsManager.status(dealId, new CompleteHandler<com.walletone.sdk.models.Deal, Throwable>() {
            @Override
            public void completed(com.walletone.sdk.models.Deal deal, Throwable error) {
                if(error == null) {
                    if (selectedRequest == null) return;
                    DealRequest request = selectedRequest;
                    switch (deal.getDealStateId()) {
                        case DEAL_STATE_ID_PROCESSING:
                            selectedRequest.setStateId(DealRequest.DealStateId.paymentProcessing);
                            detailView.updateAdapter();
                            checkStatus();
                            break;
                        case DEAL_STATE_ID_PAYMENT_HOLD:
                            selectedRequest.setStateId(DealRequest.DealStateId.paymentHold);
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
                } else {
                    detailView.showError(error);
                }
                loadRequests(true);
            }
        });
    }

    @Override
    public void createP2PDeal(final Integer paymentToolId) {
        final DealRequest request = selectedRequest;
        P2PCore.INSTANCE.dealsManager.create(
                dealId,
                request.getFreelancer().getId(), // beneficiary Id
                paymentToolId,  // payer payment tool id optional
                request.getFreelancerPaymentToolId(), // beneficiary payment tool id
                request.getAmount(), // amount
                CurrencyId.RUB, // currency
                deal.getShortDescription(), // short description
                deal.getFullDescription(), // full description
                true,
                new CompleteHandler<com.walletone.sdk.models.Deal, Throwable>() {
                    @Override
                    public void completed(com.walletone.sdk.models.Deal deal, Throwable var2) {
                        detailView.showPayDealActivity("", dealId);
                    }
                }
        );
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
