package com.walletone.p2p.controllers.freelancer;

import com.walletone.p2p.BasePresenter;
import com.walletone.p2p.BaseView;
import com.walletone.p2p.models.Freelancer;

/**
 * Created by anton on 13.09.2017.
 */

public interface FreelancerContract {

    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{
        void showUserData(Freelancer freelancer);
    }
}
