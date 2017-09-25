package com.aronskiy_anton.p2p.controllers.freelancer;

import com.aronskiy_anton.p2p.BasePresenter;
import com.aronskiy_anton.p2p.BaseView;
import com.aronskiy_anton.p2p.models.Freelancer;

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
