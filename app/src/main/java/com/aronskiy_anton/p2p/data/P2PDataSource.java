package com.aronskiy_anton.p2p.data;

import android.support.annotation.NonNull;


import com.aronskiy_anton.p2p.models.Deal;

import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public interface P2PDataSource {

    public interface LoadDealsCallback {

        void onDealsLoaded(List<Deal> deals);

        void onDataNotAvailable();
    }

    public interface GetDealCallback {

        void onDealLoaded(Deal deals);

        void onDataNotAvailable();
    }

    void getDeals(@NonNull LoadDealsCallback callback);

    void getDeal(@NonNull String dealId, @NonNull GetDealCallback callback);

    void refreshDeals();

    void saveDeal(@NonNull Deal deal);

}
