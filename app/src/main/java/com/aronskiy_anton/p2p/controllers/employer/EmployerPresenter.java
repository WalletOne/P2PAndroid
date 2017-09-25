package com.aronskiy_anton.p2p.controllers.employer;

import android.support.annotation.NonNull;

import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.Freelancer;
import com.aronskiy_anton.sdk.P2PCore;


/**
 * Created by aaronskiy on 05.09.2017.
 */

public class EmployerPresenter implements EmployerContract.Presenter{

    private Employer employer;

    private final EmployerContract.View view;

    public EmployerPresenter(@NonNull EmployerContract.View view, @NonNull Repository repository) {
        this.view = view;
        view.setPresenter(this);
        setEmployer();

        P2PCore.INSTANCE.setPayer(employer.getId(), employer.getTitle(), employer.getPhoneNumber());

        repository.setEmployer(employer);
    }

    private void setEmployer() {
        employer = new Employer();
        employer.setId("vitaliykuzmenko");
        employer.setTitle("Vitaliy Kuzmenko");
        employer.setPhoneNumber("79281234567");
    }

    @Override
    public void start() {
        view.showUserData(employer);
    }

    @Override
    public void loadDeals() {

    }
}
