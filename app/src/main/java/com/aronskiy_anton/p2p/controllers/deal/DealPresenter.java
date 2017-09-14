package com.aronskiy_anton.p2p.controllers.deal;

import android.support.annotation.NonNull;

import com.aronskiy_anton.p2p.data.P2PDataSource;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.DealRequest;

import java.util.List;

/**
 * Created by anton on 14.09.2017.
 */

public class DealPresenter implements DealContract.Presenter {

    private final Repository repository;

    private final DealContract.View detailView;

    private String dealId;

    public DealPresenter(@NonNull Repository repository, @NonNull DealContract.View detailView, String dealId) {
        this.repository = repository;
        this.detailView = detailView;
        this.dealId = dealId;
    }

    @Override
    public void start() {
        openDeal();
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

                if(null == deal){
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
    public List<DealRequest> loadRequests() {
        return null;
    }

    @Override
    public void addDealRequest(DealRequest request) {

    }

    @Override
    public void selectDealRequest(DealRequest request) {

    }
}
