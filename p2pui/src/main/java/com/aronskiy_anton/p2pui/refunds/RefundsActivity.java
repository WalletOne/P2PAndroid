package com.aronskiy_anton.p2pui.refunds;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.refunds.RefundsFragment;
import com.aronskiy_anton.p2pui.refunds.RefundsPresenter;
import com.aronskiy_anton.p2pui.util.ActivityUtils;

/**
 * Created by anton on 12.09.2017.
 */

public class RefundsActivity extends AppCompatActivity {

    public static final String ARG_DEAL_ID = "PayoutsActivity.ARG_DEAL_ID";

    private boolean isLoading = false;

    private RefundsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.refunds_layout);

        setTitle(getResources().getString(R.string.refunds_activity_title));

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        RefundsFragment refundsFragment =
                (RefundsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (refundsFragment == null) {
            // Create the fragment
            refundsFragment = RefundsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), refundsFragment, R.id.contentFrame);
        }

        presenter = new RefundsPresenter("", refundsFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
