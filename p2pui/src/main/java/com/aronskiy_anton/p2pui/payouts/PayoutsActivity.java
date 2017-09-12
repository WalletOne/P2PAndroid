package com.aronskiy_anton.p2pui.payouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.bankcard.BankCardFragment;
import com.aronskiy_anton.p2pui.bankcard.BankCardPresenter;
import com.aronskiy_anton.p2pui.util.ActivityUtils;

/**
 * Created by anton on 12.09.2017.
 */

public class PayoutsActivity extends AppCompatActivity {

    private boolean isLoading = false;

    private PayoutsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.payouts_layout);

        setTitle(getResources().getString(R.string.payouts_activity_title));

        PayoutsFragment payoutsFragment =
                (PayoutsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (payoutsFragment == null) {
            // Create the fragment
            payoutsFragment = PayoutsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), payoutsFragment, R.id.contentFrame);
        }

        presenter = new PayoutsPresenter("", payoutsFragment);
    }
}
