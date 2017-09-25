package com.aronskiy_anton.p2p.data;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.DealRequest;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.Freelancer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class Repository implements P2PDataSource {

    private static Repository INSTANCE = null;

    private static final int SERVICE_LATENCY_IN_MILLIS = 1000;

    private Employer employer;

    private Freelancer freelancer;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private Map<String, Deal> cachedDeals;

    private Map<String, List<DealRequest>> cachedDealRequests = new LinkedHashMap<>();

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    public Repository() {
        cachedDeals = new LinkedHashMap<>();
    }

    public static Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public boolean isDealRequestAlreadyExist(String dealId){
        if(cachedDealRequests.containsKey(dealId)){
            for (DealRequest request : cachedDealRequests.get(dealId)){
                if(request.getFreelancer().equals(freelancer)){
                    return true;
                }
            }
            return false;
        } else return false;
    }

    @Override
    public void getDeals(final @NonNull LoadDealsCallback callback) {
        // Simulate network by delaying the execution.
        if (cachedDeals != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onDealsLoaded(new ArrayList<>(cachedDeals.values()));
                }
            }, SERVICE_LATENCY_IN_MILLIS);
        }
    }

    @Override
    public void getDeal(@NonNull final String dealId, final @NonNull GetDealCallback callback) {
        final Deal deal = cachedDeals.get(dealId);

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
    public Deal getDealByIdFromCache(String dealId) {
        return cachedDeals.containsKey(dealId) ? cachedDeals.get(dealId) : null;
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

    @Override
    public void getDealRequests(@NonNull final String dealId, @NonNull final LoadDealRequestsCallback callback) {

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cachedDealRequests != null) {
                    List<DealRequest> list = cachedDealRequests.get(dealId);
                    callback.onRequestsLoaded(list != null ? list : new ArrayList<DealRequest>());

                } else {
                    callback.onRequestsLoaded(new ArrayList<DealRequest>());
                }
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveRequest(@NonNull DealRequest request) {
        // Do in memory cache update to keep the app UI up to date
        if (cachedDealRequests == null) {
            cachedDealRequests = new LinkedHashMap<>();
        }
        String dealId = request.getDealId();
        if (cachedDealRequests.containsKey(dealId)) {
            cachedDealRequests.get(dealId).add(request);
        } else {
            List<DealRequest> newList = new ArrayList<>();
            newList.add(request);
            cachedDealRequests.put(dealId, newList);
        }
    }

    @Override
    public void cancelRequest(@NonNull DealRequest request) {
        if (cachedDealRequests != null) {
            if (cachedDealRequests.containsKey(request.getDealId())) {
                cachedDealRequests.remove(request.getDealId());
            }
        }
    }

    @Override
    public void completeRequest(@NonNull DealRequest request) {
        if (cachedDealRequests != null) {
            if (cachedDealRequests.containsKey(request.getDealId())) {
                cachedDealRequests.get(request.getDealId());
            }
        }
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
