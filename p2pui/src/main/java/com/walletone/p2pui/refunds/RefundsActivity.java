package com.walletone.p2pui.refunds;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.walletone.p2pui.R;
import com.walletone.p2pui.W1P2PToolbar;
import com.walletone.p2pui.util.ActivityUtils;

/**
 * Created by Aronskiy Anton on 12.09.2017.
 */

public class RefundsActivity extends AppCompatActivity {

    public static final String ARG_DEAL_ID = "PayoutsActivity.ARG_DEAL_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.refunds_layout);

        W1P2PToolbar.installToolBar(this);
        setTitle(getResources().getString(R.string.refunds_activity_title));

        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            String errorMsg = "This activity requires an AppCompat theme with an action bar, finishing activity...";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        RefundsFragment refundsFragment =
                (RefundsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (refundsFragment == null) {
            // Create the fragment
            refundsFragment = RefundsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), refundsFragment, R.id.contentFrame);
        }

        new RefundsPresenter("", refundsFragment);
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
