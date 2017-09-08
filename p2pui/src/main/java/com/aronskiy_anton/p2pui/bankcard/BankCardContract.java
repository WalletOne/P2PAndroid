package com.aronskiy_anton.p2pui.bankcard;

import com.aronskiy_anton.p2pui.BasePresenter;
import com.aronskiy_anton.p2pui.BaseView;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public interface BankCardContract {

    interface Presenter extends BasePresenter{
        void loadCards(boolean forceUpdate);
    }

    interface View extends BaseView<Presenter>{
        void showCards(List<BankCard> tasks);
    }
}
