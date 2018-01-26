package com.walletone.p2p.controllers.freelancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walletone.p2p.R;
import com.walletone.p2p.controllers.deals.DealsActivity;
import com.walletone.p2p.models.Freelancer;
import com.walletone.p2p.models.UserTypeId;
import com.walletone.p2pui.paymenttool.PaymentToolActivity;
import com.walletone.p2pui.payouts.PayoutsActivity;

import static com.walletone.p2p.controllers.deals.DealsActivity.ARG_USER_TYPE_ID;
import static com.walletone.p2pui.paymenttool.PaymentToolActivity.ARG_OWNER_ID;
import static com.walletone.p2pui.paymenttool.PaymentToolPresenter.Owner.BENEFICIARY;


/**
 * Created by anton on 13.09.2017.
 */

public class FreelancerFragment extends Fragment implements FreelancerContract.View, View.OnClickListener {

    static final String ARG_DEAL_ID = "DealsFragment.ARG_DEAL_ID";

    private TextView freelancerName;

    private TextView freelancerPhone;

    private FreelancerContract.Presenter presenter;

    public FreelancerFragment() {
    }

    public static FreelancerFragment newInstance() {
        return new FreelancerFragment();
    }

    @Override
    public void setPresenter(@NonNull FreelancerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUserData(Freelancer freelancer) {
        freelancerName.setText(freelancer.getTitle());
        freelancerPhone.setText(freelancer.getFormattedPhoneNumber());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.freelancer_fragment, container, false);

        freelancerName = root.findViewById(R.id.freelancer_name);
        freelancerPhone = root.findViewById(R.id.freelancer_phone);

        ViewGroup dealsButton = root.findViewById(R.id.deals_button);
        ViewGroup paymentToolsButton = root.findViewById(R.id.payment_tool_button);
        ViewGroup payoutsButton = root.findViewById(R.id.payouts_button);

        dealsButton.setOnClickListener(this);
        paymentToolsButton.setOnClickListener(this);
        payoutsButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.deals_button:
                intent = new Intent(getContext(), DealsActivity.class);
                intent.putExtra(ARG_USER_TYPE_ID, UserTypeId.FREELANCER.getId());
                startActivity(intent);
                break;
            case R.id.payment_tool_button:
                intent = new Intent(getContext(), PaymentToolActivity.class);
                intent.putExtra(ARG_OWNER_ID, BENEFICIARY);
                startActivity(intent);
                break;
            case R.id.payouts_button:
                intent = new Intent(getContext(), PayoutsActivity.class);
                intent.putExtra(ARG_DEAL_ID, "");
                startActivity(intent);
                break;
        }
    }
}
