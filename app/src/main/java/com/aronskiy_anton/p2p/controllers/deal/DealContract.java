package com.aronskiy_anton.p2p.controllers.deal;

import com.aronskiy_anton.p2p.BasePresenter;
import com.aronskiy_anton.p2p.BaseView;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.DealRequest;

import java.util.List;

/**
 * Created by anton on 14.09.2017.
 */

public interface DealContract {

    interface Presenter extends BasePresenter{
        List<DealRequest> loadRequests();

        void addDealRequest(DealRequest request);

        void selectDealRequest(DealRequest request);
    }

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);

        void showAddRequestDialog();

        void setTitle(String title);

        void showDealInfo(Deal deal);

        void showMissingDeal();

        boolean isActive();
    }
}
