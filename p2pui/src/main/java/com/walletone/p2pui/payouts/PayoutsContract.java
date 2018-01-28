package com.walletone.p2pui.payouts;

import com.walletone.p2pui.BasePresenter;
import com.walletone.p2pui.BaseView;
import com.walletone.sdk.models.Payout;

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
