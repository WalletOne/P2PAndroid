package com.aronskiy_anton.p2p.controllers.employer;

import com.aronskiy_anton.p2p.BasePresenter;
import com.aronskiy_anton.p2p.BaseView;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.Freelancer;

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
