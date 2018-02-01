package com.walletone.p2pui.payouts;

import android.support.annotation.NonNull;

import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.models.Payout;
import com.walletone.sdk.models.PayoutResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 12.09.2017.
 */

public class PayoutsPresenter implements PayoutsContract.Presenter {

    private String dealId;

    private boolean firstLoad = true;

    private boolean isLoading = false;

    private boolean isLoadMoreInProgress = false;

    private boolean isAllowLoadMore = true;

    private Integer pageNumber = 0;

    private Integer itemsPerPage = 10;

    private List<Payout> payouts;

    private PayoutsContract.View payoutsView;

    public PayoutsPresenter(@NonNull String dealId, @NonNull PayoutsContract.View payoutsView) {
        this.dealId = dealId;
        this.payoutsView = payoutsView;
        this.payoutsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPayouts(false);
    }

    @Override
    public void loadPayouts(boolean forceUpdate) {
        loadPayouts(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadPayouts(boolean forceUpdate, final boolean showLoadingUI) {

        CompleteHandler<PayoutResult, Throwable> handler = new CompleteHandler<PayoutResult, Throwable>() {
            @Override
            public void completed(PayoutResult list, Throwable error) {

                isLoading = false;
                if(error == null) {
                    List<Payout> payoutsList = list.getPayouts();
                    pageNumber += 1;
                    isAllowLoadMore = payoutsList.size() >= itemsPerPage;
                    if (!isAllowLoadMore) {
                        payoutsView.setAllDataAreLoaded();
                    }
                    payouts = payoutsList;
                    if (payouts.size() > 0) {
                        payoutsView.showPayouts(payouts);
                    } else {
                        payoutsView.showEmptyList();
                    }
                } else {
                    payoutsView.showError(error);
                }
            }
        };

        isLoading = true;

        payouts = new ArrayList<>();

        this.pageNumber = 1;
        P2PCore.INSTANCE.payoutsManager.payouts(pageNumber, itemsPerPage, dealId, handler);
    }

    @Override
    public void loadMore() {

        CompleteHandler<PayoutResult, Throwable> handler = new CompleteHandler<PayoutResult, Throwable>() {
            @Override
            public void completed(PayoutResult list, Throwable var2) {

                List<Payout> payoutsList = list.getPayouts();
                isLoadMoreInProgress = false;
                payouts.addAll(payoutsList);
                pageNumber += 1;
                isAllowLoadMore = !payoutsList.isEmpty() && payoutsList.size() >= itemsPerPage;

                if (!isAllowLoadMore) {
                    payoutsView.setAllDataAreLoaded();
                }
                if (payouts.size() > 0) {
                    payoutsView.showPayouts(payouts);
                }

            }
        };

        if (isAllowLoadMore) {
            P2PCore.INSTANCE.payoutsManager.payouts(pageNumber, itemsPerPage, dealId, handler);
        }
    }
}
