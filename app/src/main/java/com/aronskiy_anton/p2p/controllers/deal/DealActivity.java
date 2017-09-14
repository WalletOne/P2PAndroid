package com.aronskiy_anton.p2p.controllers.deal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.data.RemoteDataSource;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.utils.ActivityUtils;

/**
 * Created by anton on 14.09.2017.
 */

public class DealActivity extends AppCompatActivity {

    @NonNull
    public static final String ARG_DEAL_ID = "DealActivity.DEAL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.deal_detail_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested deal id
        String dealId = getIntent().getStringExtra(ARG_DEAL_ID);

        DealFragment dealDetailFragment = (DealFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (dealDetailFragment == null) {
            dealDetailFragment = DealFragment.newInstance(dealId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    dealDetailFragment, R.id.contentFrame);
        }

        new DealPresenter(Repository.getInstance(RemoteDataSource.getInstance()), dealDetailFragment, dealId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
