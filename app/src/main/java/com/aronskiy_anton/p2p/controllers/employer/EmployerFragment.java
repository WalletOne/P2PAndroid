package com.aronskiy_anton.p2p.controllers.employer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.controllers.deals.DealsActivity;
import com.aronskiy_anton.p2p.models.Employer;
import com.aronskiy_anton.p2p.models.UserTypeId;
import com.aronskiy_anton.p2pui.bankcard.BankCardActivity;
import com.aronskiy_anton.p2pui.refunds.RefundsActivity;

import static com.aronskiy_anton.p2p.controllers.deals.DealsActivity.ARG_USER_TYPE_ID;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.ARG_OWNER_ID;
import static com.aronskiy_anton.p2pui.bankcard.BankCardPresenter.Owner.PAYER;
import static com.aronskiy_anton.p2pui.refunds.RefundsActivity.ARG_DEAL_ID;


/**
 * Created by anton on 13.09.2017.
 */

public class EmployerFragment extends Fragment implements EmployerContract.View, View.OnClickListener {

    private TextView employerName;

    private TextView employerPhone;

    private EmployerContract.Presenter presenter;

    public EmployerFragment() {
    }

    public static EmployerFragment newInstance() {
        return new EmployerFragment();
    }

    @Override
    public void setPresenter(@NonNull EmployerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUserData(Employer employer) {
        employerName.setText(employer.getTitle());
        employerPhone.setText(employer.getFormattedPhoneNumber());
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
        View root = inflater.inflate(R.layout.employer_fragment, container, false);

        employerName = root.findViewById(R.id.employer_name);
        employerPhone = root.findViewById(R.id.employer_phone);

        ViewGroup dealsButton = root.findViewById(R.id.deals_button);
        ViewGroup bankCardsButton = root.findViewById(R.id.bank_card_button);
        ViewGroup refundsButton = root.findViewById(R.id.refunds_button);

        dealsButton.setOnClickListener(this);
        bankCardsButton.setOnClickListener(this);
        refundsButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.deals_button:
                intent = new Intent(getContext(), DealsActivity.class);
                intent.putExtra(ARG_USER_TYPE_ID, UserTypeId.EMPLOYER.getId());
                startActivity(intent);
                break;
            case R.id.bank_card_button:
                intent = new Intent(getContext(), BankCardActivity.class);
                intent.putExtra(ARG_OWNER_ID, PAYER);
                startActivity(intent);
                break;
            case R.id.refunds_button:
                intent = new Intent(getContext(), RefundsActivity.class);
                intent.putExtra(ARG_DEAL_ID, "");
                startActivity(intent);
                break;
        }
    }
}
