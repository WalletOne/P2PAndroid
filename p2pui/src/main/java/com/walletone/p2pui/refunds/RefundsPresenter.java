package com.walletone.p2pui.refunds;

import android.support.annotation.NonNull;

import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.models.Refund;
import com.walletone.sdk.models.RefundsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 12.09.2017.
 */

public class RefundsPresenter implements RefundsContract.Presenter {

    private String dealId;

    private boolean firstLoad = true;

    private boolean isLoading = false;

    private boolean isLoadMoreInProgress = false;

    private boolean isAllowLoadMore = true;

    private Integer pageNumber = 0;

    private Integer itemsPerPage = 10;

    private List<Refund> refunds;

    private RefundsContract.View refundsView;

    public RefundsPresenter(@NonNull String dealId, @NonNull RefundsContract.View refundsView) {
        this.dealId = dealId;
        this.refundsView = refundsView;
        this.refundsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadRefunds(false);
    }

    @Override
    public void loadRefunds(boolean forceUpdate) {
        loadRefunds(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadRefunds(boolean forceUpdate, final boolean showLoadingUI) {

        CompleteHandler<RefundsResult, Throwable> handler = new CompleteHandler<RefundsResult, Throwable>() {
            @Override
            public void completed(RefundsResult list, Throwable error) {

                isLoading = false;
                if(error == null) {
                    List<Refund> refundsList = list.getRefunds();
                    pageNumber += 1;
                    isAllowLoadMore = refundsList.size() >= itemsPerPage;
                    if (!isAllowLoadMore) {
                        refundsView.setAllDataAreLoaded();
                    }
                    refunds = refundsList;
                    if (refunds.size() > 0) {
                        refundsView.showRefunds(refunds);
                    } else {
                        refundsView.showEmptyList();
                    }
                } else {
                    refundsView.showError(error);
                }
            }
        };

        isLoading = true;

        refunds = new ArrayList<>();

        this.pageNumber = 1;
        P2PCore.INSTANCE.refundsManager.refunds(pageNumber, itemsPerPage, dealId, handler);
    }


    @Override
    public void loadMore() {
        CompleteHandler<RefundsResult, Throwable> handler = new CompleteHandler<RefundsResult, Throwable>() {
            @Override
            public void completed(RefundsResult list, Throwable var2) {

                List<Refund> refundsList = list.getRefunds();
                isLoadMoreInProgress = false;
                refunds.addAll(refundsList);
                pageNumber += 1;
                isAllowLoadMore = !refundsList.isEmpty() && refundsList.size() >= itemsPerPage;

                if (!isAllowLoadMore) {
                    refundsView.setAllDataAreLoaded();
                }
                if (refunds.size() > 0) {
                    refundsView.showRefunds(refunds);
                }

            }
        };

        if (isAllowLoadMore) {
            P2PCore.INSTANCE.refundsManager.refunds(pageNumber, itemsPerPage, dealId, handler);
        }
    }
}
