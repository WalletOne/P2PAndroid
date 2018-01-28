package com.walletone.p2p.controllers.deals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.walletone.p2p.R;
import com.walletone.p2p.data.Repository;
import com.walletone.p2p.models.UserTypeId;
import com.walletone.p2p.utils.ActivityUtils;

/**
 * Created by anton on 13.09.2017.
 */

public class DealsActivity extends AppCompatActivity {

    public static final String ARG_USER_TYPE_ID = "DealsActivity.ARG_USER_TYPE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.deals);

        ActionBar ab = getSupportActionBar();
        if(ab == null) {
            String errorMsg = "This activity requires an AppCompat theme with an action bar, finishing activity...";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        DealsFragment dealsFragment =
                (DealsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (dealsFragment == null) {
            // Create the fragment
            dealsFragment = DealsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), dealsFragment, R.id.contentFrame);
        }

        // Get the requested user type id
        Integer userTypeId = getIntent().getIntExtra(ARG_USER_TYPE_ID, 2);

        new DealsPresenter(Repository.getInstance(), dealsFragment, UserTypeId.getUserTypeById(userTypeId));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
