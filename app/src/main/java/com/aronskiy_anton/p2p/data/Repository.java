package com.aronskiy_anton.p2p.data;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.Freelancer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class Repository implements P2PDataSource {

    private static Repository INSTANCE = null;

    private static final int SERVICE_LATENCY_IN_MILLIS = 1000;

    private Employer employer;

    private Freelancer freelancer;

    private final P2PDataSource localDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Deal> cachedDeals;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    public Repository(@NonNull P2PDataSource localDataSource) {
        this.localDataSource = localDataSource;

        cachedDeals = new LinkedHashMap<>(2);
        addDeal("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addDeal("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static Repository getInstance(P2PDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getDeals(final @NonNull LoadDealsCallback callback) {
        // Simulate network by delaying the execution.
        if(cachedDeals != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onDealsLoaded(new ArrayList<>(cachedDeals.values()));
                }
            }, SERVICE_LATENCY_IN_MILLIS);
        }
    }

    private void addDeal(String title, String description) {
        Deal newDeal = new Deal(title, description);
        cachedDeals.put(newDeal.getId(), newDeal);
    }

    @Override
    public void getDeal(@NonNull String cardId, final  @NonNull GetDealCallback callback) {
        final Deal deal = cachedDeals.get(cardId);

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
        this.cacheIsDirty = true;
    }

    @Override
    public void saveDeal(@NonNull Deal deal) {
        // Do in memory cache update to keep the app UI up to date
        if (cachedDeals == null) {
            cachedDeals = new LinkedHashMap<>();
        }
        cachedDeals.put(deal.getId(), deal);
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Freelancer getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }
}
