package com.aronskiy_anton.p2p.controllers.freelancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.controllers.employer.EmployerActivity;
import com.aronskiy_anton.p2p.data.Repository;
import com.aronskiy_anton.p2p.utils.ActivityUtils;

/**
 * Created by anton on 13.09.2017.
 */

public class FreelancerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freelancer_activity);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        FreelancerFragment freelancerFragment =
                (FreelancerFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (freelancerFragment == null) {
            // Create the fragment
            freelancerFragment = FreelancerFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), freelancerFragment, R.id.contentFrame);
        }

        new FreelancerPresenter(freelancerFragment, Repository.getInstance());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.freelancer_mode:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.employer_mode:
                                Intent intent =
                                        new Intent(FreelancerActivity.this, EmployerActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
