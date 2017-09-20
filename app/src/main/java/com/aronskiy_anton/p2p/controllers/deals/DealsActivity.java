package com.aronskiy_anton.p2p.controllers.deals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.data.RemoteDataSource;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.models.UserTypeId;
import com.aronskiy_anton.p2p.utils.ActivityUtils;

import javax.sql.DataSource;

/**
 * Created by anton on 13.09.2017.
 */

public class DealsActivity extends AppCompatActivity {

    public static final String ARG_USER_TYPE_ID = "DealsActivity.ARG_USER_TYPE_ID";

    private DealsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.deals);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

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

        presenter = new DealsPresenter(Repository.getInstance(RemoteDataSource.getInstance()), dealsFragment, UserTypeId.getUserTypeById(userTypeId));

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
