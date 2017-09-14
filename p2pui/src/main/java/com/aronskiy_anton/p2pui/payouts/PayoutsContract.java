package com.aronskiy_anton.p2pui.payouts;

import com.aronskiy_anton.p2pui.BasePresenter;
import com.aronskiy_anton.p2pui.BaseView;
import com.aronskiy_anton.p2pui.bankcard.BankCardContract;
import com.aronskiy_anton.sdk.models.BankCard;
import com.aronskiy_anton.sdk.models.Payout;

import java.util.List;

/**
 * Created by anton on 12.09.2017.
 */

public interface PayoutsContract {

    interface Presenter extends BasePresenter {
        void loadPayouts(boolean forceUpdate);

        void loadMore();
    }

    interface View extends BaseView<PayoutsContract.Presenter> {
        void showPayouts(List<Payout> payouts);

        void showEmptyList();

        void setAllDataAreLoaded();
    }

}
