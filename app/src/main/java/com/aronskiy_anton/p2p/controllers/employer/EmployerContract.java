package com.aronskiy_anton.p2p.controllers.employer;

import com.aronskiy_anton.p2p.BasePresenter;
import com.aronskiy_anton.p2p.BaseView;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.Freelancer;

/**
 * Created by anton on 13.09.2017.
 */

public class EmployerContract {

    public interface Presenter extends BasePresenter{
        void loadDeals();
    }

    public interface View extends BaseView<Presenter>{
        void showUserData(Employer employer);
    }
}
