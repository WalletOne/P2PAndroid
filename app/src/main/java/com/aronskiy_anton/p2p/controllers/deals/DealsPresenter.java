package com.aronskiy_anton.p2p.controllers.deals;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.aronskiy_anton.p2p.data.P2PDataSource;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.UserTypeId;
import com.aronskiy_anton.sdk.P2PCore;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aaronskiy on 05.09.2017.
 */

public class DealsPresenter implements DealsContract.Presenter {

    UserTypeId typeId;

    private boolean mFirstLoad = true;

    private final DealsContract.View view;

    private final Repository repository;

    public DealsPresenter(@NonNull Repository repository, @NonNull DealsContract.View view, UserTypeId typeId) {
        this.repository = repository;
        this.view = view;
        this.typeId = typeId;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadDeals(false);
    }

    @Override
    public void loadDeals(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadDeals(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void openDealDetails(Deal clickedDeal) {
        view.showDealDetail(clickedDeal.getId());
    }

    @Override
    public void createDeal(String title, String description) {
        Deal newDeal = new Deal(title, description);
        repository.saveDeal(newDeal);
        loadDeals(true);
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link com.aronskiy_anton.p2p.data.P2PDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadDeals(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            view.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            repository.refreshDeals();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        // EspressoIdlingResource.increment(); // App is busy until further notice

        repository.getDeals(new P2PDataSource.LoadDealsCallback() {


            @Override
            public void onDealsLoaded(List<Deal> deals) {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }

                processDeals(deals);
                if (showLoadingUI) {
                    view.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                view.showLoadingDealsError();
            }
        });
    }

    private void processDeals(List<Deal> deals) {
        if (deals.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            view.showNoDeals();
        } else {
            // Show the list of deals
            view.showDeals(deals);
        }
    }

    @Override
    public boolean isAddButtonAvailable() {
        switch (typeId) {
            case EMPLOYER:
                return true;
            default:
                return false;
        }
    }
}
