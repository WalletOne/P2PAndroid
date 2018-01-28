package com.walletone.p2p.data;

import android.support.annotation.NonNull;


import com.walletone.p2p.models.Deal;
import com.walletone.p2p.models.DealRequest;

import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public interface P2PDataSource {

    interface LoadDealsCallback {

        void onDealsLoaded(List<Deal> deals);

        void onDataNotAvailable();
    }

    interface GetDealCallback {

        void onDealLoaded(Deal deal);

        void onDataNotAvailable();
    }

    interface LoadDealRequestsCallback {

        void onRequestsLoaded(List<DealRequest> requests);

        void onDataNotAvailable();
    }

    void getDeals(@NonNull LoadDealsCallback callback);

    void getDeal(@NonNull String dealId, @NonNull GetDealCallback callback);

    void refreshDeals();

    void saveDeal(@NonNull Deal deal);

    void getDealRequests(@NonNull String dealId, @NonNull LoadDealRequestsCallback callback);

    void saveRequest(@NonNull DealRequest request);

    void cancelRequest(@NonNull DealRequest request);

    void completeRequest(@NonNull DealRequest request);

    Deal getDealByIdFromCache(String dealId);
}
