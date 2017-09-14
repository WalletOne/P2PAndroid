package com.aronskiy_anton.p2p.controllers.deals;

import android.text.Editable;

import com.aronskiy_anton.p2p.BasePresenter;
import com.aronskiy_anton.p2p.BaseView;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.Employer;

import java.util.List;

/**
 * Created by anton on 13.09.2017.
 */

public class DealsContract {

    public interface Presenter extends BasePresenter{

        void loadDeals(boolean forceUpdate);

        void openDealDetails(Deal clickedDeal);

        void createDeal(String title, String description);

        boolean isAddButtonAvailable();
    }

    public interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean show);

        void showLoadingDealsError();

        boolean isActive();

        void showDeals(List<Deal> deals);

        void showNoDeals();

        void showAddDealDialog();
    }
}
