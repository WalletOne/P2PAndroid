package com.walletone.p2pui.refunds;

import com.walletone.p2pui.BasePresenter;
import com.walletone.p2pui.BaseView;
import com.walletone.sdk.models.Refund;

import java.util.List;

/**
 * Created by anton on 12.09.2017.
 */

public interface RefundsContract {

    interface Presenter extends BasePresenter {
        void loadRefunds(boolean forceUpdate);

        void loadMore();
    }

    interface View extends BaseView<RefundsContract.Presenter> {
        void showRefunds(List<Refund> refunds);

        void showEmptyList();

        void setAllDataAreLoaded();

        void showError(Throwable error);
    }

}
