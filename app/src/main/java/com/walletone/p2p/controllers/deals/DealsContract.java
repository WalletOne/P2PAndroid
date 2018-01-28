package com.walletone.p2p.controllers.deals;

import com.walletone.p2p.BasePresenter;
import com.walletone.p2p.BaseView;
import com.walletone.p2p.models.Deal;
import com.walletone.p2p.models.UserTypeId;

import java.util.List;

/**
 * Created by anton on 13.09.2017.
 */

public interface DealsContract {

    interface Presenter extends BasePresenter{

        void loadDeals(boolean forceUpdate);

        void openDealDetails(Deal clickedDeal);

        void createDeal(String title, String description);

        boolean isAddButtonAvailable();

        UserTypeId getUserTypeId();
    }

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean show);

        void showLoadingDealsError();

        boolean isActive();

        void showDeals(List<Deal> deals);

        void showNoDeals();

        void showAddDealDialog();

        void showDealDetail(String id);
    }
}
