package com.walletone.p2p.controllers.freelancer;

import android.support.annotation.NonNull;

import com.walletone.p2p.data.Repository;
import com.walletone.p2p.models.Freelancer;
import com.walletone.sdk.P2PCore;


/**
 * Created by aaronskiy on 05.09.2017.
 */

public class FreelancerPresenter implements FreelancerContract.Presenter{

    private Freelancer freelancer;

    private final FreelancerContract.View view;

    public FreelancerPresenter(@NonNull FreelancerContract.View view, @NonNull Repository repository) {
        this.view = view;
        view.setPresenter(this);
        setFreelancer();

        P2PCore.INSTANCE.setBeneficiary(freelancer.getId(), freelancer.getTitle(), freelancer.getPhoneNumber());

        repository.setFreelancer(freelancer);
    }

    private void setFreelancer() {
        freelancer = new Freelancer();
        freelancer.setId("alinakuzmenko");
        freelancer.setTitle("Alina Kuzmenko");
        freelancer.setPhoneNumber("79287654321");
    }

    @Override
    public void start() {
        view.showUserData(freelancer);
    }

}
