package com.walletone.p2p.controllers.employer;

import com.walletone.p2p.BasePresenter;
import com.walletone.p2p.BaseView;
import com.walletone.p2p.models.Employer;

/**
 * Created by anton on 13.09.2017.
 */

public interface EmployerContract {

    interface Presenter extends BasePresenter{
        void loadDeals();
    }

    interface View extends BaseView<Presenter>{
        void showUserData(Employer employer);
    }
}
