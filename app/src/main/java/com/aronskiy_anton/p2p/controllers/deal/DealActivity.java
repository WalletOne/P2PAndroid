package com.aronskiy_anton.p2p.controllers.deal;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.models.UserTypeId;
import com.aronskiy_anton.p2p.utils.ActivityUtils;

/**
 * Created by anton on 14.09.2017.
 */

public class DealActivity extends AppCompatActivity {

    public static final String ARG_USER_TYPE_ID = "DealActivity.ARG_USER_TYPE_ID";
    public static final String ARG_DEAL_ID = "DealActivity.DEAL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.deal_detail_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            String errorMsg = "This activity requires an AppCompat theme with an action bar, finishing activity...";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        // Get the requested deal id
        String dealId = getIntent().getStringExtra(ARG_DEAL_ID);

        // Get the requested user type id
        Integer userTypeId = getIntent().getIntExtra(ARG_USER_TYPE_ID, 2);

        DealFragment dealDetailFragment = (DealFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (dealDetailFragment == null) {
            dealDetailFragment = DealFragment.newInstance(dealId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    dealDetailFragment, R.id.contentFrame);
        }

        new DealPresenter(Repository.getInstance(), dealDetailFragment, dealId, UserTypeId.getUserTypeById(userTypeId));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
