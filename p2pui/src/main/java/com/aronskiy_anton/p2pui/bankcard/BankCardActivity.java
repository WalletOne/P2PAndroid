package com.aronskiy_anton.p2pui.bankcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.util.ActivityUtils;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardActivity extends AppCompatActivity {

    BankCardPresenter.Owner owner = BankCardPresenter.Owner.BENEFICIARY;

    private BankCardPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_layout);

        setTitle(getResources().getString(R.string.bank_cards_activity_title));

        BankCardFragment bankCardFragment =
                (BankCardFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (bankCardFragment == null) {
            // Create the fragment
            bankCardFragment = BankCardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), bankCardFragment, R.id.contentFrame);
        }

        presenter = new BankCardPresenter(owner, bankCardFragment);
    }
}
