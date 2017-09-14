package com.aronskiy_anton.p2p.data;

import android.os.Handler;
import android.support.annotation.NonNull;


import com.aronskiy_anton.p2p.models.Deal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by anton on 13.09.2017.
 */

public class RemoteDataSource implements P2PDataSource {

    private static RemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, Deal> DEALS_SERVICE_DATA = new LinkedHashMap<>();

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RemoteDataSource() {}

    private static void addDeal(String title, String description) {
       /* Deal newDeal = new Deal(title, description);
        DEALS_SERVICE_DATA.put(newDeal.getId(), newDeal);*/
    }


    @Override
    public void getDeals(final @NonNull LoadDealsCallback callback) {
        // Simulate network by delaying the execution.
       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onDealsLoaded(Lists.newArrayList(DEALS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);*/
    }

    @Override
    public void getDeal(@NonNull String dealId, final @NonNull GetDealCallback callback) {
        final Deal deal = DEALS_SERVICE_DATA.get(dealId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onDealLoaded(deal);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void refreshDeals() {

    }

    @Override
    public void saveDeal(@NonNull Deal deal) {
        DEALS_SERVICE_DATA.put(deal.getId(), deal);
    }
}
